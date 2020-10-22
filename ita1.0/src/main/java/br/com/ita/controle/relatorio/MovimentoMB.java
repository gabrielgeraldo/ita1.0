package br.com.ita.controle.relatorio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import br.com.ita.controle.util.JPAUtil;
import br.com.ita.controle.util.JSFUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@ManagedBean(name = "movimentoMB")
@ViewScoped
public class MovimentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Favor digitar uma data início.")
	private Date dataInicio;

	@NotNull(message = "Favor digitar uma data final.")
	private Date dataFim;

	@NotNull(message = "Selecione relatório.")
	private String relatorio;

	@NotNull(message = "Selecione o tipo do relatório.")
	private String tipoRelatorio;

	@PostConstruct
	public void init() {
		this.tipoRelatorio = "simples";
		this.relatorio = "nfce";
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

	public String getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(String relatorio) {
		this.relatorio = relatorio;
	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public String acaoRelatorios() {
		return "/Relatorio/movimento?faces-redirect=true";
	}

	public void gerarRelatorio() {

		try {

			String caminho = null;

			Map<String, Object> parametros = new HashMap<>();

			if (this.relatorio.equals("nfce")) {

				if (this.tipoRelatorio.equals("simples")) {
					caminho = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoNFCe.jasper");
				} else if (this.tipoRelatorio.equals("completo")) {
					caminho = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoNFCeItem.jasper");
					String caminhoSub = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoNFCeItemSub.jasper");
					parametros.put("SUBREPORT_DIR", caminhoSub);
				}

			} else if (this.relatorio.equals("nfe")) {

				if (this.tipoRelatorio.equals("simples")) {
					caminho = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoNFe.jasper");
				} else if (this.tipoRelatorio.equals("completo")) {
					caminho = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoNFeItem.jasper");
					String caminhoSub = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoNFeItemSub.jasper");
					parametros.put("SUBREPORT_DIR", caminhoSub);
				}

			} else if (this.relatorio.equals("venda")) {

				if (this.tipoRelatorio.equals("simples")) {
					caminho = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoVenda.jasper");
				} else if (this.tipoRelatorio.equals("completo")) {
					caminho = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoVendaItem.jasper");
					String caminhoSub = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/report/movimentoVendaItemSub.jasper");
					parametros.put("SUBREPORT_DIR", caminhoSub);
				}

			}

			Timestamp dataInicioConvertida = new Timestamp(dataInicio.getTime());
			Timestamp dataFimConvertida = new Timestamp(dataFim.getTime());

			parametros.put("DATA_INI", dataInicioConvertida);
			parametros.put("DATA_FIM", dataFimConvertida);

			Connection conexao = JPAUtil.getConexaoJDBC();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);

			// DIRETO PRA IMPRSSORA.
			// JasperPrintManager.printReport(relatorio, true);

			// MOSTRA O RELATORIO NA TELINHA JAVA.
			// try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// JasperViewer viewer = new JasperViewer(relatorio, false);
			// viewer.setTitle("Relatório");
			// viewer.setVisible(true);
			// } catch (ClassNotFoundException e) {
			// e.printStackTrace();
			// } catch (InstantiationException e) {
			// e.printStackTrace();
			// } catch (IllegalAccessException e) {
			// e.printStackTrace();
			// } catch (UnsupportedLookAndFeelException e) {
			// e.printStackTrace();
			// }

			// MOSTRA O RELATORIO EM PDF NO BROWSER.
			byte[] b = JasperExportManager.exportReportToPdf(relatorio);
			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			res.setContentType("application/pdf");

			// Código abaixo gerar o relatório e disponibiliza diretamente na
			// página
			res.setHeader("Content-Disposition", "inline;filename=Relatorio.pdf");

			try {
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				FacesContext.getCurrentInstance().responseComplete();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSFUtil.retornarMensagemInfo("Relatório gerado com sucesso!", null, null);

		} catch (

		JRException erro) {
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
