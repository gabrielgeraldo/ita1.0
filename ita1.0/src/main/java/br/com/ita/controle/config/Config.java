package br.com.ita.controle.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.faces.context.FacesContext;

public class Config {

	// Crio uma instancia da classe properties
	private static Properties prop = new Properties();

	// Crio um m�todo est�tico que pode ser acessado por outras classes da
	// aplica��o sem a necessidade de instanciar
	public static Properties propertiesLoader() {

		// Informo o caminho onde se encontra meu arquivo properties de forma
		// din�mica
		// Poderia fazer isso de forma est�tica passando o diret�rio completo
		// mas obviamente isso n�o � muito interessante.

		String atualDir = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/conf.properties");
		
		//String atualDir = FacesContext.getCurrentInstance()
				//.getClass().getResource("META-INF/conf.properties").getPath();
		

		try { // Tento recuperar as informa��es do arquivo de propriedades

			// Apenas para testar imprimo o diret�rio atual do meu arquivo
			// properties
			//System.out.println(atualDir);
			// Crio uma inst�ncia de File passando o meu arquivo .properties via
			// construtor
			File file = new File(atualDir);
			// Agora crio uma inst�ncia de FileInputStream passando via
			// construtor o objeto file instanciado acima
			FileInputStream fileInputStream = new FileInputStream(file);
			// Leio o fileInputStream recuperando assim o mapa contendo chaves e
			// valores
			prop.load(fileInputStream);
			// Fecho o fileInputStream
			fileInputStream.close();
		} catch (Exception e) {
			System.out.println("Aten��o - falha grave: Arquivo de configura��o n�o carregado.");
			e.printStackTrace();
		}
		return prop;

	}

	public static void atualizaProperties(String key, String value) {

		String atualDir = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/conf.properties");

		//String atualDir = FacesContext.getCurrentInstance()
				//.getClass().getResource("META-INF/conf.properties").getPath();
		

		try { // Tento recuperar as informa��es do arquivo de propriedades

			// Apenas para testar imprimo o diret�rio atual do meu arquivo
			// properties
			//System.out.println(atualDir);
			// Crio uma inst�ncia de File passando o meu arquivo .properties via
			// construtor
			File file = new File(atualDir);
			// Agora crio uma inst�ncia de FileInputStream passando via
			// construtor o objeto file instanciado acima
			FileInputStream fileInputStream = new FileInputStream(file);
			// Leio o fileInputStream recuperando assim o mapa contendo chaves e
			// valores
			prop.load(fileInputStream);

			// Fecho o fileInputStream
			fileInputStream.close();

			prop.setProperty(key, value);

			//System.out.println("prop.setProperty: " + prop.getProperty(key));

			// salvar o arquivo
			FileOutputStream fos = new FileOutputStream(file);
			prop.store(fos, null);
			fos.close();

			//System.out.println("prop.getProperty: " + prop.getProperty(key));

		} catch (Exception e) {
			System.out.println("Aten��o - falha grave: Arquivo de configura��o n�o atualizado.");
			e.printStackTrace();
		}

	}
}
