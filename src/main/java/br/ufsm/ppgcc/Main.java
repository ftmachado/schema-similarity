package br.ufsm.ppgcc;

import java.io.IOException;
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
//		palavras = Util.removeUmaPalavra(palavras, "--enddoc");
		tamanhoMatriz = palavras.size();
		
		
		/*
		 * Algoritmo 3
		 * @param ArrayList com campos distintos
		 * @return Matriz Stemmer[][]
		 */
		int[][] mStem = new int[tamanhoMatriz][tamanhoMatriz];
		mStem = Algoritmos.geraMatrizStemmer( Algoritmos.aplicaStemmerList(palavras) );
//		Util.mostraMatriz(mStem);
		try {
			Util.gravaMatrizParaCsv(mStem, "src/main/resources/artefatos/radical.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*
		 * Algoritmo 4
		 * @param ArrayList com campos distintos
		 * @return Matriz Lev[][]
		 */
		double[][] mLev = new double[tamanhoMatriz][tamanhoMatriz];
		mLev = Algoritmos.aplicaLevenshtein(palavras);
//		Util.mostraMatriz(mLev);
		try {
			Util.gravaMatrizParaCsv(mLev, "src/main/resources/artefatos/levenshtein.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*
		 * Algoritmo 5
		 * @param ArrayList com campos distintos
		 * @return Matriz Lin[][]
		 */
		double[][] mLin = new double[tamanhoMatriz][tamanhoMatriz];
		mLin = Algoritmos.aplicaLin(palavras);
//		Util.mostraMatriz(mLin);
		try {
			Util.gravaMatrizParaCsv(mLin, "src/main/resources/artefatos/lin.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Algoritmo 6
		 * @param 3 matrizes das técnicas e tamanho da matriz
		 * @return Matriz Resultados[][]
		 */
		double[][] resultados = new double[tamanhoMatriz][tamanhoMatriz];
		resultados = Algoritmos.calculaEquivalencia(mStem, mLev, mLin, tamanhoMatriz);
//		Util.mostraMatriz(resultados);
//		try {
//			Util.gravaMatrizParaCsv(mLin, "src/main/resources/artefatos/resultados.csv");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		Util.mostraPalavrasEquivalentes(resultados, palavras);
	}

}
