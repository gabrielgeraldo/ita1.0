package br.com.ita.controle.relatorio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import br.com.ita.controle.util.JPAUtil;
import br.com.ita.controle.util.JSFUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Named("orcamentoRelMB")
@ViewScoped
public class OrcamentoRelMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Favor digitar uma data in�cio.")
	private Date dataInicio;

	@NotNull(message = "Favor digitar uma data final.")
	private Date dataFim;

	@NotNull(message = "Selecione o tipo do relat�rio.")
	private String tipoRelatorio;

	public String acaoRelatorios() {
		return "/Relatorio/orcamento?faces-redirect=true";
	}

	@PostConstruct
	public void init() {
		this.tipoRelatorio = "orcNaoFat";
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

	public void gerarRelatorio() {

		try {

			String caminho = null;

			Map<String, Object> parametros = new HashMap<>();

			if (this.tipoRelatorio.equals("orcNaoFat")) {

				caminho = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/report/orcNaoFat.jasper");

			} else if (this.tipoRelatorio.equals("orcFat")) {

				caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/report/orcFat.jasper");

			}

			Timestamp dataInicioConvertida = new Timestamp(dataInicio.getTime());
			Timestamp dataFimConvertida = new Timestamp(dataFim.getTime());

			parametros.put("DATA_INI", dataInicioConvertida);
			parametros.put("DATA_FIM", dataFimConvertida);

			Connection conexao = JPAUtil.getConexaoJDBC();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);

			// MOSTRA O RELATORIO EM PDF NO BROWSER.
			byte[] b = JasperExportManager.exportReportToPdf(relatorio);
			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			res.setContentType("application/pdf");

			// Gerar o relat�rio e disponibiliza diretamente na p�gina
			res.setHeader("Content-Disposition", "inline;filename=Relatorio.pdf");

			try {
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				FacesContext.getCurrentInstance().responseComplete();
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSFUtil.retornarMensagemInfo("Relatorio gerado com sucesso!", null, null);

		} catch (

		JRException erro) {
			erro.printStackTrace();
			JSFUtil.retornarMensagemErro("Erro ao gerar relatorio!", null, null);
		} catch (java.lang.NullPointerException erro) {
			erro.printStackTrace();
			JSFUtil.retornarMensagemErro("Falha ao gerar relatorio!", erro.getMessage(), null);
		} catch (Exception erro) {
			erro.printStackTrace();
			JSFUtil.retornarMensagemFatal("Erro/Falha: Desconhecido!", null, null);
		}

	}
}
