package br.com.ita.controle.nfce.util;

import java.security.NoSuchAlgorithmException;

import com.fincatto.documentofiscal.nfe.NFeConfig;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota;
import com.fincatto.documentofiscal.nfe400.utils.qrcode20.NFGeraQRCodeEmissaoNormal20;

public class QRCode extends NFGeraQRCodeEmissaoNormal20 {

	public QRCode(NFNota nota, NFeConfig config) {
		super(nota, config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getQRCode() throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		return super.getQRCode();
	}

	@Override
	public String getUrlQRCode() {
		// TODO Auto-generated method stub
		return super.getUrlQRCode();
	}

	@Override
	public String urlConsultaChaveAcesso() {
		// TODO Auto-generated method stub
		return super.urlConsultaChaveAcesso();
	}

	

}
