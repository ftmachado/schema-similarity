package br.ufsm.ppgcc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufsm.ppgcc.algoritmos.Algoritmos;
import br.ufsm.ppgcc.util.*;

public class Main {

	static String jsonDir;
	static double pontoCorte, pontoCorteAHP;
	
	public static void main(String[] args) {
		
		ArrayList<String> palavras = new ArrayList<String>();
		int tamanhoMatriz=0;
		
		init();
		
		/**
		 * Algoritmo 1 - Separar Campos dos Dados
		 * @param Documentos pertencentes à mesma coleção
		 * @return Arquivo com todos os campos: out/saidaAlg1.txt 
		 * Diretório - resources/json
		 */
		Algoritmos.separaCamposDosDados(jsonDir);
		
		/**
		 * Algoritmo 2 - Mesclar estrutura
		 * @param Arquivo com todos os campos
		 * @return ArrayList com campos distintos, arquivo lista de referências 1
		 * Diretório - src/main/resources/artefatos/saidaAlg1.txt
		 */
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
		
		/**
		 * Algoritmo 3 - Analisar Radical da Palavra
		 * @param ArrayList com campos distintos
		 * @return Matriz Stemmer[][]
		 * Diretório - src/main/resources/artefatos/radical.csv
		 */
		int[][] mStem = new int[tamanhoMatriz][tamanhoMatriz];
		mStem = Algoritmos.geraMatrizStemmer( Algoritmos.aplicaStemmerList(palavras) );
//		Util.mostraMatriz(mStem);
		try {
			Util.gravaMatrizParaCsv(mStem, new File(".").getCanonicalPath()+"/out/etapa2_matrizRadical.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*
		 * Algoritmo 4 - Analisar Similaridade Baseada em Caractere
		 * @param ArrayList com campos distintos
		 * @return Matriz Lev[][]
		 * Diretório - src/main/resources/artefatos/levenshtein.csv
		 */
		double[][] mLev = new double[tamanhoMatriz][tamanhoMatriz];
		mLev = Algoritmos.aplicaLevenshtein(palavras);
//		Util.mostraMatriz(mLev);
		try {
			Util.gravaMatrizParaCsv(mLev, new File(".").getCanonicalPath()+"/out/etapa2_matrizLevenshtein.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*
		 * Algoritmo 5 - Analisar Similaridade Baseada em Conhecimento
		 * @param ArrayList com campos distintos
		 * @return Matriz Lin[][]
		 * Diretório - src/main/resources/artefatos/lin.csv
		 */
		double[][] mLin = new double[tamanhoMatriz][tamanhoMatriz];
		mLin = Algoritmos.aplicaLin(palavras);
//		Util.mostraMatriz(mLin);
		try {
			Util.gravaMatrizParaCsv(mLev, new File(".").getCanonicalPath()+"/out/etapa2_matrizLin.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Algoritmo 6 - Calcular Equivalência
		 * @param 3 matrizes das técnicas e tamanho da matriz
		 * @return Matriz Resultados[][]
		 */
		double[][] resultados = new double[tamanhoMatriz][tamanhoMatriz];
		resultados = Algoritmos.calculaEquivalencia(mStem, mLev, mLin, tamanhoMatriz, pontoCorte, pontoCorteAHP);
//		Util.mostraMatriz(resultados);
		try {
			Util.gravaMatrizParaCsv(resultados, new File(".").getCanonicalPath()+"/out/etapa3_resultados.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Algoritmo 7 - Consolidar estrutura
		 * @param única matriz por bloco
		 * @return campos consolidados (palavras), lista de referências 2 
		 */
//		List<String[]> listaReferencias2 = new ArrayList<>();
//                
//		try {
////                    Util.geraListaReferencias2(resultados, palavras, new File(".").getCanonicalPath() + "/out/etapa3_listaReferencias2.txt");
//			listaReferencias2 = Algoritmos.consolidaEstrutura(resultados, palavras,
//					new File(".").getCanonicalPath() + "/out/etapa3_listaReferencias2.txt");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		/**
		 * Algoritmo 8 - Remontar Estrutura
		 * @param campos consolidados, lis de referencias 1, lista de referências 2
		 * @return estrutura consolidada
		 */
//		try {
//			Algoritmos.remontarEstrutura(jsonDir,
//					new File(".").getCanonicalPath() + "/out/etapa3_estruturaConsolidada.txt",
//					palavras,
//					listaReferencias2);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
	
	public static void init() {
		System.out.print("\n"+
						"    ----- /--- -   - ----  -   -   ---       -----  -  -   -  - -    ---   ---  - ----- -  -\n"+
						"    |_ _  |    | _ | | _  | \\-/ | | _ |  _   |_ _   | | \\-/ | | |   | _ | |  _| |   |    \\/\n"+
						"        | |    |   | |    |     | |   |          |  | |     | | |   |   | |  \\  |   |    | \n"+
						"    -----  ---  -  - ---- -     - -   -      -----  - -     - - --- -   - -   - -   -    -\n"+
						"                                                                              agosto de 2019\n"+
						"                                        by Fhabiana Machado, Renata Padilha, Ezequiel Ribeiro\n\n"
		);
		
		Scanner ler = new Scanner(System.in);
		System.out.println("Bem vindo ao sistema para definição de campos equivalentes em fontes de dados JSON");
		System.out.println("O nome dos arquivos devem ser do modelo doc1.json, doc2.json...");
		
		try {
			System.out.println("Seu diretório corrente é "+new File(".").getCanonicalPath()+"\\resources\\json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Informe o caminho absoluto do diretório de arquivos JSON ({basedir}\\resources\\json):");
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
