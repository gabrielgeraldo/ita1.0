package br.com.ita.controle.nfe;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import org.primefaces.PrimeFaces;

import com.fincatto.documentofiscal.nfe400.classes.evento.NFEnviaEventoRetorno;
import com.fincatto.documentofiscal.nfe400.classes.evento.NFEventoRetorno;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;

import br.com.ita.controle.util.DanfeUtil;
import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.XmlUtil;
import br.com.ita.dominio.EventosNFe;
import br.com.ita.dominio.ItemNTFe;
import br.com.ita.dominio.NTFe;
import br.com.ita.dominio.config.Configuracao;
import br.com.ita.dominio.dao.ConfiguracaoDAO;
import br.com.ita.dominio.dao.EventosNFeDAO;
import br.com.ita.dominio.dao.ItemNFeDAO;
import br.com.ita.dominio.dao.NFeDAO;
import br.com.ita.dominio.dao.filtros.FiltroNfe;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

@Named("consultarNfeMB")
@RequestScoped
public class ConsultarNfeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private NFeConfigIta config = new NFeConfigIta();

	private NTFe nfe = new NTFe();

	private NFeDAO daoNfe = new NFeDAO();

	private EventosNFe eventoNFe = new EventosNFe();

	private ItemNFeDAO daoItemNFeSaida = new ItemNFeDAO();

	private EventosNFeDAO eventosNFeDAO = new EventosNFeDAO();

	private FiltroNfe filtro = new FiltroNfe();

	private List<NTFe> nfes = null;
	private List<NTFe> nfesFiltrados = null;
	private List<ItemNTFe> itensNTFe = null;
	private List<EventosNFe> eventos = null;

	private String mensagemEvento;

	@Inject
	private ConfiguracaoDAO configuracaoDao;

	@Inject
	private Configuracao configuracao;

	@PostConstruct
	public void init() {

		configuracao = configuracaoDao.lerPorId(new Long(1));

	}

	public void consultar() {

		this.nfes = daoNfe.consultar(filtro);

		this.setFiltro(new FiltroNfe());

	}

	public String acaoConsultar() {
		return "/NFe/nfeConsultar?faces-redirect=true";
	}

	public String vizualizar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFe objetoDoBanco = this.daoNfe.lerPorId(codigo);
		this.setNfe(new NTFe());
		this.setNfe(objetoDoBanco);

		this.itensNTFe = this.daoItemNFeSaida.buscaItens(this.nfe);
		this.eventos = this.eventosNFeDAO.buscaEventos(this.nfe);

		return "/NFe/nfeVizualizar";

	}

	public void imprimirDanfe() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFe nfeDobanco = new NTFe();
		NFeDAO daoNfe = new NFeDAO();
		nfeDobanco = daoNfe.lerPorId(codigo);
		this.setNfe(new NTFe());
		this.setNfe(nfeDobanco);

		try {
			String requestSoap = XmlUtil.lerXml("c:\\ita\\xml\\nfe\\" + this.nfe.getChave() + ".xml",
					Charset.defaultCharset());

			DanfeUtil.imprimirDanfeNfe(requestSoap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemAviso("XML n�o encontrado. A DANFE n�o ser� impressa.", null, null);
		}

	}

	public void downloadXml() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFe nfeDobanco = new NTFe();
		NFeDAO daoNfe = new NFeDAO();
		nfeDobanco = daoNfe.lerPorId(codigo);
		this.setNfe(new NTFe());
		this.setNfe(nfeDobanco);

		try {
			String requestSoap = XmlUtil.lerXml("c:\\ita\\xml\\nfe\\" + this.nfe.getChave() + ".xml",
					Charset.defaultCharset());

			XmlUtil.downloadXml(requestSoap, this.nfe.getChave());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemAviso("XML n�o encontrado. O downloand n�o ser� realizado.", null, null);
		}

	}

	public String cancelarNota() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFe objetoDoBanco = this.daoNfe.lerPorId(codigo);
		this.setNfe(new NTFe());
		this.setNfe(objetoDoBanco);

		return "/NFe/nfeCancelar";

	}

	public String cancelar() {

		// Verificar se a nota est� autorizada para poder cancelar
		if (this.nfe.getStatus().equals("100")) {

			try {
				NFEnviaEventoRetorno retorno = new WSFacade(config).cancelaNota(this.nfe.getChave(),
						this.nfe.getNumProtocolo(), this.mensagemEvento);

				for (NFEventoRetorno ret : retorno.getEventoRetorno()) {
					// System.out.println("Chave: " +
					// ret.getInfoEventoRetorno().getChave());
					// System.out.println("Codigo Status: " +
					// ret.getInfoEventoRetorno().getCodigoStatus());
					// System.out.println("Motivo: " +
					// ret.getInfoEventoRetorno().getMotivo());
					// System.out.println("Numero Protocolo: " +
					// ret.getInfoEventoRetorno().getNumeroProtocolo());
					// System.out.println(
					// "Numero Sequencial Evento: " +
					// ret.getInfoEventoRetorno().getNumeroSequencialEvento());
					// System.out.println("Tipo De Evento: " +
					// ret.getInfoEventoRetorno().getTipoEvento());
					// System.out.println("Descricao De Evento: " +
					// ret.getInfoEventoRetorno().getDescricaoEvento());
					// System.out.println("Ambiente: " +
					// ret.getInfoEventoRetorno().getAmbiente().getCodigo());

					// Se concelamento autorizado, a nota ser� atualizada no bd.
					if (ret.getInfoEventoRetorno().getCodigoStatus().toString().equals("135")) {

						eventoNFe.setNfe(nfe);

						eventoNFe.setDataRecebimento(
								java.sql.Timestamp.from(ret.getInfoEventoRetorno().getDataHoraRegistro().toInstant()));
						eventoNFe.setNumProtocolo(ret.getInfoEventoRetorno().getNumeroProtocolo());
						eventoNFe.setNumeroSequencialEvento(
								ret.getInfoEventoRetorno().getNumeroSequencialEvento().toString());
						eventoNFe.setTipoEvento(ret.getInfoEventoRetorno().getTipoEvento());
						eventoNFe.setAmbienteRecebimento(ret.getInfoEventoRetorno().getAmbiente().getCodigo());

						eventosNFeDAO.merge(eventoNFe);

						return "/NFe/nfeConsultar";

					} else {

						JSFUtil.retornarMensagemAviso(ret.getInfoEventoRetorno().getMotivo(), null, null);

						return null;

					}

				}

			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
			}

		} else {

			JSFUtil.retornarMensagemAviso("Esta NF-e n�o pode ser cancelada.", null, null);

		}

		boolean fecharDialogStatus = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialogStatus", fecharDialogStatus);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialogStatus", fecharDialogStatus);

		return null;
	}

	public String corrigirNota() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFe objetoDoBanco = this.daoNfe.lerPorId(codigo);
		this.setNfe(new NTFe());
		this.setNfe(objetoDoBanco);

		return "/NFe/nfeCorrigir";
	}

	public String corrigir() {

		try {

			final NFEnviaEventoRetorno retorno = new WSFacade(config).corrigeNota(this.nfe.getChave(),
					this.mensagemEvento, 1);

			for (NFEventoRetorno ret : retorno.getEventoRetorno()) {
				// System.out.println("Chave: " +
				// ret.getInfoEventoRetorno().getChave());
				// System.out.println("Codigo Status: " +
				// ret.getInfoEventoRetorno().getCodigoStatus());
				// System.out.println("Motivo: " +
				// ret.getInfoEventoRetorno().getMotivo());
				// System.out.println("Numero Protocolo: " +
				// ret.getInfoEventoRetorno().getNumeroProtocolo());
				// System.out
				// .println("Numero Sequencial Evento: " +
				// ret.getInfoEventoRetorno().getNumeroSequencialEvento());
				// System.out.println("Tipo De Evento: " +
				// ret.getInfoEventoRetorno().getTipoEvento());
				// System.out.println("Descricao De Evento: " +
				// ret.getInfoEventoRetorno().getDescricaoEvento());
				// System.out.println("Ambiente: " +
				// ret.getInfoEventoRetorno().getAmbiente().getCodigo());

				// Se corre��o autorizada, a nota ser� atualizada no bd.
				if (ret.getInfoEventoRetorno().getCodigoStatus().toString().equals("135")) {

					eventoNFe.setNfe(nfe);

					eventoNFe.setDataRecebimento(
							java.sql.Timestamp.from(ret.getInfoEventoRetorno().getDataHoraRegistro().toInstant()));
					eventoNFe.setNumProtocolo(ret.getInfoEventoRetorno().getNumeroProtocolo());
					eventoNFe.setNumeroSequencialEvento(
							ret.getInfoEventoRetorno().getNumeroSequencialEvento().toString());
					eventoNFe.setTipoEvento(ret.getInfoEventoRetorno().getTipoEvento());
					eventoNFe.setAmbienteRecebimento(ret.getInfoEventoRetorno().getAmbiente().getCodigo());

					eventosNFeDAO.merge(eventoNFe);

					return "/NFe/nfeConsultar";

				} else {

					JSFUtil.retornarMensagemAviso(ret.getInfoEventoRetorno().getMotivo(), null, null);

					return null;

				}
			}

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		}

		boolean fecharDialogStatus = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialogStatus", fecharDialogStatus);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialogStatus", fecharDialogStatus);

		return null;

	}

	public NFeConfigIta getConfig() {
		return config;
	}

	public void setConfig(NFeConfigIta config) {
		this.config = config;
	}

	public NTFe getNfe() {
		return nfe;
	}

	public void setNfe(NTFe nfe) {
		this.nfe = nfe;
	}

	public EventosNFe getEventoNFe() {
		return eventoNFe;
	}

	public void setEventoNFe(EventosNFe eventoNFe) {
		this.eventoNFe = eventoNFe;
	}

	public NFeDAO getDaoNfe() {
		return daoNfe;
	}

	public void setDaoNfe(NFeDAO daoNfe) {
		this.daoNfe = daoNfe;
	}

	public ItemNFeDAO getDaoItemNFeSaida() {
		return daoItemNFeSaida;
	}

	public void setDaoItemNFeSaida(ItemNFeDAO daoItemNFeSaida) {
		this.daoItemNFeSaida = daoItemNFeSaida;
	}

	public EventosNFeDAO getEventosNFeDAO() {
		return eventosNFeDAO;
	}

	public void setEventosNFeDAO(EventosNFeDAO eventosNFeDAO) {
		this.eventosNFeDAO = eventosNFeDAO;
	}

	public FiltroNfe getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroNfe filtro) {
		this.filtro = filtro;
	}

	public List<NTFe> getNfes() {
		return nfes;
	}

	public void setNfes(List<NTFe> nfes) {
		this.nfes = nfes;
	}

	public List<NTFe> getNfesFiltrados() {
		return nfesFiltrados;
	}

	public void setNfesFiltrados(List<NTFe> nfesFiltrados) {
		this.nfesFiltrados = nfesFiltrados;
	}

	public List<ItemNTFe> getItensNTFe() {
		return itensNTFe;
	}

	public void setItensNTFe(List<ItemNTFe> itensNTFe) {
		this.itensNTFe = itensNTFe;
	}

	public List<EventosNFe> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventosNFe> eventos) {
		this.eventos = eventos;
	}

	public String getMensagemEvento() {
		return mensagemEvento;
	}

	public void setMensagemEvento(String mensagemEvento) {
		this.mensagemEvento = mensagemEvento;
	}

	public ConfiguracaoDAO getConfiguracaoDao() {
		return configuracaoDao;
	}

	public void setConfiguracaoDao(ConfiguracaoDAO configuracaoDao) {
		this.configuracaoDao = configuracaoDao;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

}
