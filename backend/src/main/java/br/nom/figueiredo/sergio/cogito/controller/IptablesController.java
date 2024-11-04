package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.model.IptableRule;
import br.nom.figueiredo.sergio.cogito.service.IptablesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/iptables")
public class IptablesController {

    private final IptablesService iptablesService;

    public IptablesController(IptablesService iptablesService) {
        this.iptablesService = iptablesService;
    }

    @GetMapping
    public Flux<IptableRule> get() {
        return iptablesService.getIptablesForwardRules();
    }

    @GetMapping("dropped")
    public Flux<IptableRule> getDropped() {
        return iptablesService.getIptablesForwardRules()
                .filter(rule -> "DROP".equals(rule.getTarget()));
    }

}
