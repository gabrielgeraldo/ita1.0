package br.com.ita.controle.nfce;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.fincatto.documentofiscal.nfe400.classes.evento.NFEnviaEventoRetorno;
import com.fincatto.documentofiscal.nfe400.classes.evento.NFEventoRetorno;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;

import br.com.ita.controle.util.DanfeUtil;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.XmlUtil;
import br.com.ita.dominio.EventosNFCe;
import br.com.ita.dominio.ItemNTFCe;
import br.com.ita.dominio.NTFCe;
import br.com.ita.dominio.dao.EventosNFCeDAO;
import br.com.ita.dominio.dao.ItemNFCeDAO;
import br.com.ita.dominio.dao.NTFCeDAO;
import br.com.ita.dominio.dao.filtros.FiltroNFCe;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

@Named("consultarNFCeMB")
@RequestScoped
public class ConsultarNFCeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NFeConfigIta config;

	@Inject
	private NTFCe nfce;

	@Inject
	private NTFCeDAO daoNFCe;

	@Inject
	private ItemNFCeDAO daoItem;

	@Inject
	private FiltroNFCe filtro;

	@Inject
	private EventosNFCe eventoNFCe;

	@Inject
	private EventosNFCeDAO eventosNFCeDAO;

	private String mensagemEvento;

	private List<NTFCe> notas = null;
	private List<NTFCe> notasFiltradas = null;
	private List<ItemNTFCe> itens = null;
	private List<EventosNFCe> eventos = null;

	public void consultar() {

		this.notas = daoNFCe.consultar(filtro);

		this.setFiltro(new FiltroNFCe());

	}

	public String acaoConsultar() {
		return "/NFCe/nfceConsultar?faces-redirect=true";
	}

	public String vizualizar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFCe objetoDoBanco = this.daoNFCe.lerPorId(codigo);
		this.setNfce(new NTFCe());
		this.setNfce(objetoDoBanco);

		this.itens = this.daoItem.buscaItens(this.nfce);
		this.eventos = this.eventosNFCeDAO.buscaEventos(this.nfce);

		return "/NFCe/nfceVizualizar";

	}

	public void imprimirDanfe() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFCe nfceDobanco = new NTFCe();
		NTFCeDAO daoNfce = new NTFCeDAO();
		nfceDobanco = daoNfce.lerPorId(codigo);
		this.setNfce(new NTFCe());
		this.setNfce(nfceDobanco);

		try {
			String requestSoap = XmlUtil.lerXml("c:\\ita\\xml\\nfce\\" + this.nfce.getChave() + ".xml",
					Charset.defaultCharset());

			DanfeUtil.imprimirDanfeNfce(requestSoap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemAviso("XML não encontrado. A DANFE não será impressa.", null, null);
		}

	}

	public void downloadXml() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFCe nfceDobanco = new NTFCe();
		NTFCeDAO daoNfce = new NTFCeDAO();
		nfceDobanco = daoNfce.lerPorId(codigo);
		this.setNfce(new NTFCe());
		this.setNfce(nfceDobanco);

		try {
			String requestSoap = XmlUtil.lerXml("c:\\ita\\xml\\nfce\\" + this.nfce.getChave() + ".xml",
					Charset.defaultCharset());

			XmlUtil.downloadXml(requestSoap, this.nfce.getChave());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemAviso("XML não encontrado. O downloand não será realizado.", null, null);
		}

	}

	public String cancelarNota() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFCe objetoDoBanco = this.daoNFCe.lerPorId(codigo);
		this.setNfce(new NTFCe());
		this.setNfce(objetoDoBanco);

		return "/NFCe/nfceCancelar";

	}

	@SuppressWarnings("deprecation")
	public String cancelar() {

		try {
			NFEnviaEventoRetorno retorno = new WSFacade(config).cancelaNota(this.nfce.getChave(),
					this.nfce.getNumProtocolo(), this.mensagemEvento);

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

				// Se concelamento autorizado, a nota será atualizada no bd.
				if (ret.getInfoEventoRetorno().getCodigoStatus().toString().equals("135")) {

					eventoNFCe.setNfce(nfce);

					eventoNFCe.setDataRecebimento(
							java.sql.Timestamp.from(ret.getInfoEventoRetorno().getDataHoraRegistro().toInstant()));
					eventoNFCe.setNumProtocolo(ret.getInfoEventoRetorno().getNumeroProtocolo());
					eventoNFCe.setNumeroSequencialEvento(
							ret.getInfoEventoRetorno().getNumeroSequencialEvento().toString());
					eventoNFCe.setTipoEvento(ret.getInfoEventoRetorno().getTipoEvento());
					eventoNFCe.setAmbienteRecebimento(ret.getInfoEventoRetorno().getAmbiente().getCodigo());

					eventosNFCeDAO.merge(eventoNFCe);

					return "/NFCe/nfceConsultar";

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
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("fecharDialogStatus", fecharDialogStatus);

		return null;
	}

	public NFeConfigIta getConfig() {
		return config;
	}

	public void setConfig(NFeConfigIta config) {
		this.config = config;
	}

	public NTFCe getNfce() {
		return nfce;
	}

	public void setNfce(NTFCe nfce) {
		this.nfce = nfce;
	}

	public NTFCeDAO getDaoNFCe() {
		return daoNFCe;
	}

	public void setDaoNFCe(NTFCeDAO daoNFCe) {
		this.daoNFCe = daoNFCe;
	}

	public ItemNFCeDAO getDaoItem() {
		return daoItem;
	}

	public void setDaoItem(ItemNFCeDAO daoItem) {
		this.daoItem = daoItem;
	}

	public FiltroNFCe getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroNFCe filtro) {
		this.filtro = filtro;
	}

	public List<NTFCe> getNotas() {
		return notas;
	}

	public void setNotas(List<NTFCe> notas) {
		this.notas = notas;
	}

	public List<NTFCe> getNotasFiltradas() {
		return notasFiltradas;
	}

	public void setNotasFiltradas(List<NTFCe> notasFiltradas) {
		this.notasFiltradas = notasFiltradas;
	}

	public List<ItemNTFCe> getItens() {
		return itens;
	}

	public void setItens(List<ItemNTFCe> itens) {
		this.itens = itens;
	}

	public EventosNFCe getEventoNFCe() {
		return eventoNFCe;
	}

	public void setEventoNFCe(EventosNFCe eventoNFCe) {
		this.eventoNFCe = eventoNFCe;
	}

	public EventosNFCeDAO getEventosNFCeDAO() {
		return eventosNFCeDAO;
	}

	public void setEventosNFCeDAO(EventosNFCeDAO eventosNFCeDAO) {
		this.eventosNFCeDAO = eventosNFCeDAO;
	}

	public List<EventosNFCe> getEventos() {
		return eventos;
	}

	public void setEventos(List<EventosNFCe> eventos) {
		this.eventos = eventos;
	}

	public String getMensagemEvento() {
		return mensagemEvento;
	}

	public void setMensagemEvento(String mensagemEvento) {
		this.mensagemEvento = mensagemEvento;
	}

}