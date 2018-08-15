/*
* CLASSE CARREGALIN - CARREGA A MATRIZ LIN DO ARQUIVO CSV GERADO NA CLASSE ANALISELIN,
* E DEPOIS É INSTANCIADA NA CLASSE PRINCIPAL PARA REALIZAR O CALCULO DE EQUIVALÊNCIA (ALGORITMO 6)
*/

package br.ufsm.ppgcc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CarregaLin extends AnaliseLin {
    double[][] Lin;
    public int aux_tam[];

//método para carregar a matriz que está no arquivo csv
    public void MatrizLin(int tamanho) throws IOException {
        File pasta = new File("C:\\Users\\renat\\Desktop\\Mestrado\\1º Sem\\Topicos BD\\Arq_aplicacao\\Lin");
        File[] arquivo = pasta.listFiles();
        aux_tam = new int[arquivo.length];
        //for (int m=0;m<arquivo.length;m++){*/
        Lin =
            Files.lines(Paths.get("C:\\Users\\renat\\Desktop\\Mestrado\\1º Sem\\Topicos BD\\Arq_aplicacao\\Lin\\"+tamanho+".csv"))
            .map(s -> s.split(";"))
            .map(s -> Arrays.stream(s).mapToDouble(Double::parseDouble).toArray())
            .toArray(double[][]::new);
    
        aux_tam[tamanho] = Lin.length;
        for (i=0; i<Lin.length; i++){
            System.out.println("");
            for (j=0;j<Lin.length;j++){
                if (j<i)
                    System.out.print("| - |");
                else
                    System.out.print("|"+Lin[i][j]+"|");
            }
        }
            
    }

}
      
    
