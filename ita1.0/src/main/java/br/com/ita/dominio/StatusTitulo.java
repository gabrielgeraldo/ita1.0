package br.com.ita.dominio;

public enum StatusTitulo {

	TODOS("Todos"), BAIXADO("Baixado"), EMABERTO("Em aberto"), BAIXADOPARCIAL("Baixado parcial");

	private String descricao;

	StatusTitulo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
