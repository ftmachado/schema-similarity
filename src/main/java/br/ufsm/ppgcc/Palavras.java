/*
* CLASSE PALAVRAS - LÊ O ARQUIVO TXT COM AS PALAVRAS ANALISADAS,
* E DEPOIS É INSTANCIADA NA CLASSE PRINCIPAL PARA MOSTRAR QUAIS PALAVRAS SÃO EQUIVALENTES (ALGORITMO 6)
*/
package br.ufsm.ppgcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Palavras extends Principal {
    //String[] Plv;
    String Str;
    String[] Plv; 

//método para ler arquivo txt com as palavras
    public void List_Plv(int tamanho) throws IOException {
        File pasta = new File("C:\\Users\\renat\\Desktop\\Mestrado\\1º Sem\\Topicos BD\\Arq_aplicacao\\Bloco_Palavras");
        File[] arquivo = pasta.listFiles();
       
        try {
            
            BufferedReader StrR = new BufferedReader(new FileReader(arquivo[tamanho]));
            //le cada linha do arquivo 
        while((Str = StrR.readLine())!= null){
                
            //método split divide a linha lida em um array de String passando como parametro o divisor ";".
            
           Plv = Str.split(";");
            
            //O foreach é usadao para imprimir cada célula do array de String.
           for (String inf : Plv) { 
                System.out.print(inf+"; "); 
            }
            System.out.println("\n");
        }
        //Fechamos o buffer
        StrR.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        catch (IOException ex){
            ex.printStackTrace();
        }
        
    }
    }


   
