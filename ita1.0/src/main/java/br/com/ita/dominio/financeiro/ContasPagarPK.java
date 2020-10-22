package br.com.ita.dominio.financeiro;

import java.io.Serializable;

import javax.persistence.Embeddable;

import br.com.ita.dominio.Fornecedor;

@Embeddable
public class ContasPagarPK implements Serializable {

	private static final long serialVersionUID = -637018809489152388L;

	private Integer numeroCP;

	private Integer parcelaCP;

	private Fornecedor fornecedor;

	public Integer getNumeroCP() {
		return numeroCP;
	}

	public void setNumeroCP(Integer numeroCP) {
		this.numeroCP = numeroCP;
	}

	public Integer getParcelaCP() {
		return parcelaCP;
	}

	public void setParcelaCP(Integer parcelaCP) {
		this.parcelaCP = parcelaCP;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fornecedor == null) ? 0 : fornecedor.hashCode());
		result = prime * result + ((numeroCP == null) ? 0 : numeroCP.hashCode());
		result = prime * result + ((parcelaCP == null) ? 0 : parcelaCP.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContasPagarPK other = (ContasPagarPK) obj;
		if (fornecedor == null) {
			if (other.fornecedor != null)
				return false;
		} else if (!fornecedor.equals(other.fornecedor))
			return false;
		if (numeroCP == null) {
			if (other.numeroCP != null)
				return false;
		} else if (!numeroCP.equals(other.numeroCP))
			return false;
		if (parcelaCP == null) {
			if (other.parcelaCP != null)
				return false;
		} else if (!parcelaCP.equals(other.parcelaCP))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Numero: " + numeroCP + " - Parcela: " + parcelaCP + " - Fornecedor: " + fornecedor;
	}

}
