package com.exemplo.notificacao.service;

import com.exemplo.notificacao.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(3)
public class PushService implements NotificacaoStrategy {

    private static final Logger log = LoggerFactory.getLogger(PushService.class);

    @Override
    public String canal() {
        return "push";
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

        String titulo = "Novo pedido recebido!";
        String mensagem = String.format(
            "Oi %s, recebemos seu pedido de R$ %.2f. Toque para ver os detalhes.",
            pedido.getCliente(), pedido.getValor()
        );

        try {
            // TODO: integrar com FCM/APNs
            log.info("[PUSH] Título='{}' | Mensagem='{}'", titulo, mensagem);
        } catch (Exception e) {
            log.warn("Falha ao enviar push para cliente='{}': {}",
                     pedido.getCliente(), e.getMessage(), e);
        }
    }
}
