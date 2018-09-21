package br.ufsm.ppgcc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	static String jsonDir;
	static double pontoCorte, pontoCorteAHP;
	
	public static void main(String[] args) {
		
		ArrayList<String> palavras = new ArrayList<String>();
		int tamanhoMatriz=0;
		
		init();
		
		/* Algoritmo 1
		 * @param Documentos pertencentes à mesma coleção
		 * @return Arquivo com todos os campos: artefatos/saidaAlg1.txt 
		 */
//		Algoritmos.separaCamposDosDados("src/main/resources/json");
		Algoritmos.separaCamposDosDados(jsonDir);
		
		/*
		 * Algoritmo 2
		 * @param Arquivo com todos os campos
		 * @return ArrayList com campos distintos, arquivo lista de referências 1
		 */
//		palavras = Util.leArquivo("src/main/resources/artefatos/saidaAlg1.txt");
		try {
			palavras = Util.leArquivo(new File(".").getCanonicalPath()+"/out/etapa1_docEstruturalGeral.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		palavras = Util.removeRepetidas(palavras);
//		System.out.println("Palavras "+palavras.size());
//		for(String s : palavras) {
//		     System.out.println(s);
//		}
//		
		palavras = Algoritmos.removeRepetidasComListaRef(palavras, "-enddoc");
//		palavras = Util.removeUmaPalavra(palavras, "--enddoc");
		tamanhoMatriz = palavras.size();
//		System.out.println("Tam matriz "+tamanhoMatriz);
//		System.exit(0);
		
		/*
		 * Algoritmo 3
		 * @param ArrayList com campos distintos
		 * @return Matriz Stemmer[][]
		 */
		int[][] mStem = new int[tamanhoMatriz][tamanhoMatriz];
		mStem = Algoritmos.geraMatrizStemmer( Algoritmos.aplicaStemmerList(palavras) );
//		Util.mostraMatriz(mStem);
		try {
//			Util.gravaMatrizParaCsv(mStem, "src/main/resources/artefatos/radical.csv");
			Util.gravaMatrizParaCsv(mStem, new File(".").getCanonicalPath()+"/out/etapa2_matrizRadical.csv");
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
//			Util.gravaMatrizParaCsv(mLev, "src/main/resources/artefatos/levenshtein.csv");
			Util.gravaMatrizParaCsv(mLev, new File(".").getCanonicalPath()+"/out/etapa2_matrizLevenshtein.csv");
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
//			Util.gravaMatrizParaCsv(mLin, "src/main/resources/artefatos/lin.csv");
			Util.gravaMatrizParaCsv(mLev, new File(".").getCanonicalPath()+"/out/etapa2_matrizLin.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Algoritmo 6
		 * @param 3 matrizes das técnicas e tamanho da matriz
		 * @return Matriz Resultados[][]
		 */
		double[][] resultados = new double[tamanhoMatriz][tamanhoMatriz];
		resultados = Algoritmos.calculaEquivalencia(mStem, mLev, mLin, tamanhoMatriz, pontoCorte, pontoCorteAHP);
//		Util.mostraMatriz(resultados);
//		try {
//			Util.gravaMatrizParaCsv(mLin, "src/main/resources/artefatos/resultados.csv");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {
			Algoritmos.geraListaReferencias2(resultados, palavras, new File(".").getCanonicalPath()+"/out/etapa3_listaReferencias2.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void init() {
		System.out.print("\n"+
						"    ----- /--- -   - ----  -   -   ---       -----  -  -   -  - -    ---   ---  - ----- -  -\n"+
						"    |_ _  |    | _ | | _  | \\-/ | | _ |  _   |_ _   | | \\-/ | | |   | _ | |  _| |   |    \\/\n"+
						"        | |    |   | |    |     | |   |          |  | |     | | |   |   | |  \\  |   |    | \n"+
						"    -----  ---  -  - ---- -     - -   -      -----  - -     - - --- -   - -   - -   -    -\n"+
						"                                                                              agosto de 2018\n"+
						"                                                         by Fhabiana Machado, Renata Padilha\n\n"
		);
		
		Scanner ler = new Scanner(System.in);
		System.out.println("Bem vindo ao sistema para definição de campos equivalentes em fontes de dados JSON");
		System.out.println("O nome dos arquivos devem ser do modelo doc1.json, doc2.json...");
		
		try {
			System.out.println("Seu diretório corrente é "+new File(".").getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Informe o caminho absoluto do diretório de arquivos JSON:");
		jsonDir = ler.nextLine();
		System.out.println("Para o cálculo de equivalências, defina as variáveis:");
		System.out.println("Ponto de corte: ");
		pontoCorte = ler.nextDouble();
		System.out.println("Ponto de corte para cálculo com o método AHP(analytic hierarchy process): ");
		pontoCorteAHP = ler.nextDouble();
		System.out.println("Iniciar processo...");
		ler.nextLine();
		ler.close();
//		
	}

}
