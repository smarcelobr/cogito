package br.nom.figueiredo.sergio.cogito.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.SequenceInputStream;

@RestController
@RequestMapping("api/terminal")
public class TerminalController {

    public final static Logger LOGGER = LoggerFactory.getLogger(TerminalController.class);

    @PostMapping
    public ResponseEntity<String> getTerminal(@RequestBody String comando) {

        StringBuilder saidaComando = new StringBuilder();
        try
        {
            Runtime rt = Runtime.getRuntime();

            LOGGER.info("Comando: [{}]", comando);

            saidaComando.append(comando).append("\n");

            Process pr = rt.exec(comando);

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    new SequenceInputStream(pr.getInputStream(), pr.getErrorStream())));

            String line = null;
            while ((line = input.readLine()) != null)
            {
                saidaComando.append(line).append("\n");
            }

            int exitVal = pr.waitFor();
            saidaComando.append("\nExited with error code ").append(exitVal);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            saidaComando.append("Exceção!\n").append(e.getMessage()).append("\n");
        }

        return ResponseEntity.ok(saidaComando.toString());
    }
}
