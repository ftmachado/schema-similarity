package br.ufsm.ppgcc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

		try{ 
			String arqDocEstruturalGeral = new File(".").getCanonicalPath()+"/out/etapa1_docEstruturalGeral.txt";
			String arqCampos = new File(".").getCanonicalPath()+"/out/etapa2_campos.csv";
			String arqMatrizResultados = new File(".").getCanonicalPath()+"/out/etapa2_resultados.csv";
			String arqMatrizRadical = new File(".").getCanonicalPath()+"/out/etapa2_matrizRadical.csv";
			String arqMatrizLevenshtein = new File(".").getCanonicalPath()+"/out/etapa2_matrizLevenshtein.csv";
			String arqMatrizLin = new File(".").getCanonicalPath()+"/out/etapa2_matrizLin.csv";
			String arqLista2 = new File(".").getCanonicalPath()+"/out/etapa3_listaReferencias2.csv";
			String arqConsolidados = new File(".").getCanonicalPath()+"/out/etapa3_camposConsolidados.csv";
		
			/**
			 * Algoritmo 1 - Separar Campos dos Dados
			 * @param Documentos pertencentes à mesma coleção
			 * Diretório - resources/json
			 */
			Algoritmos.separaCamposDosDados(jsonDir);
		
			/**
			 * Algoritmo 2 - Mesclar estrutura
			 * @param Arquivo com todos os arqCampos
			 * @return ArrayList com arqCampos distintos, arquivo lista de referências 1
			 */
			palavras = Util.leArquivo(arqDocEstruturalGeral);
			palavras = Util.removeRepetidas(palavras);
			palavras = Algoritmos.removeRepetidasComListaRef(palavras, "-enddoc");
	//		palavras = Util.removeUmaPalavra(palavras, "--enddoc");
			tamanhoMatriz = palavras.size();
			Util.gravaArquivoCsv(palavras, arqCampos);
		
			/**
			 * Algoritmo 3 - Analisar Radical da Palavra
			 * @param ArrayList com arqCampos distintos
			 * @return Matriz Stemmer[][]
			 */
			int[][] mStem = new int[tamanhoMatriz][tamanhoMatriz];
			mStem = Algoritmos.geraMatrizStemmer( Algoritmos.aplicaStemmerList(palavras) );
	//		Util.mostraMatriz(mStem);
			Util.gravaMatrizParaCsv(mStem, arqMatrizRadical);
		
		
			/*
			* Algoritmo 4 - Analisar Similaridade Baseada em Caractere
			* @param ArrayList com arqCampos distintos
			* @return Matriz Lev[][]
			*/
			double[][] mLev = new double[tamanhoMatriz][tamanhoMatriz];
			mLev = Algoritmos.aplicaLevenshtein(palavras);
	//		Util.mostraMatriz(mLev);
			Util.gravaMatrizParaCsv(mLev, arqMatrizLevenshtein);
		
		
			/*
			* Algoritmo 5 - Analisar Similaridade Baseada em Conhecimento
			* @param ArrayList com arqCampos distintos
			* @return Matriz Lin[][]
			* Diretório - src/main/resources/artefatos/lin.csv
			*/
			double[][] mLin = new double[tamanhoMatriz][tamanhoMatriz];
			mLin = Algoritmos.aplicaLin(palavras);
	//		Util.mostraMatriz(mLin);
			Util.gravaMatrizParaCsv(mLev, arqMatrizLin);


			/*
			* Algoritmo 6 - Calcular Equivalência
			* @param 3 matrizes das técnicas e tamanho da matriz
			* @return Matriz Resultados[][]
			*/
			double[][] resultados = new double[tamanhoMatriz][tamanhoMatriz];
			resultados = Algoritmos.calculaEquivalencia(mStem, mLev, mLin, tamanhoMatriz, pontoCorte, pontoCorteAHP);
	//		Util.mostraMatriz(resultados);
			Util.gravaMatrizParaCsv(resultados, arqMatrizResultados);

			/*
			* Algoritmo 7 - Consolidar estrutura
			* @param arquivos de entrada da matriz única e dos arqCampos e arquivos de saída
			*/
			Algoritmos.consolidaEstrutura(arqCampos, arqMatrizResultados, arqLista2, arqConsolidados);
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Algoritmo 8 - Remontar Estrutura
		 * @param arqCampos arqConsolidados, lis de referencias 1, lista de referências 2
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
		System.out.println("Bem vindo ao sistema para definição de arqCampos equivalentes em fontes de dados JSON");
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
