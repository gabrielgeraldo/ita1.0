package br.com.ita.controle.financeiro;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.DataParaConsulta;
import br.com.ita.dominio.StatusTitulo;
import br.com.ita.dominio.dao.ClienteDAO;
import br.com.ita.dominio.dao.filtros.FiltroContasReceber;
import br.com.ita.dominio.dao.financeiro.ContasReceberDAO;
import br.com.ita.dominio.financeiro.ContasReceberPK;
import br.com.ita.dominio.financeiro.ContasReceber;

@Named("contasReceberMB")
@RequestScoped
public class MBContasReceber {

	@Inject
	private ContasReceber contasReceber;

	@Inject
	private ContasReceberDAO contasReceberDAO;

	private List<ContasReceber> contasReceberLista = null;

	private List<ContasReceber> contasReceberListaFiltrados = null;

	@Inject
	private ClienteDAO daoClientes;

	@Inject
	private ContasReceberPK contasReceberPK;

	@Inject
	private FiltroContasReceber filtro;

	private List<DataParaConsulta> datasParaConsulta = null;

	private List<StatusTitulo> statusTitulo = null;

	@PostConstruct
	public void init() {

		this.setFiltro(new FiltroContasReceber());

		this.filtro.setStatusTitulo(StatusTitulo.EMABERTO);
		this.filtro.setDataParaConsulta(DataParaConsulta.VENCIMENTO);

	}

	public void consultar() {

		this.contasReceberLista = contasReceberDAO.consultar(filtro);

		this.setFiltro(new FiltroContasReceber());

	}

	public String listar() {

		return "/Financeiro/financeiroListarCR?faces-redirect=true";

	}

	public List<Cliente> completeCliente(String cliente) {
		return this.daoClientes.autoCompleteCliente(cliente);
	}

	public String novoTitulo() {

		// limpar o objeto da p�gina
		this.setContasReceber(new ContasReceber());
		this.setContasReceberPK(new ContasReceberPK());

		return "/Financeiro/financeiroEditarCR";

	}

	public String salvar() {

		this.contasReceber.setId(contasReceberPK);

		/*
		 * if (this.getContasReceber().getCodigo() == null) { ContasReceber
		 * objetoDoBanco =
		 * this.contasReceberDAO.lerPorNumeroCliente(this.getContasReceber().
		 * getNumero(), this.getContasReceber().getCliente());
		 * 
		 * if (objetoDoBanco != null) { JSFUtil.retornarMensagemAviso(null,
		 * "Outro t�tulo com o mesmo n�mero/cliente j� existe no sistema.",
		 * null); return null; } }
		 */

		this.contasReceber.setSaldo(this.contasReceber.getValor());
		this.contasReceberDAO.Salvar(this.getContasReceber());
		// limpa a lista
		this.contasReceberLista = null;

		// limpar o objeto da p�gina
		this.setContasReceber(new ContasReceber());

		return "/Financeiro/financeiroListarCR";
	}

	public String baixarTitulo() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		ContasReceber objetoDoBanco = this.contasReceberDAO.lerPorId(codigo);
		this.setContasReceber(new ContasReceber());
		this.setContasReceber(objetoDoBanco);

		if (this.contasReceber.getBaixa() != null) {

			JSFUtil.retornarMensagemErro("T�tulo j� baixado.", null, null);

			return null;

		}

		return "/Financeiro/financeiroBaixarCR";

	}

	public String baixar() {

		this.contasReceberDAO.merge(contasReceber);

		return "/Financeiro/financeiroListarCR";

	}

	public String excluirBaixaTitulo() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		ContasReceber objetoDoBanco = this.contasReceberDAO.lerPorId(codigo);
		this.setContasReceber(new ContasReceber());
		this.setContasReceber(objetoDoBanco);

		this.contasReceber.setBaixa(null);

		this.contasReceberDAO.merge(contasReceber);

		return "/Financeiro/financeiroListarCR";

	}

	public String excluir() {

		ContasReceberPK codigo = (ContasReceberPK) JSFUtil.getValorExpressao("item.id");
		//Long codigo = JSFUtil.getParametroLong("item.id");
		
		System.out.println("codigo: "+codigo);
		
		ContasReceber objetoDoBanco = this.contasReceberDAO.lerPorId(codigo);
		
		System.out.println("objetoDoBanco: "+objetoDoBanco);

		if (objetoDoBanco.getBaixa() == null) {

			this.contasReceberDAO.remove(objetoDoBanco);

			if (this.contasReceberDAO.lerPorId(objetoDoBanco.getId()) == null) {
				JSFUtil.retornarMensagemInfo(null, "Exclu�do com sucesso.", null);
			}

			// limpar o objeto da p�gina
			this.setContasReceber(new ContasReceber());
			// limpa a lista
			this.contasReceberLista = null;

			return "/Financeiro/financeiroListarCR";

		} else {

			JSFUtil.retornarMensagemAviso(null, "T�tulo j� baixado.", null);

			return null;

		}
	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setContasReceber(new ContasReceber());

		return "/Financeiro/financeiroListarCR";

	}

	public ContasReceber getContasReceber() {
		return contasReceber;
	}

	public ContasReceberDAO getContasReceberDAO() {
		return contasReceberDAO;
	}

	public List<ContasReceber> getContasReceberLista() {

		// if (this.contasReceberLista == null)
		// this.contasReceberLista = this.contasReceberDAO.lerTodos();

		return contasReceberLista;
	}

	public List<ContasReceber> getContasReceberListaFiltrados() {
		return contasReceberListaFiltrados;
	}

	public void setContasReceber(ContasReceber contasReceber) {
		this.contasReceber = contasReceber;
	}

	public void setContasReceberDAO(ContasReceberDAO contasReceberDAO) {
		this.contasReceberDAO = contasReceberDAO;
	}

	public void setContasReceberLista(List<ContasReceber> contasReceberLista) {
		this.contasReceberLista = contasReceberLista;
	}

	public void setContasReceberListaFiltrados(List<ContasReceber> contasReceberListaFiltrados) {
		this.contasReceberListaFiltrados = contasReceberListaFiltrados;
	}

	public ClienteDAO getDaoClientes() {
		return daoClientes;
	}

	public void setDaoClientes(ClienteDAO daoClientes) {
		this.daoClientes = daoClientes;
	}

	public ContasReceberPK getContasReceberPK() {
		return contasReceberPK;
	}

	public void setContasReceberPK(ContasReceberPK contasReceberPK) {
		this.contasReceberPK = contasReceberPK;
	}

	public FiltroContasReceber getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroContasReceber filtro) {
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
