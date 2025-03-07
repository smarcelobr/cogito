package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.LatexUtil;
import br.nom.figueiredo.sergio.cogito.controller.dto.*;
import br.nom.figueiredo.sergio.cogito.model.Gabarito;
import br.nom.figueiredo.sergio.cogito.model.Opcao;
import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import br.nom.figueiredo.sergio.cogito.service.PerguntaService;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api/pergunta")
public class PerguntaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerguntaController.class);
    private final PerguntaService perguntaService;

    public PerguntaController(PerguntaService perguntaService) {
        this.perguntaService = perguntaService;
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<PerguntaDto>> get(@PathVariable("id") Long perguntaId,
                                                 @RequestParam(required = false) Long seed) {
        seed = Optional.ofNullable(seed).orElse(perguntaId);
        return this.perguntaService.getPerguntaCompleta(perguntaId, seed)
                .map(this::convertDto)
                .map(pergunta -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pergunta));
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<PerguntaDto>> put(@PathVariable("id") Long perguntaId,
                                                 @RequestBody PerguntaDto perguntaDto,
                                                 @RequestParam(required = false) Long seed) {
        seed = Optional.ofNullable(seed).orElse(perguntaId);
        return this.perguntaService.getPerguntaCompleta(perguntaId, seed)
                .doOnNext(p -> {
                    /* atualiza registro recuperado com os dados que vieram */
                    p.setDisciplina(perguntaDto.getDisciplina());
                    p.setQuestao(perguntaDto.getEnunciadoLatex());
                    for (OpcaoDto opcDto : perguntaDto.getOpcoes()) {
                        p.getOpcoes().stream()
                                .filter(opc -> opc.getId().equals(opcDto.getId()))
                                .findFirst()
                                .ifPresent(opcao -> opcao.setAlternativa(opcDto.getAlternativaLatex()));
                        p.getGabarito().stream()
                                .filter(opc -> opc.getOpcaoId().equals(opcDto.getId()))
                                .findFirst()
                                .ifPresent(gabarito -> {
                                    gabarito.setCorreta(opcDto.getCorreta());
                                    gabarito.setExplicacao(opcDto.getExplicacaoLatex());
                                });
                    }
                })
                .flatMap(this.perguntaService::savePerguntaCompleta)
                .map(this::convertDto)
                .map(pergunta -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pergunta));
    }

    @PostMapping("clone")
    public Mono<ResponseEntity<PerguntaDto>> put(@RequestBody PerguntaCloneRequest cloneRequest) {

        return this.perguntaService.clonar(cloneRequest.getOrigem())
                .map(this::convertDto)
                .map(pergunta -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pergunta));
    }

    @GetMapping("{id}/img")
    public Mono<ResponseEntity<PerguntaImgResponse>> getPNG(@PathVariable("id") Long perguntaId,
                                                            @RequestParam(value = "rndSeed", required = false)
                                                            Long optRndSeed) {

        return this.perguntaService.getPerguntaCompleta(perguntaId, optRndSeed)
                .map(this::criaIMG)
                .map(perguntaImgResponse -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(perguntaImgResponse));
    }

    private PerguntaImgResponse criaIMG(Pergunta pergunta) {
        PerguntaImgResponse perguntaImgResponse = new PerguntaImgResponse();
        perguntaImgResponse.setId(pergunta.getId());
        perguntaImgResponse.setDisciplina(pergunta.getDisciplina());
        perguntaImgResponse.setBase64PNG(LatexUtil.latexToBase64PNG(pergunta.getQuestao()));
        int num = 0;
        for (Opcao opcao : pergunta.getOpcoes()) {
            Gabarito gabarito = pergunta.getGabarito().stream()
                    .filter(g -> g.getOpcaoId().equals(opcao.getId()))
                    .findFirst().orElse(null);
            OpcaoImg opcaoImg = new OpcaoImg();
            opcaoImg.setId(opcao.getId());
            opcaoImg.setLetra(String.valueOf((char) ('a' + num)));
            opcaoImg.setBase64PNG(LatexUtil.latexToBase64PNG(opcao.getAlternativa()));
            if (nonNull(gabarito)) {
                opcaoImg.setCorreta(gabarito.getCorreta());
                opcaoImg.setExplicacaoBase64PNG(LatexUtil.latexToBase64PNG(gabarito.getExplicacao()));
            }
            perguntaImgResponse.getOpcoes().add(opcaoImg);
            num++;
        }

        return perguntaImgResponse;
    }

    @GetMapping(value = "{id}/img", headers = "accept=image/png")
    public Mono<ResponseEntity<DataBuffer>> getImgPergunta(@PathVariable("id") Long perguntaId,
                                                           @RequestParam(value = "rndSeed", required = false)
                                                           Long optRndSeed) {

        return perguntaService.getPerguntaCompleta(perguntaId, optRndSeed)
                .map(this::criaPNG)
                .map(imageData -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imageData));
    }

    private DataBuffer criaPNG(Pergunta pergunta) {
        TeXFormula formula = new TeXFormula(montaLatex(pergunta));
        BufferedImage img = (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 20, Color.black, Color.white);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "png", baos);
            byte[] barr = baos.toByteArray();
            return DefaultDataBufferFactory.sharedInstance.wrap(barr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* Monta o latex assim:
\textbf{[Cálculo algébrico]}\\
\text{ Qual das opções é igual à expressão }(3x-2)^2\text{ ?}\\
\begin{array}{rl}
a) &= (3x-2) + (3x-2)\\
b) &= (3x-2)(3x-2)\\
c) &= 9x^2-4\\
d) &\text{nenhuma das alternativas acima.}
\end{array}\\
     */
    private String montaLatex(Pergunta pergunta) {
        StringBuilder latex = new StringBuilder(String.format("""
                        \\textbf{[%s]}\\\\
                        %s\\\\
                        \\begin{array}{rl}
                        """,
                pergunta.getDisciplina(),
                pergunta.getQuestao()));
        int num = 0;
        for (Opcao opcao : pergunta.getOpcoes()) {
            if (num > 0) {
                latex.append("\\\\\n");
            }
            latex.append((char) ('a' + num))
                    .append(") &")
                    .append(opcao.getAlternativa());
            num++;
        }

        latex.append("\n\\end{array}\n");
        return latex.toString();
    }

    private PerguntaDto convertDto(Pergunta pergunta) {
        PerguntaDto dto = new PerguntaDto();
        dto.setId(pergunta.getId());
        dto.setEnunciadoLatex(pergunta.getQuestao());
        dto.setDisciplina(pergunta.getDisciplina());
        List<OpcaoDto> opcaoDtoList = new ArrayList<>(pergunta.getOpcoes().size());
        dto.setOpcoes(opcaoDtoList);
        for (Opcao opcao : pergunta.getOpcoes()) {
            Optional<Gabarito> gabarito = pergunta.getGabarito().stream()
                    .filter((g) -> opcao.getId().equals(g.getOpcaoId()))
                    .findFirst();
            opcaoDtoList.add(convertDto(opcao, gabarito));
        }
        return dto;
    }

    private OpcaoDto convertDto(Opcao opcao, Optional<Gabarito> gabarito) {
        OpcaoDto dto = new OpcaoDto();
        dto.setId(opcao.getId());
        dto.setAlternativaLatex(opcao.getAlternativa());
        if (gabarito.isPresent()) {
            dto.setCorreta(gabarito.get().getCorreta());
            dto.setExplicacaoLatex(gabarito.get().getExplicacao());
        }
        return dto;
    }

}
