package br.com.ita.controle.financeiro;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.DataParaConsulta;
import br.com.ita.dominio.Fornecedor;
import br.com.ita.dominio.StatusTitulo;
import br.com.ita.dominio.dao.FornecedorDAO;
import br.com.ita.dominio.dao.filtros.FiltroContasPagar;
import br.com.ita.dominio.dao.financeiro.ContasPagarDAO;
import br.com.ita.dominio.financeiro.ContasPagar;
import br.com.ita.dominio.financeiro.ContasPagarPK;

@Named("contasPagarMB")
@RequestScoped
public class MBContasPagar {

	@Inject
	private ContasPagar contasPagar;

	@Inject
	private ContasPagarDAO contasPagarDAO;

	private List<ContasPagar> contasPagarLista = null;

	private List<ContasPagar> contasPagarListaFiltrados = null;

	@Inject
	private FornecedorDAO daoFornecedor;

	@Inject
	private ContasPagarPK contasPagarPK;

	@Inject
	private FiltroContasPagar filtro;

	private List<DataParaConsulta> datasParaConsulta = null;

	private List<StatusTitulo> statusTitulo = null;

	@PostConstruct
	public void init() {

		this.setFiltro(new FiltroContasPagar());

		this.filtro.setStatusTitulo(StatusTitulo.EMABERTO);
		this.filtro.setDataParaConsulta(DataParaConsulta.VENCIMENTO);

	}

	public void consultar() {

		this.contasPagarLista = contasPagarDAO.consultar(filtro);

		this.setFiltro(new FiltroContasPagar());

	}

	public String listar() {

		return "/Financeiro/financeiroListarCP?faces-redirect=true";

	}

	public List<Fornecedor> completeFornecedor(String fornecedor) {
		return this.daoFornecedor.autoCompleteFornecedor(fornecedor);
	}

	public String novoTitulo() {

		// limpar o objeto da p�gina
		this.setContasPagar(new ContasPagar());
		this.setContasPagarPK(new ContasPagarPK());

		return "/Financeiro/financeiroEditarCP";

	}

	public String salvar() {

		this.contasPagar.setId(contasPagarPK);

		/*
		 * if (this.getContasPagar().getCodigo() == null) { ContasPagar
		 * objetoDoBanco =
		 * this.contasPagarDAO.lerPorNumeroFornecedor(this.getContasPagar().
		 * getNumero(), this.getContasPagar().getFornecedor());
		 * 
		 * if (objetoDoBanco != null) { JSFUtil.retornarMensagemAviso(null,
		 * "Outro t�tulo com o mesmo n�mero/fornecedor j� existe no sistema.",
		 * null); return null; } }
		 */

		this.contasPagar.setSaldo(this.contasPagar.getValor());
		this.contasPagarDAO.Salvar(this.getContasPagar());

		// limpa a lista
		this.contasPagarLista = null;

		// limpar o objeto da p�gina
		this.setContasPagar(new ContasPagar());

		return "/Financeiro/financeiroListarCP";
	}

	public String excluir() {

		ContasPagarPK codigo = (ContasPagarPK) JSFUtil.getValorExpressao("item.id");

		ContasPagar objetoDoBanco = this.contasPagarDAO.lerPorId(codigo);

		this.contasPagarDAO.remove(objetoDoBanco);

		if (this.contasPagarDAO.lerPorId(objetoDoBanco.getId()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Exclu�do com sucesso.", null);
		}

		// limpar o objeto da p�gina
		this.setContasPagar(new ContasPagar());
		// limpa a lista
		this.contasPagarLista = null;

		return "/Financeiro/financeiroListarCP";

	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setContasPagar(new ContasPagar());

		return "/Financeiro/financeiroListarCP";

	}

	public ContasPagar getContasPagar() {
		return contasPagar;
	}

	public ContasPagarDAO getContasPagarDAO() {
		return contasPagarDAO;
	}

	public List<ContasPagar> getContasPagarLista() {

		// if (this.contasPagarLista == null)
		// this.contasPagarLista = this.contasPagarDAO.lerTodos();

		return contasPagarLista;
	}

	public List<ContasPagar> getContasPagarListaFiltrados() {
		return contasPagarListaFiltrados;
	}

	public void setContasPagar(ContasPagar contasPagar) {
		this.contasPagar = contasPagar;
	}

	public void setContasPagarDAO(ContasPagarDAO contasPagarDAO) {
		this.contasPagarDAO = contasPagarDAO;
	}

	public void setContasPagarLista(List<ContasPagar> contasPagarLista) {
		this.contasPagarLista = contasPagarLista;
	}

	public void setContasPagarListaFiltrados(List<ContasPagar> contasPagarListaFiltrados) {
		this.contasPagarListaFiltrados = contasPagarListaFiltrados;
	}

	public FornecedorDAO getDaoFornecedor() {
		return daoFornecedor;
	}

	public void setDaoFornecedor(FornecedorDAO daoFornecedor) {
		this.daoFornecedor = daoFornecedor;
	}

	public ContasPagarPK getContasPagarPK() {
		return contasPagarPK;
	}

	public void setContasPagarPK(ContasPagarPK contasPagarPK) {
		this.contasPagarPK = contasPagarPK;
	}

	public FiltroContasPagar getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroContasPagar filtro) {
		this.filtro = filtro;
	}

	public List<DataParaConsulta> getDatasParaConsulta() {
		if (this.datasParaConsulta == null)
			this.datasParaConsulta = Arrays.asList(DataParaConsulta.values());
		return datasParaConsulta;
	}

	public void setDatasParaConsulta(List<DataParaConsulta> datasParaConsulta) {
		this.datasParaConsulta = datasParaConsulta;
	}

	public List<StatusTitulo> getStatusTitulo() {
		if (this.statusTitulo == null)
			this.statusTitulo = Arrays.asList(StatusTitulo.values());
		return statusTitulo;
	}

	public void setStatusTitulo(List<StatusTitulo> statusTitulo) {
		this.statusTitulo = statusTitulo;
	}

}
