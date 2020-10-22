package br.com.ita.controle.relatorio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import br.com.ita.controle.util.JPAUtil;
import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.dao.financeiro.BancoDAO;
import br.com.ita.dominio.financeiro.Banco;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@ManagedBean(name = "movBancariaMB")
@ViewScoped
public class MovimentacaoBancariaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Favor digitar uma data início.")
	private Date dataInicio;

	@NotNull(message = "Favor digitar uma data final.")
	private Date dataFim;

	@NotNull(message = "Selecione o banco.")
	private Banco banco;

	@Inject
	private BancoDAO bancoDAO;

	private List<Banco> bancos = null;

	public String acaoRelatorios() {
		return "/Relatorio/movBancaria?faces-redirect=true";
	}

	public void gerarRelatorio() {
		try {

			String caminho = null;

			Map<String, Object> parametros = new HashMap<>();

			caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/report/movBancaria.jasper");

			Timestamp dataInicioConvertida = new Timestamp(dataInicio.getTime());
			Timestamp dataFimConvertida = new Timestamp(dataFim.getTime());

			parametros.put("DATA_INI", dataInicioConvertida);
			parametros.put("DATA_FIM", dataFimConvertida);
			parametros.put("BANCO", banco.getCodigo());

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

	public Date getDataInicio() {
		return dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public BancoDAO getBancoDAO() {
		return bancoDAO;
	}

	public List<Banco> getBancos() {
		if (this.bancos == null)
			this.bancos = this.bancoDAO.lerTodos();

		return bancos;
	}

	public void setBancoDAO(BancoDAO bancoDAO) {
		this.bancoDAO = bancoDAO;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

}
