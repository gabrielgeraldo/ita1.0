package br.com.ita.controle.venda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.venda.util.VendaService;
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.CondicaoPagamento;
import br.com.ita.dominio.ItemOrcamento;
import br.com.ita.dominio.ItemVenda;
import br.com.ita.dominio.Orcamento;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.TipoPesquisaProduto;
import br.com.ita.dominio.Venda;
import br.com.ita.dominio.dao.ClienteDAO;
import br.com.ita.dominio.dao.CondicaoPagamentoDAO;
import br.com.ita.dominio.dao.ItemOrcamentoDAO;
import br.com.ita.dominio.dao.OrcamentoDAO;
import br.com.ita.dominio.dao.ProdutoDAO;
import br.com.ita.dominio.dao.VendaDAO;

@Named("vendaMB")
@ViewScoped
public class EfetuarVendaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Produto> produtos = null;

	private List<Produto> produtosFiltrados = null;

	@Inject
	private ProdutoDAO daoProduto;

	private List<CondicaoPagamento> condicoesPagamentos = null;

	@Inject
	private CondicaoPagamentoDAO condicaoPagamentoDao;

	@Inject
	private ClienteDAO daoClientes;

	@Inject
	private Venda venda;

	@Inject
	private Produto produto;

	@Inject
	private ItemVenda itemVenda;

	private List<ItemVenda> itensVenda;

	private String produtoEscolhido;

	private VendaService vendaService = null;

	private boolean desabilitaHabilita;

	@Inject
	private ItemOrcamentoDAO daoItemOrcamento;

	@Inject
	private OrcamentoDAO daoOrcamento;

	private List<TipoPesquisaProduto> tiposPesquisaProduto = null;

	private TipoPesquisaProduto tipoPesquisaProduto;

	@PostConstruct
	public void init() {
		venda.setTotal(new BigDecimal("0.00"));
		this.setItensVenda(new ArrayList<ItemVenda>());
		
		itemVenda.setQuantidade(1);

		this.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);

	}

	public List<Produto> completeProduto(String produto) {
		if (tipoPesquisaProduto == TipoPesquisaProduto.CODIGO) {
			return this.daoProduto.autoCompleteProdutoPorCodigo(produto);
		}

		if (tipoPesquisaProduto == TipoPesquisaProduto.CODIGOBARRAS) {
			return this.daoProduto.autoCompleteProdutoPorCodigoDeBarras(produto);
		}

		if (tipoPesquisaProduto == TipoPesquisaProduto.DESCRICAO) {
			return this.daoProduto.autoCompleteProdutoPorDescricao(produto);
		}

		return null;
	}

	public List<Cliente> completeCliente(String cliente) {
		return this.daoClientes.autoCompleteCliente(cliente);
	}

	public List<Orcamento> completeOrcamento(String orcamento) {
		return this.daoOrcamento.autoCompleteOrcamento(orcamento);
	}

	public void adicionarProdutoPeloCodigo() {

		Produto produto = null;

		if (produtoEscolhido != null) {
			produto = daoProduto.lerPorId(produtoEscolhido);
		} else {
			JSFUtil.retornarMensagemErro(null, "Produto n�o cadastrado.", null);
		}

		if (produto != null) {

			int posicaoEncntrada = -1;

			for (int i = 0; i < itensVenda.size() && posicaoEncntrada < 0; i++) {
				ItemVenda itemTemp = itensVenda.get(i);

				if (itemTemp.getProduto().equals(produto)) {
					posicaoEncntrada = i;
				}
			}

			itemVenda.setProduto(produto);

			if (posicaoEncntrada < 0) {
				itemVenda.setQuantidade(1);
				itemVenda.setPrecoCusto(produto.getPrecoCusto());
				itemVenda.setPrecoVenda(produto.getPrecoUnitario());
				itensVenda.add(itemVenda);
			} else {
				ItemVenda itemTemp = itensVenda.get(posicaoEncntrada);
				itemVenda.setQuantidade(itemTemp.getQuantidade() + 1);
				itemVenda.setPrecoCusto(produto.getPrecoCusto());
				itemVenda.setPrecoVenda(produto.getPrecoUnitario().multiply(new BigDecimal(itemVenda.getQuantidade())));
				itensVenda.set(posicaoEncntrada, itemVenda);
			}

			venda.setTotal(venda.getTotal().add(produto.getPrecoUnitario()));

			this.setItemVenda(new ItemVenda());

		} else if (produto == null && produtoEscolhido != null) {
			JSFUtil.retornarMensagemErro(null, "Produto n�o cadastrado.", null);
		}

		this.produtoEscolhido = null;

	}

	public void adicionarProdutoPorPesquisa() {

		Produto produto = itemVenda.getProduto();

		int posicaoEncntrada = -1;

		for (int i = 0; i < itensVenda.size() && posicaoEncntrada < 0; i++) {
			ItemVenda itemTemp = itensVenda.get(i);

			if (itemTemp.getProduto().equals(produto)) {
				posicaoEncntrada = i;
			}
		}

		itemVenda.setProduto(produto);

		if (posicaoEncntrada < 0) {
			itemVenda.setQuantidade(1);
			itemVenda.setPrecoCusto(produto.getPrecoCusto());
			itemVenda.setPrecoVenda(produto.getPrecoUnitario());
			itensVenda.add(itemVenda);
		} else {
			ItemVenda itemTemp = itensVenda.get(posicaoEncntrada);
			itemVenda.setQuantidade(itemTemp.getQuantidade() + 1);
			itemVenda.setPrecoCusto(produto.getPrecoCusto());
			itemVenda.setPrecoVenda(produto.getPrecoUnitario().multiply(new BigDecimal(itemVenda.getQuantidade())));
			itensVenda.set(posicaoEncntrada, itemVenda);
		}

		venda.setTotal(venda.getTotal().add(produto.getPrecoUnitario()));

		this.setItemVenda(new ItemVenda());

		JSFUtil.retornarMensagemInfo(null, "Produto adicionado.", null);

	}

	public void adicionar() {

		Produto produto = itemVenda.getProduto();

		// -------------------- Método adicionar.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensVenda.size() && posicaoEncntrada < 0; i++) {
			ItemVenda itemTemp = itensVenda.get(i);

			if (itemTemp.getProduto().equals(produto)) {
				posicaoEncntrada = i;
			}
		}

		itemVenda.setProduto(produto);

		if (posicaoEncntrada < 0) {
			itemVenda.setPrecoCusto(produto.getPrecoCusto());
			itemVenda.setPrecoVenda(produto.getPrecoUnitario());
			itensVenda.add(itemVenda);
		} else {
			ItemVenda itemTemp = itensVenda.get(posicaoEncntrada);
			itemVenda.setQuantidade(itemTemp.getQuantidade() + itemVenda.getQuantidade());
			itemVenda.setPrecoCusto(produto.getPrecoCusto());
			itemVenda.setPrecoVenda(produto.getPrecoUnitario());
			itensVenda.set(posicaoEncntrada, itemVenda);
		}

		venda.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensVenda.size(); j++) {
			venda.setTotal(venda.getTotal().add(
					itensVenda.get(j).getPrecoVenda().multiply(new BigDecimal(itensVenda.get(j).getQuantidade()))));

		}
		// -------------------- Método adicionar.

		this.setItemVenda(new ItemVenda());
		itemVenda.setQuantidade(1);

		boolean fecharDialog = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialog", fecharDialog);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialog", fecharDialog);

		JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

	}

	public void importarOrcamento() {

		if (venda.getOrcamento() == null) {

			JSFUtil.retornarMensagemAviso(null, "Orçamento não selecionado.", null);

		} else {

			venda.setCliente(venda.getOrcamento().getCliente());

			List<ItemOrcamento> itensPv = daoItemOrcamento.buscaItens(this.venda.getOrcamento());

			for (ItemOrcamento item : itensPv) {

				ItemVenda itenVenda = new ItemVenda();

				itenVenda.setCodigo(item.getCodigo());
				itenVenda.setVenda(this.venda);
				itenVenda.setPrecoCusto(item.getPrecoCusto());
				itenVenda.setPrecoVenda(item.getPrecoVenda());
				itenVenda.setProduto(item.getProduto());
				itenVenda.setQuantidade(item.getQuantidade());

				this.itensVenda.add(itenVenda);

			}

			venda.setTotal(venda.getOrcamento().getTotal());

			this.setDesabilitaHabilita(true);

			JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

		}
	}

	public void removerProduto(ItemVenda itemVenda) {

		int posicaoEncntrada = -1;

		for (int i = 0; i < itensVenda.size() && posicaoEncntrada < 0; i++) {
			ItemVenda itemTemp = itensVenda.get(i);

			if (itemTemp.getProduto().equals(itemVenda.getProduto())) {
				posicaoEncntrada = i;
			}
		}

		if (posicaoEncntrada > -1) {
			itensVenda.remove(posicaoEncntrada);
			venda.setTotal(venda.getTotal().subtract(itemVenda.getPrecoVenda()));
		}

	}

	public String iniciarVenda() {

		venda = new Venda();
		venda.setTotal(new BigDecimal("0.00"));

		itensVenda = new ArrayList<ItemVenda>();

		this.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);
		
		itemVenda.setQuantidade(1);

		return "/EfetuarVenda/efetuarVenda?faces-redirect=true";
	}

	public String finalizarVenda() {

		if (venda.getTotal().equals(new BigDecimal("0.00"))) {
			JSFUtil.retornarMensagemErro(null, "Esta venda deve ter 1 ou mais itens.", null);
			return null;
		}

		if (venda.getTotal().compareTo(venda.getValorPagamento()) > 0) {
			JSFUtil.retornarMensagemErro(null, "Valor pagamento menor que o total.", null);
			return null;
		}

		venda.setSituacao("FINALIZADA");

		VendaDAO daoVenda = new VendaDAO();
		Venda vendaSalva = daoVenda.salvarVenda(venda, itensVenda);

		if (vendaSalva != null && vendaSalva.getCodigo() != null)
			JSFUtil.retornarMensagemInfo(null, "Venda finalizada com sucesso! Código: " + vendaSalva.getCodigo(), null);

		this.iniciarVenda();

		return "/EfetuarVenda/efetuarVenda";

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

	public List<Produto> getProdutos() {
		return this.produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public String getProdutoEscolhido() {
		return produtoEscolhido;
	}

	public void setProdutoEscolhido(String produtoEscolhido) {
		this.produtoEscolhido = produtoEscolhido;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
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

	public ClienteDAO getDaoClientes() {
		return daoClientes;
	}

	public void setDaoClientes(ClienteDAO daoClientes) {
		this.daoClientes = daoClientes;
	}

	public ItemVenda getItemVenda() {
		return itemVenda;
	}

	public void setItemVenda(ItemVenda itemVenda) {
		this.itemVenda = itemVenda;
	}

	public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public void setProdutosFiltrados(List<Produto> produtosFiltrados) {
		this.produtosFiltrados = produtosFiltrados;
	}

	public VendaService getVendaService() {
		return vendaService;
	}

	public void setVendaService(VendaService vendaService) {
		this.vendaService = vendaService;
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

	public OrcamentoDAO getDaoOrcamento() {
		return daoOrcamento;
	}

	public void setDaoItemOrcamento(ItemOrcamentoDAO daoItemOrcamento) {
		this.daoItemOrcamento = daoItemOrcamento;
	}

	public void setDaoOrcamento(OrcamentoDAO daoOrcamento) {
		this.daoOrcamento = daoOrcamento;
	}

	public List<TipoPesquisaProduto> getTiposPesquisaProduto() {
		if (this.tiposPesquisaProduto == null)
			this.tiposPesquisaProduto = Arrays.asList(TipoPesquisaProduto.values());

		return tiposPesquisaProduto;
	}

	public TipoPesquisaProduto getTipoPesquisaProduto() {
		return tipoPesquisaProduto;
	}

	public void setTiposPesquisaProduto(List<TipoPesquisaProduto> tiposPesquisaProduto) {
		this.tiposPesquisaProduto = tiposPesquisaProduto;
	}

	public void setTipoPesquisaProduto(TipoPesquisaProduto tipoPesquisaProduto) {
		this.tipoPesquisaProduto = tipoPesquisaProduto;
	}

}
