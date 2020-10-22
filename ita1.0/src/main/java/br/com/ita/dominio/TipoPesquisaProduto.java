package br.com.ita.dominio;

public enum TipoPesquisaProduto {

	CODIGO("Código"), DESCRICAO("Descrição");

	private String descricao;

	TipoPesquisaProduto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
