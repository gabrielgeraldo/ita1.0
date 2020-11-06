package br.com.ita.controle.nfe;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.classes.distribuicao.NFDistribuicaoDFeLote;
import com.fincatto.documentofiscal.nfe.classes.distribuicao.NFDistribuicaoIntRetorno;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;

import br.com.ita.dominio.notafiscal.NFeConfigIta;

@ManagedBean(name = "consultarNfeNaSefazMB")
@RequestScoped
public class ConsultarNfeNaSefazMB {

	private NFeConfigIta config = new NFeConfigIta();

	public void acaoConsultar() {

		try {

			System.out.println("CONSULTANDO...");

			final NFDistribuicaoIntRetorno retorno = new WSFacade(config).consultarDistribuicaoDFe("16993758000108",
					DFUnidadeFederativa.RJ, "33191229230227000105550020000000421327691311", null, null);

			System.out.println("Codigo Status Reposta: " + retorno.getCodigoStatusReposta());
			System.out.println("Data Hora Resposta: " + retorno.getDataHoraResposta());
			System.out.println("Maximo NSU: " + retorno.getMaximoNSU());
			System.out.println("Motivo: " + retorno.getMotivo());
			System.out.println("Ultimo NSU: " + retorno.getUltimoNSU());
			System.out.println("Versao: " + retorno.getVersao());
			System.out.println("Versao Aplicativo: " + retorno.getVersaoAplicativo());
			System.out.println("Ambiente: " + retorno.getAmbiente());

			NFDistribuicaoDFeLote doc = retorno.getLote();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public NFeConfigIta getConfig() {
		return config;
	}

	public void setConfig(NFeConfigIta config) {
		this.config = config;
	}

}
