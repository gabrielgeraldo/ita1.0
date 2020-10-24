package br.com.ita.controle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import br.com.ita.dominio.StatusTitulo;
import br.com.ita.dominio.dao.filtros.FiltroContasPagar;
import br.com.ita.dominio.dao.filtros.FiltroContasReceber;
import br.com.ita.dominio.dao.financeiro.ContasPagarDAO;
import br.com.ita.dominio.dao.financeiro.ContasReceberDAO;
import br.com.ita.dominio.dao.financeiro.MovimentacaoBancariaDAO;
import br.com.ita.dominio.financeiro.ContasPagar;
import br.com.ita.dominio.financeiro.ContasReceber;

@Named("homeMB")
@ViewScoped
public class HomeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ContasPagar> contasPagarLista = null;

	@Inject
	private ContasPagarDAO contasPagarDAO;

	private Date data = new Date();

	private PieChartModel pieModelContasPagar;

	@Inject
	private FiltroContasPagar filtroCP;

	private int qtdAbertoCP, qtdBaixadoCP, qtdBaixadoPacialCP;

	private BigDecimal totalPagar;

	private PieChartModel pieModelContasReceber;

	private int qtdAbertoCR, qtdBaixadoCR, qtdBaixadoPacialCR;

	@Inject
	private ContasReceberDAO contasReceberDAO;

	@Inject
	private FiltroContasReceber filtroCR;

	private List<ContasReceber> contasReceberLista = null;

	private BigDecimal totalReceber;

	private BigDecimal totalEntrada;

	private BigDecimal totalSaida;

	@Inject
	private MovimentacaoBancariaDAO movimentacaoBancariaDAO;

	@PostConstruct
	public void init() {
		createPieModelContasPagar();
		calculaTotalPagar();
		createPieModelContasReceber();
		calculaTotalReceber();

		totalEntrada = movimentacaoBancariaDAO.getTotalEntrada(getData()) != null
				? movimentacaoBancariaDAO.getTotalEntrada(getData()) : new BigDecimal("0.00");

		totalSaida = movimentacaoBancariaDAO.getTotalSaida(getData()) != null
				? movimentacaoBancariaDAO.getTotalSaida(getData()) : new BigDecimal("0.00");

	}

	private void createPieModelContasPagar() {

		pieModelContasPagar = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();

		this.setFiltroCP(new FiltroContasPagar());
		this.filtroCP.setStatusTitulo(StatusTitulo.EMABERTO);
		values.add(this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP).size());
		qtdAbertoCP = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP).size();

		this.setFiltroCP(new FiltroContasPagar());
		this.filtroCP.setStatusTitulo(StatusTitulo.BAIXADO);
		values.add(this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP).size());
		qtdBaixadoCP = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP).size();

		this.setFiltroCP(new FiltroContasPagar());
		this.filtroCP.setStatusTitulo(StatusTitulo.BAIXADOPARCIAL);
		values.add(this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP).size());
		qtdBaixadoPacialCP = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP).size();

		dataSet.setData(values);

		List<String> bgColors = new ArrayList<>();
		bgColors.add("rgb(199, 14, 14)");
		bgColors.add("rgb(20, 128, 36)");
		bgColors.add("rgb(240, 157, 2)");
		dataSet.setBackgroundColor(bgColors);

		data.addChartDataSet(dataSet);
		List<String> labels = new ArrayList<>();
		labels.add("Em aberto + B. parcial");
		labels.add("Baixado");
		labels.add("B. parcial");
		data.setLabels(labels);

		pieModelContasPagar.setData(data);

	}

	private void createPieModelContasReceber() {

		pieModelContasReceber = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();

		this.setFiltroCR(new FiltroContasReceber());
		this.filtroCR.setStatusTitulo(StatusTitulo.EMABERTO);
		values.add(this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR).size());
		qtdAbertoCR = this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR).size();

		this.setFiltroCR(new FiltroContasReceber());
		this.filtroCR.setStatusTitulo(StatusTitulo.BAIXADO);
		values.add(this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR).size());
		qtdBaixadoCR = this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR).size();

		this.setFiltroCR(new FiltroContasReceber());
		this.filtroCR.setStatusTitulo(StatusTitulo.BAIXADOPARCIAL);
		values.add(this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR).size());
		qtdBaixadoPacialCR = this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR).size();

		dataSet.setData(values);

		List<String> bgColors = new ArrayList<>();
		bgColors.add("rgb(199, 14, 14)");
		bgColors.add("rgb(20, 128, 36)");
		bgColors.add("rgb(240, 157, 2)");
		dataSet.setBackgroundColor(bgColors);

		data.addChartDataSet(dataSet);
		List<String> labels = new ArrayList<>();
		labels.add("Em aberto + B. parcial");
		labels.add("Baixado");
		labels.add("B. parcial");
		data.setLabels(labels);

		pieModelContasReceber.setData(data);

	}

	public void calculaTotalPagar() {

		this.setTotalPagar(new BigDecimal("0.00"));

		this.setFiltroCP(new FiltroContasPagar());
		this.filtroCP.setStatusTitulo(StatusTitulo.EMABERTO);

		List<ContasPagar> lista = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP);

		for (int i = 0; i < lista.size(); i++) {

			totalPagar = totalPagar.add(lista.get(i).getSaldo());

		}
	}

	public void calculaTotalReceber() {

		this.setTotalReceber(new BigDecimal("0.00"));

		this.setFiltroCR(new FiltroContasReceber());
		this.filtroCR.setStatusTitulo(StatusTitulo.EMABERTO);

		List<ContasReceber> lista = this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR);

		for (int i = 0; i < lista.size(); i++) {

			totalReceber = totalReceber.add(lista.get(i).getSaldo());

		}
	}

	public String getData() {
		SimpleDateFormat formatado = new SimpleDateFormat("MM/yyyy");
		String dataFormat = formatado.format(data);
		return dataFormat;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<ContasPagar> getContasPagarLista() {
		if (this.contasPagarLista == null) {
			this.setFiltroCP(new FiltroContasPagar());
			this.filtroCP.setStatusTitulo(StatusTitulo.TODOS);
			this.contasPagarLista = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtroCP);
		}
		return contasPagarLista;
	}

	public void setContasPagarLista(List<ContasPagar> contasPagarLista) {
		this.contasPagarLista = contasPagarLista;
	}

	public ContasPagarDAO getContasPagarDAO() {
		return contasPagarDAO;
	}

	public void setContasPagarDAO(ContasPagarDAO contasPagarDAO) {
		this.contasPagarDAO = contasPagarDAO;
	}

	public PieChartModel getPieModelContasPagar() {
		return pieModelContasPagar;
	}

	public void setPieModelContasPagar(PieChartModel pieModelContasPagar) {
		this.pieModelContasPagar = pieModelContasPagar;
	}

	public FiltroContasPagar getFiltroCP() {
		return filtroCP;
	}

	public void setFiltroCP(FiltroContasPagar filtroCP) {
		this.filtroCP = filtroCP;
	}

	public int getQtdAbertoCP() {
		return qtdAbertoCP;
	}

	public void setQtdAbertoCP(int qtdAbertoCP) {
		this.qtdAbertoCP = qtdAbertoCP;
	}

	public int getQtdBaixadoCP() {
		return qtdBaixadoCP;
	}

	public void setQtdBaixadoCP(int qtdBaixadoCP) {
		this.qtdBaixadoCP = qtdBaixadoCP;
	}

	public int getQtdBaixadoPacialCP() {
		return qtdBaixadoPacialCP;
	}

	public void setQtdBaixadoPacialCP(int qtdBaixadoPacialCP) {
		this.qtdBaixadoPacialCP = qtdBaixadoPacialCP;
	}

	public BigDecimal getTotalPagar() {
		return totalPagar;
	}

	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
	}

	public PieChartModel getPieModelContasReceber() {
		return pieModelContasReceber;
	}

	public void setPieModelContasReceber(PieChartModel pieModelContasReceber) {
		this.pieModelContasReceber = pieModelContasReceber;
	}

	public int getQtdAbertoCR() {
		return qtdAbertoCR;
	}

	public void setQtdAbertoCR(int qtdAbertoCR) {
		this.qtdAbertoCR = qtdAbertoCR;
	}

	public int getQtdBaixadoCR() {
		return qtdBaixadoCR;
	}

	public void setQtdBaixadoCR(int qtdBaixadoCR) {
		this.qtdBaixadoCR = qtdBaixadoCR;
	}

	public int getQtdBaixadoPacialCR() {
		return qtdBaixadoPacialCR;
	}

	public void setQtdBaixadoPacialCR(int qtdBaixadoPacialCR) {
		this.qtdBaixadoPacialCR = qtdBaixadoPacialCR;
	}

	public FiltroContasReceber getFiltroCR() {
		return filtroCR;
	}

	public void setFiltroCR(FiltroContasReceber filtroCR) {
		this.filtroCR = filtroCR;
	}

	public ContasReceberDAO getContasReceberDAO() {
		return contasReceberDAO;
	}

	public List<ContasReceber> getContasReceberLista() {

		if (this.contasReceberLista == null) {
			this.setFiltroCR(new FiltroContasReceber());
			this.filtroCR.setStatusTitulo(StatusTitulo.TODOS);
			this.contasReceberLista = this.contasReceberDAO.buscaContasReceberPorMes(getData(), this.filtroCR);
		}

		return contasReceberLista;
	}

	public void setContasReceberDAO(ContasReceberDAO contasReceberDAO) {
		this.contasReceberDAO = contasReceberDAO;
	}

	public void setContasReceberLista(List<ContasReceber> contasReceberLista) {
		this.contasReceberLista = contasReceberLista;
	}

	public BigDecimal getTotalReceber() {
		return totalReceber;
	}

	public void setTotalReceber(BigDecimal totalReceber) {
		this.totalReceber = totalReceber;
	}

	public BigDecimal getTotalEntrada() {
		return totalEntrada;
	}

	public BigDecimal getTotalSaida() {
		return totalSaida;
	}

	public void setTotalEntrada(BigDecimal totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public void setTotalSaida(BigDecimal totalSaida) {
		this.totalSaida = totalSaida;
	}

	public MovimentacaoBancariaDAO getMovimentacaoBancariaDAO() {
		return movimentacaoBancariaDAO;
	}

	public void setMovimentacaoBancariaDAO(MovimentacaoBancariaDAO movimentacaoBancariaDAO) {
		this.movimentacaoBancariaDAO = movimentacaoBancariaDAO;
	}

}
