package br.com.ita.dominio;

public enum MeioPagamento {

	DINHEIRO("Dinheiro"),
    CHEQUE("Cheque"),
    CARTAO_CREDITO("Cart\u00e3o de cr\u00e9dito"),
    CARTAO_DEBITO("Cart\u00e3o de d\u00e9bito"),
    CARTAO_LOJA("Cart\u00e3o da loja"),
    VALE_ALIMENTACAO("Vale alimenta\u00e7\u00e3o"),
    VALE_REFEICAO("Vale refei\u00e7\u00e3o"),
    VALE_PRESENTE("Vale presente"),
    VALE_COMBUSTIVEL("Vale combust\u00edvel"),
    DUPLICATA_MERCANTIL("Duplicata mercantil"),
    BOLETO_BANCARIO("Boleto Bancario"),
    SEM_PAGAMENTO("Sem pagamento"),
    OUTRO("Outro");
	
	private String descricao;

	MeioPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
