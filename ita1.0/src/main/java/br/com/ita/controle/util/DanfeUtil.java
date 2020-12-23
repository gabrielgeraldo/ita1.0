package br.com.ita.controle.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.ita.controle.config.Config;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class DanfeUtil {

	/**
	 * @param args
	 *            the command line arguments Fonte:
	 *            http://www.javac.com.br/jc/posts/list/2471.page
	 */
	@SuppressWarnings("unchecked") // VERIFICAR.
	public static void imprimirDanfeNfe(String Xml) throws FileNotFoundException, IOException {
		String stringComEstruturaDoXML = Xml;
		try {

			InputStream stream = new ByteArrayInputStream(stringComEstruturaDoXML.getBytes("utf-8"));
			// JRXmlDataSource xml = new JRXmlDataSource(stream,
			// "/nfeProc/NFe/infNFe/det");

			// ImageIcon imagemTituloJanela = new ImageIcon("C:/NFe/nfe.gif");
			// Caminho do arquivo .JASPER

			// String relatorio = ("C:/NFe/danfe1.jasper");
			String relatorio = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/report/danfe1.jasper");

			// Configurando a classe JRXmlDataSource que apontara o caminho do
			// nosso XML de dados e sua pesquisa XPath geral
			JRXmlDataSource xml = new JRXmlDataSource(stream, "/nfeProc/NFe/infNFe/det");
			@SuppressWarnings("rawtypes") // VERIFICAR.
			HashMap mapa = new HashMap();

			String caminhoLogoDanfe = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/report/logo-nfe.png");

			mapa.put("Logo", caminhoLogoDanfe);

			// Gerando o relatorio (Filling) informando o caminho do relatorio,
			// os parametros (neste caso nenhum paramentro esta sendo passado ao
			// relatorio, por isso o HashMap esta vazio) e o objeto
			// JRXmlDataSource configurado)
			JasperPrint jp = JasperFillManager.fillReport(relatorio, mapa, xml);
			// Utilizando o JasperView, uma classe desktop do jasper para
			// visualiza��o dos relatorios

			// Tema form java
			// try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// } catch (ClassNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (InstantiationException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IllegalAccessException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (UnsupportedLookAndFeelException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// exportando arquivo para formulario java.
			// JasperViewer jv = new JasperViewer(jp, false);
			// jv.setTitle("VISUALIZADOR DE DOCUMENTO FISCAL ELETR�NICA");
			// jv.setIconImage(imagemTituloJanela.getImage());
			// jv.setVisible(true);

			// MOSTRA O RELATORIO EM PDF NO BROWSER.
			byte[] b = JasperExportManager.exportReportToPdf(jp);
			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			res.setContentType("application/pdf");

			// C�digo abaixo gerar o relat�rio e disponibiliza diretamente na
			// p�gina
			res.setHeader("Content-Disposition", "inline;filename=Relatorio.pdf");

			try {
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				FacesContext.getCurrentInstance().responseComplete();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            the command line arguments Fonte:
	 *            http://www.javac.com.br/jc/posts/list/2471.page
	 */
	@SuppressWarnings({ "unchecked" }) // VERIFICAR.
	public static void imprimirComprovanteVenda(String Xml) throws FileNotFoundException, IOException {
		String stringComEstruturaDoXML = Xml;
		try {

			InputStream stream = new ByteArrayInputStream(stringComEstruturaDoXML.getBytes("utf-8"));

			String relatorio = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/report/compVenda.jasper");

			JRXmlDataSource xml = new JRXmlDataSource(stream, "/");
			@SuppressWarnings("rawtypes")
			HashMap mapa = new HashMap();

			JasperPrint jp = JasperFillManager.fillReport(relatorio, mapa, xml);

			byte[] b = JasperExportManager.exportReportToPdf(jp);

			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			res.setContentType("application/pdf");
			res.setHeader("Content-Disposition", "inline;;filename=Relatorio.pdf");

			try {
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				FacesContext.getCurrentInstance().responseComplete();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (JRException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 *            the command line arguments Fonte:
	 *            http://www.javac.com.br/jc/posts/list/2471.page
	 */
	@SuppressWarnings({ "unchecked" }) // VERIFICAR.
	public static void gravaComprovanteVenda(String Xml, Long codigoVenda) throws FileNotFoundException, IOException {

		String stringComEstruturaDoXML = Xml;

		try {

			InputStream stream = new ByteArrayInputStream(stringComEstruturaDoXML.getBytes("utf-8"));

			String relatorio = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/report/compVenda.jasper");

			JRXmlDataSource xml = new JRXmlDataSource(stream, "/");
			@SuppressWarnings("rawtypes")
			HashMap mapa = new HashMap();

			JasperPrint jp = JasperFillManager.fillReport(relatorio, mapa, xml);

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jp));
			exporter.setExporterOutput(
					new SimpleOutputStreamExporterOutput("c:\\ita\\temp\\compVenda" + codigoVenda + ".pdf"));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setMetadataAuthor("Petter"); // why not set some
														// config as we like
			exporter.setConfiguration(configuration);
			exporter.exportReport();

		} catch (JRException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private static byte[] getBytes(JRTextExporter exporter, ByteArrayOutputStream baos, JasperPrint jp) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 *            the command line arguments Fonte:
	 *            http://www.javac.com.br/jc/posts/list/2471.page
	 */
	@SuppressWarnings("unchecked") // VERIFICAR.
	public static void imprimirDanfeNfce(String Xml) throws FileNotFoundException, IOException {

		String stringComEstruturaDoXML = Xml;
		try {

			if (Config.propertiesLoader().getProperty("tpImp").equals("1")) {

				InputStream stream = new ByteArrayInputStream(stringComEstruturaDoXML.getBytes("utf-8"));

				String relatorio = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/report/danfeNfce.jasper");

				// JRXmlDataSource xml = new JRXmlDataSource(stream,
				// "/nfeProc/NFe/infNFe/det");
				JRXmlDataSource xml = new JRXmlDataSource(stream, "/");
				@SuppressWarnings("rawtypes")
				HashMap mapa = new HashMap();

				JasperPrint jp = JasperFillManager.fillReport(relatorio, mapa, xml);

				// MOSTRA O RELATORIO EM PDF NO BROWSER.
				byte[] b = JasperExportManager.exportReportToPdf(jp);
				HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
						.getResponse();
				res.setContentType("application/pdf");

				// C�digo abaixo gerar o relat�rio e disponibiliza diretamente
				// na
				// p�gina
				res.setHeader("Content-Disposition", "inline;filename=Relatorio.pdf");

				try {
					res.getOutputStream().write(b);
					res.getOutputStream().flush();
					res.getOutputStream().close();
					FacesContext.getCurrentInstance().responseComplete();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {

				InputStream stream = new ByteArrayInputStream(stringComEstruturaDoXML.getBytes("utf-8"));
				// JRXmlDataSource xml = new JRXmlDataSource(stream,
				// "/nfeProc/NFe/infNFe/det");

				// ImageIcon imagemTituloJanela = new
				// ImageIcon("C:/NFe/nfe.gif");
				// Caminho do arquivo .JASPER

				// String relatorio = ("C:/NFe/danfe1.jasper");
				String relatorio = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/report/danfeNfce.jasper");

				// Configurando a classe JRXmlDataSource que apontara o caminho
				// do
				// nosso XML de dados e sua pesquisa XPath geral
				JRXmlDataSource xml = new JRXmlDataSource(stream, "/");
				@SuppressWarnings("rawtypes") // VERIFICAR.
				HashMap mapa = new HashMap();

				// String caminhoLogoDanfe =
				// FacesContext.getCurrentInstance().getExternalContext()
				// .getRealPath("/report/logo-nfe.png");

				// mapa.put("Logo", caminhoLogoDanfe);

				// Gerando o relatorio (Filling) informando o caminho do
				// relatorio,
				// os parametros (neste caso nenhum paramentro esta sendo
				// passado ao
				// relatorio, por isso o HashMap esta vazio) e o objeto
				// JRXmlDataSource configurado)
				JasperPrint jp = JasperFillManager.fillReport(relatorio, mapa, xml);
				// Utilizando o JasperView, uma classe desktop do jasper para
				// visualiza��o dos relatorios

				// Tema form java
				// try {
				// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				// } catch (ClassNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (InstantiationException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (IllegalAccessException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (UnsupportedLookAndFeelException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// exportando arquivo para formulario java.
				// JasperViewer jv = new JasperViewer(jp, false);
				// jv.setTitle("VISUALIZADOR DE DOCUMENTO FISCAL ELETR�NICA");
				// jv.setIconImage(imagemTituloJanela.getImage());
				// jv.setVisible(true);

				/*
				 * // MOSTRA O RELATORIO EM PDF NO BROWSER. byte[] b =
				 * JasperExportManager.exportReportToPdf(jp);
				 * HttpServletResponse res = (HttpServletResponse)
				 * FacesContext.getCurrentInstance().getExternalContext()
				 * .getResponse(); res.setContentType("application/pdf");
				 * 
				 * // C�digo abaixo gerar o relat�rio e disponibiliza
				 * diretamente na // p�gina res.setHeader("Content-Disposition",
				 * "inline;filename=Relatorio.pdf");
				 * 
				 * try { res.getOutputStream().write(b);
				 * res.getOutputStream().flush(); res.getOutputStream().close();
				 * FacesContext.getCurrentInstance().responseComplete(); } catch
				 * (IOException e) { e.printStackTrace(); }
				 */

				// se nao achar impressora configurada imprima na impressora
				// padrao
				try {
					JasperPrintManager.printReport(jp, false);
					System.out.println("Sem erro.");
				} catch (Exception e) {
					System.out.println("Com erro.");
					e.printStackTrace();
				}
			}

		} catch (JRException e) {
			e.printStackTrace();
		}
	}

}
