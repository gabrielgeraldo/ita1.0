package br.com.ita.dominio;

public enum TipoPesquisaProduto {

	CODIGO("Código"), CODIGOBARRAS("Código de barras"), DESCRICAO("Descrição");

	private String descricao;

	TipoPesquisaProduto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
