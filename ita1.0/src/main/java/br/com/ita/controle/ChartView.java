package br.com.ita.controle;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.ita.controle.config.Config;
import br.com.ita.dominio.dao.VendaDAO;

@ManagedBean
@ViewScoped
public class ChartView implements Serializable {

	private static final long serialVersionUID = 1L;

	private BarChartModel animatedModel2;

	private PieChartModel pieModel1;

	private VendaDAO daoVenda = new VendaDAO();

	private List<Object[]> vendasDinheiro = null;

	private List<Object[]> vendasCartao = null;

	private List<Object[]> vendasOutras = null;

	private List<Object> vendasTodasMes = null;

	private String razaoSocial;

	private String cnpj;
	
	private String nomeFantasia;

	@PostConstruct
	public void init() {
		this.getVendasDinheiro();
		this.getVendasCartao();
		this.getVendasOutras();
		this.getVendasTodasMes();
		createAnimatedModels();
		createPieModels();

		razaoSocial = Config.propertiesLoader().getProperty("razaoSocial");
		nomeFantasia = Config.propertiesLoader().getProperty("nomeFantasia");
		cnpj = Config.propertiesLoader().getProperty("cnpj");

	}

	public BarChartModel getAnimatedModel2() {
		return animatedModel2;
	}

	public PieChartModel getPieModel1() {
		return pieModel1;
	}

	private void createAnimatedModels() {

		animatedModel2 = initBarModel();
		animatedModel2.setTitle("NF-e / NFC-e / Venda");
		animatedModel2.setAnimate(true);
		animatedModel2.setLegendPosition("ne");
		Axis yAxis = animatedModel2.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(3000);

	}

	private BarChartModel initBarModel() {
		BarChartModel model = new BarChartModel();

		ChartSeries dinheiro = new ChartSeries();
		dinheiro.setLabel("NFE-e");
		for (Object[] a : this.vendasDinheiro) {

			if (a[0] == null)
				a[0] = 0.00;

			dinheiro.set(a[1].toString(), ((Number) a[0]).intValue());
		}

		ChartSeries cartao = new ChartSeries();
		cartao.setLabel("NFC-e");
		for (Object[] a : this.vendasCartao) {

			if (a[0] == null)
				a[0] = 0.00;

			cartao.set(a[1].toString(), ((Number) a[0]).intValue());
		}

		ChartSeries outras = new ChartSeries();
		outras.setLabel("Venda");
		for (Object[] a : this.vendasOutras) {

			if (a[0] == null)
				a[0] = 0.00;

			outras.set(a[1].toString(), ((Number) a[0]).intValue());
		}

		model.addSeries(dinheiro);
		model.addSeries(cartao);
		model.addSeries(outras);

		return model;
	}

	private void createPieModels() {
		createPieModel1();
	}

	private void createPieModel1() {
		pieModel1 = new PieChartModel();

		Number NFe = this.vendasTodasMes.get(0) != null ? (Number) this.vendasTodasMes.get(0) : 0;
		Number NFCe = this.vendasTodasMes.get(1) != null ? (Number) this.vendasTodasMes.get(1) : 0;
		Number Venda = this.vendasTodasMes.get(2) != null ? (Number) this.vendasTodasMes.get(2) : 0;

		pieModel1.set("NF-e", NFe);
		pieModel1.set("NFC-e", NFCe);
		pieModel1.set("Venda", Venda);

		Calendar cal = GregorianCalendar.getInstance();

		pieModel1.setTitle(
				"NF-e / NFC-e / Venda" + " - " + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));
		pieModel1.setLegendPosition("w");
	}

	public VendaDAO getDaoVenda() {
		return daoVenda;
	}

	public void setDaoVenda(VendaDAO daoVenda) {
		this.daoVenda = daoVenda;
	}

	public List<Object[]> getVendasDinheiro() {
		this.daoVenda = new VendaDAO();

		if (this.vendasDinheiro == null)
			this.vendasDinheiro = this.daoVenda.lerNFe();

		return this.vendasDinheiro;
	}

	public void setVendasDinheiro(List<Object[]> vendasDinheiro) {
		this.vendasDinheiro = vendasDinheiro;
	}

	public List<Object[]> getVendasCartao() {
		this.daoVenda = new VendaDAO();

		if (this.vendasCartao == null)
			this.vendasCartao = this.daoVenda.lerNFCe();

		return this.vendasCartao;
	}

	public void setVendasCartao(List<Object[]> vendasCartao) {
		this.vendasCartao = vendasCartao;
	}

	public List<Object[]> getVendasOutras() {
		this.daoVenda = new VendaDAO();

		if (this.vendasOutras == null)
			this.vendasOutras = this.daoVenda.lerVenda();

		return this.vendasCartao;
	}

	public void setVendasOutras(List<Object[]> vendasOutras) {
		this.vendasOutras = vendasOutras;
	}

	public List<Object> getVendasTodasMes() {

		this.daoVenda = new VendaDAO();

		if (this.vendasTodasMes == null)
			this.vendasTodasMes = this.daoVenda.lerVendasTodasMesNFeNFCeVenda();

		return vendasTodasMes;
	}

	public void setVendasTodasMes(List<Object> vendasTodasMes) {
		this.vendasTodasMes = vendasTodasMes;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

}