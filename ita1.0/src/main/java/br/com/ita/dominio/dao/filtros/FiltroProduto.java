package br.com.ita.dominio.dao.filtros;

import java.io.Serializable;

import br.com.ita.dominio.TipoPesquisaProduto;

public class FiltroProduto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigo;

	private String descricao;

	private String codigoBarras;

	private TipoPesquisaProduto tipoPesquisaProduto;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoPesquisaProduto getTipoPesquisaProduto() {
		return tipoPesquisaProduto;
	}

	public void setTipoPesquisaProduto(TipoPesquisaProduto tipoPesquisaProduto) {
		this.tipoPesquisaProduto = tipoPesquisaProduto;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
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
		FiltroProduto other = (FiltroProduto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FiltroProduto [descricao=" + descricao + "]";
	}

}
