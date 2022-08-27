package br.com.ita.controle.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ItaMail {

	public static void mail(String assunto, String corpo) {

		System.out.print("*** Enviando e-mail... ***");

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("infosistemaita@gmail.com", "ita*4*7*");
			}
		});

		/** Ativa Debug para sess�o */
		session.setDebug(false);

		try {

			// Remetente
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("infosistemaita@gmail.com"));

			// Destinat�rio(s)
			Address[] toUser = InternetAddress.parse("gabrieldomingosgeraldo@gmail.com");

			message.setRecipients(Message.RecipientType.TO, toUser);

			// Assunto
			message.setSubject(assunto);
			message.setText(corpo);
			/** M�todo para enviar a mensagem criada */
			Transport.send(message);

		} catch (MessagingException e) {
			System.out.print("*** Erro no envio do e-mail... ***");
			throw new RuntimeException(e);
		}

	}
}