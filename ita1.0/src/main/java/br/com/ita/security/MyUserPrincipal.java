package br.com.ita.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.ita.dominio.Papel;
import br.com.ita.dominio.Usuario;

public class MyUserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Usuario user;

	public MyUserPrincipal(Usuario user) {
		this.setUser(user);
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Papel papeis : user.getPapeis()) {
			authorities.add(new SimpleGrantedAuthority(papeis.getNome().toUpperCase()));
		}

		// System.out.println("Papeis:" + authorities);

		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getSenha();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		// System.out.println(user.getUsuario());
		return user.getUsuario();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}