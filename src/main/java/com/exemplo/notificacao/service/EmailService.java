package com.exemplo.notificacao.service;

import com.exemplo.notificacao.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(1) // opcional: define a ordem de execução
public class EmailService implements NotificacaoStrategy {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Override
    public String canal() {
        return "email";
    }

    @Override
    public void notificar(Pedido pedido) {
        // validação defensiva (sem try/catch)
        Objects.requireNonNull(pedido, "pedido não pode ser nulo");
        if (pedido.getCliente() == null || pedido.getCliente().isBlank()) {
            throw new IllegalArgumentException("cliente obrigatório");
        }
        if (pedido.getValor() < 0) {
            throw new IllegalArgumentException("valor não pode ser negativo");
        }

        // montagem de mensagem (exemplo simples)
        String assunto = "Confirmação do seu pedido";
        String corpo = String.format(
            "Olá %s,%nSeu pedido no valor de R$ %.2f foi recebido e está em processamento.%n" +
            "Acompanhe pelo portal ou aplicativo.%n%nAbraços,%nEquipe",
            pedido.getCliente(), pedido.getValor()
        );

        // chamada ao provider (aqui sim usamos try/catch)
        try {
            // TODO: integrar com SMTP/SES/etc
            log.info("[EMAIL] Assunto='{}' | Para='{}' | Corpo='{}'",
                     assunto, pedido.getCliente(), corpo);
        } catch (Exception e) {
            log.warn("Falha ao enviar e-mail para cliente='{}': {}",
                     pedido.getCliente(), e.getMessage(), e);
        }
    }
}
