package br.unifor.healthsys.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @RequestMapping("/{serviceName}")
    public ResponseEntity<Map<String, String>> fallback(
            @PathVariable String serviceName,
            ServerWebExchange exchange
    ) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "status", "error",
                        "message", "Servico " + serviceName + " temporariamente indisponivel. Tente novamente em instantes.",
                        "service", serviceName,
                        "method", exchange.getRequest().getMethod().name(),
                        "path", exchange.getRequest().getPath().value()
                ));
    }
}
