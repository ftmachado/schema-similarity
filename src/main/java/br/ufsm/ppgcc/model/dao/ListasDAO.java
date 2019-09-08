/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsm.ppgcc.model.dao;

import br.ufsm.ppgcc.model.estruturas.ElementoBloco;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe DAO para manipulação das Listas de referências 1 e 2
 * Também dos mapeamentos (gravar) e listaEspecialista (ler)
 * @author ezequielrr
 */
public class ListasDAO {
    
    
    /**
     * Método para carregar a primeira lista de referências
     * carrega a partir do arquivo(fixo) lista-referencias1.csv
     * @return 
     * @throws java.io.FileNotFoundException 
     */
    public List<List<ElementoBloco>> lerListaCamposConsolidados()
            throws FileNotFoundException {
        String arquivo = "src/main/artefatos-saida/campos-consolidados.csv";
        List<List<ElementoBloco>> listaCamposConsolidados = new ArrayList<>();
        List<ElementoBloco> l;
        Scanner scanner = new Scanner(new FileReader(arquivo))
                .useDelimiter("\\n");

        while (scanner.hasNext()) {
            String temp = scanner.next();
            String[] expl = temp.split(";");
            l = new ArrayList<>();
            for (String s : expl) {
                ElementoBloco e = new ElementoBloco();
                e.setNome(s.trim());
                l.add(e);
            }
            listaCamposConsolidados.add(l);
        }
        return listaCamposConsolidados;
    }
    
    public void gravarListaCamposConsolidados(List<String> listaCamposConsolidados, String caminho) throws IOException {
        FileWriter arq = new FileWriter(caminho);
        PrintWriter gravarArq = new PrintWriter(arq);

        for (int i = 0; i < listaCamposConsolidados.size(); i++) {
            gravarArq.printf(listaCamposConsolidados.get(i)+";");
        }
        
        gravarArq.close();
        arq.close();
    }
    
    
    /**
     * Método para carregar a primeira lista de referências
     * carrega a partir do arquivo(fixo) lista-referencias1.csv
     */
    public List<List<String>> lerListaReferencias1()
            throws FileNotFoundException {
        String arquivo = "src/main/artefatos-entrada/lista-referencias-1.csv";
        List<List<String>> listaReferencias = new ArrayList<>();
        List<String> l;
        Scanner scanner = new Scanner(new FileReader(arquivo))
                .useDelimiter("\\n");

        while (scanner.hasNext()) {
            String temp = scanner.next();
            String[] expl = temp.split(";");
            l = new ArrayList<>();
            for (String s : expl) {
                l.add(s.trim());
            }
            listaReferencias.add(l);
        }
        return listaReferencias;
    }
    
    /**
     * Método para carregar a segunda lista de referências
     * carrega a partir do arquivo(fixo) lista-referencias2.csv
     */
    public List<String[]> lerListaReferencias2()
            throws FileNotFoundException {
        String arquivo = "src/main/artefatos-saida/lista-referencias-2.csv";
        List<String[]> listaReferencias = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(arquivo))
                .useDelimiter("\\;|\\n");

        while (scanner.hasNext()) {
            String nomeCampo = scanner.next();
            String nomeEquiv = scanner.next();
            String[] equivalencias = new String[2];
            equivalencias[0] = nomeCampo.trim();
            equivalencias[1] = nomeEquiv.trim();
            listaReferencias.add(equivalencias);
        }
        return listaReferencias;
    }
    
    /**
     * grava a lista de referências 2 em um arquivo de texto 
     * (lista-referencias-2.csv)
     */
    public void gravarListaReferencias2(List<String[]> listaReferencias2, String caminho) throws IOException {
        FileWriter arq = new FileWriter(caminho);
        PrintWriter gravarArq = new PrintWriter(arq);
        
        for(String[] referencia : listaReferencias2) {
            gravarArq.println(referencia[0]+";"+referencia[1]);
        }
        
        gravarArq.close();
        arq.close();
    }
    
    /**
     * grava a lista de referências 2 em um arquivo de texto 
     * (lista-referencias-2.csv)
     */
    public void gravarMapeamentos(List<String[]> mapeamentos) throws IOException {
        FileWriter arq = new FileWriter("src/main/artefatos-saida/mapeamentos.csv");
        PrintWriter gravarArq = new PrintWriter(arq);
        for(String[] mapeamento : mapeamentos) {
            gravarArq.println(mapeamento[0]+";"+mapeamento[1]+";"+mapeamento[2]);
        }
        gravarArq.close();
        arq.close();
    }
    
    
    /**
     * Método para carregar a estrutura com instruções do especialista
     * carrega a partir do arquivo(fixo) lista-especialista.csv
     */
    public List<List<String>> lerListaEspecialista() 
            throws FileNotFoundException {
        String arquivo = "src/main/artefatos-entrada/lista-especialista.csv";
        List<List<String>> listaEspecialista = new ArrayList<>();
        List<String> l;
        Scanner scanner = new Scanner(new FileReader(arquivo))
                .useDelimiter("\\n");

        while (scanner.hasNext()) {
            String temp = scanner.next();
            String[] expl = temp.split(";");
            l = new ArrayList<>();
            for (String s : expl) {
                l.add(s.trim());
            }
            listaEspecialista.add(l);
        }
        return listaEspecialista;
    }
    
}
