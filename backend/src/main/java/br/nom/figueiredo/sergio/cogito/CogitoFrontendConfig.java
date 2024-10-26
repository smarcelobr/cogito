package br.nom.figueiredo.sergio.cogito;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CogitoFrontendConfig {

    /**
     * Retorna o 'index.html' do angular quando requisitar qualquer página HTML.
     * @param indexHtml index.html do angular
     * @return resposta com o conteúdo do index.html
     */
    @Bean
    RouterFunction<ServerResponse> angularRouter(@Value("classpath:/static/index.html") final Resource indexHtml) {

        return route(method(HttpMethod.GET)
                        .and(accept(MediaType.TEXT_HTML))
                        .and(path("/api/**").negate())
                        .and(pathExtension(Objects::isNull)),
                request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(indexHtml));
    }

}
