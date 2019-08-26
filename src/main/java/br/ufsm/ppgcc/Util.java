package br.ufsm.ppgcc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Arrays;

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
	 * Mostra uma matriz de double acima da diagonal principal
	 * @param matriz - Matriz de inteiros
	 */
	 public static void mostraMatriz(double[][] matriz) {
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

	 /**
	  * Lê um arquivo no formato CSV e gera uma matriz de inteiros
	  * @param caminho absoluto do arquivo csv
	  * @return uma matriz resultando do tipo int[][]
	  */
	public static int[][] csvParaMatrizInt(String caminho) throws IOException{
		int[][] matriz;
		Stream<String> lines = Files.lines(Paths.get(caminho));
		matriz = lines
            .map(s -> s.split(";"))
            .map(s -> Arrays.stream(s).mapToInt(Integer::parseInt).toArray())
			.toArray(int[][]::new);
		lines.close();
		return matriz;
	}

	 /**
	  * Lê um arquivo no formato CSV e gera uma matriz de double
	  * @param caminho absoluto do arquivo csv
	  * @return uma matriz resultando do tipo double[][]
	  */
	  public static double[][] csvParaMatrizDouble(String caminho) throws IOException{
		double[][] matriz;
		Stream<String> lines = Files.lines(Paths.get(caminho));
		matriz = lines
            .map(s -> s.split(";"))
            .map(s -> Arrays.stream(s).mapToDouble(Double::parseDouble).toArray())
			.toArray(double[][]::new);
		
		lines.close();
		return matriz;
	}

	public static String[] palavrasStrings;
	/**
	 * Lê um arquivo no formato CSV e gera um vetor de Strings
	 * @param caminho absoluto do arquivo csv
	 */
	public static String[] csvParaVetorStrings(String caminho) throws IOException{
		String auxString;
		
		try {
			BufferedReader StrR = new BufferedReader(new FileReader(caminho));
			while((auxString = StrR.readLine())!= null){
				//divide a linha lida em um array de String passando como parametro o divisor ";".
				palavrasStrings = auxString.split(";"); 
			}
			StrR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex){
			ex.printStackTrace();
		}
		
		return palavrasStrings;
	}

	/**
	 * Grava uma matriz em um arquivo no formato CSV
	 * @param matriz para gravar, caminho absoluto do destivo com a extensão .csv
	 */
	public static void gravaMatrizParaCsv(double[][] matriz, String destino) throws IOException{
		int k, l;
		try {

			FileWriter writer = new FileWriter(destino);
			for(k=0; k < matriz.length; k++){  
				for (l=0;l< matriz.length;l++){
					if (k == l){
						writer.write(1.0+";");
					} else{
						
						if (matriz[k][l] == -0.0) {writer.write(0.0+";");} else {writer.write(matriz[k][l]+";");}
						
					}
				}
				writer.write("\n"); 
			}
			writer.close();

		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Grava uma matriz em um arquivo no formato CSV
	 * @param matriz para gravar, caminho absoluto do destivo com a extensão .csv
	 */
	public static void gravaMatrizParaCsv(int[][] matriz, String destino) throws IOException{
		int k, l;
		try {

			FileWriter writer = new FileWriter(destino);
			for(k=0; k < matriz.length; k++){  
				for (l=0;l< matriz.length;l++){
					if (k == l){
						writer.write(1.0+";");
					} else{
						
						if (matriz[k][l] == -0.0) {writer.write(0.0+";");} else {writer.write(matriz[k][l]+";");}
						
					}
				}
				writer.write("\n"); 
			}
			writer.close();

		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * 
	 * Gera lista de referências 2 - retorna as palavras equivalentes
	 * @param matriz resultado, ArrayList<String> com as palavras e o caminho do arquivo de saída a ser salvo
	 * @return arquivo com a lista de referências 2
	 * @author Fhabiana Machado
	 * @since 20 de setembro de 2018
	 */
	public static void geraListaReferencias2(double[][] resultado, ArrayList<String> palavras, String caminho){
		int k, l;
		try {
			FileWriter writer = new FileWriter(caminho);
			
			writer.write("\n\n");
			for (k=0; k<resultado.length ; k++){
				for (l=0;l<resultado.length;l++){
					
					if ((l>k) && (k != l)){
						if (resultado[k][l] == 1.0){
							writer.write("A palavra "+palavras.get(k)+" é equivalente a "+palavras.get(l)+"\n");
						}					
					}
				}
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
