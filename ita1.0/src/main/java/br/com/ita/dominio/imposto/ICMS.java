package br.com.ita.dominio.imposto;

public enum ICMS {
	
	ICMS00("ICMS00"),
	ICMS10("ICMS10"), 
	ICMS20("ICMS20"), 
	ICMS30("ICMS30"), 
	ICMS40("ICMS40"), 
	ICMS51("ICMS51"), 
	ICMS60("ICMS60"), 
	ICMS70("ICMS70"), 
	ICMS90("ICMS90"), 
	ICMSPart("ICMSPart"), 
	ICMSST("ICMSST"), 
	ICMSSN101("ICMSSN101"), 
	ICMSSN102("ICMSSN102"), 
	ICMSSN201("ICMSSN201"), 
	ICMSSN202("ICMSSN202"), 
	ICMSSN500("ICMSSN500"), 
	ICMSSN900("ICMSSN900");

	private String descricao;

	ICMS(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
