package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.model.Opcao;
import br.nom.figueiredo.sergio.cogito.model.Pergunta;
import br.nom.figueiredo.sergio.cogito.repository.PerguntaRepository;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/pergunta")
public class PerguntaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerguntaController.class);
    private final PerguntaRepository perguntaRepository;

    public PerguntaController(PerguntaRepository perguntaRepository) {
        this.perguntaRepository = perguntaRepository;
    }

    @GetMapping
    public Mono<ResponseEntity<DataBuffer>> getPergunta(@RequestParam("id") Long perguntaId) {

        return perguntaRepository.findById(perguntaId)
                .map(this::criaPNG)
                .map(imageData -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imageData));
    }

    private DataBuffer criaPNG(Pergunta pergunta) {
            TeXFormula formula = new TeXFormula(montaLatex(pergunta));
            BufferedImage img = (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 30, Color.white, Color.black);
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
                        \\text{%s}\\\\
                        \\begin{array}{rl}
                        """,
                pergunta.getDisciplina(),
                pergunta.getQuestao()));
        int num = 0;
        for (Opcao opcao: pergunta.getOpcoes()) {
            if (num>0) {
                latex.append("\\\\\n");
            }
            latex.append((char)('a'+num))
                    .append(") &")
                    .append(opcao.getAlternativa());
            num++;
        }

        latex.append("\n\\end{array}\n");
        return latex.toString();
    }
}
