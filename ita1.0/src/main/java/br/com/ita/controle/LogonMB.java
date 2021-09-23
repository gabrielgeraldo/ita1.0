package br.com.ita.controle;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.inject.Specializes;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.github.adminfaces.template.session.AdminSession;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
import br.com.ita.dominio.dao.PapelDAO;
import br.com.ita.dominio.dao.UsuarioDAO;
import br.com.ita.dominio.dao.util.CfopDAO;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;
import br.com.ita.dominio.dao.util.NcmDAO;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

@Named
@ManagedBean(name = "logonMB")
@SessionScoped
@Specializes
public class LogonMB extends AdminSession implements Serializable {

	private static final long serialVersionUID = 1L;

	// HttpServletRequest request;

	private String usuario;

	// @Inject
	private UsuarioDAO usuarioDao;

	// @Inject
	private PapelDAO papelDao;

	// @Inject
	private CfopDAO cfopDao;

	// @Inject
	private NcmDAO ncmDao;

	private EstadoDAO estadoDao;

	private MunicipioDAO municipioDao;

	// @Inject
	private ControleNumerosDAO controleNumerosDao;

	private String currentUser;

	public void preRender() {

		// if ("true".equals(request.getParameter("invalid"))) {
		// JSFUtil.retornarMensagemErro("Usu�rio ou senha inv�lido!", null,
		// null);
		// }
	}

	public void doLogin() throws IOException, ServletException {

		currentUser = usuario;
		this.addDetailMessage("Logged in successfully as <b>" + usuario + "</b>", null);
		Faces.getExternalContext().getFlash().setKeepMessages(true);

		// System.out.println(usuario);

		usuarioDao = new UsuarioDAO();
		usuarioDao.criaUsuarioPadrao();
		usuarioDao = null;

		papelDao = new PapelDAO();
		papelDao.criaPapeisUsuarioPadrao();
		papelDao = null;

		cfopDao = new CfopDAO();
		cfopDao.criaCfop();
		cfopDao = null;

		ncmDao = new NcmDAO();
		ncmDao.criaNcm();
		ncmDao = null;

		estadoDao = new EstadoDAO();
		estadoDao.criaEstado();
		estadoDao = null;

		municipioDao = new MunicipioDAO();
		municipioDao.criaMunicipio();
		municipioDao = null;

		controleNumerosDao = new ControleNumerosDAO();
		controleNumerosDao.criaNumerosPadrao();
		controleNumerosDao = null;

		try {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = ((HttpServletRequest) context.getRequest());
			ServletResponse resposnse = ((ServletResponse) context.getResponse());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
			dispatcher.forward(request, resposnse);
			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void logout() {
		try {
			RequestDispatcher dispatcher = JSFUtil.getServletRequest()
					.getRequestDispatcher("/j_spring_security_logout");
			dispatcher.forward(JSFUtil.getServletRequest(), JSFUtil.getServletResponse());
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean isLoggedIn() {
		return currentUser != null;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public UsuarioDAO getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDAO usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public PapelDAO getPapelDao() {
		return papelDao;
	}

	public void setPapelDao(PapelDAO papelDao) {
		this.papelDao = papelDao;
	}

	public CfopDAO getCfopDao() {
		return cfopDao;
	}

	public void setCfopDao(CfopDAO cfopDao) {
		this.cfopDao = cfopDao;
	}

	public NcmDAO getNcmDao() {
		return ncmDao;
	}

	public void setNcmDao(NcmDAO ncmDao) {
		this.ncmDao = ncmDao;
	}

	public ControleNumerosDAO getControleNumerosDao() {
		return controleNumerosDao;
	}

	public void setControleNumerosDao(ControleNumerosDAO controleNumerosDao) {
		this.controleNumerosDao = controleNumerosDao;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public void addDetailMessage(String message, FacesMessage.Severity severity) {

		FacesMessage facesMessage = Messages.create("").detail(message).get();
		if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
			facesMessage.setSeverity(severity);
		} else {
			Messages.add(null, facesMessage);
		}
	}

	public EstadoDAO getEstadoDao() {
		return estadoDao;
	}

	public void setEstadoDao(EstadoDAO estadoDao) {
		this.estadoDao = estadoDao;
	}

	public MunicipioDAO getMunicipioDao() {
		return municipioDao;
	}

	public void setMunicipioDao(MunicipioDAO municipioDao) {
		this.municipioDao = municipioDao;
	}

}