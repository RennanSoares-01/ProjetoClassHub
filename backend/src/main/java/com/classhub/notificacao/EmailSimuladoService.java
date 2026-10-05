package com.classhub.notificacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Simula o envio de e-mail (sem integração real com provedor SMTP neste MVP, ver .ai/tech-stack.md).
 */
@Service
public class EmailSimuladoService {

    private static final Logger log = LoggerFactory.getLogger(EmailSimuladoService.class);

    public void enviar(String destinatarioEmail, String assunto, String mensagem) {
        log.info("[E-MAIL SIMULADO] Para: {} | Assunto: {} | Mensagem: {}", destinatarioEmail, assunto, mensagem);
    }
}
