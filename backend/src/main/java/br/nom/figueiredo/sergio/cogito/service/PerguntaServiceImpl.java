package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import br.nom.figueiredo.sergio.cogito.repository.GabaritoRepository;
import br.nom.figueiredo.sergio.cogito.repository.OpcaoRepository;
import br.nom.figueiredo.sergio.cogito.repository.PerguntaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<Pergunta> getPerguntas(int quantidade) {
        return this.perguntaRepository.findAllBy(Pageable.ofSize(5));
    }

    @Override
    public Mono<Pergunta> getPerguntaCompleta(Long id) {
        return this.perguntaRepository.findById(id)
                .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Pergunta %d não encontrada", id))))
                .delayUntil(pergunta -> opcaoRepository
                        .findAllByPerguntaId(pergunta.getId())
                        .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Pergunta %d não tem opções cadastradas", pergunta.getId()))))
                        .collectList()
                        .doOnNext(pergunta::withOpcoes)
                        .and(gabaritoRepository
                                .findAllByPerguntaId(pergunta.getId())
                                .switchIfEmpty(Mono.error(new CogitoServiceException(String.format("Pergunta %d não tem gabarito cadastrado", pergunta.getId()))))
                                .collectList()
                                .doOnNext(pergunta::withGabaritos)
                        )
                );
    }
}
