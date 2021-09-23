package br.com.ita.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import br.com.ita.controle.util.SegurancaUtil;

//FOMTE:https://javapointers.com/tutorial/spring-custom-logoutsuccesshandler-example/
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {

		if (authentication != null && authentication.getDetails() != null) {
			try {
				httpServletRequest.getSession().invalidate();
				// System.out.println("USUARIO DESLOGADO!");
				// voc� pode adicionar mais c�digos aqui quando o usu�rio logar
				// com sucesso Fora, como atualizar o banco de dados para o
				// �ltimo ativo.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (SegurancaUtil.verificaMec()) {

			if (SegurancaUtil.verificaDadosDaEmpresaEDadosConfiguracao()) {

				if (SegurancaUtil.verificaSenhaMes()) {

					httpServletResponse.setStatus(HttpServletResponse.SC_OK);
					// redirect to login
					httpServletResponse.sendRedirect("login.xhtml");

				} else {

					httpServletResponse.setStatus(HttpServletResponse.SC_OK);
					// redirect to login
					httpServletResponse.sendRedirect("_senhaMes.xhtml");

				}

			} else {
				httpServletResponse.setCharacterEncoding("UTF-8");
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
				// redirect to login
				httpServletResponse.sendRedirect("_dadosDaEmpresa.xhtml");
			}

		} else {
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			// redirect to login
			httpServletResponse.sendRedirect("_sistemaNaoRegistrado.xhtml");
		}
	}
}