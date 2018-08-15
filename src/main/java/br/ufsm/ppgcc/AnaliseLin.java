/*
* CLASSE ANALISELIN - REALIZA O CALCULO DE LIN (WORDNET) GRAVANDO SEUS RESULTADOS EM 
* UMA MATRIZ EM UM ARQUIVO CSV
*/

package br.ufsm.ppgcc;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AnaliseLin extends Principal{
    
 public String[] Palavras;
 public String Str;
 public int tam_Lin, tam_Palavras;
 public double Lin[][];
 public double distance2;

//métodos que realizam o calculo de lin por meio da utilização das bibliotecas da wordnet
 private static ILexicalDatabase db = new NictWordNet();
 
 private static double compute(String word1, String word2) {
  WS4JConfiguration.getInstance().setMFS(false);
  double s = new Lin(db).calcRelatednessOfWords(word1, word2);
  return s;
 }

//Método para ler o arquivo txt com as palavras e gravar em um vetor Palavras
public void CalculoLin() throws IOException{
    File pasta = new File("C:\\Users\\renat\\Desktop\\Mestrado\\1º Sem\\Topicos BD\\Arq_aplicacao\\Bloco_Palavras");
    File[] arquivo = pasta.listFiles();
    for (int m=0;m<arquivo.length;m++){
    try {
        BufferedReader StrR = new BufferedReader(new FileReader(arquivo[m]));
        //le cada linha do arquivo 
        while((Str = StrR.readLine())!= null){
            //método split divide a linha lida em um array de String passando como parametro o divisor ";".
            Palavras = Str.split(";"); 
            //System.out.println("\n");
        }
        StrR.close();
    } 
    catch (FileNotFoundException e) {
        e.printStackTrace();
    } 
    catch (IOException ex){
        ex.printStackTrace();
    }
    

//realiza o calculo lin
    
    
    tam_Palavras=Palavras.length;
    double Lin[][]= new double[tam_Palavras][tam_Palavras]; 			
    for(i=0; i<tam_Palavras-1; i++){
        for(j=i+1; j<tam_Palavras; j++){
            Lin[i][j] = compute(Palavras[i], Palavras[j]);
            
        }
    }
    tam_Lin = Lin.length;

    
//grava a matriz de lin em um arquivo csv

    try {
        FileWriter writer = new FileWriter("C:\\Users\\renat\\Desktop\\Mestrado\\1º Sem\\Topicos BD\\Arq_aplicacao\\Lin\\"+m+".csv");
        for(k=0; k<tam_Lin; k++){  
            for (l=0;l<tam_Lin;l++){
                if (k == l){
                    writer.write(1.0+";");
                } else{
                    if (Lin[k][l] == -0.0){
                        writer.write(0.0+";");}
                    else{
                    writer.write(Lin[k][l]+";");
                    }
                }
            }
            writer.write("\n"); 
        }
        writer.close();
    } 
    catch (IOException exc) {
    }
}
    }
}
