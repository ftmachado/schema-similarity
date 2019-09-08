/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsm.ppgcc.model.dao;

import br.ufsm.ppgcc.util.ParserEstruturaConsolidada;
import br.ufsm.ppgcc.model.estruturas.ElementoBloco;
import br.ufsm.ppgcc.model.estruturas.NodoEstruturaConsolidada;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author ezequielrr
 */
public class EstruturaConsolidadaDAO {
    
    public NodoEstruturaConsolidada lerEstruturaConsolidada() 
            throws FileNotFoundException, IOException, Exception {
        String nomeArquivo = "src/main/artefatos-saida/estrutura-consolidada.txt";
        ParserEstruturaConsolidada notacoes = new ParserEstruturaConsolidada();
        FileReader arq = new FileReader(nomeArquivo);
        BufferedReader lerArq = new BufferedReader(arq);
        int c;

        while ((c = lerArq.read()) != -1) {
            notacoes.lerCaractere((char)c);
        }
        lerArq.close();
        arq.close();
        return notacoes.getNodoEstruturaConsolidada();
    }

    /**
     * Grava a estrutura consolidada a partir de uma estrutura remontada
     * @param elementos
     * @throws java.io.IOException
     */
    public void gravarEstruturaConsolidada(List<ElementoBloco> elementos) throws IOException {
        FileWriter arq = new FileWriter("src/main/artefatos-saida/estrutura-consolidada.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.println(getStringEstruturaConsolidada(elementos));
        gravarArq.close();
        arq.close();
    }
    
    public void gravarEstruturaConsolidada(ElementoBloco elemento) throws IOException {
        FileWriter arq = new FileWriter("src/main/artefatos-saida/estrutura-consolidada.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.println(getStringEstruturaConsolidada(elemento));
        gravarArq.close();
        arq.close();
    }

    private String getStringEstruturaConsolidada(ElementoBloco elemento) {
        String str = "";
        String escNome = "";
            if (elemento.getNomeConsolidado() != null && !elemento.getNomeConsolidado().equals("")) {
                escNome = elemento.getNomeConsolidado();
            } else {
                escNome = elemento.getNome();
            }
        str += "" + escNome + ";" + elemento.getAbreDelimitador()
                + getStringEstruturaConsolidada(elemento.getBlocoFilho())
                + elemento.getFechaDelimitador() + "";
        return str;
    }
    
    /**
     * Grava o esquema conceitual a partir da estrutura consolidada
     */
    public void gravarEsquemaConceitual(NodoEstruturaConsolidada estrutura) 
            throws IOException {
        FileWriter arq = new FileWriter("src/main/artefatos-saida/esquema-conceitual.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.println(estrutura.toString());
        gravarArq.close();
        arq.close();        
    }
    
    /**
     * Método auxiliar usado para tornar a lista remontada em uma string
     * de uma estrutura consolidada
     */
    private String getStringEstruturaConsolidada(List<ElementoBloco> elementos) {
        String str = "";
        String escNome = "";
        for (ElementoBloco e : elementos) {
            if (e.getNomeConsolidado() != null && !e.getNomeConsolidado().equals("")) {
                escNome = e.getNomeConsolidado();
            } else {
                escNome = e.getNome();
            }
            if (e.getTipo() != ElementoBloco.OBJETO && e.getTipo()
                    != ElementoBloco.ARR_OBJETO) {
                str += escNome + ";" + e.getAbreDelimitador()
                        + e.getFechaDelimitador();
            } else {
                str += escNome + ";" + e.getAbreDelimitador()
                        + getStringEstruturaConsolidada(e.getBlocoFilho())
                        + e.getFechaDelimitador();
            }
        }
        return str;
    }
}
