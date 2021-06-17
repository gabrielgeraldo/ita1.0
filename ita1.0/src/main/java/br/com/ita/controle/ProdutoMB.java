package br.com.ita.controle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Categoria;
import br.com.ita.dominio.Fornecedor;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.TipoPesquisaProduto;
import br.com.ita.dominio.dao.CategoriaDAO;
import br.com.ita.dominio.dao.FornecedorDAO;
import br.com.ita.dominio.dao.ProdutoDAO;
import br.com.ita.dominio.dao.filtros.FiltroProduto;
import br.com.ita.dominio.dao.util.NcmDAO;
import br.com.ita.dominio.util.Ncm;

@Named("produtoMB")
@RequestScoped
public class ProdutoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Produto produto;

	@Inject
	private ProdutoDAO daoProduto;

	@Inject
	private CategoriaDAO daoCategoria;

	@Inject
	private FornecedorDAO daoFornecedor;

	@Inject
	private NcmDAO daoNcm;

	@Inject
	private FiltroProduto filtro;

	private List<Produto> produtos = null;
	private List<Categoria> categorias = null;
	private List<Fornecedor> fornecedores = null;

	private Long cont;

	private Long produtosSemNcm;

	private BigDecimal margemLucro = new BigDecimal(0);

	private List<TipoPesquisaProduto> tiposPesquisaProduto = null;

	@PostConstruct
	public void init() {

		this.filtro.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);

		cont = daoProduto.contaRegistros();

		produtosSemNcm = daoProduto.contaProdutosSemNcm();

	}

	public void calculaMargemLucro() {

		System.out.println(this.margemLucro);
		System.out.println(this.produto.getPrecoUnitario());

		// resultado = valor + (valor*porcentagem)/100;

		// this.produto.setPrecoUnitario(this.produto.getPrecoUnitario()
		// .add(this.margemLucro.multiply(this.produto.getPrecoUnitario()).divide(new
		// BigDecimal("100"))));

	}

	public void consultar() {

		this.produtos = daoProduto.consultar(filtro);

	}

	public String novo() {

		this.setProduto(new Produto());

		return "/Produto/produtoEditar";

	}

	public String listar() {

		return "/Produto/produtoListar?faces-redirect=true";

	}

	public String alterar() {

		String codigo = JSFUtil.getParametro("itemcodigo");

		Produto objetoDoBanco = this.daoProduto.lerPorId(codigo);
		this.setProduto(new Produto());
		this.setProduto(objetoDoBanco);

		return "/Produto/produtoAlterar";

	}

	public String salvar() {

		if (this.getProduto() != null) {
			Produto objetoDoBanco = this.daoProduto.lerPorId(this.getProduto().getCodigo());

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null, "Outro produto com o mesmo código já existe no sistema.", null);
				return null;
			}
		}

		this.daoProduto.persist(this.getProduto());

		// limpa a lista
		this.produtos = null;

		// limpar o objeto da p�gina
		this.setProduto(new Produto());

		cont = daoProduto.contaRegistros();

		produtosSemNcm = daoProduto.contaProdutosSemNcm();

		JSFUtil.retornarMensagemInfo(null, "Salvo com sucesso.", null);

		return "/Produto/produtoListar";

	}

	public String salvarAlteracao() {

		this.daoProduto.merge(this.getProduto());

		// limpa a lista
		this.produtos = null;

		// limpar o objeto da p�gina
		this.setProduto(new Produto());

		cont = daoProduto.contaRegistros();

		produtosSemNcm = daoProduto.contaProdutosSemNcm();

		JSFUtil.retornarMensagemInfo(null, "Alterado com sucesso.", null);

		return "/Produto/produtoListar";

	}

	public String excluir() {

		String codigo = JSFUtil.getParametro("itemcodigo");

		this.daoProduto = new ProdutoDAO();
		Produto objetoDoBanco = this.daoProduto.lerPorId(codigo);

		this.daoProduto.remove(objetoDoBanco);

		if (this.daoProduto.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Exclu�do com sucesso.", null);
		}

		// limpar o objeto da p�gina
		this.setProduto(new Produto());

		// limpa a lista
		this.produtos = null;

		cont = daoProduto.contaRegistros();

		produtosSemNcm = daoProduto.contaProdutosSemNcm();

		return "/Produto/produtoListar";

	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setProduto(new Produto());

		return "/Produto/produtoListar";

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

	public List<Categoria> getCategorias() {

		if (this.categorias == null) {

			this.daoCategoria = new CategoriaDAO();
			this.categorias = this.daoCategoria.lerTodos();

		}

		return this.categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Fornecedor> getFornecedores() {

		if (this.fornecedores == null) {

			this.daoFornecedor = new FornecedorDAO();
			this.fornecedores = this.daoFornecedor.lerTodos();

		}

		return this.fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public CategoriaDAO getDaoCategoria() {
		return daoCategoria;
	}

	public void setDaoCategoria(CategoriaDAO daoCategoria) {
		this.daoCategoria = daoCategoria;
	}

	public FornecedorDAO getDaoFornecedor() {
		return daoFornecedor;
	}

	public void setDaoFornecedor(FornecedorDAO daoFornecedor) {
		this.daoFornecedor = daoFornecedor;
	}

	public NcmDAO getDaoNcm() {
		return daoNcm;
	}

	public void setDaoNcm(NcmDAO daoNcm) {
		this.daoNcm = daoNcm;
	}

	public List<Ncm> completeNcm(String ncm) {
		return this.daoNcm.autoCompleteNcm(ncm);
	}

	public List<Produto> getProdutos() {

		// if (this.produtos == null) {
		// produtos = daoProduto.consultar(filtro);
		// }

		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public FiltroProduto getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroProduto filtro) {
		this.filtro = filtro;
	}

	public Long getCont() {
		return cont;
	}

	public void setCont(Long cont) {
		this.cont = cont;
	}

	public Long getProdutosSemNcm() {
		return produtosSemNcm;
	}

	public void setProdutosSemNcm(Long produtosSemNcm) {
		this.produtosSemNcm = produtosSemNcm;
	}

	public BigDecimal getMargemLucro() {
		return margemLucro;
	}

	public void setMargemLucro(BigDecimal margemLucro) {
		this.margemLucro = margemLucro;
	}

	public List<TipoPesquisaProduto> getTiposPesquisaProduto() {

		if (this.tiposPesquisaProduto == null)
			// não estava funcionando.
			// this.tiposPesquisaProduto = Arrays.asList(TipoPesquisaProduto.values());
			this.tiposPesquisaProduto = Arrays.asList(TipoPesquisaProduto.DESCRICAO);

		return tiposPesquisaProduto;
	}

	public void setTiposPesquisaProduto(List<TipoPesquisaProduto> tiposPesquisaProduto) {
		this.tiposPesquisaProduto = tiposPesquisaProduto;
	}

}
