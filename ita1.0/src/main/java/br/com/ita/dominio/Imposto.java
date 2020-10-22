package br.com.ita.dominio;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoItemModalidadeBCICMSST;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;

/*
import com.fincatto.nfe310.classes.NFNotaInfoItemModalidadeBCICMS;
import com.fincatto.nfe310.classes.NFNotaInfoItemModalidadeBCICMSST;
import com.fincatto.nfe310.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.nfe310.classes.NFOrigem;
*/

@Entity
@Table(name = "imposto")
public class Imposto implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_imposto", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_imposto", sequenceName = "codigo_imposto", allocationSize = 1)
	private Long codigo;

	@Column(length = 30, nullable = false)
	@NotNull(message = "Descri��o n�o definida.")
	@Size(min = 2, max = 30, message = "A descri��o deve ter entre 2 e 30 caracteres.")
	private String descricao;

	@Column(length = 2)
	@NotNull(message = "Situa��o Trib. do ICMS n�o definida.")
	private NFNotaSituacaoOperacionalSimplesNacional situacaoTribdICMS;

	@Column(length = 2)
	@NotNull(message = "Origem n�o definida.")
	private NFOrigem origemdoProduto;

	@Column(length = 2)
	@NotNull(message = "Modalidade BC do ICMS n�o definida.")
	private NFNotaInfoItemModalidadeBCICMS modalidadeBCdoICMS;

	@NotNull(message = "Perc. da BC do ICMS n�o definida.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal percdaBCdoICMS;

	@NotNull(message = "Perc. do ICMS n�o definida.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal percdoICMS;

	@Column(length = 2)
	@NotNull(message = "Modalidade BC da ST n�o definida.")
	private NFNotaInfoItemModalidadeBCICMSST modalidadeBCdaST;

	@NotNull(message = "Perc. Acres. BC da ST n�o definida.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal percAcresBCdaST;

	@NotNull(message = "Perc. Red. BC da ST n�o definida.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal percRedBCdaST;

	@NotNull(message = "Perc. BC da ST n�o definida.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal percBCdaST;

	@NotNull(message = "Perc. do ICMS da ST n�o definida.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal percdoICMSdaST;

	// @NotNull(message = "Aliq.ICMS a descontar, n�o destacado pelo SN n�o
	// definida.")
	@Column(precision = 7, scale = 2)
	private BigDecimal aliqICMSAdescontarNaoDestacadopeloSN;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public NFNotaSituacaoOperacionalSimplesNacional getSituacaoTribdICMS() {
		return situacaoTribdICMS;
	}

	public void setSituacaoTribdICMS(NFNotaSituacaoOperacionalSimplesNacional situacaoTribdICMS) {
		this.situacaoTribdICMS = situacaoTribdICMS;
	}

	public NFOrigem getOrigemdoProduto() {
		return origemdoProduto;
	}

	public void setOrigemdoProduto(NFOrigem origemdoProduto) {
		this.origemdoProduto = origemdoProduto;
	}

	public NFNotaInfoItemModalidadeBCICMS getModalidadeBCdoICMS() {
		return modalidadeBCdoICMS;
	}

	public void setModalidadeBCdoICMS(NFNotaInfoItemModalidadeBCICMS modalidadeBCdoICMS) {
		this.modalidadeBCdoICMS = modalidadeBCdoICMS;
	}

	public BigDecimal getPercdaBCdoICMS() {
		return percdaBCdoICMS;
	}

	public void setPercdaBCdoICMS(BigDecimal percdaBCdoICMS) {
		this.percdaBCdoICMS = percdaBCdoICMS;
	}

	public BigDecimal getPercdoICMS() {
		return percdoICMS;
	}

	public void setPercdoICMS(BigDecimal percdoICMS) {
		this.percdoICMS = percdoICMS;
	}

	public NFNotaInfoItemModalidadeBCICMSST getModalidadeBCdaST() {
		return modalidadeBCdaST;
	}

	public void setModalidadeBCdaST(NFNotaInfoItemModalidadeBCICMSST modalidadeBCdaST) {
		this.modalidadeBCdaST = modalidadeBCdaST;
	}

	public BigDecimal getPercAcresBCdaST() {
		return percAcresBCdaST;
	}

	public void setPercAcresBCdaST(BigDecimal percAcresBCdaST) {
		this.percAcresBCdaST = percAcresBCdaST;
	}

	public BigDecimal getPercRedBCdaST() {
		return percRedBCdaST;
	}

	public void setPercRedBCdaST(BigDecimal percRedBCdaST) {
		this.percRedBCdaST = percRedBCdaST;
	}

	public BigDecimal getPercBCdaST() {
		return percBCdaST;
	}

	public void setPercBCdaST(BigDecimal percBCdaST) {
		this.percBCdaST = percBCdaST;
	}

	public BigDecimal getPercdoICMSdaST() {
		return percdoICMSdaST;
	}

	public void setPercdoICMSdaST(BigDecimal percdoICMSdaST) {
		this.percdoICMSdaST = percdoICMSdaST;
	}

	public BigDecimal getAliqICMSAdescontarNaoDestacadopeloSN() {
		return aliqICMSAdescontarNaoDestacadopeloSN;
	}

	public void setAliqICMSAdescontarNaoDestacadopeloSN(BigDecimal aliqICMSAdescontarNaoDestacadopeloSN) {
		this.aliqICMSAdescontarNaoDestacadopeloSN = aliqICMSAdescontarNaoDestacadopeloSN;
	}

}
