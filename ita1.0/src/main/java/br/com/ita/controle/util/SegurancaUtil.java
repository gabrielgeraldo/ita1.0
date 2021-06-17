package br.com.ita.controle.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import br.com.ita.controle.config.Config;

import java.util.*;

public class SegurancaUtil {

	public static boolean verificaMec() {
		/*
		 * String chave = "MINHACHAVESEGITA"; DescriptografiaAES aes = new
		 * DescriptografiaAES(chave); String criptografado =
		 * Config.propertiesLoader().getProperty("mecClient"); String txt =
		 * aes.descriptografa(criptografado); System.out.println(
		 * "MEC REGISTRADO: " + txt);
		 * 
		 * System.out.println("MEC PC: " + NetwrokUtil.getMecAddr());
		 * 
		 * if (NetwrokUtil.getMecAddr().equals(txt)) { System.out.println(
		 * "SISTEMA REGISTRADO!"); return true; }
		 * 
		 * else { System.out.println("SISTEMA NAO REGISTRADO!"); return false; }
		 */
		return true;
	}

	public static boolean verificaSenhaMes() {

		int mes, ano, senhaMes;

		if (NetwrokUtil.verificaConexaoInternet2("https://www.google.com")) {

			// PEGANDO A DATA DA INTERNET.
			// String TIME_SERVER = "time-a.nist.gov";
			String TIME_SERVER = "a.st1.ntp.br";
			NTPUDPClient timeClient = new NTPUDPClient();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(TIME_SERVER);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			TimeInfo timeInfo = null;
			try {
				timeInfo = timeClient.getTime(inetAddress);
			} catch (IOException e) {
				e.printStackTrace();
			}
			long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
			Date time = new Date(returnTime);
			// System.out.println("Time: " + time);
			SimpleDateFormat formatoMes = new SimpleDateFormat("MM");
			// System.out.println("MES NA INTERNET: " +
			// formatoMes.format(time));
			SimpleDateFormat formatoAno = new SimpleDateFormat("yyyy");
			// System.out.println("ANO NA INTERNET: " +
			// formatoAno.format(time));

			mes = Integer.parseInt(formatoMes.format(time));
			ano = Integer.parseInt(formatoAno.format(time));

		} else {
			// PEGANDO A DATA LOCAL.
			/*
			 * Date time = new Date(); SimpleDateFormat formatoMes = new
			 * SimpleDateFormat("MM"); System.out.println("MES LOCAL: " +
			 * formatoMes.format(time)); SimpleDateFormat formatoAno = new
			 * SimpleDateFormat("YYYY"); System.out.println("ANO LOCAL: " +
			 * formatoAno.format(time));
			 * 
			 * mes = Integer.parseInt(formatoMes.format(time)); ano =
			 * Integer.parseInt(formatoAno.format(time));
			 */

			Calendar time = Calendar.getInstance();

			ano = time.get(Calendar.YEAR);
			mes = time.get(Calendar.MONTH) + 1;
			// M�s da classe Calendar � 0 based, isto �, janeiro = 0, fevereiro
			// =1, mar�o = 3 e assim por diante.

			// System.out.println("ANO LOCAL: " + ano);
			// System.out.println("MES LOCAL: " + mes);

		}

		senhaMes = Integer.parseInt(Config.propertiesLoader().getProperty("codClient")) * mes * ano + 1;

		// System.out.println("SENHA MES: " + senhaMes);
		// System.out.println("SENHA MES Sistema: " +
		// Integer.parseInt(Config.propertiesLoader().getProperty("senhaMes")));

		if (senhaMes == Integer.parseInt(Config.propertiesLoader().getProperty("senhaMes")))
			return true;
		else
			return false;
	}

	// Fonte:https://www.mkyong.com/java/how-to-create-xml-file-in-java-jdom-parser/
	public static void criarXmlDeRegistroClient(String cgc, String mec) {

		CriptografiaXmlClientDES x = new CriptografiaXmlClientDES();

		System.out.print("encriptar: " + x.encriptar(cgc));
		System.out.print("decriptar: " + x.decriptar(x.encriptar(cgc)));

		Document doc = new Document();

		Element system = new Element("system");
		doc.setRootElement(system);

		Element one = new Element("one");
		one.addContent(new Element("one").setText(x.encriptar(cgc)));
		doc.getRootElement().addContent(one);

		Element two = new Element("two");
		two.addContent(new Element("two").setText(x.encriptar(mec)));
		doc.getRootElement().addContent(two);

		try {
			Writer out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("c:/Ita/registroClient.xml"), "UTF8"));
			XMLOutputter xout = new XMLOutputter();
			xout.output(doc, out);
			// System.out.println("XML criado com sucesso!");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Fonte:http://www.guj.com.br/t/criando-xml-com-jdom/40920
	public static boolean validarXmlDeRegistro() {

		boolean registrado = false;

		String twoXmlServer = null;

		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("c:/Ita/registroServer.xml");

		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> listTwo = rootNode.getChildren("two");

			for (int i = 0; i < listTwo.size(); i++) {

				Element node = (Element) listTwo.get(i);
				twoXmlServer = node.getChildText("two");

			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}

		if (NetwrokUtil.getMecAddr().equals(twoXmlServer))
			registrado = true;

		return registrado;
	}

}
