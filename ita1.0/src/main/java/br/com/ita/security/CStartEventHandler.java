package br.com.ita.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

public class CStartEventHandler implements ApplicationListener<AuthenticationSuccessEvent> {

	// FONTE:https://www.luckyryan.com/2014/05/28/spring-application-event-listeners/
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent applicationEvent) {
		// System.out.println("USUARIO LOGADO!");
	}

}