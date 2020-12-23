package br.com.ita.controle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.hibernate.validator.constraints.NotEmpty;
import org.primefaces.PrimeFaces;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.ItemOrcamento;
import br.com.ita.dominio.Orcamento;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.TipoPesquisaProduto;
import br.com.ita.dominio.dao.ClienteDAO;
import br.com.ita.dominio.dao.ItemOrcamentoDAO;
import br.com.ita.dominio.dao.OrcamentoDAO;
import br.com.ita.dominio.dao.ProdutoDAO;
import br.com.ita.dominio.dao.UsuarioDAO;
import br.com.ita.dominio.dao.filtros.FiltroOrcamento;
import br.com.ita.dominio.dao.filtros.FiltroProduto;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;

@Path("/orcamento")
@Named("orcamentoMB")
@ConversationScoped
public class OrcamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversacao;

	@Inject
	private Orcamento orcamento;

	@Inject
	private Produto produto;

	@Inject
	private ProdutoDAO daoProduto;

	@Inject
	private ClienteDAO daoCliente;

	@Inject
	private ItemOrcamento itemOrcamento;

	@Inject
	private OrcamentoDAO daoOrcamento;

	private List<ItemOrcamento> itensOrcamento = null;

	@Inject
	private FiltroOrcamento filtro;

	private List<Orcamento> orcamentos = null;

	private List<Orcamento> orcamentosFiltrados = null;

	@Inject
	private ItemOrcamentoDAO daoItemOrcamento;

	@Inject
	private ControleNumerosDAO daoControleNumeros;

	@Inject
	private FiltroProduto filtroProduto;

	private List<Produto> produtos = null;

	private List<Produto> produtosFiltrados = null;

	private List<TipoPesquisaProduto> tiposPesquisaProduto = null;

	@Inject
	private UsuarioDAO daoUsuario;

	@NotEmpty(message = "A senha é de preenchimento obrigatório.")
	@NotNull(message = "A senha é de preenchimento obrigatório.")
	private String senha;

	@Context
	private ServletContext context;

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/getRazaoSocial")
	public String getRazaoSocial() {

		String atualDir = context.getRealPath("/resources/conf.properties");

		Properties props = new Properties();
		FileInputStream file = null;
		try {
			file = new FileInputStream(atualDir);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			props.load(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return props.getProperty("razaoSocial");
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/completeCliente/{cliente}")
	public List<Cliente> completeCliente(@PathParam("cliente") String cliente) {
		return this.daoCliente.autoCompleteCliente(cliente);
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/completeProduto/{tipoPesquisaProduto}/{produto}")
	public List<Produto> completeProduto(@PathParam("tipoPesquisaProduto") int tipoPesquisaProduto,
			@PathParam("produto") String produto) {

		if (tipoPesquisaProduto == 1) {
			return this.daoProduto.autoCompleteProdutoPorCodigo(produto);
		}

		if (tipoPesquisaProduto == 2) {
			return this.daoProduto.autoCompleteProdutoPorDescricao(produto);
		}

		return null;
	}

	@PostConstruct
	public void init() {
		getRazaoSocial();
	}

	public String listar() {

		return "/Orcamento/orcamentoListar?faces-redirect=true";

	}

	public void consultar() {

		this.orcamentos = daoOrcamento.consultar(filtro);

		this.setFiltro(new FiltroOrcamento());

	}

	public void consultarProduto() {

		this.produtos = daoProduto.consultar(filtroProduto);

	}

	public String vizualizar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Orcamento objetoDoBanco = this.daoOrcamento.lerPorId(codigo);
		this.setOrcamento(new Orcamento());
		this.setOrcamento(objetoDoBanco);

		this.itensOrcamento = this.daoItemOrcamento.buscaItens(this.orcamento);

		return "/Orcamento/orcamentoVizualizar";

	}

	public String novo() {

		if (conversacao.isTransient()) {
			conversacao.setTimeout(900000);
			conversacao.begin();
		}

		this.setOrcamento(new Orcamento());
		this.setItensOrcamento(new ArrayList<ItemOrcamento>());

		// ControleNumeros n =
		// daoControleNumeros.buscaNumeroPorTabelaEChave("orc", "1");
		// orcamento.setCodigo(Long.valueOf(n.getNumeroAtual()));

		// daoControleNumeros.atualizaNumeroPorTabelaEChave("orc", "1");

		orcamento.setTotal(new BigDecimal("0.00"));

		itemOrcamento.setQuantidade(1);

		this.filtroProduto.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);

		this.orcamento
				.setUsuario(daoUsuario.lerPorUsuario(SecurityContextHolder.getContext().getAuthentication().getName()));

		return "/Orcamento/orcamentoEditar";

	}

	public void removerProduto(ItemOrcamento itemOrcamento) {

		// -------------------- Método remover.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensOrcamento.size() && posicaoEncntrada < 0; i++) {
			ItemOrcamento itemTemp = itensOrcamento.get(i);

			if (itemTemp.getProduto().equals(itemOrcamento.getProduto())) {
				posicaoEncntrada = i;
			}
		}

		if (posicaoEncntrada > -1) {
			itensOrcamento.remove(posicaoEncntrada);
		}

		orcamento.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensOrcamento.size(); j++) {
			orcamento.setTotal(orcamento.getTotal().add(itensOrcamento.get(j).getPrecoVenda()
					.multiply(new BigDecimal(itensOrcamento.get(j).getQuantidade()))));

		}
		// -------------------- Método remover.

	}

	public String finalizar() {

		boolean loggedIn = false;

		if (!this.orcamento.getUsuario().getSenha().equals(this.senha)) {
			JSFUtil.retornarMensagemAviso(null, "Senha inválida!", null);
			loggedIn = true;
			return null;
		}

		PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);

		try {

			Orcamento orcamentoSalvo = daoOrcamento.salvarOrcamento(orcamento, itensOrcamento);

			if (orcamentoSalvo != null && orcamentoSalvo.getCodigo() != null)
				JSFUtil.retornarMensagemInfo(null,
						"Orcamento finalizado com sucesso! Codigo: " + orcamentoSalvo.getCodigo(), null);

			orcamento = new Orcamento();
			orcamento.setTotal(new BigDecimal("0.00"));

			itensOrcamento = new ArrayList<ItemOrcamento>();

			if (!conversacao.isTransient()) {
				conversacao.end();
			}

			this.novo();

			return null;

		} catch (Exception e) {

			JSFUtil.retornarMensagemFatal(null, "Falha ao salvar.", null);

			System.out.println(e);

			return null;

		}

	}

	@POST
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@Path("/finalizarPeloRest")
	public String finalizarPeloRest(Orcamento orcamento) {

		try {

			Orcamento orcamentoSalvo = daoOrcamento.salvarOrcamento(orcamento, orcamento.getItens());

			return "Orcamento finalizado com sucesso! Codigo: " + orcamentoSalvo.getCodigo();

		} catch (Exception e) {

			return e.toString();

		}

	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setOrcamento(new Orcamento());

		return "/Orcamento/orcamentoListar";
	}

	public void adicionar() {

		String codigo = JSFUtil.getParametro("itemcodigo");

		Produto produto = this.daoProduto.lerPorId(codigo);

		// -------------------- Metodo adicionar.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensOrcamento.size() && posicaoEncntrada < 0; i++) {
			ItemOrcamento itemTemp = itensOrcamento.get(i);

			if (itemTemp.getProduto().equals(produto)) {
				posicaoEncntrada = i;
			}
		}

		itemOrcamento.setProduto(produto);

		if (posicaoEncntrada < 0) {
			itemOrcamento.setPrecoCusto(produto.getPrecoCusto());
			itemOrcamento.setPrecoVenda(produto.getPrecoUnitario());
			itensOrcamento.add(itemOrcamento);
		} else {
			ItemOrcamento itemTemp = itensOrcamento.get(posicaoEncntrada);
			itemOrcamento.setQuantidade(itemTemp.getQuantidade() + itemOrcamento.getQuantidade());
			itemOrcamento.setPrecoCusto(produto.getPrecoCusto());
			itemOrcamento.setPrecoVenda(produto.getPrecoUnitario());
			itensOrcamento.set(posicaoEncntrada, itemOrcamento);
		}

		orcamento.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensOrcamento.size(); j++) {
			orcamento.setTotal(orcamento.getTotal().add(itensOrcamento.get(j).getPrecoVenda()
					.multiply(new BigDecimal(itensOrcamento.get(j).getQuantidade()))));

		}
		// -------------------- Metodo adicionar.

		this.setItemOrcamento(new ItemOrcamento());
		itemOrcamento.setQuantidade(1);

		this.produtos = null;

		boolean fecharDialog = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialog", fecharDialog);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialog", fecharDialog);

		JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Orcamento objetoDoBanco = this.daoOrcamento.lerPorId(codigo);
		this.setOrcamento(new Orcamento());
		this.setOrcamento(objetoDoBanco);

		this.itensOrcamento = this.daoItemOrcamento.buscaItens(this.orcamento);

		this.daoOrcamento.excluirOrcamento(objetoDoBanco, this.itensOrcamento);

		if (this.daoOrcamento.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Excluído com sucesso.", null);
		}

		// limpar o objeto da p�gina
		this.setOrcamento(new Orcamento());
		// limpa a lista
		this.orcamentos = null;

		return "/Orcamento/orcamentoVendaListar";

	}

	public Conversation getConversacao() {
		return conversacao;
	}

	public void setConversacao(Conversation conversacao) {
		this.conversacao = conversacao;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
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

	public ClienteDAO getDaoCliente() {
		return daoCliente;
	}

	public void setDaoCliente(ClienteDAO daoCliente) {
		this.daoCliente = daoCliente;
	}

	public ItemOrcamento getItemOrcamento() {
		return itemOrcamento;
	}

	public void setItemOrcamento(ItemOrcamento itemOrcamento) {
		this.itemOrcamento = itemOrcamento;
	}

	public OrcamentoDAO getDaoOrcamento() {
		return daoOrcamento;
	}

	public void setDaoOrcamento(OrcamentoDAO daoOrcamento) {
		this.daoOrcamento = daoOrcamento;
	}

	public List<ItemOrcamento> getItensOrcamento() {
		return itensOrcamento;
	}

	public void setItensOrcamento(List<ItemOrcamento> itensOrcamento) {
		this.itensOrcamento = itensOrcamento;
	}

	public FiltroOrcamento getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroOrcamento filtro) {
		this.filtro = filtro;
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	public List<Orcamento> getOrcamentosFiltrados() {
		return orcamentosFiltrados;
	}

	public void setOrcamentosFiltrados(List<Orcamento> orcamentosFiltrados) {
		this.orcamentosFiltrados = orcamentosFiltrados;
	}

	public ItemOrcamentoDAO getDaoItemOrcamento() {
		return daoItemOrcamento;
	}

	public void setDaoItemOrcamento(ItemOrcamentoDAO daoItemOrcamento) {
		this.daoItemOrcamento = daoItemOrcamento;
	}

	public ControleNumerosDAO getDaoControleNumeros() {
		return daoControleNumeros;
	}

	public void setDaoControleNumeros(ControleNumerosDAO daoControleNumeros) {
		this.daoControleNumeros = daoControleNumeros;
	}

	public FiltroProduto getFiltroProduto() {
		return filtroProduto;
	}

	public void setFiltroProduto(FiltroProduto filtroProduto) {
		this.filtroProduto = filtroProduto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public void setProdutosFiltrados(List<Produto> produtosFiltrados) {
		this.produtosFiltrados = produtosFiltrados;
	}

	public List<TipoPesquisaProduto> getTiposPesquisaProduto() {

		if (this.tiposPesquisaProduto == null)
			this.tiposPesquisaProduto = Arrays.asList(TipoPesquisaProduto.values());

		return tiposPesquisaProduto;
	}

	public void setTiposPesquisaProduto(List<TipoPesquisaProduto> tiposPesquisaProduto) {
		this.tiposPesquisaProduto = tiposPesquisaProduto;
	}

	public UsuarioDAO getDaoUsuario() {
		return daoUsuario;
	}

	public void setDaoUsuario(UsuarioDAO daoUsuario) {
		this.daoUsuario = daoUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
