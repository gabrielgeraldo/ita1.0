package br.com.ita.dominio.financeiro;

import java.io.Serializable;

import javax.persistence.Embeddable;

import br.com.ita.dominio.Cliente;

@Embeddable
public class ContasReceberPK implements Serializable {

	private static final long serialVersionUID = -637018809489152388L;

	private Integer numeroCR;

	private Integer parcelaCR;

	private Cliente cliente;

	public Integer getNumeroCR() {
		return numeroCR;
	}

	public void setNumeroCR(Integer numeroCR) {
		this.numeroCR = numeroCR;
	}

	public Integer getParcelaCR() {
		return parcelaCR;
	}

	public void setParcelaCR(Integer parcelaCR) {
		this.parcelaCR = parcelaCR;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((numeroCR == null) ? 0 : numeroCR.hashCode());
		result = prime * result + ((parcelaCR == null) ? 0 : parcelaCR.hashCode());
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
		ContasReceberPK other = (ContasReceberPK) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (numeroCR == null) {
			if (other.numeroCR != null)
				return false;
		} else if (!numeroCR.equals(other.numeroCR))
			return false;
		if (parcelaCR == null) {
			if (other.parcelaCR != null)
				return false;
		} else if (!parcelaCR.equals(other.parcelaCR))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Numero: " + numeroCR + " - Parcela: " + parcelaCR + " - Cliente: " + cliente;
	}

}
