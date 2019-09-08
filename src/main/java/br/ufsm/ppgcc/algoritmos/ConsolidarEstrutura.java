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

    public void consolidarEstruturaBlocos() throws FileNotFoundException,IOException {
        // Carrega os artefatos de entrada
        MatrizResultadosDAO carrega = new MatrizResultadosDAO();
        List<MatrizResultados> matrizBlocos
                = carrega.lerMatrizUnicaResultadosBlocos();
        List<List<String>> palavrasConsolidadas = new ArrayList<>();
        List<List<String[]>> listaReferencias2 = new ArrayList<>();

        // Consolidar blocos da estrutura
        for (MatrizResultados matrizBloco : matrizBlocos) {
            List<String> palavrasConsolidadasBloco = new ArrayList<>();
            List<String[]> listaReferencias2Bloco = new ArrayList<>();

            // Executa o algoritmo de consolidação de estrutura
            consolidaEstruturaBloco(matrizBloco, listaReferencias2Bloco,
                    palavrasConsolidadasBloco);

            palavrasConsolidadas.add(palavrasConsolidadasBloco);
            listaReferencias2.add(listaReferencias2Bloco);
        }

        //Une os resultados da lista de referencias 2 em uma única lista
        List<String[]> listaReferencias2Final = new ArrayList<>();
        for (List<String[]> lista : listaReferencias2) {
            for (String[] elemento : lista) {
                listaReferencias2Final.add(elemento);
            }
        }

        //Grava em arquivos os artefatos:
        //lista de referências 2 e campos consolidados
        ListasDAO l = new ListasDAO();
        l.gravarListaCamposConsolidados(palavrasConsolidadas);
        l.gravarListaReferencias2(listaReferencias2Final);
    }

    /**
     * Método auxiliar - Algoritmo 7 de MACHADO(2017)
     */
    private void consolidaEstruturaBloco(MatrizResultados matrizUnicaResultados,
            List<String[]> listaReferencias2,
            List<String> listaPalavrasConsolidadas) {
        List<String> aRemover = new ArrayList<>();
        // Caminhamento em diagonal (acima da diagonal principal)
        // Movimentação das colunas

        List<List<Integer>> matrizBloco = matrizUnicaResultados.getMatriz();
        // Consolida todas as pralavras da coluna
        List<String> palavrasConsolidadas = new ArrayList<>(0);
        for (String palavra : matrizUnicaResultados.getCamposColuna()) {
            palavrasConsolidadas.add(palavra);
        }

        for (int j = 0; j < matrizBloco.size(); j++) {
            if (j == 0) {
                continue;
            }
            // Movimentação das linhas
            for (int i = j - 1; i >= 0; i--) {
                if (matrizBloco.get(i).get(j) == 1) {
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
        //Adiciona as palavras consolidadas à lista de palavras consolidadas
        for (String palavraConsolidada : palavrasConsolidadas) {
            listaPalavrasConsolidadas.add(palavraConsolidada);
        }
    }
}
