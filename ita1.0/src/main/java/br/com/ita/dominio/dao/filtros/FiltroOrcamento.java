package br.com.ita.dominio.dao.filtros;

import java.io.Serializable;
import java.util.Date;

public class FiltroOrcamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigo;

	private Date dataInicio;

	private Date dataFim;

	public Long getCodigo() {
		return codigo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}
