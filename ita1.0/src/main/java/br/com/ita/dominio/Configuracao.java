package br.com.ita.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "configuracoes")
public class Configuracao implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull(message = "O código é de preenchimento obrigatório.")
	private Long codigo;

	@Column(length = 3)
	private String csosn;

	@Column(precision = 7, scale = 2)
	private BigDecimal pCredSN;

	@Column(precision = 7, scale = 2)
	private BigDecimal vCredICMSSN;

	@Column(precision = 7, scale = 2)
	private BigDecimal vBCSTRet;

	@Column(precision = 7, scale = 2)
	private BigDecimal vICMSSTRet;

	@Column(precision = 7, scale = 2)
	private BigDecimal pST;

	private Integer m;

	public Long getCodigo() {
		return codigo;
	}

	public String getCsosn() {
		return csosn;
	}

	public BigDecimal getpCredSN() {
		return pCredSN;
	}

	public BigDecimal getvCredICMSSN() {
		return vCredICMSSN;
	}

	public BigDecimal getvBCSTRet() {
		return vBCSTRet;
	}

	public BigDecimal getvICMSSTRet() {
		return vICMSSTRet;
	}

	public BigDecimal getpST() {
		return pST;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setCsosn(String csosn) {
		this.csosn = csosn;
	}

	public void setpCredSN(BigDecimal pCredSN) {
		this.pCredSN = pCredSN;
	}

	public void setvCredICMSSN(BigDecimal vCredICMSSN) {
		this.vCredICMSSN = vCredICMSSN;
	}

	public void setvBCSTRet(BigDecimal vBCSTRet) {
		this.vBCSTRet = vBCSTRet;
	}

	public void setvICMSSTRet(BigDecimal vICMSSTRet) {
		this.vICMSSTRet = vICMSSTRet;
	}

	public void setpST(BigDecimal pST) {
		this.pST = pST;
	}

	public Integer getM() {
		return m;
	}

	public void setM(Integer m) {
		this.m = m;
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
		Configuracao other = (Configuracao) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Configuracao [codigo=" + codigo + "]";
	}

}
