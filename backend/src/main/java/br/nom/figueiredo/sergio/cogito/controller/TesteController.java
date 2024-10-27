package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.controller.dto.TesteResponse;
import br.nom.figueiredo.sergio.cogito.model.TesteStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("teste")
public class TesteController {

    @GetMapping
    public Mono<TesteResponse> get(ServerHttpRequest request) {
        String ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();

        TesteResponse testeResponse = new TesteResponse();
        testeResponse.setId(99L);
        testeResponse.setNota(null);
        testeResponse.setStatus(TesteStatus.NOVO.name());
        return Mono.just(testeResponse);
    }
}
