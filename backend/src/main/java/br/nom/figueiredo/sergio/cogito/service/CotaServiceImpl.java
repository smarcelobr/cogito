package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.Cota;
import br.nom.figueiredo.sergio.cogito.repository.CotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@Service
public class CotaServiceImpl implements CotaService {
    private final CotaRepository cotaRepository;
//    private final Scheduler jdbcScheduler;

    public CotaServiceImpl(CotaRepository cotaRepository/*, Scheduler jdbcScheduler*/) {
        this.cotaRepository = cotaRepository;
        //this.jdbcScheduler = jdbcScheduler;
    }

    @Override
    public Flux<Cota> findAll() {
        return this.cotaRepository.findAll();
//        Flux<Cota> defer = Flux.defer(() -> Flux.fromIterable(this.cotaRepository.findAll()));
//        return defer.subscribeOn(jdbcScheduler);
    }
}
