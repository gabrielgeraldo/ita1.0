package br.com.ita.controle.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {

	// Fonte:https://stackoverflow.com/questions/17853541/java-how-to-convert-a-xml-string-into-an-xml-file
	public static void armazernarXmlLocalNfe(String xmlSource, String chave)
			throws SAXException, ParserConfigurationException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));
		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		// StreamResult result = new StreamResult(new File("c:/Ita/Ita_xml/" +
		// chave + ".xml"));
		StreamResult result = new StreamResult(new File("c:/Ita/xml/nfe/" + chave + ".xml"));
		transformer.transform(source, result);

	}

	// Fonte:https://stackoverflow.com/questions/17853541/java-how-to-convert-a-xml-string-into-an-xml-file
	public static void armazernarXmlLocalNfce(String xmlSource, String chave)
			throws SAXException, ParserConfigurationException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));
		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("c:/Ita/xml/nfce/" + chave + ".xml"));
		transformer.transform(source, result);

	}

	// VERIFICAR. Está fazendo o downloand mas com falhas.
	// Fonte:https://stackoverflow.com/questions/23615744/how-to-download-a-xml-dynamic-generated-file-in-jsf
	public static void downloadXml(String xmlSource, String chave) throws IOException {

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", "attachment;filename=" + chave + ".xml");

		ServletOutputStream out = response.getOutputStream();
		out.write(xmlSource.getBytes());
		out.flush();

	}

	public static String lerXml(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
