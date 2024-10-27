package br.nom.figueiredo.sergio.cogito;

import br.nom.figueiredo.sergio.cogito.controller.dto.LatexResponse;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class LatexUtil {

    /**
     * Compila e converte uma expressão latex em PNG (base64)
     * @param latex expressão latex.
     * @return base64 do PNG equivalente.
     */
    public static String latexToBase64PNG(String latex) {
        try {
            TeXFormula formula = new TeXFormula(latex);
            BufferedImage img = (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 16, Color.black, Color.white);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(img, "png", baos);
                byte[] barr = baos.toByteArray();
                return Base64.getEncoder().encodeToString(barr);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
