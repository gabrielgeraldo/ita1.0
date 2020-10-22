package br.com.ita.controle.nfe.util;

import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN101;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN102;

/*
import com.fincatto.nfe310.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.nfe310.classes.NFOrigem;
import com.fincatto.nfe310.classes.nota.NFNotaInfoItemImpostoICMSSN101;
import com.fincatto.nfe310.classes.nota.NFNotaInfoItemImpostoICMSSN102;
*/

public class Icms {

	public NFNotaInfoItemImpostoICMSSN101 getNFNotaInfoItemImpostoICMSSN101(NFOrigem origem) {
        
		final NFNotaInfoItemImpostoICMSSN101 icmsSn101 = new NFNotaInfoItemImpostoICMSSN101();
		icmsSn101.setOrigem(origem);
		icmsSn101.setSituacaoOperacaoSN(NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_COM_PERMISSAO_CREDITO);
	
		
        return icmsSn101;
    }

	public NFNotaInfoItemImpostoICMSSN102 getNFNotaInfoItemImpostoICMSSN102(NFOrigem origem) {
        
		final NFNotaInfoItemImpostoICMSSN102 icmsSn102 = new NFNotaInfoItemImpostoICMSSN102();
     
		
		
        return icmsSn102;
    }

}
