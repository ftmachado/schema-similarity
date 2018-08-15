package br.ufsm.ppgcc;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<String> palavras = new ArrayList<String>();
		int tamanhoMatriz=0;
		
		/* Algoritmo 1
		 * @param Documentos pertencentes à mesma coleção
		 * @return Arquivo com todos os campos: artefatos/saidaAlg1.txt 
		 */
		Algoritmos.separaCamposDosDados("src/main/resources/json");
		
		
		/*
		 * Algoritmo 2
		 * @param Arquivo com todos os campos
		 * @return ArrayList com campos distintos, arquivo lista de referências 1
		 */
		palavras = Util.leArquivo("src/main/resources/artefatos/saidaAlg1.txt");
		palavras = Algoritmos.removeRepetidasComListaRef(palavras, "-enddoc");
		palavras = Util.removeUmaPalavra(palavras, "--enddoc");
		tamanhoMatriz = palavras.size();
		
		
		/*
		 * Algoritmo 3
		 * @param ArrayList com campos distintos
		 * @return Matriz Stemmer[][]
		 */
		int[][] mStem = new int[tamanhoMatriz][tamanhoMatriz];
		palavras = Algoritmos.aplicaStemmerList(palavras);
		mStem = Algoritmos.geraMatrizStemmer(palavras);
		Util.mostraMatriz(mStem);
		
		
		/*
		 * Algoritmo 4
		 * @param ArrayList com campos distintos
		 * @return Matriz Lev[][]
		 */
		
//		int[][] mLev = new int[tamanhoMatriz][tamanhoMatriz];
		//aplicar o levenshtein nesse arraylist palavras
//		mLev = Algoritmos.geraMatrizStemmer(palavras, tamanhoMatriz);
//		Util.mostraMatriz(mStem, tamanhoMatriz);
		
		
		/*
		 * Algoritmo 5
		 * @param ArrayList com campos distintos
		 * @return Matriz Lin[][]
		 */
//		int[][] mLin = new int[tamanhoMatriz][tamanhoMatriz];
		//aplicar o lin nesse arraylist palavras
//		mLev = Algoritmos.geraMatrizStemmer(palavras, tamanhoMatriz);
//		Util.mostraMatriz(mStem, tamanhoMatriz);
	}

}
