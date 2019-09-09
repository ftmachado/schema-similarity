package br.ufsm.ppgcc.algoritmos;

import br.ufsm.ppgcc.model.dao.EstruturaConsolidadaDAO;
import br.ufsm.ppgcc.model.estruturas.NodoEstruturaConsolidada;

/**
 * @author ezequielrr
 */
public class AdaptarNotacaoAgregados {

    public AdaptarNotacaoAgregados() {
    }
    
    public void adaptarNotacaoAgregados(String arqEstruturaUnificada) throws Exception {
        // Carrega os artefatos de entrada
        EstruturaConsolidadaDAO ecd = new EstruturaConsolidadaDAO();
        NodoEstruturaConsolidada n = ecd.lerEstruturaConsolidada(arqEstruturaUnificada);

        // Grava esquema conceitual
        ecd.gravarEsquemaConceitual(n);
    }
    
}
