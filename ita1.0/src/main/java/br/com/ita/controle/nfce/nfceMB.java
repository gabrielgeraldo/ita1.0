package br.com.ita.controle.nfce;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.primefaces.PrimeFaces;
import org.xml.sax.SAXException;

import br.com.ita.controle.nfce.util.NTFCeService;
import br.com.ita.controle.util.ItaMail;
import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.XmlUtil;
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.CondicaoPagamento;
import br.com.ita.dominio.ItemNTFCe;
import br.com.ita.dominio.ItemOrcamento;
import br.com.ita.dominio.NTFCe;
import br.com.ita.dominio.Orcamento;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.TipoPesquisaProduto;
import br.com.ita.dominio.config.Configuracao;
import br.com.ita.dominio.dao.ClienteDAO;
import br.com.ita.dominio.dao.CondicaoPagamentoDAO;
import br.com.ita.dominio.dao.ConfiguracaoDAO;
import br.com.ita.dominio.dao.EmpresaDAO;
import br.com.ita.dominio.dao.ItemOrcamentoDAO;
import br.com.ita.dominio.dao.ProdutoDAO;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;
import br.com.ita.dominio.empresa.Empresa;
import br.com.ita.dominio.util.ControleNumeros;
import br.com.ita.dominio.dao.NTFCeDAO;
import br.com.ita.dominio.dao.OrcamentoDAO;

