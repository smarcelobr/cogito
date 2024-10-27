package br.nom.figueiredo.sergio.cogito.controller;

import br.nom.figueiredo.sergio.cogito.controller.dto.LatexRequest;
import br.nom.figueiredo.sergio.cogito.controller.dto.LatexResponse;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api/latex")
public class LatexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LatexController.class);

    @PostMapping
    public ResponseEntity<LatexResponse> getLatex(@RequestBody LatexRequest latex) {

        try {
            TeXFormula formula = new TeXFormula(latex.getLatex());
            BufferedImage img = (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 16, Color.black, Color.white);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(img, "png", baos);
                byte[] barr = baos.toByteArray();
                LatexResponse resposta = new LatexResponse();
                resposta.setText("Teste");
                resposta.setBase64PNG(Base64.getEncoder().encodeToString(barr));
                return ResponseEntity.ok(resposta);
//                DataBuffer imageData = DefaultDataBufferFactory.sharedInstance.wrap(barr);
//                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
//                        .body(imageData);
            }
        } catch (Exception e) {
            LOGGER.error("An unknown IO error occurred while writing pixel", e);
        }
        return ResponseEntity.noContent().build();
    }
}
