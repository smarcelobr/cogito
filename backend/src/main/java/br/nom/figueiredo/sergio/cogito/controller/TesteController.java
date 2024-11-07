package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.LatexUtil;
import br.nom.figueiredo.sergio.cogito.controller.dto.*;
import br.nom.figueiredo.sergio.cogito.model.Gabarito;
import br.nom.figueiredo.sergio.cogito.model.Teste;
import br.nom.figueiredo.sergio.cogito.model.TesteQuestao;
import br.nom.figueiredo.sergio.cogito.model.TesteStatus;
import br.nom.figueiredo.sergio.cogito.service.TesteService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("{testeId}/marcar_opcao")
    public Mono<ResponseEntity<TesteQuestaoDto>> marcarOpcao(
            @PathVariable Long testeId,
            @RequestBody MarcarOpcaoRequest marcarOpcaoRequest) {

        return this.testeService.marcarOpcao(testeId,
                        marcarOpcaoRequest.getQuestaoId(), marcarOpcaoRequest.getOpcaoId())
                .map(this::convertDto)
                .map(dto ->
                        ResponseEntity.ok()
                                .body(dto));
    }

    @PutMapping("{testeId}/desmarcar_opcao")
    public Mono<ResponseEntity<TesteQuestaoDto>> desmarcarOpcao(
            @PathVariable Long testeId,
            @RequestBody DesmarcarOpcaoRequest marcarOpcaoRequest) {

        return this.testeService.desmarcarOpcao(testeId,
                        marcarOpcaoRequest.getQuestaoId())
                .map(this::convertDto)
                .map(dto ->
                        ResponseEntity.ok()
                                .body(dto));
    }

    @GetMapping("{testeId}/corrigir")
    public Mono<ResponseEntity<TesteResponse>> corrigir(
            @PathVariable Long testeId) {

        return this.testeService.corrigir(testeId)
                .map(this::convertDto)
                .map(dto ->
                        ResponseEntity.ok()
                                .body(dto));
    }

    private TesteResponse convertDto(Teste teste) {
        TesteResponse testeResponse = new TesteResponse();
        testeResponse.setId(teste.getId());
        testeResponse.setDataCriacao(teste.getDataCriacao());
        testeResponse.setStatus(teste.getStatus());
        testeResponse.setNota(teste.getNota());
        testeResponse.setIp(teste.getIp());
        testeResponse.setDataConclusao(teste.getDataConclusao());
        for (TesteQuestao questao : teste.getQuestoes()) {
            TesteQuestaoDto testeQuestaoDto = convertDto(questao);
            if (teste.getStatus()==TesteStatus.CORRIGIDO) {
                Gabarito gabarito = questao.getPergunta().getGabarito()
                        .stream()
                                .filter(gab->gab.getOpcaoId().equals(questao.getOpcaoId()))
                                        .findFirst().orElse(null);
                testeQuestaoDto.setCorreto(Objects.requireNonNull(gabarito).getCorreta());
                testeQuestaoDto.setExplicacao(LatexUtil.latexToBase64PNG(gabarito.getExplicacao()));
            }
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
