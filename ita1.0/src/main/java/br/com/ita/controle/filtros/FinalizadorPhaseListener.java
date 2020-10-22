package br.com.ita.controle.filtros;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.ita.controle.util.JPAUtil;

@SuppressWarnings("serial")
public class FinalizadorPhaseListener implements PhaseListener {

	// CICLO DE VIDA DO JSF
	// 1. Restore view
	// 2. Apply request values; process events
	// 3. Process validations; process events
	// 4. Update model values; process events
	// 5. Invoke application; process events
	// 6. Render response

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE; // executar na última fase (final do
										// processamento)
	}

	@Override
	public void afterPhase(PhaseEvent event) {
		// liberar o objeto armazenado no Cache da JPAUtil
		JPAUtil.limparCacheEntityManager();

		//System.out.println("INICIANDO FASE: " + event.getPhaseId());

	}

	@Override
	public void beforePhase(PhaseEvent event) {

		//System.out.println("FINALIZANDO FASE: " + event.getPhaseId());

	}

}
