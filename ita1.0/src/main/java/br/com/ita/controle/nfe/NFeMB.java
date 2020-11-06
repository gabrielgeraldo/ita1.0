package br.com.ita.controle.nfe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.fincatto.documentofiscal.nfe400.classes.NFFinalidade;
import com.fincatto.documentofiscal.nfe400.classes.NFModalidadeFrete;
import com.fincatto.documentofiscal.nfe400.classes.NFTipo;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIdentificadorLocalDestinoOperacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorPresencaComprador;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFOperacaoConsumidorFinal;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.CondicaoPagamento;
import br.com.ita.dominio.FormaPagamento;
import br.com.ita.dominio.ItemNTFe;
import br.com.ita.dominio.ItemOrcamento;
import br.com.ita.dominio.NFEntrada;
import br.com.ita.dominio.NTFe;
import br.com.ita.dominio.Orcamento;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.TipoPesquisaProduto;
import br.com.ita.dominio.dao.ClienteDAO;
import br.com.ita.dominio.dao.CondicaoPagamentoDAO;
import br.com.ita.dominio.dao.ItemNFeDAO;
import br.com.ita.dominio.dao.ItemOrcamentoDAO;
import br.com.ita.dominio.dao.NFEntradaDAO;
import br.com.ita.dominio.dao.NFeDAO;
import br.com.ita.dominio.dao.OrcamentoDAO;
import br.com.ita.dominio.dao.ProdutoDAO;
import br.com.ita.dominio.dao.util.CfopDAO;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;
import br.com.ita.dominio.util.Cfop;

