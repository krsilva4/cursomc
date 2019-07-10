package com.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		// recebedor do email.
		sm.setTo(obj.getCliente().getEmail());
		// quem enviou o email.
		sm.setFrom(sender);
		// assunto do email.
		sm.setSubject("Pedido confirmado! Código " + obj.getId());
		// data do email
		sm.setSentDate(new Date(System.currentTimeMillis()));
		// corpo do email
		sm.setText(obj.toString());
		return sm;
	}

}
