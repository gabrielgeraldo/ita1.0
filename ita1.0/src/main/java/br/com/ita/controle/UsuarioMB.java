package br.com.ita.controle;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Papel;
import br.com.ita.dominio.Usuario;
import br.com.ita.dominio.dao.PapelDAO;
import br.com.ita.dominio.dao.UsuarioDAO;

@Path("/usuario")
@Named("userMB")
@RequestScoped
public class UsuarioMB {

	@Inject
	private Usuario usuario;

	@Inject
	private UsuarioDAO usuarioDao;

	@Inject
	private PapelDAO daoPapel;

	private List<Usuario> usuarios = null;
	private List<Usuario> usuariosFiltrados = null;

	private List<Papel> papeis = null;

	public String listar() {

		return "/Usuario/usuarioListar?faces-redirect=true";

	}

	public String novo() {

		// limpar o objeto da p�gina
		this.setUsuario(new Usuario());

		return "/Usuario/usuarioEditar";

	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Usuario objetoDoBanco = this.usuarioDao.lerPorId(codigo);
		this.setUsuario(objetoDoBanco);

		return "/Usuario/usuarioEditar";

	}

	public String salvar() {

		if (this.getUsuario().getCodigo() == null) {
			Usuario objetoDoBanco = this.usuarioDao.lerPorUsuario(this.getUsuario().getUsuario());

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null, "Outro usu�rio com o mesmo nome j� existe no sistema.", null);
				return null;
			}
		}

		if (this.getUsuario().getPapeis().size() > 1) {
			JSFUtil.retornarMensagemAviso(null, "Selecione apenas um papel para esse usu�rio.", null);
			return null;
		}

		this.usuarioDao.merge(this.getUsuario());
		// limpa a lista
		this.usuarios = null;

		// limpar o objeto da p�gina
		this.setUsuario(new Usuario());

		JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

		return "/Usuario/usuarioListar";

	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setUsuario(new Usuario());

		return "/Usuario/usuarioListar";

	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Usuario objetoDoBanco = this.usuarioDao.lerPorId(codigo);
		this.usuarioDao.remove(objetoDoBanco);

		if (this.usuarioDao.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Exclu�do com sucesso.", null);
		}

		// limpar o objeto da p�gina
		this.setUsuario(new Usuario());
		// limpa a lista
		this.usuarios = null;

		return "/Usuario/usuarioListar";
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/getUsuarios")
	public List<Usuario> getUsuarios() {
		if (this.usuarios == null)
			this.usuarios = this.usuarioDao.lerTodos();

		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDAO getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDAO usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}

	public List<Papel> getPapeis() {

		if (this.papeis == null) {

			this.daoPapel = new PapelDAO();
			this.papeis = this.daoPapel.lerTodos();

		}

		return papeis;
	}

	public void setPapeis(List<Papel> papeis) {
		this.papeis = papeis;
	}

	public PapelDAO getDaoPapel() {
		return daoPapel;
	}

	public void setDaoPapel(PapelDAO daoPapel) {
		this.daoPapel = daoPapel;
	}

}
