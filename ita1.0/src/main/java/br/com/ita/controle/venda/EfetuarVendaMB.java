package br.com.ita.controle.venda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.ita.controle.config.Config;
import br.com.ita.controle.util.DanfeUtil;
import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.venda.util.VendaService;
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.CondicaoPagamento;
import br.com.ita.dominio.ItemVenda;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.Venda;
import br.com.ita.dominio.dao.ClienteDAO;
import br.com.ita.dominio.dao.CondicaoPagamentoDAO;
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

	@PostConstruct
	public void init() {
		venda.setTotal(new BigDecimal("0.00"));
		this.setItensVenda(new ArrayList<ItemVenda>());
	}

	public List<Produto> completeProduto(String produto) {
		return this.daoProduto.autoCompleteProdutoPorDescricao(produto);
	}

	public List<Cliente> completeCliente(String cliente) {
		return this.daoClientes.autoCompleteCliente(cliente);
	}

	public void adicionarProdutoPeloCodigo() {

		Produto produto = null;

		if (produtoEscolhido != null) {
			produto = daoProduto.lerPorId(produtoEscolhido);
		} else {
			JSFUtil.retornarMensagemErro(null, "Produto não cadastrado.", null);
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
			JSFUtil.retornarMensagemErro(null, "Produto não cadastrado.", null);
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

		// boolean fecharDialog = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialog", fecharDialog);

		JSFUtil.retornarMensagemInfo(null, "Produto adicionado.", null);

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

		return "/EfetuarVenda/efetuarVenda?faces-redirect=true";
	}

	@SuppressWarnings("deprecation")
	public String finalizarVenda() {

		if (venda.getTotal().equals(new BigDecimal("0.00"))) {
			JSFUtil.retornarMensagemErro(null, "Esta venda deve ter 1 ou mais itens.", null);
			return "/EfetuarVenda/efetuarVenda";
		}

		venda.setSituacao("FINALIZADA");

		VendaDAO daoVenda = new VendaDAO();
		Venda vendaSalva = daoVenda.salvarVenda(venda, itensVenda);

		if (vendaSalva != null && vendaSalva.getCodigo() != null)
			JSFUtil.retornarMensagemInfo(null, "Venda finalizada com sucesso! Código: " + vendaSalva.getCodigo(), null);

		if (Config.propertiesLoader().getProperty("tpImpVenda").equals("1")) {

			String XMLVenda = null;

			// GERA COMPROVANTE DA VENDA.
			if (vendaSalva != null && vendaSalva.getCodigo() != null) {

				vendaService = new VendaService(vendaSalva, itensVenda);
				try {

					XMLVenda = vendaService.gerarXMLVenda();

				} catch (Exception e) {
					e.printStackTrace();
					JSFUtil.retornarMensagemErro(null, "Erro ao gerar comprovante: " + e.getMessage(), null);
				}

			} else {
				JSFUtil.retornarMensagemErro(null, "Erro ao imprimir comprovante.", null);
			}

			// IMPRIMI A VENDA.
			if (XMLVenda != null) {

				try {
					DanfeUtil.imprimirComprovanteVenda(XMLVenda);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
					JSFUtil.retornarMensagemErro(null, "Erro ao gerar comprovante: " + e.getMessage(), null);
				}

			} else {
				JSFUtil.retornarMensagemErro(null, "Erro ao imprimir comprovante.", null);
			}

		}

		this.iniciarVenda();

		boolean fecharDialog = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("fecharDialog", fecharDialog);

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

}