@Named("nfceMB")
@ViewScoped
public class nfceMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Produto> produtos = null;

	@Inject
	private ProdutoDAO daoProduto;

	private List<CondicaoPagamento> condicoesPagamentos = null;

	@Inject
	private CondicaoPagamentoDAO condicaoPagamentoDao;

	@Inject
	private ClienteDAO daoClientes;

	@Inject
	private NTFCe nfce;

	@Inject
	private Produto produto;

	@Inject
	private ItemNTFCe itemNfce;

	private List<ItemNTFCe> itensNfce;

	private String produtoEscolhido = null;

	private NTFCeService nfceService = null;

	@Inject
	private ControleNumerosDAO daoControleNumeros;

	@Inject
	private ItemOrcamentoDAO daoItemOrcamento;

	@Inject
	private OrcamentoDAO daoOrcamento;

	private boolean desabilitaHabilita;

	private String ambienteConfigurado;

	private List<TipoPesquisaProduto> tiposPesquisaProduto = null;

	private TipoPesquisaProduto tipoPesquisaProduto;

	@Inject
	private EmpresaDAO empresaDao;

	@Inject
	private ConfiguracaoDAO configuracaoDao;

	@Inject
	private Empresa empresa;

	@Inject
	private Configuracao configuracao;

	// SEM ESSE CONTRUTOR e APRESENTADO ERRO AO ADICIONAR UM PRODUTO.
	@PostConstruct
	public void init() {

		empresa = empresaDao.lerPorId(new Long(1));

		configuracao = configuracaoDao.lerPorId(new Long(1));

		this.novaNfce();

		ambienteConfigurado = configuracao.getAmbiente();

		this.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);

		itemNfce.setQuantidade(1);

	}

	public String iniciarNTFCe() {

		this.novaNfce();

		return "/NFCe/nfce.xhtml?faces-redirect=true";

	}

	public void novaNfce() {

		this.desabilitaHabilita = false;

		nfceService = null;

		nfce = new NTFCe();
		nfce.setTotal(new BigDecimal("0.00"));
		itensNfce = new ArrayList<ItemNTFCe>();

		// String sessionId = VaadinSession.getCurrent().getSession().getId();

		ControleNumeros n = daoControleNumeros.buscaNumeroPorTabelaEChave("nfce", "1");
		nfce.setSerie(Integer.parseInt(n.getChave()));
		nfce.setNumero(n.getNumeroAtual());

		this.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);

		itemNfce.setQuantidade(1);

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
			JSFUtil.retornarMensagemErro(null, "Produto não cadastrado.", null);
		}

		if (produto != null) {

			int posicaoEncntrada = -1;

			for (int i = 0; i < itensNfce.size() && posicaoEncntrada < 0; i++) {
				ItemNTFCe itemTemp = itensNfce.get(i);

				if (itemTemp.getProduto().equals(produto)) {
					posicaoEncntrada = i;
				}
			}

			itemNfce.setProduto(produto);

			if (posicaoEncntrada < 0) {
				itemNfce.setQuantidade(1);
				itemNfce.setPrecoCusto(produto.getPrecoCusto());
				itemNfce.setPrecoVenda(produto.getPrecoUnitario());
				itensNfce.add(itemNfce);
			} else {
				ItemNTFCe itemTemp = itensNfce.get(posicaoEncntrada);
				itemNfce.setQuantidade(itemTemp.getQuantidade() + 1);
				itemNfce.setPrecoCusto(produto.getPrecoCusto());
				itemNfce.setPrecoVenda(produto.getPrecoUnitario().multiply(new BigDecimal(itemNfce.getQuantidade())));
				itensNfce.set(posicaoEncntrada, itemNfce);
			}

			nfce.setTotal(nfce.getTotal().add(produto.getPrecoUnitario()));

			this.setItemNfce(new ItemNTFCe());

		} else if (produto == null && produtoEscolhido != null) {
			JSFUtil.retornarMensagemErro(null, "Produto não cadastrado.", null);
		}

		this.produtoEscolhido = null;

	}

	public void adicionarProdutoPorPesquisa() {

		Produto produto = itemNfce.getProduto();

		int posicaoEncntrada = -1;

		for (int i = 0; i < itensNfce.size() && posicaoEncntrada < 0; i++) {
			ItemNTFCe itemTemp = itensNfce.get(i);

			if (itemTemp.getProduto().equals(produto)) {
				posicaoEncntrada = i;
			}
		}

		itemNfce.setProduto(produto);

		if (posicaoEncntrada < 0) {
			itemNfce.setQuantidade(1);
			itemNfce.setPrecoCusto(produto.getPrecoCusto());
			itemNfce.setPrecoVenda(produto.getPrecoUnitario());
			itensNfce.add(itemNfce);
		} else {
			ItemNTFCe itemTemp = itensNfce.get(posicaoEncntrada);
			itemNfce.setQuantidade(itemTemp.getQuantidade() + 1);
			itemNfce.setPrecoCusto(produto.getPrecoCusto());
			itemNfce.setPrecoVenda(produto.getPrecoUnitario().multiply(new BigDecimal(itemNfce.getQuantidade())));
			itensNfce.set(posicaoEncntrada, itemNfce);
		}

		nfce.setTotal(nfce.getTotal().add(produto.getPrecoUnitario()));

		this.setItemNfce(new ItemNTFCe());

		// boolean fecharDialog = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialog", fecharDialog);

		JSFUtil.retornarMensagemInfo(null, "Produto adicionado.", null);

	}

	public void adicionar() {

		Produto produto = itemNfce.getProduto();

		// -------------------- Método adicionar.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensNfce.size() && posicaoEncntrada < 0; i++) {
			ItemNTFCe itemTemp = itensNfce.get(i);

			if (itemTemp.getProduto().equals(produto)) {
				posicaoEncntrada = i;
			}
		}

		itemNfce.setProduto(produto);

		if (posicaoEncntrada < 0) {
			itemNfce.setPrecoCusto(produto.getPrecoCusto());
			itemNfce.setPrecoVenda(produto.getPrecoUnitario());
			itensNfce.add(itemNfce);
		} else {
			ItemNTFCe itemTemp = itensNfce.get(posicaoEncntrada);
			itemNfce.setQuantidade(itemTemp.getQuantidade() + itemNfce.getQuantidade());
			itemNfce.setPrecoCusto(produto.getPrecoCusto());
			itemNfce.setPrecoVenda(produto.getPrecoUnitario());
			itensNfce.set(posicaoEncntrada, itemNfce);
		}

		nfce.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensNfce.size(); j++) {
			nfce.setTotal(nfce.getTotal()
					.add(itensNfce.get(j).getPrecoVenda().multiply(new BigDecimal(itensNfce.get(j).getQuantidade()))));

		}
		// -------------------- Método adicionar.

		this.setItemNfce(new ItemNTFCe());
		itemNfce.setQuantidade(1);

		boolean fecharDialog = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialog", fecharDialog);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialog", fecharDialog);

		JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

	}

	public void importarOrcamento() {

		if (nfce.getOrcamento() == null) {

			JSFUtil.retornarMensagemAviso(null, "Orçamento não selecionado.", null);

		} else {

			nfce.setCliente(nfce.getOrcamento().getCliente());

			List<ItemOrcamento> itensPv = daoItemOrcamento.buscaItens(this.nfce.getOrcamento());

			for (ItemOrcamento item : itensPv) {

				ItemNTFCe itenNfce = new ItemNTFCe();

				itenNfce.setCodigo(item.getCodigo());
				itenNfce.setNfce(this.nfce);
				itenNfce.setPrecoCusto(item.getPrecoCusto());
				itenNfce.setPrecoVenda(item.getPrecoVenda());
				itenNfce.setProduto(item.getProduto());
				itenNfce.setQuantidade(item.getQuantidade());

				this.itensNfce.add(itenNfce);

			}

			nfce.setTotal(nfce.getOrcamento().getTotal());

			this.setDesabilitaHabilita(true);

			boolean fecharDialog = true;
			// RequestContext context = RequestContext.getCurrentInstance();
			// context.addCallbackParam("fecharDialog", fecharDialog);
			PrimeFaces.current().ajax().addCallbackParam("fecharDialog", fecharDialog);

			JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

		}
	}

	public void removerProduto(ItemNTFCe itemNfce) {

		int posicaoEncntrada = -1;

		for (int i = 0; i < itensNfce.size() && posicaoEncntrada < 0; i++) {
			ItemNTFCe itemTemp = itensNfce.get(i);

			if (itemTemp.getProduto().equals(itemNfce.getProduto())) {
				posicaoEncntrada = i;
			}
		}

		if (posicaoEncntrada > -1) {
			itensNfce.remove(posicaoEncntrada);
			nfce.setTotal(nfce.getTotal().subtract(itemNfce.getPrecoVenda()));
		}

	}

	public String finalizarNTFCe() {

		if (nfce.getTotal().equals(new BigDecimal("0.00"))) {
			JSFUtil.retornarMensagemErro(null, "Esta venda deve ter 1 ou mais itens.", null);
			return "/EfetuarNTFCe/efetuarNTFCe";

		}

		nfceService = new NTFCeService(nfce, itensNfce, empresa, configuracao);

		try {

			// TRANSMITINDO A NOTA.
			nfce = nfceService.transmitir();

		} catch (NullPointerException e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(null, "Verifique a configuração dos impostos.", null);

		} catch (java.lang.IndexOutOfBoundsException e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(null, "Verifique a configuração dos impostos.", null);

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(null, "Erro durante a Trasmissão: " + e.getMessage(), null);

		} finally {

			boolean fecharDialogStatus = true;
			PrimeFaces.current().ajax().addCallbackParam("fecharDialogStatus", fecharDialogStatus);

		}

		// TUDO OK, NOTA TRANSMITIDA.
		if (nfce.getStatus() != null && nfce.getStatus().equals("100")) {

			// ARMANEZANDO XML.
			try {
				XmlUtil.armazernarXmlLocalNfce(nfce.getXml(), nfce.getChave());
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();

				System.out.println("----------------------------------------------------------------------------");
				System.out.println("Erro durante o armazenamento do XML: " + nfce.getChave());
				System.out.println("----------------------------------------------------------------------------");

				ItaMail.mail(
						"Erro durante o armazenamento do XML: " + empresa.getCnpj() + ", " + empresa.getCodClient(),
						"NFC-e:" + nfce.getChave());

				JSFUtil.retornarMensagemErro(null, "Erro durante o armazenamento do XML: " + e.getMessage(), null);
			}

			// SALVANDO A NOTA NO BANCO DE DADOS.
			try {
				NTFCeDAO daoNTFCe = new NTFCeDAO();
				daoNTFCe.salvarNTFCe(nfce, itensNfce);
			} catch (Exception e) {

				e.printStackTrace();

				System.out.println("-------------------------------------------------------------");
				System.out.println("Erro ao salvar NFC-e: " + nfce.getChave());
				System.out.println("-------------------------------------------------------------");

				ItaMail.mail("Erro ao salvar NFC-e: " + empresa.getCnpj() + ", " + empresa.getCodClient(),
						"NFC-e:" + nfce.getChave());

				JSFUtil.retornarMensagemErro(null, "Erro ao salvar NFC-e: " + e.getMessage(), null);
			}

		} else {
			JSFUtil.retornarMensagemErro(null, nfce.getRejeicaoMotivo(), null);
		}

		this.novaNfce();

		return "/EfetuarNTFCe/efetuarNTFCe";

	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public ProdutoDAO getDaoProduto() {
		return daoProduto;
	}

	public void setDaoProduto(ProdutoDAO daoProduto) {
		this.daoProduto = daoProduto;
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

	public NTFCe getNfce() {
		return nfce;
	}

	public void setNfce(NTFCe nfce) {
		this.nfce = nfce;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ItemNTFCe getItemNfce() {
		return itemNfce;
	}

	public void setItemNfce(ItemNTFCe itemNfce) {
		this.itemNfce = itemNfce;
	}

	public List<ItemNTFCe> getItensNfce() {
		return itensNfce;
	}

	public void setItensNfce(List<ItemNTFCe> itensNfce) {
		this.itensNfce = itensNfce;
	}

	public String getProdutoEscolhido() {
		return produtoEscolhido;
	}

	public void setProdutoEscolhido(String produtoEscolhido) {
		this.produtoEscolhido = produtoEscolhido;
	}

	public NTFCeService getNfceService() {
		return nfceService;
	}

	public void setNfceService(NTFCeService nfceService) {
		this.nfceService = nfceService;
	}

	public ControleNumerosDAO getDaoControleNumeros() {
		return daoControleNumeros;
	}

	public void setDaoControleNumeros(ControleNumerosDAO daoControleNumeros) {
		this.daoControleNumeros = daoControleNumeros;
	}

	public ItemOrcamentoDAO getDaoItemOrcamento() {
		return daoItemOrcamento;
	}

	public void setDaoItemOrcamento(ItemOrcamentoDAO daoItemOrcamento) {
		this.daoItemOrcamento = daoItemOrcamento;
	}

	public boolean isDesabilitaHabilita() {
		return desabilitaHabilita;
	}

	public void setDesabilitaHabilita(boolean desabilitaHabilita) {
		this.desabilitaHabilita = desabilitaHabilita;
	}

	public OrcamentoDAO getDaoOrcamento() {
		return daoOrcamento;
	}

	public void setDaoOrcamento(OrcamentoDAO daoOrcamento) {
		this.daoOrcamento = daoOrcamento;
	}

	public String getAmbienteConfigurado() {
		return ambienteConfigurado;
	}

	public void setAmbienteConfigurado(String ambienteConfigurado) {
		this.ambienteConfigurado = ambienteConfigurado;
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

	public EmpresaDAO getEmpresaDao() {
		return empresaDao;
	}

	public void setEmpresaDao(EmpresaDAO empresaDao) {
		this.empresaDao = empresaDao;
	}

	public ConfiguracaoDAO getConfiguracaoDao() {
		return configuracaoDao;
	}

	public void setConfiguracaoDao(ConfiguracaoDAO configuracaoDao) {
		this.configuracaoDao = configuracaoDao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

}
