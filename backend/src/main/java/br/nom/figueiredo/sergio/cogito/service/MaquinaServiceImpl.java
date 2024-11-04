package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Maquina;
import br.nom.figueiredo.sergio.cogito.repository.MaquinaIpRepository;
import br.nom.figueiredo.sergio.cogito.repository.MaquinaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MaquinaServiceImpl implements MaquinaService {
    private final MaquinaRepository maquinaRepository;
    private final MaquinaIpRepository maquinaIpRepository;

    public MaquinaServiceImpl(MaquinaRepository maquinaRepository, MaquinaIpRepository maquinaIpRepository) {
        this.maquinaRepository = maquinaRepository;
        this.maquinaIpRepository = maquinaIpRepository;
    }

    @Override
    public Flux<Maquina> findAllMaquinas() {
        return maquinaRepository.findAll()
                /* carrega todos os IPs de cada máquina. */
                .delayUntil(maquina -> maquinaIpRepository.findAllByMaquinaId(maquina.getId())
                        .collectList()
                        .doOnNext(maquina::withIps));
    }
}
