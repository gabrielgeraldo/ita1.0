package br.com.ita.dominio.dao.filtros;

import java.io.Serializable;
import java.util.Date;

public class FiltroNfe implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numero;

	private Date dataInicio;

	private Date dataFim;

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}
