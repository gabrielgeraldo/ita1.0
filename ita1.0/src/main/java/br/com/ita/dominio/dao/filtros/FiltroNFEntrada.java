package br.com.ita.dominio.dao.filtros;

import java.io.Serializable;
import java.util.Date;

public class FiltroNFEntrada implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numero;

	private Date dataEmissaoInicio;

	private Date dataEmissaoFim;

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDataEmissaoInicio() {
		return dataEmissaoInicio;
	}

	public void setDataEmissaoInicio(Date dataEmissaoInicio) {
		this.dataEmissaoInicio = dataEmissaoInicio;
	}

	public Date getDataEmissaoFim() {
		return dataEmissaoFim;
	}

	public void setDataEmissaoFim(Date dataEmissaoFim) {
		this.dataEmissaoFim = dataEmissaoFim;
	}

}
