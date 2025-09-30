package com.exemplo.notificacao.service;

import com.exemplo.notificacao.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(2)
public class SmsService implements NotificacaoStrategy {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    @Override
    public String canal() {
        return "sms";
    }

    @Override
    public void notificar(Pedido pedido) {
        // validação defensiva
        Objects.requireNonNull(pedido, "pedido não pode ser nulo");
        if (pedido.getCliente() == null || pedido.getCliente().isBlank()) {
            throw new IllegalArgumentException("cliente obrigatório");
        }
        if (pedido.getValor() < 0) {
            throw new IllegalArgumentException("valor não pode ser negativo");
        }

        String texto = String.format(
            "Pedido confirmado! Cliente: %s | Total R$ %.2f. Acompanhe no app.",
            pedido.getCliente(), pedido.getValor()
        );

        try {
            // TODO: integrar com gateway de SMS (Twilio, etc.)
            log.info("[SMS] {}", texto);
        } catch (Exception e) {
            log.warn("Falha ao enviar SMS para cliente='{}': {}",
                     pedido.getCliente(), e.getMessage(), e);
        }
    }
}
