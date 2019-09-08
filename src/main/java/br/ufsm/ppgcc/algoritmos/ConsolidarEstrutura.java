package br.ufsm.ppgcc.algoritmos;

import br.ufsm.ppgcc.model.estruturas.MatrizResultados;
import br.ufsm.ppgcc.model.dao.ListasDAO;
import br.ufsm.ppgcc.model.dao.MatrizResultadosDAO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ezequielrr
 */
public class ConsolidarEstrutura {

    /**
     * Método auxiliar - Algoritmo 7 de MACHADO(2017)
     * 
     * @throws IOException
     */
    public void consolidaEstruturaBloco(String arquivoCampos, String arquivoMatriz, String outListaRef2,
            String outConsolidados) throws IOException {
        
        //LOG
        System.out.printf("\n\tConsolidando estrutura...");

        // Carrega os artefatos de entrada
        MatrizResultadosDAO carrega = new MatrizResultadosDAO();
        MatrizResultados matrizUnicaResultados = carrega.lerMatrizUnicaResultados(arquivoCampos, arquivoMatriz);

        List<String[]> listaReferencias2 = new ArrayList<>();
        List<String> palavrasConsolidadas = new ArrayList<>();
        List<String> aRemover = new ArrayList<>();
        List<List<Integer>> matriz = matrizUnicaResultados.getMatriz();
        
        // Caminhamento em diagonal (acima da diagonal principal)

        // Consolida todas as pralavras da coluna
        for (String palavra : matrizUnicaResultados.getCamposColuna()) {
            palavrasConsolidadas.add(palavra);
        }

        for (int j = 0; j < matriz.size(); j++) {
            if (j == 0) {
                continue;
            }
            // Movimentação das linhas
            for (int i = j - 1; i >= 0; i--) {
                if (matriz.get(i).get(j) == 1) {
                    String[] equivalencia = new String[2];
                    equivalencia[0] = palavrasConsolidadas.get(i);
                    equivalencia[1] = palavrasConsolidadas.get(j);
                    
                    // Adiciona as equivalências a lista de equivalencias
                    listaReferencias2.add(equivalencia);

                    /* Adiciona a palavra da coluna a uma lista de palavras a
                     * serem excluídas da lista de palavras consolidadas
                     * posteriormente */
                    aRemover.add(palavrasConsolidadas.get(j));
                }
            }
        }

        //Remove da lista de elementos consolidados os elementos presentes 
        // na lista temporária aRemover
        for (String removerPalavra : aRemover) {
            palavrasConsolidadas.remove(removerPalavra);
        }

        //Grava em arquivos os artefatos:
        //lista de referências 2 e campos consolidados
        ListasDAO l = new ListasDAO();
        l.gravarListaCamposConsolidados(palavrasConsolidadas, outConsolidados);
        l.gravarListaReferencias2(listaReferencias2, outListaRef2);

    }

}
