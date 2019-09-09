package br.ufsm.ppgcc.model.dao;

import br.ufsm.ppgcc.model.estruturas.MatrizResultados;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Ezequiel Ribeiro
 */
public class MatrizResultadosDAO {

    public MatrizResultados lerMatrizUnicaResultados(String arquivoCampos, String arquivoMatriz)
            throws FileNotFoundException, IOException{

        List<List<Integer>> matriz = new ArrayList<>(0);

        List<String> listaColuna = lerCamposMatrizUnicaResultados(arquivoCampos);
        List<String> listaLinha = listaColuna;

        FileReader arq = new FileReader(arquivoMatriz);
        Scanner scanner = new Scanner(arq);
        scanner.useDelimiter("\\n");

        while (scanner.hasNext()) {

            List<Integer> l = new ArrayList<>(0);
            String temp = scanner.next();
            String[] expl = temp.split(";");
            l = new ArrayList<>();

            for (String s : expl) {
                double d = Double.parseDouble(s.trim());
                l.add((int) d);
            }

            matriz.add(l);

        }
        arq.close();
        scanner.close();
        return new MatrizResultados(matriz, listaLinha, listaColuna);
    }

    private List<String> lerCamposMatrizUnicaResultados(String arquivo) throws IOException {
        
        List<String> listaCampos = new ArrayList<>();
        FileReader arq = new FileReader(arquivo);
        Scanner scanner = new Scanner(arq);
        scanner.useDelimiter("\\;|\\n");

        // System.out.printf("\n\t\tLendo campos do arquivo "+arquivo+"\n");

        while (scanner.hasNext()) {
            String nomeCampo = scanner.next();
            listaCampos.add(nomeCampo);
        }

        scanner.close();
        arq.close();
        return listaCampos;
    }

}
