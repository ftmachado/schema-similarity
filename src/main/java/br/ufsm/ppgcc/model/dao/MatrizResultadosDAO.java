/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsm.ppgcc.model.dao;

import br.ufsm.ppgcc.model.estruturas.MatrizResultados;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ezequielrr
 */
public class MatrizResultadosDAO {

    public MatrizResultados lerMatrizUnicaResultados() throws FileNotFoundException {
        return lerMatrizUnicaResultados("artefatos-entrada/matriz-unica-resultados-campos.csv",
                "artefatos-entrada/matriz-unica-resultados.csv");
    }

    public List<MatrizResultados> lerMatrizUnicaResultadosBlocos() throws FileNotFoundException {
        List<MatrizResultados> blocos = new ArrayList<>(0);
        int i = 0;
        while (true) {
            File f = new File("artefatos-entrada/matriz-unica-resultados-campos-" + i + ".csv");
            if (!f.exists()) {
                break;
            }
            MatrizResultados t = lerMatrizUnicaResultados("artefatos-entrada/"
                    + "matriz-unica-resultados-campos-" + i + ".csv",
                    "artefatos-entrada/matriz-unica-resultados-" + i + ".csv");
            blocos.add(t);
            i++;
        }
        return blocos;
    }

    private MatrizResultados lerMatrizUnicaResultados(String arquivoCampos,String arquivoMatriz) throws FileNotFoundException {
    
        List<List<Integer>> matriz = new ArrayList<>(0);
        List<String> listaColuna = lerCamposMatrizUnicaResultados(arquivoCampos);
        List<String> listaLinha = lerCamposMatrizUnicaResultados(arquivoCampos);

        Scanner scanner = new Scanner(new FileReader(arquivoMatriz))
                .useDelimiter("\\n");

        while (scanner.hasNext()) {
            List<Integer> l = new ArrayList<>(0);
            String temp = scanner.next();
            String[] expl = temp.split(";");
            l = new ArrayList<>();
            for (String s : expl) {
                l.add(Integer.parseInt(s.trim()));
            }
            matriz.add(l);
        }
        return new MatrizResultados(matriz, listaLinha, listaColuna);
    }

    private List<String> lerCamposMatrizUnicaResultados(String arquivo) throws FileNotFoundException {
        List<String> listaCampos = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(arquivo)).useDelimiter("\\;|\\n");

        System.out.println("Lendo campos do arquivo "+arquivo);

        while (scanner.hasNext()) {
            String nomeCampo = scanner.next();
            listaCampos.add(nomeCampo);
        }
        return listaCampos;
    }

}
