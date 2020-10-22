package br.com.ita.dominio;

public enum DataParaConsulta {

	EMISSAO("Emiss�o"), VENCIMENTO("Vencimento");

	private String descricao;

	DataParaConsulta(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
