package br.com.ita.dominio.notafiscal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import com.fincatto.documentofiscal.DFAmbiente;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFeConfig;

import br.com.ita.controle.config.Config;

public class NFeConfigIta extends NFeConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private KeyStore keyStoreCertificado = null;
	private KeyStore keyStoreCadeia = null;

	@Override
	public String getVersao() {
		return "4.00";
	}

	@Override
	public Integer getCodigoSegurancaContribuinteID() {
		return Integer.parseInt(Config.propertiesLoader().getProperty("idCodigoSegurancaContribuinte"));
	}

	@Override
	public String getCodigoSegurancaContribuinte() {
		return Config.propertiesLoader().getProperty("codigoSegurancaContribuinte");
	}

	@Override
	public DFAmbiente getAmbiente() {
		return DFAmbiente.valueOfCodigo(Config.propertiesLoader().getProperty("ambiente"));
	}

	@Override
	public DFUnidadeFederativa getCUF() {
		return DFUnidadeFederativa.RJ;
	}

	@Override
	public KeyStore getCadeiaCertificadosKeyStore() throws KeyStoreException {
		if (this.keyStoreCadeia == null) {
			this.keyStoreCadeia = KeyStore.getInstance("JKS");
			try (InputStream cadeia = new FileInputStream("C:/Ita/cert/homologacao.cacerts")) {
				this.keyStoreCadeia.load(cadeia, this.getCadeiaCertificadosSenha().toCharArray());
			} catch (CertificateException | NoSuchAlgorithmException | IOException e) {
				this.keyStoreCadeia = null;
				throw new KeyStoreException("Nao foi possibel montar o KeyStore com o certificado", e);
			}
		}
		return this.keyStoreCadeia;
	}

	@Override
	public String getCadeiaCertificadosSenha() {
		return "4074";
	}

	@Override
	public KeyStore getCertificadoKeyStore() throws KeyStoreException {
		if (this.keyStoreCertificado == null) {
			this.keyStoreCertificado = KeyStore.getInstance("PKCS12");
			try (InputStream certificadoStream = new FileInputStream("C:/Ita/cert/cert.pfx")) {
				this.keyStoreCertificado.load(certificadoStream, this.getCertificadoSenha().toCharArray());
			} catch (CertificateException | NoSuchAlgorithmException | IOException e) {
				this.keyStoreCadeia = null;
				throw new KeyStoreException("Nao foi possibel montar o KeyStore com a cadeia de certificados", e);
			}
		}
		return this.keyStoreCertificado;
	}

	@Override
	public String getCertificadoSenha() {
		return Config.propertiesLoader().getProperty("senhaCert");
	}

	public KeyStore getKeyStoreCertificado() {
		return keyStoreCertificado;
	}

	public void setKeyStoreCertificado(KeyStore keyStoreCertificado) {
		this.keyStoreCertificado = keyStoreCertificado;
	}

	public KeyStore getKeyStoreCadeia() {
		return keyStoreCadeia;
	}

	public void setKeyStoreCadeia(KeyStore keyStoreCadeia) {
		this.keyStoreCadeia = keyStoreCadeia;
	}

}
