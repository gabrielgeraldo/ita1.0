package br.com.ita.dominio.imposto;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoImpostoTributacaoICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMSST;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaIPI;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaPIS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaMotivoDesoneracaoICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;
import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;

import br.com.ita.dominio.BaseEntity;

@Entity
@Table(name = "Imposto")
public class Imposto implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_imposto", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_imposto", sequenceName = "codigo_imposto", allocationSize = 1)
	private Long codigo;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O regimeTributario é de preenchimento obrigatório.")
	private NFRegimeTributario regimeTributario;

	@Enumerated(EnumType.STRING)
	private NFOrigem origem;

	@Column()
	private NFNotaInfoImpostoTributacaoICMS situacaoTributaria;

	@Column()
	private NFNotaInfoItemModalidadeBCICMS modalidadeBCICMS;

	@Column(length = 80)
	private String valorBaseCalculo;

	@Column()
	private String percentualAliquota;

	@Column()
	private String valorTributo;

	@Column()
	private String percentualFundoCombatePobreza;

	@Column()
	private String valorFundoCombatePobreza;

	@Column()
	private String valorBaseCalculoFundoCombatePobreza;

	@Column()
	private NFNotaInfoItemModalidadeBCICMSST modalidadeBCICMSST;

	@Column()
	private String percentualMargemValorAdicionadoICMSST;

	@Column()
	private String percentualReducaoBCICMSST;

	@Column()
	private String valorBCICMSST;

	@Column()
	private String percentualAliquotaImpostoICMSST;

	@Column()
	private String valorICMSST;

	@Column()
	private String valorBCFundoCombatePobrezaST;

	@Column()
	private String percentualFundoCombatePobrezaST;

	@Column()
	private String valorFundoCombatePobrezaST;

	@Column()
	private String valorImpostoICMSST;

	@Column()
	private String valorICMSDesoneracao;

	@Column()
	private NFNotaMotivoDesoneracaoICMS desoneracao;

	@Column()
	private NFNotaMotivoDesoneracaoICMS motivoDesoneracaoICMS;

	@Column()
	private String percentualReducaoBC;

	@Column()
	private String valorBCICMS;

	@Column()
	private String percentualICMS;

	@Column()
	private String valorICMSOperacao;

	@Column()
	private String percentualDiferimento;

	@Column()
	private String valorICMSDiferimento;

	@Column()
	private String valorICMS;

	@Column()
	private String valorBCFundoCombatePobreza;

	@Column()
	private String valorBCICMSSTRetido;

	@Column()
	private String percentualAliquotaICMSSTConsumidorFinal;

	@Column()
	private String valorICMSSubstituto;

	@Column()
	private String valorICMSSTRetido;

	@Column()
	private String valorBCFundoCombatePobrezaRetidoST;

	@Column()
	private String percentualFundoCombatePobrezaRetidoST;

	@Column()
	private String valorFundoCombatePobrezaRetidoST;

	@Column()
	private String percentualReducaoBCEfetiva;

	@Column()
	private String valorBCEfetiva;

	@Column()
	private String percentualAliquotaICMSEfetiva;

	@Column()
	private String valorICMSEfetivo;

	@Column()
	private String valorBC;

	@Column()
	private String valorBCST;

	@Column()
	private String percentualAliquotaImposto;

	@Column()
	private String percentualBCOperacaoPropria;

	@Column()
	private String ufICMSST;

	@Column()
	private String valorBCICMSSTRetidoUFRemetente;

	@Column()
	private String aliqSuportadaConsFinal;

	@Column()
	private String valorICMSSTRetidoUFRemetente;

	@Column()
	private String valorBCICMSSTUFDestino;

	@Column()
	private String valorICMSSTUFDestino;

	@Column()
	private NFNotaSituacaoOperacionalSimplesNacional situacaoOperacaoSN;

	@Column()
	private String percentualAliquotaAplicavelCalculoCreditoSN;

	@Column()
	private String valorCreditoICMSSN;

	@Column()
	private String percentualICMSSTRetido;

	@Column()
	private String cnpjProdutor;

	@Column()
	private String codigoSelo;

	@Column()
	private BigInteger quantidadeSelo;

	@Column()
	private String codigoEnquadramento;

	@Column()
	private NFNotaInfoSituacaoTributariaIPI situacaoTributariaIPI;

	@Column()
	private String valorBaseCalculoIPI;

	@Column()
	private String percentualAliquotaIPI;

	@Column()
	private String quantidadeIPI;

	@Column()
	private String valorUnidadeTributavelIPI;

	@Column()
	private String valorTributoIPI;

	@Column()
	private NFNotaInfoSituacaoTributariaPIS situacaoTributariaPIS;

	@Column()
	private String valorBaseCalculoPIS;

	@Column()
	private String percentualAliquotaPIS;

	@Column()
	private String valorTributoPIS;

	@Column()
	private String quantidadeVendidaPIS;

	@Column()
	private String valorAliquotaPIS;

	@Column()
	private NFNotaInfoSituacaoTributariaCOFINS situacaoTributariaCOFINS;

	@Column()
	private String valorBaseCalculoCOFINS;

	@Column()
	private String percentualAliquotaCOFINS;

	@Column()
	private String valorCOFINS;

	@Column()
	private String quantidadeVendidaCOFINS;

	@Column()
	private String valorAliquotaCOFINS;

	@Column()
	private String valorTributoCOFINS;

	@Column()
	private String percentualCOFINS;

	@Enumerated(EnumType.STRING)
	private ICMS ICMS;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public NFRegimeTributario getRegimeTributario() {
		return regimeTributario;
	}

	public void setRegimeTributario(NFRegimeTributario regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	public NFOrigem getOrigem() {
		return origem;
	}

	public void setOrigem(NFOrigem origem) {
		this.origem = origem;
	}

	public NFNotaInfoImpostoTributacaoICMS getSituacaoTributaria() {
		return situacaoTributaria;
	}

	public void setSituacaoTributaria(NFNotaInfoImpostoTributacaoICMS situacaoTributaria) {
		this.situacaoTributaria = situacaoTributaria;
	}

	public NFNotaInfoItemModalidadeBCICMS getModalidadeBCICMS() {
		return modalidadeBCICMS;
	}

	public void setModalidadeBCICMS(NFNotaInfoItemModalidadeBCICMS modalidadeBCICMS) {
		this.modalidadeBCICMS = modalidadeBCICMS;
	}

	public String getValorBaseCalculo() {
		return valorBaseCalculo;
	}

	public void setValorBaseCalculo(String valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}

	public String getPercentualAliquota() {
		return percentualAliquota;
	}

	public void setPercentualAliquota(String percentualAliquota) {
		this.percentualAliquota = percentualAliquota;
	}

	public String getValorTributo() {
		return valorTributo;
	}

	public void setValorTributo(String valorTributo) {
		this.valorTributo = valorTributo;
	}

	public String getPercentualFundoCombatePobreza() {
		return percentualFundoCombatePobreza;
	}

	public void setPercentualFundoCombatePobreza(String percentualFundoCombatePobreza) {
		this.percentualFundoCombatePobreza = percentualFundoCombatePobreza;
	}

	public String getValorFundoCombatePobreza() {
		return valorFundoCombatePobreza;
	}

	public void setValorFundoCombatePobreza(String valorFundoCombatePobreza) {
		this.valorFundoCombatePobreza = valorFundoCombatePobreza;
	}

	public String getValorBaseCalculoFundoCombatePobreza() {
		return valorBaseCalculoFundoCombatePobreza;
	}

	public void setValorBaseCalculoFundoCombatePobreza(String valorBaseCalculoFundoCombatePobreza) {
		this.valorBaseCalculoFundoCombatePobreza = valorBaseCalculoFundoCombatePobreza;
	}

	public NFNotaInfoItemModalidadeBCICMSST getModalidadeBCICMSST() {
		return modalidadeBCICMSST;
	}

	public void setModalidadeBCICMSST(NFNotaInfoItemModalidadeBCICMSST modalidadeBCICMSST) {
		this.modalidadeBCICMSST = modalidadeBCICMSST;
	}

	public String getPercentualMargemValorAdicionadoICMSST() {
		return percentualMargemValorAdicionadoICMSST;
	}

	public void setPercentualMargemValorAdicionadoICMSST(String percentualMargemValorAdicionadoICMSST) {
		this.percentualMargemValorAdicionadoICMSST = percentualMargemValorAdicionadoICMSST;
	}

	public String getPercentualReducaoBCICMSST() {
		return percentualReducaoBCICMSST;
	}

	public void setPercentualReducaoBCICMSST(String percentualReducaoBCICMSST) {
		this.percentualReducaoBCICMSST = percentualReducaoBCICMSST;
	}

	public String getValorBCICMSST() {
		return valorBCICMSST;
	}

	public void setValorBCICMSST(String valorBCICMSST) {
		this.valorBCICMSST = valorBCICMSST;
	}

	public String getPercentualAliquotaImpostoICMSST() {
		return percentualAliquotaImpostoICMSST;
	}

	public void setPercentualAliquotaImpostoICMSST(String percentualAliquotaImpostoICMSST) {
		this.percentualAliquotaImpostoICMSST = percentualAliquotaImpostoICMSST;
	}

	public String getValorICMSST() {
		return valorICMSST;
	}

	public void setValorICMSST(String valorICMSST) {
		this.valorICMSST = valorICMSST;
	}

	public String getValorBCFundoCombatePobrezaST() {
		return valorBCFundoCombatePobrezaST;
	}

	public void setValorBCFundoCombatePobrezaST(String valorBCFundoCombatePobrezaST) {
		this.valorBCFundoCombatePobrezaST = valorBCFundoCombatePobrezaST;
	}

	public String getPercentualFundoCombatePobrezaST() {
		return percentualFundoCombatePobrezaST;
	}

	public void setPercentualFundoCombatePobrezaST(String percentualFundoCombatePobrezaST) {
		this.percentualFundoCombatePobrezaST = percentualFundoCombatePobrezaST;
	}

	public String getValorFundoCombatePobrezaST() {
		return valorFundoCombatePobrezaST;
	}

	public void setValorFundoCombatePobrezaST(String valorFundoCombatePobrezaST) {
		this.valorFundoCombatePobrezaST = valorFundoCombatePobrezaST;
	}

	public String getValorImpostoICMSST() {
		return valorImpostoICMSST;
	}

	public void setValorImpostoICMSST(String valorImpostoICMSST) {
		this.valorImpostoICMSST = valorImpostoICMSST;
	}

	public String getValorICMSDesoneracao() {
		return valorICMSDesoneracao;
	}

	public void setValorICMSDesoneracao(String valorICMSDesoneracao) {
		this.valorICMSDesoneracao = valorICMSDesoneracao;
	}

	public NFNotaMotivoDesoneracaoICMS getDesoneracao() {
		return desoneracao;
	}

	public void setDesoneracao(NFNotaMotivoDesoneracaoICMS desoneracao) {
		this.desoneracao = desoneracao;
	}

	public NFNotaMotivoDesoneracaoICMS getMotivoDesoneracaoICMS() {
		return motivoDesoneracaoICMS;
	}

	public void setMotivoDesoneracaoICMS(NFNotaMotivoDesoneracaoICMS motivoDesoneracaoICMS) {
		this.motivoDesoneracaoICMS = motivoDesoneracaoICMS;
	}

	public String getPercentualReducaoBC() {
		return percentualReducaoBC;
	}

	public void setPercentualReducaoBC(String percentualReducaoBC) {
		this.percentualReducaoBC = percentualReducaoBC;
	}

	public String getValorBCICMS() {
		return valorBCICMS;
	}

	public void setValorBCICMS(String valorBCICMS) {
		this.valorBCICMS = valorBCICMS;
	}

	public String getPercentualICMS() {
		return percentualICMS;
	}

	public void setPercentualICMS(String percentualICMS) {
		this.percentualICMS = percentualICMS;
	}

	public String getValorICMSOperacao() {
		return valorICMSOperacao;
	}

	public void setValorICMSOperacao(String valorICMSOperacao) {
		this.valorICMSOperacao = valorICMSOperacao;
	}

	public String getPercentualDiferimento() {
		return percentualDiferimento;
	}

	public void setPercentualDiferimento(String percentualDiferimento) {
		this.percentualDiferimento = percentualDiferimento;
	}

	public String getValorICMSDiferimento() {
		return valorICMSDiferimento;
	}

	public void setValorICMSDiferimento(String valorICMSDiferimento) {
		this.valorICMSDiferimento = valorICMSDiferimento;
	}

	public String getValorICMS() {
		return valorICMS;
	}

	public void setValorICMS(String valorICMS) {
		this.valorICMS = valorICMS;
	}

	public String getValorBCFundoCombatePobreza() {
		return valorBCFundoCombatePobreza;
	}

	public void setValorBCFundoCombatePobreza(String valorBCFundoCombatePobreza) {
		this.valorBCFundoCombatePobreza = valorBCFundoCombatePobreza;
	}

	public String getValorBCICMSSTRetido() {
		return valorBCICMSSTRetido;
	}

	public void setValorBCICMSSTRetido(String valorBCICMSSTRetido) {
		this.valorBCICMSSTRetido = valorBCICMSSTRetido;
	}

	public String getPercentualAliquotaICMSSTConsumidorFinal() {
		return percentualAliquotaICMSSTConsumidorFinal;
	}

	public void setPercentualAliquotaICMSSTConsumidorFinal(String percentualAliquotaICMSSTConsumidorFinal) {
		this.percentualAliquotaICMSSTConsumidorFinal = percentualAliquotaICMSSTConsumidorFinal;
	}

	public String getValorICMSSubstituto() {
		return valorICMSSubstituto;
	}

	public void setValorICMSSubstituto(String valorICMSSubstituto) {
		this.valorICMSSubstituto = valorICMSSubstituto;
	}

	public String getValorICMSSTRetido() {
		return valorICMSSTRetido;
	}

	public void setValorICMSSTRetido(String valorICMSSTRetido) {
		this.valorICMSSTRetido = valorICMSSTRetido;
	}

	public String getValorBCFundoCombatePobrezaRetidoST() {
		return valorBCFundoCombatePobrezaRetidoST;
	}

	public void setValorBCFundoCombatePobrezaRetidoST(String valorBCFundoCombatePobrezaRetidoST) {
		this.valorBCFundoCombatePobrezaRetidoST = valorBCFundoCombatePobrezaRetidoST;
	}

	public String getPercentualFundoCombatePobrezaRetidoST() {
		return percentualFundoCombatePobrezaRetidoST;
	}

	public void setPercentualFundoCombatePobrezaRetidoST(String percentualFundoCombatePobrezaRetidoST) {
		this.percentualFundoCombatePobrezaRetidoST = percentualFundoCombatePobrezaRetidoST;
	}

	public String getValorFundoCombatePobrezaRetidoST() {
		return valorFundoCombatePobrezaRetidoST;
	}

	public void setValorFundoCombatePobrezaRetidoST(String valorFundoCombatePobrezaRetidoST) {
		this.valorFundoCombatePobrezaRetidoST = valorFundoCombatePobrezaRetidoST;
	}

	public String getPercentualReducaoBCEfetiva() {
		return percentualReducaoBCEfetiva;
	}

	public void setPercentualReducaoBCEfetiva(String percentualReducaoBCEfetiva) {
		this.percentualReducaoBCEfetiva = percentualReducaoBCEfetiva;
	}

	public String getValorBCEfetiva() {
		return valorBCEfetiva;
	}

	public void setValorBCEfetiva(String valorBCEfetiva) {
		this.valorBCEfetiva = valorBCEfetiva;
	}

	public String getPercentualAliquotaICMSEfetiva() {
		return percentualAliquotaICMSEfetiva;
	}

	public void setPercentualAliquotaICMSEfetiva(String percentualAliquotaICMSEfetiva) {
		this.percentualAliquotaICMSEfetiva = percentualAliquotaICMSEfetiva;
	}

	public String getValorICMSEfetivo() {
		return valorICMSEfetivo;
	}

	public void setValorICMSEfetivo(String valorICMSEfetivo) {
		this.valorICMSEfetivo = valorICMSEfetivo;
	}

	public String getValorBC() {
		return valorBC;
	}

	public void setValorBC(String valorBC) {
		this.valorBC = valorBC;
	}

	public String getValorBCST() {
		return valorBCST;
	}

	public void setValorBCST(String valorBCST) {
		this.valorBCST = valorBCST;
	}

	public String getPercentualAliquotaImposto() {
		return percentualAliquotaImposto;
	}

	public void setPercentualAliquotaImposto(String percentualAliquotaImposto) {
		this.percentualAliquotaImposto = percentualAliquotaImposto;
	}

	public String getPercentualBCOperacaoPropria() {
		return percentualBCOperacaoPropria;
	}

	public void setPercentualBCOperacaoPropria(String percentualBCOperacaoPropria) {
		this.percentualBCOperacaoPropria = percentualBCOperacaoPropria;
	}

	public String getUfICMSST() {
		return ufICMSST;
	}

	public void setUfICMSST(String ufICMSST) {
		this.ufICMSST = ufICMSST;
	}

	public String getValorBCICMSSTRetidoUFRemetente() {
		return valorBCICMSSTRetidoUFRemetente;
	}

	public void setValorBCICMSSTRetidoUFRemetente(String valorBCICMSSTRetidoUFRemetente) {
		this.valorBCICMSSTRetidoUFRemetente = valorBCICMSSTRetidoUFRemetente;
	}

	public String getAliqSuportadaConsFinal() {
		return aliqSuportadaConsFinal;
	}

	public void setAliqSuportadaConsFinal(String aliqSuportadaConsFinal) {
		this.aliqSuportadaConsFinal = aliqSuportadaConsFinal;
	}

	public String getValorICMSSTRetidoUFRemetente() {
		return valorICMSSTRetidoUFRemetente;
	}

	public void setValorICMSSTRetidoUFRemetente(String valorICMSSTRetidoUFRemetente) {
		this.valorICMSSTRetidoUFRemetente = valorICMSSTRetidoUFRemetente;
	}

	public String getValorBCICMSSTUFDestino() {
		return valorBCICMSSTUFDestino;
	}

	public void setValorBCICMSSTUFDestino(String valorBCICMSSTUFDestino) {
		this.valorBCICMSSTUFDestino = valorBCICMSSTUFDestino;
	}

	public String getValorICMSSTUFDestino() {
		return valorICMSSTUFDestino;
	}

	public void setValorICMSSTUFDestino(String valorICMSSTUFDestino) {
		this.valorICMSSTUFDestino = valorICMSSTUFDestino;
	}

	public NFNotaSituacaoOperacionalSimplesNacional getSituacaoOperacaoSN() {
		return situacaoOperacaoSN;
	}

	public void setSituacaoOperacaoSN(NFNotaSituacaoOperacionalSimplesNacional situacaoOperacaoSN) {
		this.situacaoOperacaoSN = situacaoOperacaoSN;
	}

	public String getPercentualAliquotaAplicavelCalculoCreditoSN() {
		return percentualAliquotaAplicavelCalculoCreditoSN;
	}

	public void setPercentualAliquotaAplicavelCalculoCreditoSN(String percentualAliquotaAplicavelCalculoCreditoSN) {
		this.percentualAliquotaAplicavelCalculoCreditoSN = percentualAliquotaAplicavelCalculoCreditoSN;
	}

	public String getValorCreditoICMSSN() {
		return valorCreditoICMSSN;
	}

	public void setValorCreditoICMSSN(String valorCreditoICMSSN) {
		this.valorCreditoICMSSN = valorCreditoICMSSN;
	}

	public String getPercentualICMSSTRetido() {
		return percentualICMSSTRetido;
	}

	public void setPercentualICMSSTRetido(String percentualICMSSTRetido) {
		this.percentualICMSSTRetido = percentualICMSSTRetido;
	}

	public String getCnpjProdutor() {
		return cnpjProdutor;
	}

	public void setCnpjProdutor(String cnpjProdutor) {
		this.cnpjProdutor = cnpjProdutor;
	}

	public String getCodigoSelo() {
		return codigoSelo;
	}

	public void setCodigoSelo(String codigoSelo) {
		this.codigoSelo = codigoSelo;
	}

	public BigInteger getQuantidadeSelo() {
		return quantidadeSelo;
	}

	public void setQuantidadeSelo(BigInteger quantidadeSelo) {
		this.quantidadeSelo = quantidadeSelo;
	}

	public String getCodigoEnquadramento() {
		return codigoEnquadramento;
	}

	public void setCodigoEnquadramento(String codigoEnquadramento) {
		this.codigoEnquadramento = codigoEnquadramento;
	}

	public NFNotaInfoSituacaoTributariaIPI getSituacaoTributariaIPI() {
		return situacaoTributariaIPI;
	}

	public void setSituacaoTributariaIPI(NFNotaInfoSituacaoTributariaIPI situacaoTributariaIPI) {
		this.situacaoTributariaIPI = situacaoTributariaIPI;
	}

	public String getValorBaseCalculoIPI() {
		return valorBaseCalculoIPI;
	}

	public void setValorBaseCalculoIPI(String valorBaseCalculoIPI) {
		this.valorBaseCalculoIPI = valorBaseCalculoIPI;
	}

	public String getPercentualAliquotaIPI() {
		return percentualAliquotaIPI;
	}

	public void setPercentualAliquotaIPI(String percentualAliquotaIPI) {
		this.percentualAliquotaIPI = percentualAliquotaIPI;
	}

	public String getQuantidadeIPI() {
		return quantidadeIPI;
	}

	public void setQuantidadeIPI(String quantidadeIPI) {
		this.quantidadeIPI = quantidadeIPI;
	}

	public String getValorUnidadeTributavelIPI() {
		return valorUnidadeTributavelIPI;
	}

	public void setValorUnidadeTributavelIPI(String valorUnidadeTributavelIPI) {
		this.valorUnidadeTributavelIPI = valorUnidadeTributavelIPI;
	}

	public String getValorTributoIPI() {
		return valorTributoIPI;
	}

	public void setValorTributoIPI(String valorTributoIPI) {
		this.valorTributoIPI = valorTributoIPI;
	}

	public NFNotaInfoSituacaoTributariaPIS getSituacaoTributariaPIS() {
		return situacaoTributariaPIS;
	}

	public void setSituacaoTributariaPIS(NFNotaInfoSituacaoTributariaPIS situacaoTributariaPIS) {
		this.situacaoTributariaPIS = situacaoTributariaPIS;
	}

	public String getValorBaseCalculoPIS() {
		return valorBaseCalculoPIS;
	}

	public void setValorBaseCalculoPIS(String valorBaseCalculoPIS) {
		this.valorBaseCalculoPIS = valorBaseCalculoPIS;
	}

	public String getPercentualAliquotaPIS() {
		return percentualAliquotaPIS;
	}

	public void setPercentualAliquotaPIS(String percentualAliquotaPIS) {
		this.percentualAliquotaPIS = percentualAliquotaPIS;
	}

	public String getValorTributoPIS() {
		return valorTributoPIS;
	}

	public void setValorTributoPIS(String valorTributoPIS) {
		this.valorTributoPIS = valorTributoPIS;
	}

	public String getQuantidadeVendidaPIS() {
		return quantidadeVendidaPIS;
	}

	public void setQuantidadeVendidaPIS(String quantidadeVendidaPIS) {
		this.quantidadeVendidaPIS = quantidadeVendidaPIS;
	}

	public String getValorAliquotaPIS() {
		return valorAliquotaPIS;
	}

	public void setValorAliquotaPIS(String valorAliquotaPIS) {
		this.valorAliquotaPIS = valorAliquotaPIS;
	}

	public NFNotaInfoSituacaoTributariaCOFINS getSituacaoTributariaCOFINS() {
		return situacaoTributariaCOFINS;
	}

	public void setSituacaoTributariaCOFINS(NFNotaInfoSituacaoTributariaCOFINS situacaoTributariaCOFINS) {
		this.situacaoTributariaCOFINS = situacaoTributariaCOFINS;
	}

	public String getValorBaseCalculoCOFINS() {
		return valorBaseCalculoCOFINS;
	}

	public void setValorBaseCalculoCOFINS(String valorBaseCalculoCOFINS) {
		this.valorBaseCalculoCOFINS = valorBaseCalculoCOFINS;
	}

	public String getPercentualAliquotaCOFINS() {
		return percentualAliquotaCOFINS;
	}

	public void setPercentualAliquotaCOFINS(String percentualAliquotaCOFINS) {
		this.percentualAliquotaCOFINS = percentualAliquotaCOFINS;
	}

	public String getValorCOFINS() {
		return valorCOFINS;
	}

	public void setValorCOFINS(String valorCOFINS) {
		this.valorCOFINS = valorCOFINS;
	}

	public String getQuantidadeVendidaCOFINS() {
		return quantidadeVendidaCOFINS;
	}

	public void setQuantidadeVendidaCOFINS(String quantidadeVendidaCOFINS) {
		this.quantidadeVendidaCOFINS = quantidadeVendidaCOFINS;
	}

	public String getValorAliquotaCOFINS() {
		return valorAliquotaCOFINS;
	}

	public void setValorAliquotaCOFINS(String valorAliquotaCOFINS) {
		this.valorAliquotaCOFINS = valorAliquotaCOFINS;
	}

	public String getValorTributoCOFINS() {
		return valorTributoCOFINS;
	}

	public void setValorTributoCOFINS(String valorTributoCOFINS) {
		this.valorTributoCOFINS = valorTributoCOFINS;
	}

	public String getPercentualCOFINS() {
		return percentualCOFINS;
	}

	public void setPercentualCOFINS(String percentualCOFINS) {
		this.percentualCOFINS = percentualCOFINS;
	}

	public ICMS getICMS() {
		return ICMS;
	}

	public void setICMS(ICMS iCMS) {
		ICMS = iCMS;
	}

}
