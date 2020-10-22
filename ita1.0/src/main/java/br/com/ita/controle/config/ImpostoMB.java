package br.com.ita.controle.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMSST;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;

//import com.fincatto.nfe310.classes.NFNotaInfoItemModalidadeBCICMS;
//import com.fincatto.nfe310.classes.NFNotaInfoItemModalidadeBCICMSST;
//import com.fincatto.nfe310.classes.NFNotaSituacaoOperacionalSimplesNacional;
//import com.fincatto.nfe310.classes.NFOrigem;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Imposto;
import br.com.ita.dominio.dao.ImpostoDAO;

@Named("impostoMB")
@RequestScoped
public class ImpostoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Imposto imposto;

	@Inject
	private ImpostoDAO impostoDao;

	private List<Imposto> impostos = null;
	private List<Imposto> impostosFiltrados = null;

	private List<NFNotaSituacaoOperacionalSimplesNacional> situacaoTribdICMS = null;

	private List<NFOrigem> origemdoProduto = null;

	private List<NFNotaInfoItemModalidadeBCICMS> modalidadeBCdoICMS = null;

	private List<NFNotaInfoItemModalidadeBCICMSST> modalidadeBCdaST = null;

	public String listar() {
		return "/Configuracao/impostoListar.xhtml?faces-redirect=true";
	}

	/**
	 * 
	 */
	public String acaoAbrirInclusao() {
		// limpar o objeto da página
		this.setImposto(new Imposto());

		return "/Configuracao/impostoEditar";
	}

	/**
	 * 
	 */
	public String alterarImposto() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Imposto objetoDoBanco = this.impostoDao.lerPorId(codigo);
		this.setImposto(objetoDoBanco);

		return "/Configuracao/impostoEditar";

	}

	/**
	 * 
	 */
	public String acaoSalvar() {
		/**
		 * Deve limpar o ID com valor zero, pois o JSF sempre converte o campo
		 * vazio para um LONG = 0.
		 */
		if ((this.getImposto().getCodigo() != null) && (this.getImposto().getCodigo().longValue() == 0))
			this.getImposto().setCodigo(null);

		/**
		 * Se o usuário não tiver ID, deve testar se existe o mesmo no banco
		 */
		if (this.getImposto().getCodigo() == null) {
			Imposto objetoDoBanco = this.impostoDao.lerPorDescricao(this.getImposto().getDescricao());

			System.out.println(objetoDoBanco);

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso("Outra imposto com a mesma descrição já existe no sistema.", null,
						null);
				return null; // volta p/mesma página
			}
		}

		this.impostoDao.merge(this.getImposto());
		// limpa a lista
		this.impostos = null;

		// limpar o objeto da página
		this.setImposto(new Imposto());

		return "/Configuracao/impostoListar";
	}

	/**
	 * 
	 */
	public String acaoCancelar() {
		// limpar o objeto da página
		this.setImposto(new Imposto());

		return "/Configuracao/impostoListar";
	}

	/**
	 * 
	 */
	public String excluirImposto() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Imposto objetoDoBanco = this.impostoDao.lerPorId(codigo);
		this.impostoDao.remove(objetoDoBanco);

		// limpar o objeto da página
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

	public ImpostoDAO getImpostoDao() {
		return impostoDao;
	}

	public void setImpostoDao(ImpostoDAO impostoDao) {
		this.impostoDao = impostoDao;
	}

	public List<NFNotaSituacaoOperacionalSimplesNacional> getSituacaoTribdICMS() {
		if (this.situacaoTribdICMS == null)
			this.situacaoTribdICMS = Arrays.asList(NFNotaSituacaoOperacionalSimplesNacional.values());
		return situacaoTribdICMS;
	}

	public void setSituacaoTribdICMS(List<NFNotaSituacaoOperacionalSimplesNacional> situacaoTribdICMS) {
		this.situacaoTribdICMS = situacaoTribdICMS;
	}

	public List<NFOrigem> getOrigemdoProduto() {
		if (this.origemdoProduto == null)
			this.origemdoProduto = Arrays.asList(NFOrigem.values());
		return origemdoProduto;
	}

	public void setOrigemdoProduto(List<NFOrigem> origemdoProduto) {
		this.origemdoProduto = origemdoProduto;
	}

	public List<NFNotaInfoItemModalidadeBCICMS> getModalidadeBCdoICMS() {
		if (this.modalidadeBCdoICMS == null)
			this.modalidadeBCdoICMS = Arrays.asList(NFNotaInfoItemModalidadeBCICMS.values());
		return modalidadeBCdoICMS;
	}

	public void setModalidadeBCdoICMS(List<NFNotaInfoItemModalidadeBCICMS> modalidadeBCdoICMS) {
		this.modalidadeBCdoICMS = modalidadeBCdoICMS;
	}

	public List<NFNotaInfoItemModalidadeBCICMSST> getModalidadeBCdaST() {
		if (this.modalidadeBCdaST == null)
			this.modalidadeBCdaST = Arrays.asList(NFNotaInfoItemModalidadeBCICMSST.values());
		return modalidadeBCdaST;
	}

	public void setModalidadeBCdaST(List<NFNotaInfoItemModalidadeBCICMSST> modalidadeBCdaST) {
		this.modalidadeBCdaST = modalidadeBCdaST;
	}

}
