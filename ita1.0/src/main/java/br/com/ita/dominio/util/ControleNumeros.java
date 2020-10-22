package br.com.ita.dominio.util;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.ita.dominio.BaseEntity;

@Entity
@Table(name = "controle_numeros")
public class ControleNumeros implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codigo_controlenumeros")
	@SequenceGenerator(sequenceName = "codigo_controlenumeros", name = "codigo_controlenumeros", allocationSize = 1)
	private Long codigo;

	@Column(length = 5, nullable = false)
	@NotNull(message = "Favor digitar uma descrição.")
	@Size(min = 1, max = 5, message = "A tabela deve ter entre 1 e 5 caracteres.")
	private String tabela;

	@Column(length = 2, nullable = false)
	@NotNull(message = "Favor digitar uma descrição.")
	@Size(min = 1, max = 2, message = "A chave deve ter entre 1 e 2 caracteres.")
	private String chave;

	@Column
	@NotNull(message = "Favor digitar um número anterior.")
	private Integer numeroAnterior;

	@Column
	@NotNull(message = "Favor digitar um número atual.")
	private Integer numeroAtual;

	@Column
	@NotNull(message = "Favor digitar um proximo número.")
	private Integer proximoNumero;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Integer getNumeroAnterior() {
		return numeroAnterior;
	}

	public void setNumeroAnterior(Integer numeroAnterior) {
		this.numeroAnterior = numeroAnterior;
	}

	public Integer getNumeroAtual() {
		return numeroAtual;
	}

	public void setNumeroAtual(Integer numeroAtual) {
		this.numeroAtual = numeroAtual;
	}

	public Integer getProximoNumero() {
		return proximoNumero;
	}

	public void setProximoNumero(Integer proximoNumero) {
		this.proximoNumero = proximoNumero;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		ControleNumeros other = (ControleNumeros) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return tabela;
	}

}
