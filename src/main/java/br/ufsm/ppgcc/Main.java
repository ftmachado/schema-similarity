package br.ufsm.ppgcc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import br.ufsm.ppgcc.algoritmos.Algoritmos;
import br.ufsm.ppgcc.algoritmos.MontarMapeamentos;
import br.ufsm.ppgcc.algoritmos.RemontarEstrutura;
import br.ufsm.ppgcc.util.*;

public class Main {

	static String jsonDir;
	static double pontoCorte, pontoCorteAHP;
	
	public static void main(String[] args) throws Exception {
		
		ArrayList<String> palavras = new ArrayList<String>();
		int tamanhoMatriz=0;
		
		init();

		try{ 
			String arqDocEstruturalGeral = new File(".").getCanonicalPath()+"/out/etapa1_docEstruturalGeral.txt";
			String arqLista1 = new File(".").getCanonicalPath()+"/out/etapa1_listaReferencias1.csv";
			String arqCampos = new File(".").getCanonicalPath()+"/out/etapa3_matrizUnicaResultadosCampos.csv";
			String arqMatrizResultados = new File(".").getCanonicalPath()+"/out/etapa3_matrizUnicaResultados.csv";
			String arqMatrizRadical = new File(".").getCanonicalPath()+"/out/etapa2_matrizRadical.csv";
			String arqMatrizLevenshtein = new File(".").getCanonicalPath()+"/out/etapa2_matrizLevenshtein.csv";
			String arqMatrizLin = new File(".").getCanonicalPath()+"/out/etapa2_matrizLin.csv";
			String arqLista2 = new File(".").getCanonicalPath()+"/out/etapa3_listaReferencias2.csv";
			String arqConsolidados = new File(".").getCanonicalPath()+"/out/etapa3_camposConsolidados.csv";
			String arqEstruturaUnificada = new File(".").getCanonicalPath() + "/out/etapa3_estruturaUnificada.txt";
			String arqMapeamentos = new File(".").getCanonicalPath() + "/out/etapa4_mapeamentos.csv";
			String arqEsquemaConceitual = new File(".").getCanonicalPath() + "/out/etapa4_esquemaConceitual.txt";
			String fim_doc = "-enddoc";
		
			/*
			 * Algoritmo 1 - Separar Campos dos Dados
			 */
			palavras = Algoritmos.separaCamposDosDados(jsonDir, arqDocEstruturalGeral, fim_doc);

			/*
			 * Algoritmo 2 - Mesclar estrutura
			 */
			// palavras = Util.removeRepetidas(palavras);
			palavras = Algoritmos.mesclarEstrutura(palavras, fim_doc, arqLista1);
			tamanhoMatriz = palavras.size();
			Util.gravaArquivoCsv(palavras, arqCampos);
		
			/**
			 * Algoritmo 3 - Analisar Radical da Palavra
			 */
			double[][] mStem = new double[tamanhoMatriz][tamanhoMatriz];
			mStem = Algoritmos.geraMatrizStemmer( Algoritmos.aplicaStemmerList(palavras) );
	//		Util.mostraMatriz(mStem);
			Util.gravaMatrizParaCsv(mStem, arqMatrizRadical);
		
		
			/*
			* Algoritmo 4 - Analisar Similaridade Baseada em Caractere
			*/
			double[][] mLev = new double[tamanhoMatriz][tamanhoMatriz];
			mLev = Algoritmos.aplicaLevenshtein(palavras);
	//		Util.mostraMatriz(mLev);
			Util.gravaMatrizParaCsv(mLev, arqMatrizLevenshtein);
		
		
			/*
			* Algoritmo 5 - Analisar Similaridade Baseada em Conhecimento
			*/
			double[][] mLin = new double[tamanhoMatriz][tamanhoMatriz];
			mLin = Algoritmos.aplicaLin(palavras);
	//		Util.mostraMatriz(mLin);
			Util.gravaMatrizParaCsv(mLev, arqMatrizLin);


			/*
			* Algoritmo 6 - Calcular Equivalência
			*/
			double[][] resultados = new double[tamanhoMatriz][tamanhoMatriz];
			resultados = Algoritmos.calculaEquivalencia(mStem, mLev, mLin, tamanhoMatriz, pontoCorte, pontoCorteAHP);
	//		Util.mostraMatriz(resultados);
			Util.gravaMatrizParaCsv(resultados, arqMatrizResultados);

			/*
			* Algoritmo 7 - Consolidar estrutura
			*/
			Algoritmos.consolidaEstrutura(arqCampos, arqMatrizResultados, arqLista2, arqConsolidados);
		
			/*
			 * Algoritmo 8 - Remontar Estrutura
			 */
			RemontarEstrutura.remontar(jsonDir, arqLista2, arqEstruturaUnificada, arqConsolidados);

			/**
			 * Algoritmo 9 - Montar maperamentos
			 */
			MontarMapeamentos.montar(arqLista1, arqLista2, arqEstruturaUnificada, arqMapeamentos);

			/**
			 * Algoritmo 10 - Adaptar Notação de Agregados
			 */
			Algoritmos.adaptarNotacaoAgregados(arqEstruturaUnificada, arqEsquemaConceitual);

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
						"                                                                            setembro de 2019\n"+
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
