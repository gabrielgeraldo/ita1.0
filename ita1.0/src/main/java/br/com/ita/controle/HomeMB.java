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
import br.com.ita.dominio.dao.financeiro.ContasPagarDAO;
import br.com.ita.dominio.financeiro.ContasPagar;

@Named("homeMB")
@ViewScoped
public class HomeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ContasPagar> contasPagarLista = null;

	@Inject
	private ContasPagarDAO contasPagarDAO;

	private Date data = new Date();

	private PieChartModel pieModel;

	@Inject
	private FiltroContasPagar filtro;

	private int qtdAberto, qtdBaixado, qtdBaixadoPacial;

	private BigDecimal totalPagar;

	@PostConstruct
	public void init() {
		createPieModel();
		calculaTotalPagar();
	}

	private void createPieModel() {

		pieModel = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();

		this.setFiltro(new FiltroContasPagar());
		this.filtro.setStatusTitulo(StatusTitulo.EMABERTO);
		values.add(this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro).size());
		qtdAberto = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro).size();

		this.setFiltro(new FiltroContasPagar());
		this.filtro.setStatusTitulo(StatusTitulo.BAIXADO);
		values.add(this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro).size());
		qtdBaixado = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro).size();

		this.setFiltro(new FiltroContasPagar());
		this.filtro.setStatusTitulo(StatusTitulo.BAIXADOPARCIAL);
		values.add(this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro).size());
		qtdBaixadoPacial = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro).size();

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

		pieModel.setData(data);

	}

	public void calculaTotalPagar() {

		this.setTotalPagar(new BigDecimal("0.00"));

		this.setFiltro(new FiltroContasPagar());
		this.filtro.setStatusTitulo(StatusTitulo.EMABERTO);

		List<ContasPagar> lista = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro);

		for (int i = 0; i < lista.size(); i++) {

			totalPagar = totalPagar.add(lista.get(i).getSaldo());

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
			this.setFiltro(new FiltroContasPagar());
			this.filtro.setStatusTitulo(StatusTitulo.TODOS);
			this.contasPagarLista = this.contasPagarDAO.buscaContasPagarPorMes(getData(), this.filtro);
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

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

	public FiltroContasPagar getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroContasPagar filtro) {
		this.filtro = filtro;
	}

	public int getQtdAberto() {
		return qtdAberto;
	}

	public void setQtdAberto(int qtdAberto) {
		this.qtdAberto = qtdAberto;
	}

	public int getQtdBaixado() {
		return qtdBaixado;
	}

	public void setQtdBaixado(int qtdBaixado) {
		this.qtdBaixado = qtdBaixado;
	}

	public int getQtdBaixadoPacial() {
		return qtdBaixadoPacial;
	}

	public void setQtdBaixadoPacial(int qtdBaixadoPacial) {
		this.qtdBaixadoPacial = qtdBaixadoPacial;
	}

	public BigDecimal getTotalPagar() {
		return totalPagar;
	}

	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
	}

}
