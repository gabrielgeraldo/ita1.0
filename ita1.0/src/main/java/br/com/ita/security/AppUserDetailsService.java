package br.com.ita.security;

import java.io.Serializable;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.ita.dominio.Usuario;
import br.com.ita.dominio.dao.UsuarioDAO;

public class AppUserDetailsService implements UserDetailsService, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	UsuarioDAO daoUsuario = new UsuarioDAO();

	@Override
	public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {

		Usuario usuarioAutenticado = daoUsuario.lerPorUsuario(usuario);

		if (usuarioAutenticado == null) {
			throw new UsernameNotFoundException(usuario);
		}

		return new MyUserPrincipal(usuarioAutenticado);

	}

}
