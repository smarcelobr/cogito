package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.model.Cota;
import br.nom.figueiredo.sergio.cogito.service.CotaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/cotas")
public class CotaController {

    private final CotaService cotaService;

    public CotaController(CotaService cotaService) {
        this.cotaService = cotaService;
    }

    @GetMapping()
    public Flux<Cota> listCotas() {

/*
        Cota cota1 = new Cota();
        cota1.setId(1L);
        cota1.setIp("192.168.2.10");
        cota1.setEndTime(LocalDateTime.now()
                .plusMinutes(50)
                .withSecond(0)
                .withNano(0));

        Cota cota2 = new Cota();
        cota2.setId(2L);
        cota2.setIp("192.168.2.10");
        cota2.setEndTime(LocalDateTime.now()
                .plusMinutes(20)
                .withSecond(0)
                .withNano(0));

        return Flux.fromArray(new Cota[] {cota1, cota2});
*/

        return cotaService.findAll();
    }
}

