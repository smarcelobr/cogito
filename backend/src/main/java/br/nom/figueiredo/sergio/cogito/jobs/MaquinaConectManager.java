package br.nom.figueiredo.sergio.cogito.jobs;

import br.nom.figueiredo.sergio.cogito.model.Maquina;
import br.nom.figueiredo.sergio.cogito.repository.TesteRepository;
import br.nom.figueiredo.sergio.cogito.service.IptablesService;
import br.nom.figueiredo.sergio.cogito.service.MaquinaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Classe responsável por liberar ou proibir internet das máquinas.
 *
 */
@Component
public class MaquinaConectManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaquinaConectManager.class);

    private final TesteRepository testeRepository;
    private final IptablesService iptablesService;
    private final MaquinaService maquinaService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MaquinaConectManager(TesteRepository testeRepository,
                                IptablesService iptablesService,
                                MaquinaService maquinaService,
                                ApplicationEventPublisher applicationEventPublisher) {
        this.testeRepository = testeRepository;
        this.iptablesService = iptablesService;
        this.maquinaService = maquinaService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    public void confereConexoes() {
        LOGGER.trace("conferencia periódica...");
        this.applicationEventPublisher.publishEvent(new ConfereConexoesEvent(this));
    }

    @EventListener
    public void confereConexoes(ConfereConexoesEvent cse) {
        LOGGER.debug("conferindo conexões...");

        maquinaService.findAllMaquinas()
                /* carrega os testes na validade */
                .flatMap(maquina -> this.testeRepository.findCorrigidosByMaquinaAndValidade(maquina.getId(), LocalDateTime.now())
                        .take(1)
                        .map(teste-> new MaquinaGrant(maquina, true))
                        .switchIfEmpty(Mono.just(new MaquinaGrant(maquina, false)))

                )
                .delayUntil(grant -> {
                    if (!grant.isGranted()) {
                        LOGGER.info("ADD DROP {}", grant.getMaquina().getNome());
                        return this.iptablesService.addDropRule(grant.getMaquina());
                    } else {
                        LOGGER.info("DELETE DROP {}", grant.getMaquina().getNome());
                        return this.iptablesService.deleteDropRule(grant.getMaquina());
                    }
                })
                .ignoreElements()
                .subscribe();
    }

    public static class MaquinaGrant {
        private final Maquina maquina;
        private final boolean granted;

        public MaquinaGrant(Maquina maquina, boolean granted) {
            this.maquina = maquina;
            this.granted = granted;
        }

        public Maquina getMaquina() {
            return maquina;
        }

        public boolean isGranted() {
            return granted;
        }

    }

}
