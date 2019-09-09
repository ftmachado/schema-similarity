package br.ufsm.ppgcc.algoritmos;

import br.ufsm.ppgcc.model.estruturas.NodoEstruturaConsolidada;
import br.ufsm.ppgcc.model.dao.ListasDAO;
import br.ufsm.ppgcc.model.dao.EstruturaConsolidadaDAO;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmo 9 - Montar Mapeamentos
 * @param arqLista1 - arquivo que contem lista de referências 1
 * @param arqLista2 - arquivo que contem lista de referências 2
 * @param arqEstruturaUnificada - arquivo que contem a estrutura unificada
 * @param arqMapeamentos - arquivo para gravar os mapeamentos
 * @return Mapeamentos
 * @author Ezequiel Ribeiro, Fhabiana Machado
 * @since 09 de setembro de 2019
 */
public class MontarMapeamentos {

    public static void montar(String arqLista1, String arqLista2,
        String arqEstruturaUnificada, String arqMapeamentos) throws FileNotFoundException, Exception {

        //LOG
        System.out.printf("\n\tMontando mapeamentos...");
        
        ListasDAO l = new ListasDAO();
        EstruturaConsolidadaDAO e = new EstruturaConsolidadaDAO();
        
        /*
        * Carrega os artefatos
        */
        NodoEstruturaConsolidada estrCons = e.lerEstruturaConsolidada(arqEstruturaUnificada);
        List<String> listaCamposConsolidados = estrCons.getListaNomes();
        List<String> listaJsonPathCamposConsolidados = estrCons.getListaJsonPath();

        List<List<String>> listaRef1 = l.lerListaReferencias1(arqLista1);
        List<String[]> listaRef2 = l.lerListaReferencias2(arqLista2);

        /*
        * Monta os mapeamentos
        */
        int i = 0;
        List<String> fixados = new ArrayList<>(0);
        List<String[]> mapeamentos = new ArrayList<>(0);

        for (String elem : listaCamposConsolidados) {

            i++;
            List<String> termosEquivalentes = new ArrayList<>();
            obterTermosEquivalentes(elem, listaRef2, termosEquivalentes, fixados);

            for (String termo : termosEquivalentes) {

                List<String> docOrig = getDocOrigem(termo, listaRef1);
                
                for (String documentoOrig : docOrig) {
                    i = listaCamposConsolidados.indexOf(elem);
                    String[] dados = {listaJsonPathCamposConsolidados.get(i),termo, documentoOrig};
                    mapeamentos.add(dados);
                }

            }

        }

        //Grava a tabela de mapeamentos
        l.gravarMapeamentos(mapeamentos, arqMapeamentos);

    }

    private static void obterTermosEquivalentes(String termo, List<String[]> listaRef2,
            List<String> equivalencias, List<String> fixados) {

        if (fixados.contains(termo)) {
            return;
        }

        for (String[] termoAtual : listaRef2) {

            if (termoAtual[0].equals(termo)) {
                if (!fixados.contains(termoAtual[1]) && !equivalencias.contains(termoAtual[1])) {
                    equivalencias.add(termoAtual[1]);
                    fixados.add(termo);
                    obterTermosEquivalentes(termoAtual[1], listaRef2, equivalencias, fixados);
                }
            } else if (termoAtual[1].equals(termo)) {
                if (!fixados.contains(termoAtual[0]) && !equivalencias.contains(termoAtual[0])) {
                    equivalencias.add(termoAtual[0]);
                    fixados.add(termo);
                    obterTermosEquivalentes(termoAtual[0], listaRef2, equivalencias, fixados);
                }
            }
            
        }

    }

    private static List<String> getDocOrigem(String termo,List<List<String>> listaRef1) {
        
        List<String> res = new ArrayList<>();
        for (List<String> elem : listaRef1) {
            if (elem.size() > 0) {
                if (elem.get(0).equalsIgnoreCase(termo)) {
                    for (int i = 1; i < elem.size(); i++) {
                        res.add(elem.get(i));
                    }
                }
            }
        }
        return res;

    }

}
