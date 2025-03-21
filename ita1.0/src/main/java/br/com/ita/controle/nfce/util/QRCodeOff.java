package br.com.ita.controle.nfce.util;

import com.fincatto.documentofiscal.nfe.NFeConfig;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota;
import com.fincatto.documentofiscal.nfe400.utils.qrcode20.NFGeraQRCodeContingenciaOffline20;

public class QRCodeOff extends NFGeraQRCodeContingenciaOffline20 {

	public QRCodeOff(NFNota nota, NFeConfig config) {
		super(nota, config);
		// TODO Auto-generated constructor stub
	}

}
