package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.controller.PerguntaController;
import br.nom.figueiredo.sergio.cogito.model.Gabarito;
import br.nom.figueiredo.sergio.cogito.model.Opcao;
import br.nom.figueiredo.sergio.cogito.model.OpcaoGabarito;
import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import br.nom.figueiredo.sergio.cogito.repository.GabaritoRepository;
import br.nom.figueiredo.sergio.cogito.repository.OpcaoRepository;
import br.nom.figueiredo.sergio.cogito.repository.PerguntaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.nonNull;

@Service
public class PerguntaServiceImpl implements PerguntaService {
    private final PerguntaRepository perguntaRepository;
    private final OpcaoRepository opcaoRepository;
    private final GabaritoRepository gabaritoRepository;

    public PerguntaServiceImpl(PerguntaRepository perguntaRepository, OpcaoRepository opcaoRepository, GabaritoRepository gabaritoRepository) {
        this.perguntaRepository = perguntaRepository;
        this.opcaoRepository = opcaoRepository;
        this.gabaritoRepository = gabaritoRepository;
    }

    @Override
    public Flux<Pergunta> getRandom(String ip, int quantidade) {
        return this.perguntaRepository.findRandom(ip, Pageable.ofSize(quantidade));
    }

    @Override
    public Mono<Pergunta> getPerguntaCompleta(Long id, Long rndSeed) {
        Objects.requireNonNull(id);
        Random random = new Random(Objects.requireNonNullElse(rndSeed, id));
        return this.perguntaRepository.findById(id)
                .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Pergunta %d não encontrada", id))))
                .delayUntil(pergunta -> opcaoRepository
                        .findAllByPerguntaId(pergunta.getId())
                        .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Pergunta %d não tem opções cadastradas", pergunta.getId()))))
                        .collectList()
                        .doOnNext((opcoes -> Collections.shuffle(opcoes, random)))
                        .doOnNext(pergunta::withOpcoes)
                        .and(gabaritoRepository
                                .findAllByPerguntaId(pergunta.getId())
                                .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Pergunta %d não tem gabarito cadastrado", pergunta.getId()))))
                                .collectList()
                                .doOnNext(pergunta::withGabaritos)
                        )
                );
    }

    @Override
    public Mono<Pergunta> savePerguntaCompleta(Pergunta perguntaCompleto) {
        return perguntaRepository.save(perguntaCompleto)
                .delayUntil(p -> Flux.fromIterable(perguntaCompleto.getOpcoes())
                        .doOnNext(opcao -> opcao.setPerguntaId(p.getId()))
                        .flatMap(this.opcaoRepository::save)
                        .collectList()
                        .doOnNext(p::withOpcoes))
                .delayUntil(p -> Flux.fromIterable(perguntaCompleto.getGabarito())
                        .doOnNext(gabarito -> gabarito.setPerguntaId(p.getId()))
                        .flatMap(this.gabaritoRepository::save)
                        .collectList()
                        .doOnNext(p::withGabaritos));
    }

    @Override
    @Transactional
    public Mono<Pergunta> clonar(Long perguntaIdOrigem) {

        return this.getPerguntaCompleta(perguntaIdOrigem, null)
                .doOnNext(clone -> clone.setId(null))
                .flatMap(clone -> perguntaRepository.save(clone)
                        .delayUntil(salva ->
                                Flux.fromIterable(clone.getOpcoes())
                                        .map(opcao -> {
                                            // acha o gabarito correspondente a esta opcao:
                                            Gabarito gabarito = clone.getGabarito().stream()
                                                    .filter(g -> opcao.getId().equals(g.getOpcaoId()) && opcao.getPerguntaId().equals(g.getPerguntaId()))
                                                    .findFirst().orElse(null);

                                            /* atualiza o id da pergunta na opcao e no gabarito */
                                            opcao.setPerguntaId(salva.getId());
                                            opcao.setId(null);
                                            if (nonNull(gabarito)) {
                                                gabarito.setId(null);
                                                gabarito.setPerguntaId(salva.getId());
                                                gabarito.setOpcaoId(null);
                                            }
                                            return new OpcaoGabarito(opcao, gabarito);
                                        })
                                        .flatMap(og -> this.opcaoRepository.save(og.opcao())
                                                .map(opcaoSalva -> {
                                                    /* atualiza o id da opcao no gabarito associado. */
                                                    og.gabarito().setOpcaoId(opcaoSalva.getId());
                                                    return new OpcaoGabarito(opcaoSalva, og.gabarito());
                                                })
                                        )
                                        .flatMap(og -> this.gabaritoRepository.save(og.gabarito())
                                                .map(gabaritoSalvo-> new OpcaoGabarito(og.opcao(), gabaritoSalvo))
                                        )
                                        .collectList()
                                        .doOnNext(ogList -> {
                                            List<Opcao> opcoes = ogList.stream().map(OpcaoGabarito::opcao).toList();
                                            List<Gabarito> gabaritos = ogList.stream().map(OpcaoGabarito::gabarito).toList();
                                            salva.withOpcoes(opcoes);
                                            salva.withGabaritos(gabaritos);
                                        })
                        )
                );

    }
}
