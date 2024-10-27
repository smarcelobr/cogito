package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.controller.dto.TesteQuestaoDto;
import br.nom.figueiredo.sergio.cogito.controller.dto.TesteResponse;
import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import br.nom.figueiredo.sergio.cogito.service.TesteService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/api/teste")
public class TesteController {

    private final TesteService testeService;

    public TesteController(TesteService testeService) {
        this.testeService = testeService;
    }

    @GetMapping
    public Mono<TesteResponse> get(ServerHttpRequest request) {
        String ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();

        return this.testeService.pegaTesteDoIP(ip)
                .map(this::convertDto);
    }

    private TesteResponse convertDto(Teste teste) {
        TesteResponse testeResponse = new TesteResponse();
        testeResponse.setId(teste.getId());
        testeResponse.setDataCriacao(teste.getDataCriacao());
        testeResponse.setNota(teste.getNota());
        testeResponse.setStatus(teste.getStatus());
        for (TesteQuestao questao: teste.getQuestoes()) {
            TesteQuestaoDto testeQuestaoDto = convertDto(questao);
            testeResponse.getPerguntas().add(testeQuestaoDto);
        }
        return testeResponse;
    }

    private TesteQuestaoDto convertDto(TesteQuestao questao) {
        TesteQuestaoDto dto = new TesteQuestaoDto();
        dto.setId(questao.getId());
        dto.setPerguntaId(questao.getPerguntaId());
        dto.setOpcaoId(questao.getOpcaoId());
        return dto;
    }

}
