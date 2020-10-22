package br.com.ita.controle.relatorio;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.ita.controle.util.JPAUtil;
import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.CondicaoPagamento;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.dao.CondicaoPagamentoDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

@ManagedBean(name = "relatorioMB")
@ViewScoped
public class RelatorioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Favor digitar uma data início.")
	private Date dataInicio;

	@NotNull(message = "Favor digitar uma data final.")
	private Date dataFim;

	@NotNull(message = "Selecione o tipo do relatório.")
	private String tipoRelatorio;

	@NotEmpty(message = "Selecione as possíveis situações.")
	private List<String> situacaoSelecionada = new ArrayList<>();

	@NotEmpty(message = "Favor selecionar as formas de pagamento.")
	private List<String> fpgtSelecionada = new ArrayList<>();

	private List<CondicaoPagamento> formasPagamento = null;

	private CondicaoPagamentoDAO daoFormasPagamento = null;

	private List<Produto> dataSource = new ArrayList<>();

	@PostConstruct
	public void init() {
		this.tipoRelatorio = "simples";
		this.situacaoSelecionada.add("FINALIZADA");
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;

	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public List<String> getSituacaoSelecionada() {
		return situacaoSelecionada;
	}

	public void setSituacaoSelecionada(List<String> situacaoSelecionada) {
		this.situacaoSelecionada = situacaoSelecionada;
	}

	public List<String> getFpgtSelecionada() {
		return fpgtSelecionada;
	}

	public void setFpgtSelecionada(List<String> fpgtSelecionada) {
		this.fpgtSelecionada = fpgtSelecionada;
	}

	public List<CondicaoPagamento> getFormasPagamento() {
		this.daoFormasPagamento = new CondicaoPagamentoDAO();

		if (this.formasPagamento == null)
			this.formasPagamento = this.daoFormasPagamento.lerTodos();

		return this.formasPagamento;
	}

	public void setFormasPagamento(List<CondicaoPagamento> formasPagamento) {
		this.formasPagamento = formasPagamento;
	}

	public CondicaoPagamentoDAO getDaoFormasPagamento() {
		return daoFormasPagamento;
	}

	public void setDaoFormasPagamento(CondicaoPagamentoDAO daoFormasPagamento) {
		this.daoFormasPagamento = daoFormasPagamento;
	}

	public String acaoRelatorios() {
		return "/Relatorio/relatorioVenda?faces-redirect=true";
	}

	public List<Produto> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<Produto> dataSource) {
		this.dataSource = dataSource;
	}

	public void gerarRelatorio() {
		try {

			String caminho = null;

			Map<String, Object> parametros = new HashMap<>();

			if (this.tipoRelatorio.equals("simples")) {

				caminho = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/report/relatorioVenda.jasper");

				// System.out.println(caminho);

			} else if (this.tipoRelatorio.equals("completo")) {

				caminho = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/report/relatorioVendaItem.jasper");

				String caminhoSub = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/report/relatorioVendaItemSub.jasper");

				// System.out.println(caminhoSub);

				// PASSANDO O CAMINHO DO JASPER (SUBREPORT) PARA O JASPER
				// MESTRE.
				parametros.put("SUBREPORT_DIR", caminhoSub);

			}

			Timestamp dataInicioConvertida = new Timestamp(dataInicio.getTime());
			Timestamp dataFimConvertida = new Timestamp(dataFim.getTime());

			parametros.put("VENDA_DATA_INI", dataInicioConvertida);
			parametros.put("VENDA_DATA_FIM", dataFimConvertida);

			parametros.put("SITUACAO", this.getSituacaoSelecionada());
			parametros.put("FOR_PGT", this.getFpgtSelecionada());

			// System.out.println("SITUACAO" + this.getFpgtSelecionada());
			// System.out.println("FOR_PGT" + this.getSituacaoSelecionada());

			Connection conexao = JPAUtil.getConexaoJDBC();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);

			// DIRETO PRA IMPRSSORA.
			// JasperPrintManager.printReport(relatorio, true);

			// MOSTRA O RELATORIO NA TELINHA JAVA.
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JasperViewer viewer = new JasperViewer(relatorio, false);
				viewer.setTitle("Relatório");
				viewer.setVisible(true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}

			// MOSTRA O RELATORIO EM PDF NO BROWSER.
			/*
			 * byte[] b = JasperExportManager.exportReportToPdf(relatorio);
			 * HttpServletResponse res = (HttpServletResponse)
			 * FacesContext.getCurrentInstance().getExternalContext()
			 * .getResponse(); res.setContentType("application/pdf"); // Código
			 * abaixo gerar o relatório e disponibiliza diretamente na // página
			 * res.setHeader("Content-Disposition",
			 * "inline; filename=Relatorio.pdf"); // Código abaixo gerar o
			 * relatório e disponibiliza para o cliente // baixar ou salvar //
			 * res.setHeader("Content-disposition", //
			 * "attachment;filename=arquivo.pdf");
			 * res.getOutputStream().write(b); res.getCharacterEncoding();
			 * FacesContext.getCurrentInstance().responseComplete();
			 */

			JSFUtil.retornarMensagemInfo("Relatório gerado com sucesso!", null, null);

		} catch (JRException erro) {
			erro.printStackTrace();
			JSFUtil.retornarMensagemErro("Erro ao gerar relatório!", null, null);
		} catch (java.lang.NullPointerException erro) {
			System.out.println("Erro/Fala: (java.lang.NullPointerException))");
			erro.printStackTrace();
			JSFUtil.retornarMensagemErro("Falha ao gerar relatório!", erro.getMessage(), null);
		} catch (Exception erro) {
			System.out.println("Erro/Falha: Desconhecido).");
			erro.printStackTrace();
			JSFUtil.retornarMensagemFatal("Erro/Falha: Desconhecido!", null, null);
		}

	}

}
