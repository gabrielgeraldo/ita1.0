package br.com.ita.controle.imposto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoImpostoTributacaoICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMSST;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaIPI;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaPIS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaMotivoDesoneracaoICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;
import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.dao.imposto.ImpostoDAO;
import br.com.ita.dominio.imposto.Imposto;
import br.com.ita.dominio.imposto.ICMS;

@Named("impostoMB")
@RequestScoped
public class ImpostoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Imposto imposto = new Imposto();

	private ImpostoDAO impostoDao = new ImpostoDAO();

	private List<Imposto> impostos = null;

	private List<Imposto> impostosFiltrados = null;

	private List<NFRegimeTributario> regimeTributario = null;

	private List<NFOrigem> origem = null;

	private List<NFNotaInfoImpostoTributacaoICMS> situacaoTributaria = null;

	private List<NFNotaSituacaoOperacionalSimplesNacional> situacaoOperacaoSN = null;

	private List<NFNotaInfoItemModalidadeBCICMS> modalidadeBCICMS = null;

	private boolean renderizaCompoentesSimplesNacional = false;

	private boolean renderizaCompoentesRegimeNormal = false;

	private boolean renderizaCompoentesCSOSN = false;

	private boolean renderizaCompoentesCST = false;

	private List<NFNotaInfoItemModalidadeBCICMSST> modalidadeBCICMSST = null;

	private List<NFNotaMotivoDesoneracaoICMS> desoneracao = null;

	private List<NFNotaMotivoDesoneracaoICMS> motivoDesoneracaoICMS = null;

	private List<NFNotaInfoSituacaoTributariaIPI> situacaoTributariaIPI = null;

	private List<NFNotaInfoSituacaoTributariaPIS> situacaoTributariaPIS = null;

	private List<NFNotaInfoSituacaoTributariaCOFINS> situacaoTributariaCOFINS = null;

	private List<ICMS> ICMS = null;

	public void renderizaCompoentesSimplesNacionalOuRegimeNormal() {

		if (this.imposto.getRegimeTributario() == NFRegimeTributario.SIMPLES_NACIONAL
				|| this.imposto.getRegimeTributario() == NFRegimeTributario.SIMPLES_NACIONAL_EXCESSO_RECEITA) {
			this.renderizaCompoentesRegimeNormal = true;
		}

		if (this.imposto.getRegimeTributario() == NFRegimeTributario.NORMAL) {
			this.renderizaCompoentesSimplesNacional = true;

		}

	}

	public void renderizaCompoentesCSTOuCSOSN() {

		System.out.println("verificar necessidade do deste metodo");

		// A IDEIA E DESABILITAR OS COMPONENTES APOS A ESCOLHA DO CST OU CSOSN

		// DAR UPDATE NOS CAMPOS QUE DEVEM SER DESABILITADOS.

		// CST 00
		if (this.imposto.getSituacaoTributaria() == NFNotaInfoImpostoTributacaoICMS.TRIBUTACAO_INTEGRALMENTE) {

			renderizaCompoentesCST = true;
			renderizaCompoentesCSOSN = true;

			// DAR UPDATE NOS CAMPOS QUE DEVEM SER DESABILITADOS.

			PrimeFaces.current().ajax().update("form:tabViewImposto:tabViewICMS:modalidadeBCICMS");

		}

	}

	public String listar() {

		return "/Configuracao/impostoListar.xhtml?faces-redirect=true";
	}

	public String salvar() {

		/*
		 * if (this.getImposto().getCodigo() == null) { Imposto objetoDoBanco =
		 * this.impostoDao.lerPorId(imposto.getCodigo());
		 * 
		 * if (objetoDoBanco != null) { JSFUtil.retornarMensagemAviso(null,
		 * "Outro cadastro com o mesmo ICMS/CST já existe no sistema.", null); return
		 * null; } }
		 */

		this.impostoDao.merge(this.getImposto());

		this.setImposto(new Imposto());

		JSFUtil.retornarMensagemInfo(null, "Salvo com sucesso.", null);

		return "/Configuracao/impostoListar";
	}

	public String novo() {

		this.setImposto(new Imposto());

		return "/Configuracao/impostoEditar.xhtml";

	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Imposto objetoDoBanco = this.impostoDao.lerPorId(codigo);
		this.setImposto(objetoDoBanco);

		renderizaCompoentesSimplesNacionalOuRegimeNormal();
		renderizaCompoentesCSTOuCSOSN();

		return "/Configuracao/impostoEditar.xhtml";

	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Imposto objetoDoBanco = this.impostoDao.lerPorId(codigo);
		this.impostoDao.remove(objetoDoBanco);

		if (this.impostoDao.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Excluido com sucesso.", null);
		}

		// limpar o objeto da p�gina
		this.setImposto(new Imposto());
		// limpa a lista
		this.impostos = null;

		return "/Configuracao/impostoListar";

	}

	public Imposto getImposto() {
		return imposto;
	}

	public void setImposto(Imposto imposto) {
		this.imposto = imposto;
	}

	public ImpostoDAO getImpostoDao() {
		return impostoDao;
	}

	public void setImpostoDao(ImpostoDAO impostoDao) {
		this.impostoDao = impostoDao;
	}

	public List<Imposto> getImpostos() {

		if (this.impostos == null)
			this.impostos = this.impostoDao.lerTodos();

		return impostos;
	}

	public void setImpostos(List<Imposto> impostos) {
		this.impostos = impostos;
	}

	public List<Imposto> getImpostosFiltrados() {
		return impostosFiltrados;
	}

	public void setImpostosFiltrados(List<Imposto> impostosFiltrados) {
		this.impostosFiltrados = impostosFiltrados;
	}

	public List<NFRegimeTributario> getRegimeTributario() {

		if (this.regimeTributario == null)
			this.regimeTributario = Arrays.asList(NFRegimeTributario.values());

		return regimeTributario;
	}

	public void setRegimeTributario(List<NFRegimeTributario> regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	public List<NFOrigem> getOrigem() {

		if (this.origem == null)
			this.origem = Arrays.asList(NFOrigem.values());

		return origem;
	}

	public void setOrigem(List<NFOrigem> origem) {
		this.origem = origem;
	}

	public List<NFNotaInfoImpostoTributacaoICMS> getSituacaoTributaria() {

		if (this.situacaoTributaria == null)
			this.situacaoTributaria = Arrays.asList(NFNotaInfoImpostoTributacaoICMS.values());

		return situacaoTributaria;
	}

	public void setSituacaoTributaria(List<NFNotaInfoImpostoTributacaoICMS> situacaoTributaria) {
		this.situacaoTributaria = situacaoTributaria;
	}

	public List<NFNotaSituacaoOperacionalSimplesNacional> getSituacaoOperacaoSN() {

		if (this.situacaoOperacaoSN == null)
			this.situacaoOperacaoSN = Arrays.asList(NFNotaSituacaoOperacionalSimplesNacional.values());

		return situacaoOperacaoSN;
	}

	public void setSituacaoOperacaoSN(List<NFNotaSituacaoOperacionalSimplesNacional> situacaoOperacaoSN) {
		this.situacaoOperacaoSN = situacaoOperacaoSN;
	}

	public boolean isRenderizaCompoentesSimplesNacional() {
		return renderizaCompoentesSimplesNacional;
	}

	public void setRenderizaCompoentesSimplesNacional(boolean renderizaCompoentesSimplesNacional) {
		this.renderizaCompoentesSimplesNacional = renderizaCompoentesSimplesNacional;
	}

	public List<NFNotaInfoItemModalidadeBCICMS> getModalidadeBCICMS() {

		if (this.modalidadeBCICMS == null)
			this.modalidadeBCICMS = Arrays.asList(NFNotaInfoItemModalidadeBCICMS.values());

		return modalidadeBCICMS;
	}

	public void setModalidadeBCICMS(List<NFNotaInfoItemModalidadeBCICMS> modalidadeBCICMS) {
		this.modalidadeBCICMS = modalidadeBCICMS;
	}

	public boolean isRenderizaCompoentesRegimeNormal() {
		return renderizaCompoentesRegimeNormal;
	}

	public void setRenderizaCompoentesRegimeNormal(boolean renderizaCompoentesRegimeNormal) {
		this.renderizaCompoentesRegimeNormal = renderizaCompoentesRegimeNormal;
	}

	public boolean isRenderizaCompoentesCSOSN() {
		return renderizaCompoentesCSOSN;
	}

	public void setRenderizaCompoentesCSOSN(boolean renderizaCompoentesCSOSN) {
		this.renderizaCompoentesCSOSN = renderizaCompoentesCSOSN;
	}

	public boolean isRenderizaCompoentesCST() {
		return renderizaCompoentesCST;
	}

	public void setRenderizaCompoentesCST(boolean renderizaCompoentesCST) {
		this.renderizaCompoentesCST = renderizaCompoentesCST;
	}

	public List<NFNotaInfoItemModalidadeBCICMSST> getModalidadeBCICMSST() {

		if (this.modalidadeBCICMSST == null)
			this.modalidadeBCICMSST = Arrays.asList(NFNotaInfoItemModalidadeBCICMSST.values());

		return modalidadeBCICMSST;
	}

	public void setModalidadeBCICMSST(List<NFNotaInfoItemModalidadeBCICMSST> modalidadeBCICMSST) {
		this.modalidadeBCICMSST = modalidadeBCICMSST;
	}

	public List<NFNotaMotivoDesoneracaoICMS> getDesoneracao() {

		if (this.desoneracao == null)
			this.desoneracao = Arrays.asList(NFNotaMotivoDesoneracaoICMS.values());

		return desoneracao;
	}

	public void setDesoneracao(List<NFNotaMotivoDesoneracaoICMS> desoneracao) {
		this.desoneracao = desoneracao;
	}

	public List<NFNotaMotivoDesoneracaoICMS> getMotivoDesoneracaoICMS() {

		if (this.motivoDesoneracaoICMS == null)
			this.motivoDesoneracaoICMS = Arrays.asList(NFNotaMotivoDesoneracaoICMS.values());

		return motivoDesoneracaoICMS;
	}

	public void setMotivoDesoneracaoICMS(List<NFNotaMotivoDesoneracaoICMS> motivoDesoneracaoICMS) {
		this.motivoDesoneracaoICMS = motivoDesoneracaoICMS;
	}

	public List<NFNotaInfoSituacaoTributariaIPI> getSituacaoTributariaIPI() {

		if (this.situacaoTributariaIPI == null)
			this.situacaoTributariaIPI = Arrays.asList(NFNotaInfoSituacaoTributariaIPI.values());

		return situacaoTributariaIPI;
	}

	public void setSituacaoTributariaIPI(List<NFNotaInfoSituacaoTributariaIPI> situacaoTributariaIPI) {
		this.situacaoTributariaIPI = situacaoTributariaIPI;
	}

	public List<NFNotaInfoSituacaoTributariaPIS> getSituacaoTributariaPIS() {

		if (this.situacaoTributariaPIS == null)
			this.situacaoTributariaPIS = Arrays.asList(NFNotaInfoSituacaoTributariaPIS.values());

		return situacaoTributariaPIS;
	}

	public void setSituacaoTributariaPIS(List<NFNotaInfoSituacaoTributariaPIS> situacaoTributariaPIS) {
		this.situacaoTributariaPIS = situacaoTributariaPIS;
	}

	public List<NFNotaInfoSituacaoTributariaCOFINS> getSituacaoTributariaCOFINS() {

		if (this.situacaoTributariaCOFINS == null)
			this.situacaoTributariaCOFINS = Arrays.asList(NFNotaInfoSituacaoTributariaCOFINS.values());

		return situacaoTributariaCOFINS;
	}

	public void setSituacaoTributariaCOFINS(List<NFNotaInfoSituacaoTributariaCOFINS> situacaoTributariaCOFINS) {
		this.situacaoTributariaCOFINS = situacaoTributariaCOFINS;
	}

	public List<ICMS> getICMS() {

		if (this.ICMS == null)
			this.ICMS = Arrays.asList(br.com.ita.dominio.imposto.ICMS.values());

		return ICMS;
	}

	public void setICMS(List<ICMS> iCMS) {
		ICMS = iCMS;
	}

}