@Named("nfeMB")
@ConversationScoped
public class NFeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversacao;

	@Inject
	private NTFe nfe;

	@Inject
	private Produto produto;

	@Inject
	private ProdutoDAO daoProduto;

	@Inject
	private ClienteDAO daoClientes;

	@Inject
	private ItemNTFe itemNFe;

	@Inject
	private CfopDAO daoCfop;

	@Inject
	private NFeDAO daoNFe;

	@Inject
	private ControleNumerosDAO daoControleNumeros;

	private List<ItemNTFe> itensNfe = null;

	private List<Cfop> cfop = null;

	private List<NTFe> notasParaCorrigir = null;

	private List<NTFe> notasParaCorrigirFiltrados = null;

	private List<NFTipo> tipos = null;

	private List<NFIdentificadorLocalDestinoOperacao> nfIdentificadorLocalDestinoOperacao = null;

	private List<NFOperacaoConsumidorFinal> nfOperacaoConsumidorFinal = null;

	private List<NFIndicadorPresencaComprador> nfIndicadorPresencaComprador = null;

	private List<NFModalidadeFrete> nfModalidadeFrete = null;

	private List<NFFinalidade> nfFinalidade = null;

	@Inject
	private NFEntradaDAO daoNFEntrada;

	private boolean desabilitaHabilita;

	@Inject
	private OrcamentoDAO daoOrcamento;

	@Inject
	private ItemOrcamentoDAO daoItemOrcamento;

	private List<CondicaoPagamento> condicoesPagamentos = null;

	@Inject
	private CondicaoPagamentoDAO condicaoPagamentoDao;

	private boolean renderizaCompoentesDeParcelas;

	private boolean desabilitaHabilitaCompoentesDeParcelas;

	private boolean devolucao;

	private List<TipoPesquisaProduto> tiposPesquisaProduto = null;

	private TipoPesquisaProduto tipoPesquisaProduto;

	public void selecionaDevolucao() {

		if (this.nfe.getNfFfinalidade() == NFFinalidade.DEVOLUCAO_OU_RETORNO) {

			this.devolucao = false;
			this.nfe.setCliente(null);

		} else {
			this.nfe.setNfEntrada(null);
			this.devolucao = true;

		}

	}

	public void selecionaSerie() {
		nfe.setNumero(
				daoControleNumeros.buscaNumeroPorTabelaEChave("nfe", String.valueOf(nfe.getSerie())).getNumeroAtual());
	}

	public void renderizaCompoentesDeParcelas() {
		if (this.nfe.getCondicaoPagamento().getFormaPagamento() == FormaPagamento.APRAZO)
			this.renderizaCompoentesDeParcelas = true;
		else
			this.renderizaCompoentesDeParcelas = false;

	}

	public void calcularParcelas() {

		this.nfe.calculaParcelas();

		JSFUtil.retornarMensagemAviso(null, "Parcelas calculadas.", null);

	}

	public void excluirParcelas() {

		this.nfe.excluirParcelas();

		JSFUtil.retornarMensagemAviso(null, "Parcelas excluidas.", null);
	}

	public List<Cliente> completeCliente(String cliente) {
		return this.daoClientes.autoCompleteCliente(cliente);
	}

	public List<Produto> completeProduto(String produto) {

		if (tipoPesquisaProduto == TipoPesquisaProduto.CODIGO) {
			return this.daoProduto.autoCompleteProdutoPorCodigo(produto);
		}

		if (tipoPesquisaProduto == TipoPesquisaProduto.DESCRICAO) {
			return this.daoProduto.autoCompleteProdutoPorDescricao(produto);
		}

		return null;
	}

	public List<NFEntrada> completeNFEntrada(String nfEntrada) {
		return this.daoNFEntrada.autoCompleteNFEntrada(nfEntrada);
	}

	public List<Orcamento> completeOrcamento(String orcamento) {
		return this.daoOrcamento.autoCompleteOrcamento(orcamento);
	}

	public void adicionar() {

		Produto produto = itemNFe.getProduto();

		// -------------------- Método adicionar.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensNfe.size() && posicaoEncntrada < 0; i++) {
			ItemNTFe itemTemp = itensNfe.get(i);

			if (itemTemp.getProduto().equals(produto)) {
				posicaoEncntrada = i;
			}
		}

		itemNFe.setProduto(produto);

		if (posicaoEncntrada < 0) {
			itemNFe.setPrecoCusto(produto.getPrecoCusto());
			itemNFe.setPrecoVenda(produto.getPrecoUnitario());
			itensNfe.add(itemNFe);
		} else {
			ItemNTFe itemTemp = itensNfe.get(posicaoEncntrada);
			itemNFe.setQuantidade(itemTemp.getQuantidade() + itemNFe.getQuantidade());
			itemNFe.setPrecoCusto(produto.getPrecoCusto());
			itemNFe.setPrecoVenda(produto.getPrecoUnitario());
			itensNfe.set(posicaoEncntrada, itemNFe);
		}

		nfe.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensNfe.size(); j++) {
			nfe.setTotal(nfe.getTotal()
					.add(itensNfe.get(j).getPrecoVenda().multiply(new BigDecimal(itensNfe.get(j).getQuantidade()))));

		}
		// -------------------- Método adicionar.

		this.setItemNFe(new ItemNTFe());
		itemNFe.setQuantidade(1);

		boolean fecharDialog = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialog", fecharDialog);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialog", fecharDialog);

		JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

	}

	public void importarOrcamento() {

		if (nfe.getOrcamento() == null) {

			JSFUtil.retornarMensagemAviso(null, "Orçamento não selecionado.", null);

		} else {

			nfe.setCliente(nfe.getOrcamento().getCliente());
			nfe.setNfEntrada(null);

			List<ItemOrcamento> itensPv = daoItemOrcamento.buscaItens(this.nfe.getOrcamento());

			for (ItemOrcamento item : itensPv) {

				ItemNTFe itenNfe = new ItemNTFe();

				itenNfe.setCfop(this.cfop.get(289));
				itenNfe.setCodigo(item.getCodigo());
				itenNfe.setNfe(this.nfe);
				itenNfe.setPrecoCusto(item.getPrecoCusto());
				itenNfe.setPrecoVenda(item.getPrecoVenda());
				itenNfe.setProduto(item.getProduto());
				itenNfe.setQuantidade(item.getQuantidade());

				this.itensNfe.add(itenNfe);

			}

			nfe.setTotal(nfe.getOrcamento().getTotal());

			this.desabilitaHabilita = true;

			boolean fecharDialog = true;
			// RequestContext context = RequestContext.getCurrentInstance();
			// context.addCallbackParam("fecharDialog", fecharDialog);
			PrimeFaces.current().ajax().addCallbackParam("fecharDialog", fecharDialog);

			JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

		}
	}

	public void removerProduto(ItemNTFe itemNTFe) {

		// -------------------- M�todo remover.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensNfe.size() && posicaoEncntrada < 0; i++) {
			ItemNTFe itemTemp = itensNfe.get(i);

			if (itemTemp.getProduto().equals(itemNTFe.getProduto())) {
				posicaoEncntrada = i;
			}
		}

		if (posicaoEncntrada > -1) {
			itensNfe.remove(posicaoEncntrada);
		}

		nfe.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensNfe.size(); j++) {
			nfe.setTotal(nfe.getTotal()
					.add(itensNfe.get(j).getPrecoVenda().multiply(new BigDecimal(itensNfe.get(j).getQuantidade()))));

		}
		// -------------------- M�todo remover.

	}

	public String listar() {

		return "/NFe/nfeListar?faces-redirect=true";

	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setNfe(new NTFe());

		return "/NFe/nfeListar";
	}

	public String novo() {

		if (conversacao.isTransient()) {
			conversacao.setTimeout(900000);
			conversacao.begin();
		}

		this.desabilitaHabilita = false;
		this.renderizaCompoentesDeParcelas = false;
		// this.desabilitaHabilitaCompoentesDeParcelas = true;

		this.devolucao = true;

		this.setNfe(new NTFe());

		nfe.setTipo(NFTipo.SAIDA);
		nfe.setNfIdentificadorLocalDestinoOperacao(NFIdentificadorLocalDestinoOperacao.OPERACAO_INTERNA);
		nfe.setNfOperacaoConsumidorFinal(NFOperacaoConsumidorFinal.SIM);
		nfe.setNfIndicadorPresencaComprador(NFIndicadorPresencaComprador.NAO_APLICA);
		nfe.setNfModalidadeFrete(NFModalidadeFrete.SEM_OCORRENCIA_TRANSPORTE);

		this.setItensNfe(new ArrayList<ItemNTFe>());

		nfe.setTotal(new BigDecimal("0.00"));

		itemNFe.setQuantidade(1);

		this.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);

		return "/NFe/nfeEditar";

	}

	public String alterarNFe() {

		if (conversacao.isTransient()) {
			conversacao.setTimeout(900000);
			conversacao.begin();
		}

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NTFe nfeDobanco = new NTFe();
		NFeDAO daoNfe = new NFeDAO();
		nfeDobanco = daoNfe.lerPorId(codigo);
		this.setNfe(new NTFe());
		this.setNfe(nfeDobanco);

		List<ItemNTFe> itensNfeDoBanco = new ArrayList<ItemNTFe>();
		ItemNFeDAO daoItemNfe = new ItemNFeDAO();
		itensNfeDoBanco = daoItemNfe.buscaItens(this.getNfe());
		this.setItensNfe(new ArrayList<ItemNTFe>());
		this.setItensNfe(itensNfeDoBanco);

		if (this.nfe.getOrcamento() != null)
			this.desabilitaHabilita = true;
		else
			this.desabilitaHabilita = false;

		this.renderizaCompoentesDeParcelas();
		this.desabilitaHabilitaCompoentesDeParcelas = true;

		this.selecionaDevolucao();

		return "/NFe/nfeEditar";

	}

	public String finalizar() {

		if (this.nfe.getNfFfinalidade() != NFFinalidade.DEVOLUCAO_OU_RETORNO && this.nfe.getCliente() == null) {

			JSFUtil.retornarMensagemErro(null, "O cliente é de preenchimento obrigatório.", null);

			return null;

		}

		if (this.nfe.getNfFfinalidade() == NFFinalidade.DEVOLUCAO_OU_RETORNO && this.nfe.getNfEntrada() == null) {

			JSFUtil.retornarMensagemErro(null, "A Nota fiscal de entrada é de preenchimento obrigatório.", null);

			return null;

		}

		try {

			if (this.nfe.getCondicaoPagamento().getFormaPagamento() == FormaPagamento.APRAZO
					&& this.nfe.getContasReceber() == null && this.nfe.getNumeroDeParcelas() >= 1) {

				this.nfe.calculaParcelas();

				JSFUtil.retornarMensagemAviso(null, "Parcelas calculadas.", null);

				return null;

			}

			daoNFe.salvarNFe(nfe, itensNfe);

			nfe = new NTFe();
			nfe.setTotal(new BigDecimal("0.00"));

			itensNfe = new ArrayList<ItemNTFe>();

			// Gambiarra para atualizar datatable ao finalizar.
			this.notasParaCorrigir = null;
			this.notasParaCorrigir = this.getNotasParaCorrigir();

			if (!conversacao.isTransient()) {
				conversacao.end();
			}

			this.novo();

			return null;

		} catch (Exception e) {

			System.out.println(e);

			JSFUtil.retornarMensagemErro(null, "Erro ao salvar/alterar NF-e: " + e.getMessage(), null);

			return null;

		}

	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ProdutoDAO getDaoProduto() {
		return daoProduto;
	}

	public void setDaoProduto(ProdutoDAO daoProduto) {
		this.daoProduto = daoProduto;
	}

	public NTFe getNfe() {
		return nfe;
	}

	public void setNfe(NTFe nfe) {
		this.nfe = nfe;
	}

	public ClienteDAO getDaoClientes() {
		return daoClientes;
	}

	public void setDaoClientes(ClienteDAO daoClientes) {
		this.daoClientes = daoClientes;
	}

	public ItemNTFe getItemNFe() {
		return itemNFe;
	}

	public void setItemNFe(ItemNTFe itemNFe) {
		this.itemNFe = itemNFe;
	}

	public List<ItemNTFe> getItensNfe() {
		return itensNfe;
	}

	public void setItensNfe(List<ItemNTFe> itensNfe) {
		this.itensNfe = itensNfe;
	}

	public List<NFTipo> getTipos() {
		if (this.tipos == null)
			this.tipos = Arrays.asList(NFTipo.values());
		return tipos;
	}

	public void setTipos(List<NFTipo> tipos) {
		this.tipos = tipos;
	}

	public List<NFIdentificadorLocalDestinoOperacao> getNfIdentificadorLocalDestinoOperacao() {
		if (this.nfIdentificadorLocalDestinoOperacao == null)
			this.nfIdentificadorLocalDestinoOperacao = Arrays.asList(NFIdentificadorLocalDestinoOperacao.values());
		return nfIdentificadorLocalDestinoOperacao;
	}

	public void setNfIdentificadorLocalDestinoOperacao(
			List<NFIdentificadorLocalDestinoOperacao> nfIdentificadorLocalDestinoOperacao) {
		this.nfIdentificadorLocalDestinoOperacao = nfIdentificadorLocalDestinoOperacao;
	}

	public List<NFOperacaoConsumidorFinal> getNfOperacaoConsumidorFinal() {
		if (this.nfOperacaoConsumidorFinal == null)
			this.nfOperacaoConsumidorFinal = Arrays.asList(NFOperacaoConsumidorFinal.values());
		return nfOperacaoConsumidorFinal;
	}

	public void setNfOperacaoConsumidorFinal(List<NFOperacaoConsumidorFinal> nfOperacaoConsumidorFinal) {
		this.nfOperacaoConsumidorFinal = nfOperacaoConsumidorFinal;
	}

	public List<NFIndicadorPresencaComprador> getNfIndicadorPresencaComprador() {
		if (this.nfIndicadorPresencaComprador == null)
			this.nfIndicadorPresencaComprador = Arrays.asList(NFIndicadorPresencaComprador.values());
		return nfIndicadorPresencaComprador;
	}

	public void setNfIndicadorPresencaComprador(List<NFIndicadorPresencaComprador> nfIndicadorPresencaComprador) {
		this.nfIndicadorPresencaComprador = nfIndicadorPresencaComprador;
	}

	public List<NFModalidadeFrete> getNfModalidadeFrete() {
		if (this.nfModalidadeFrete == null)
			this.nfModalidadeFrete = Arrays.asList(NFModalidadeFrete.values());
		return nfModalidadeFrete;
	}

	public void setNfModalidadeFrete(List<NFModalidadeFrete> nfModalidadeFrete) {
		this.nfModalidadeFrete = nfModalidadeFrete;
	}

	public List<NFFinalidade> getNfFinalidade() {
		if (this.nfFinalidade == null)
			this.nfFinalidade = Arrays.asList(NFFinalidade.values());
		return nfFinalidade;
	}

	public void setNfFinalidade(List<NFFinalidade> nfFinalidade) {
		this.nfFinalidade = nfFinalidade;
	}

	public List<Cfop> getCfop() {

		if (this.cfop == null)
			this.cfop = this.daoCfop.lerTodos();

		return cfop;
	}

	public void setCfop(List<Cfop> cfop) {
		this.cfop = cfop;
	}

	public CfopDAO getDaoCfop() {
		return daoCfop;
	}

	public void setDaoCfop(CfopDAO daoCfop) {
		this.daoCfop = daoCfop;
	}

	public ControleNumerosDAO getDaoControleNumeros() {
		return daoControleNumeros;
	}

	public void setDaoControleNumeros(ControleNumerosDAO daoControleNumeros) {
		this.daoControleNumeros = daoControleNumeros;
	}

	public List<NTFe> getNotasParaCorrigir() {

		if (this.notasParaCorrigir == null)
			this.notasParaCorrigir = daoNFe.buscaNotasNaoTramitidas();

		return notasParaCorrigir;
	}

	public void setNotasParaCorrigir(List<NTFe> notasParaCorrigir) {
		this.notasParaCorrigir = notasParaCorrigir;
	}

	public List<NTFe> getNotasParaCorrigirFiltrados() {
		return notasParaCorrigirFiltrados;
	}

	public void setNotasParaCorrigirFiltrados(List<NTFe> notasParaCorrigirFiltrados) {
		this.notasParaCorrigirFiltrados = notasParaCorrigirFiltrados;
	}

	public Conversation getConversacao() {
		return conversacao;
	}

	public void setConversacao(Conversation conversacao) {
		this.conversacao = conversacao;
	}

	public NFeDAO getDaoNFe() {
		return daoNFe;
	}

	public void setDaoNFe(NFeDAO daoNFe) {
		this.daoNFe = daoNFe;
	}

	public NFEntradaDAO getDaoNFEntrada() {
		return daoNFEntrada;
	}

	public void setDaoNFEntrada(NFEntradaDAO daoNFEntrada) {
		this.daoNFEntrada = daoNFEntrada;
	}

	public OrcamentoDAO getDaoOrcamento() {
		return daoOrcamento;
	}

	public void setDaoOrcamento(OrcamentoDAO daoOrcamento) {
		this.daoOrcamento = daoOrcamento;
	}

	public boolean isDesabilitaHabilita() {
		return desabilitaHabilita;
	}

	public void setDesabilitaHabilita(boolean desabilitaHabilita) {
		this.desabilitaHabilita = desabilitaHabilita;
	}

	public ItemOrcamentoDAO getDaoItemOrcamento() {
		return daoItemOrcamento;
	}

	public void setDaoItemOrcamento(ItemOrcamentoDAO daoItemOrcamento) {
		this.daoItemOrcamento = daoItemOrcamento;
	}

	public List<CondicaoPagamento> getCondicoesPagamentos() {
		if (this.condicoesPagamentos == null)
			this.condicoesPagamentos = this.condicaoPagamentoDao.lerTodos();
		return condicoesPagamentos;
	}

	public void setCondicoesPagamentos(List<CondicaoPagamento> condicoesPagamentos) {
		this.condicoesPagamentos = condicoesPagamentos;
	}

	public CondicaoPagamentoDAO getCondicaoPagamentoDao() {
		return condicaoPagamentoDao;
	}

	public void setCondicaoPagamentoDao(CondicaoPagamentoDAO condicaoPagamentoDao) {
		this.condicaoPagamentoDao = condicaoPagamentoDao;
	}

	public boolean isRenderizaCompoentesDeParcelas() {
		return renderizaCompoentesDeParcelas;
	}

	public void setRenderizaCompoentesDeParcelas(boolean renderizaCompoentesDeParcelas) {
		this.renderizaCompoentesDeParcelas = renderizaCompoentesDeParcelas;
	}

	public boolean isDesabilitaHabilitaCompoentesDeParcelas() {
		return desabilitaHabilitaCompoentesDeParcelas;
	}

	public void setDesabilitaHabilitaCompoentesDeParcelas(boolean desabilitaHabilitaCompoentesDeParcelas) {
		this.desabilitaHabilitaCompoentesDeParcelas = desabilitaHabilitaCompoentesDeParcelas;
	}

	public boolean isDevolucao() {
		return devolucao;
	}

	public void setDevolucao(boolean devolucao) {
		this.devolucao = devolucao;
	}

	public List<TipoPesquisaProduto> getTiposPesquisaProduto() {

		if (this.tiposPesquisaProduto == null)
			this.tiposPesquisaProduto = Arrays.asList(TipoPesquisaProduto.values());

		return tiposPesquisaProduto;
	}

	public void setTiposPesquisaProduto(List<TipoPesquisaProduto> tiposPesquisaProduto) {
		this.tiposPesquisaProduto = tiposPesquisaProduto;
	}

	public TipoPesquisaProduto getTipoPesquisaProduto() {
		return tipoPesquisaProduto;
	}

	public void setTipoPesquisaProduto(TipoPesquisaProduto tipoPesquisaProduto) {
		this.tipoPesquisaProduto = tipoPesquisaProduto;
	}

}
