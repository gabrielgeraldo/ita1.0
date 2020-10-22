package br.com.ita.dominio.dao.filtros;

import java.io.Serializable;
import java.util.Date;

import br.com.ita.dominio.DataParaConsulta;
import br.com.ita.dominio.StatusTitulo;

public class FiltroContasReceber implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer numero;

	private Date dataInicio;

	private Date dataFim;

	private DataParaConsulta dataParaConsulta;

	private StatusTitulo statusTitulo;

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

	public DataParaConsulta getDataParaConsulta() {
		return dataParaConsulta;
	}

	public void setDataParaConsulta(DataParaConsulta dataParaConsulta) {
		this.dataParaConsulta = dataParaConsulta;
	}

	public StatusTitulo getStatusTitulo() {
		return statusTitulo;
	}

	public void setStatusTitulo(StatusTitulo statusTitulo) {
		this.statusTitulo = statusTitulo;
	}

}
