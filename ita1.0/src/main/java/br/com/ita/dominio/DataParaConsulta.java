package br.com.ita.dominio;

public enum DataParaConsulta {

	EMISSAO("Emissão"), VENCIMENTO("Vencimento");

	private String descricao;

	DataParaConsulta(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
