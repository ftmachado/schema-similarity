package br.ufsm.ppgcc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * @since 19 de fevereiro de 2018
 * @author Fhabiana Machado
 * @description Funções auxiliares utilizadas em todo o projeto da dissertação
 */

public class Util {

	/**
	 * Lê um arquivo retornando um ArrayList
	 * @param origemArquivo - caminho absoluto do arquivo
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> leArquivo(String origemArquivo){
		 
		ArrayList<String> palavras = new ArrayList<String>();
		
	    try {  
            BufferedReader in = new BufferedReader(new FileReader(origemArquivo));
            String str;
            while (in.ready()) {  
                str = in.readLine();  
                palavras.add(str);
            }  
            in.close();  
        } catch (IOException e) {
	        System.out.println("Ero ao ler arquivo:" + e);
	    }  
	    return palavras;  
	}
	
	
	/**
	 * Remove as palavras repetidas retornando um ArrayList
	 * @param palavras - ArrayList de palavras
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> removeRepetidas(ArrayList<String> palavras) {
		
        for (int i = 0; i < palavras.size() - 1; i++) {  
            for (int j = i + 1; j < palavras.size(); j++) {  
                String str1 = palavras.get(i);
                String str2 = palavras.get(j);
                
                if (str1.equals(str2) == true) {// verifica se as palavras são iguais
                    palavras.remove(j);
                    j--;  
                }
            }  
        }  
        return palavras;  
    }
	
	
	
	
	/**
	 * Remove apenas uma palavra do ArrayList
	 * @param palavras - ArrayList de palavras
	 * @param r - String a ser removida
	 * @return ArrayList sem a palavra
	 */
	public static ArrayList<String> removeUmaPalavra(ArrayList<String> palavras, String r) {
		
		for (int i=0; i< palavras.size() - 1; i++) {
			
			if (palavras.get(i).equals(r)) {
				palavras.remove(i);
			}
			
		}
		return palavras;
	}
	
	
	/**
	 * Grava um arquivo com as palavras do ArrayList
	 * @param palavras - ArrayList de palavras
	 * @param destinoArquivo - caminho do arquivo que será salvo
	 */
	 public static void gravaArquivo(ArrayList<String> palavras, String destinoArquivo) {  
		  
	        try {  
	            FileWriter arq = new FileWriter(destinoArquivo);  
	            PrintWriter gravarArq = new PrintWriter(arq);  
	            
	            for (int i = 0; i < palavras.size(); i++) {  
	                gravarArq.printf(palavras.get(i));  
	                gravarArq.printf("\r\n");
	            }
	            
	            arq.close();  
	            System.out.println("Arquivo gravado com sucesso!");
	        } catch (IOException e) {  
	            System.out.println("Erro ao gravar arquivo: " + e);
	        }  
	  
	  }
	 
	 
	 /**
	 * Mostra uma matriz de inteiros acima da diagonal principal
	 * @param matriz - Matriz de inteiros
	 */
	 public static void mostraMatriz(int[][] matriz) {
		 int t = matriz.length;
		 for (int i = 0; i < t; i++) {
				System.out.println("");
	            for (int j = 0; j < t; j++) {
	            	if (j < i || j == i)
						System.out.print("| - ");
					else
						System.out.print("|" + matriz[i][j]);
	            	
//	            	if (matriz[i][j] == 1) {
//	            		System.out.print("Palavra repetida é a da linha: "+i);
//	            	}
	            }
		}
		 
	}

	 /**
	 * Mostra uma matriz de floats acima da diagonal principal
	 * @param matriz - Matriz de inteiros
	 */
	 public static void mostraMatriz(float[][] matriz) {
		 int t = matriz.length;
		 for (int i = 0; i < t; i++) {
				System.out.println("");
	            for (int j = 0; j < t; j++) {
	            	if (j < i || j == i)
						System.out.print("| - ");
					else
						System.out.print("|" + matriz[i][j]);
	            	
//		            	if (matriz[i][j] == 1) {
//		            		System.out.print("Palavra repetida é a da linha: "+i);
//		            	}
	            }
		}
			 
		}
	 
	 /**
	 * Lê e conta o número de arquivos JSON de uma pasta
	 * @param caminho - caminho absoluto da pasta
	 */
	 public static int numArquivosJsonPasta(String caminho) {
		int n_arquivos=0;
		 
		String[] x = new File(caminho).list();
			
		for (String e: x) {
			if (e.contains(".json")) { n_arquivos++; }
		}
		
		return n_arquivos;
	 }
	 
	 /**
	 * Lê e conta o número de linhas de um arquivo texto
	 * @param caminho - caminho absoluto da pasta
	 */
	 public static int numLinhasArquivo(String caminho) {

		 int n_linhas=0;
		 try {
			File arqLeitura = new File(caminho);
			
			//pega o tamanho
			long t = arqLeitura.length();
			
			DataInputStream in = new DataInputStream(new FileInputStream(arqLeitura));
			LineNumberReader l = new LineNumberReader(new InputStreamReader(in));
			l.skip(t);
			
			//conta o numero de linhas do arquivo, começa em zero
			n_linhas = l.getLineNumber() + 1;
			l.close();
		} catch (FileNotFoundException e) {
			System.out.println("Erro: " + e);
		} catch (IOException e) {
			System.out.println("Erro: " + e);
		}
		 
		return n_linhas;
		
	 }

}
