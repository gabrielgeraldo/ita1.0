package br.com.ita.security;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.SegurancaUtil;

//FONTE:http://www.baeldung.com/spring_redirect_after_login
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	protected Log logger = LogFactory.getLog(this.getClass());

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	// fonte: https://www.baeldung.com/spring_redirect_after_login
	protected String determineTargetUrl(Authentication authentication) {

		// boolean isVendedor = false;
		// boolean isAdmin = false;

		// Collection<? extends GrantedAuthority> authorities =
		// authentication.getAuthorities();

		if (SegurancaUtil.verificaMec()) {

			if (SegurancaUtil.verificaSenhaMes()) {
				/*
				 * for (GrantedAuthority grantedAuthority : authorities) {
				 * System.out.println(grantedAuthority.getAuthority());
				 * 
				 * if (grantedAuthority.getAuthority().equals("VENDEDOR")) {
				 * isVendedor = true; }
				 * 
				 * if (grantedAuthority.getAuthority().equals("GERENTE")) {
				 * isAdmin = true; }
				 * 
				 * }
				 * 
				 * System.out.println("isAdmin: " + isAdmin);
				 * System.out.println("isVendedor: " + isVendedor);
				 * 
				 * if (isVendedor == true && isAdmin == false) { return
				 * "/home.xhtml"; } else if (isVendedor == true && isAdmin ==
				 * true) { return "/home.xhtml"; } else { return
				 * "/_acessoNegado.xhtml"; }
				 */

				return "/home.xhtml";

			} else {

				try {
					RequestDispatcher dispatcher = JSFUtil.getServletRequest()
							.getRequestDispatcher("/j_spring_security_logout");
					dispatcher.forward(JSFUtil.getServletRequest(), JSFUtil.getServletResponse());
					FacesContext.getCurrentInstance().responseComplete();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return null;
			}

		} else {

			try {
				RequestDispatcher dispatcher = JSFUtil.getServletRequest()
						.getRequestDispatcher("/j_spring_security_logout");
				dispatcher.forward(JSFUtil.getServletRequest(), JSFUtil.getServletResponse());
				FacesContext.getCurrentInstance().responseComplete();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;

		}

	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
}