package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import br.nom.figueiredo.sergio.cogito.repository.PerguntaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PerguntaServiceImpl implements PerguntaService {
    private final PerguntaRepository perguntaRepository;

    public PerguntaServiceImpl(PerguntaRepository perguntaRepository) {
        this.perguntaRepository = perguntaRepository;
    }

    @Override
    public Flux<Pergunta> getPerguntas(int quantidade) {
        return this.perguntaRepository.findAllBy(Pageable.ofSize(5));
    }
}
