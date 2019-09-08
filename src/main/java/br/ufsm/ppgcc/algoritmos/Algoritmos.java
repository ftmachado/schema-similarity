package br.ufsm.ppgcc.algoritmos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.tartarus.martin.Stemmer;

import br.ufsm.ppgcc.util.Util;
import br.ufsm.ppgcc.util.UtilJSON;
import br.ufsm.ppgcc.model.estruturas.ElementoBloco;

public class Algoritmos {


	/** 
	* Algoritmo 1 - Separar Campos dos Dados
	* Esta função descreve um parser json gerando um arquivo com saída formatada
	* com apenas os campos do json. Utiliza o delimitador --enddoc para marcar o
	* fim de um documento.
	* @lib javax.json-api-1.0.jar
	* @author Fhabiana Machado
	* @since 15 de fevereiro de 2018
	* @param caminho - pasta de origem dos arquivos json
	* @return Arquivo .txt com todos os campos de cada documento - artefatos/saidaAlg1.txt
	*/

	public static void separaCamposDosDados(String caminho) {
		
		try {
			int i=0;
			int n_arquivos=Util.numArquivosJsonPasta(caminho);
		
			//LOG
			System.out.printf("\n\tSeparando campos dos dados...");
	
			JsonReader reader;

//			FileWriter arq = new FileWriter("src/main/resources/artefatos/saidaAlg1.txt");
			FileWriter arq = new FileWriter(new File(".").getCanonicalPath() + "/out/etapa1_docEstruturalGeral.txt");
			
			PrintWriter gravarArq = new PrintWriter(arq);
			
			for (i=1; i<=n_arquivos; i++) {
				
				if (i!= 1) {
					gravarArq.println("-enddoc");
				}
			
				String file = caminho+"/doc"+i+".json";
				
				reader = Json.createReader(new FileReader(file));
				JsonStructure jsonst = reader.read();
				
				StringWriter stWriter = new StringWriter();
				try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
					jsonWriter.write(jsonst);
				}
			
				String jsonData = stWriter.toString();
				
				JsonParser parser = Json.createParser(new StringReader(jsonData));
			
				while (parser.hasNext()) {
					JsonParser.Event event = parser.next();
					
					switch (event) {
					
						case KEY_NAME:
							gravarArq.println(parser.getString());
							break;
							
						default:
							break;
					}
				}
			} // fim do loop nos arquivos
		
			arq.close();
			
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Algoritmos.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Algoritmos.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	
	/**
	 * Algoritmo 2 - Mesclar Estrutura
	 * Remove as palavras repetidas de um ArrayList gravando a lista de referências 1 
	 * em um arquivo.
	 * 
	 * @author Fhabiana Machado
	 * @param palavras - ArrayList<palavras>, String delimitador para marcar fim do documento
	 * @param fim_doc - String que representa o fim de um documento
	 * @return ArrayList apenas com palavras distintas, um arquivo em artefatos/listaRef1.txt
	 * @since 19 de fevereiro de 2018
	 */
	public static ArrayList<String> removeRepetidasComListaRef(ArrayList<String> palavras, String fim_doc) {
		
		//LOG
		System.out.printf("\n\tRemovendo palavras repetidas...");
		
		try {
//			FileWriter arq = new FileWriter("src/main/resources/artefatos/listaRef1.txt");
			FileWriter arq = new FileWriter(new File(".").getCanonicalPath() + "/out/etapa1_listaReferencias1.txt");
			
            PrintWriter gravarArq = new PrintWriter(arq);
		
			String doc_atuali="", doc_atualj=""; int k=2;
			
	        for (int i = 0; i <= palavras.size() - 1; i++) {
	        	
	        	if (i==0) {	
	        		doc_atuali = "doc1";
	        	}
	        	if (palavras.get(i).equals(fim_doc)) {
	        		doc_atuali = "doc"+k;
	        		k++;
	        		continue;
	        	}
	        	
	        	gravarArq.print(palavras.get(i));
	        	
	            for (int j = i + 1; j < palavras.size(); j++) {
	            	int k2=2;
	            	
	            	if (palavras.get(j).equals(fim_doc)) {
	            		doc_atualj = "doc"+k2;
	            		k2++;
	            	} else {
	            		String str1 = palavras.get(i);
	            		String str2 = palavras.get(j);
	            		
	            		if (str1.equals(str2) == true) {// verifica se as palavras são iguais
	            			palavras.remove(j);
	            			j--;
	            			gravarArq.print(", " + doc_atualj);
	            			break;
	            		}
	            	}
	            	
	            }
	            
	            gravarArq.print(", " + doc_atuali + " \n");
	        }
	        
	        arq.close();
        
		} catch(IOException e) {
			System.out.println("Erro: " + e);
		}
        palavras = Util.removeUmaPalavra(palavras, fim_doc);
        return palavras;  
    }
	
	/**
	 * Algoritmo 3 - Analisar Radical da Palavra
	 * Aplica o algoritmo Porter Stemmer em um arquivo texto
	 * 
	 * @param origemArquivo - Caminho do arquivo de origem
	 * @return ArrayList de palavras com apenas o radical
	 * @author Fhabiana Machado
	 * @since 02 de março de 2018
	 */
	public static ArrayList<String> aplicaStemmerArquivo(String origemArquivo) {
		
		ArrayList<String> palavras = new ArrayList<String>();
		Stemmer s = new Stemmer();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(origemArquivo));
			String w;
			while (in.ready()) { //para cada linha
				
				w = in.readLine();
				
				for (int i = 0; i < w.length(); i++) {
					s.add(w.charAt(i));
				}
				s.stem();
				palavras.add(s.toString());
				
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Ero ao ler arquivo:" + e);
		}
		
		return palavras;
	
	}
	
	/**
	 * Algoritmo 3 - Analisar Radical da Palavra
	 * Aplica o algoritmo Porter Stemmer em um ArrayList
	 * 
	 * @param words - ArrayList<String> de palavras
	 * @return ArrayList de palavras com apenas o radical
	 * @author Fhabiana Machado
	 * @return 
	 * @since 02 de março de 2018
	 */
	public static ArrayList<String> aplicaStemmerList(ArrayList<String> words) {
		
		ArrayList<String> palavras = new ArrayList<String>();
		Stemmer s = new Stemmer();
		
		for (String w : words) {
			
			for (int i = 0; i < w.length(); i++) {
				s.add(w.charAt(i));
			}
			s.stem();
			palavras.add(s.toString());
			
		}
		// Mostrar palavras
		for (int i = 0; i < palavras.size(); i++) {
//			System.out.println(palavras.get(i).toString());
		}
		
		return palavras;
	
	}
	
	/**
	 * Algoritmo 3 - Analisar Radical da Palavra
	 * Gera a matriz do Stemmer, o índice da palavra será a ordem dela no arquivo
	 * 
	 * @param palavras - ArrayList<String> com as palavras já extraídas o radical
	 * @author Fhabiana Machado
	 * @since 19 de fevereiro de 2018
	 * @return Matriz com 0 e 1
	 */
	public static int[][] geraMatrizStemmer(ArrayList<String> palavras){
		
		//LOG
		System.out.printf("\n\tGerando matriz com radicais...");
		
		int n_palavras = palavras.size();
		int[][] matriz = new int[n_palavras][n_palavras];
		
		for (int i = 0; i < n_palavras; i++) {
            for (int j = 0; j < n_palavras; j++) {
            	//Se for a diagonal principal vai ser 1
            	if (i==j) {
            		matriz[i][j]=1;
            	} else if (palavras.get(i).equals(palavras.get(j)) == true) {
            			matriz[i][j]=1;
            		} else {
            			matriz[i][j]=0;
            		}
            }
        }
		
		return matriz;
	}
	
	
	/**
	 * Algoritmo 4 - Analisar Similaridade Baseada em Caractere
	 * Aplica a função de similaridade Levenshtein em um ArrayList<String>
	 * de palavras comparando todas com todas. A linha da palavra será
	 * seu índice.
	 * @param palavras - ArrayList de Strings
	 * @return Matriz double[][] com resultados par a par
	 * @author Fhabiana Machado
	 * @since 02 de março de 2018
	 */
	public static double[][] aplicaLevenshtein(ArrayList<String> palavras) {
		
		//LOG
		System.out.printf("\n\tGerando matriz de levenshtein...");
		
		int t = palavras.size();
		double lev[][] = new double[t][t];
		StringMetric metric = StringMetrics.levenshtein();
		
		for (int i = 0; i < palavras.size(); i++) {
            for (int j = 0; j < palavras.size(); j++) {
            	
//            	System.out.println("Matriz["+i+"]["+j+"]");
            	
            	if (i==j) {
            		lev[i][j]=1;
            	} else {
            		lev[i][j] = metric.compare(palavras.get(i), palavras.get(j));
            	}
            	
            }
		}
     
		return lev;
	}

	/**
	 * Algoritmo 5 - Analisar Similaridade Baseada em Conhecimento
	 * Aplica a função de similaridade de conhecimento Lin em um ArrayList<String>
	 * de palavras comparando todas com todas. A linha da palavra será
	 * seu índice.
	 * @param palavras - ArrayList de Strings
	 * @return Matriz double[][] com resultados par a par
	 * @author Renata Padilha, Fhabiana Machado
	 * @since 15 de agosto de 2018
	 */
	public static double[][] aplicaLin(ArrayList<String> palavras){
		
		//LOG
		System.out.printf("\n\tGerando matriz com base no conhecimento...");
		
		int t = palavras.size();
		double lin[][] = new double[t][t];
		
		ILexicalDatabase db = new NictWordNet();
		WS4JConfiguration.getInstance().setMFS(false);
		
		for (int i = 0; i < palavras.size(); i++) {
            for (int j = 0; j < palavras.size(); j++) {
            	
//            	System.out.println("Matriz["+i+"]["+j+"]");
            	
            	if (i==j) {
            		lin[i][j]=1;
            	} else {
					lin[i][j] = new Lin(db).calcRelatednessOfWords(palavras.get(i), palavras.get(j));
            	}
            	
            }
		}

		return lin;
	}

	/**
	 * Algoritmo 6 - Calcular Equivalência
	 * Calcula a equivalência a partir das 3 matrizes de resultados das demais técnicas
	 * @param 
	 * @return Única matriz double com o resultado
	 * @author Renata Padilha, Fhabiana Machado
	 * @since 15 de agosto de 2018
	 */
	public static double[][] calculaEquivalencia(int[][] radical, double[][] lev, double[][] lin, int tamanho, double PONTO_CORTE, double PONTO_CORTE_AHP){

		//LOG
		System.out.printf("\n\tCalculando equivalência...");
	
		int i, j, count=0;
		final int pesoA=1, pesoB=3, pesoC=2;
		double aux=0, media_ponderada=0;
		double resultado[][]= new double[tamanho][tamanho];

		for (i=0; i<tamanho; i++){
			for (j=i; j<tamanho; j++){

				//1º caso
				if ((lev[i][j] == 1.0) || (radical[i][j] == 1.0) || (lin[i][j] == 1.0)){
					resultado[i][j] = 1;
				} else {
					//2º caso
					if (lev[i][j] == 0 && radical[i][j] == 0 && lin[i][j] == 0)	{
						resultado[i][j] = 0;
					} else {
						count=0;
						//3º caso
						if (lev[i][j] >0.0 && lev[i][j] < 1.0) {
							aux = lev[i][j];
							count++;
						} 
					
						if (radical[i][j] >0.0 && radical[i][j] < 1.0){
							aux = radical[i][j];
							count++;
						}
								
						if (lin[i][j] >0.0 && lin[i][j] < 1.0){
							aux = lin[i][j];
							count++;
						}
						
						if (count==1){
							//se valor maior que ponto de corte(0.7)
							resultado[i][j] = (aux > PONTO_CORTE) ? 1 : 0;

						} else {
							if (count>1){
								//4º caso valores entre 0 < x < 1
								media_ponderada = (pesoA*radical[i][j] + pesoB*lev[i][j] + pesoC*lin[i][j])/6;
								resultado[i][j] = (media_ponderada > PONTO_CORTE_AHP) ? 1 : 0;
							}
						}
					
					}
				}
			} // fim for coluna
		} // fim for linha	

	return resultado;
	}

	/**
	 * Algoritmo 7 - Consolidar Estrutura Apaga as demais ocorrências do campo que
	 * estão escritas de forma distinta e as guarda na lista de referências
	 *
	 * @param resultado - matriz única
	 * @return campos consolidados.txt
	 * @author Ezequiel Ribeiro, Fhabiana Machado
	 * @return listaReferencias2 - Lista de strings com palavra[0] e equivalencia[1]
	 * @since 04 de agosto de 2019
	 */
	public static List<String[]> consolidaEstrutura(double[][] resultado, ArrayList<String> palavras,
		String caminho) throws IOException {

		//LOG
		System.out.printf("\n\tConsolidando estrutura...");

		ArrayList<String> aRemover = new ArrayList<String>();
		List<String[]> listaReferencias2 = new ArrayList<>();
		int k, l;
		int tam = resultado.length;

		//Consolida todas as palavras da coluna, logo, coluna = ArrayList palavras

		try {
			FileWriter writer = new FileWriter(caminho);
			
			writer.write("\n");
			for (k=0; k<tam ; k++){
				for (l=0;l<tam;l++){
					
					String[] equivalencias = new String[2];

					//Para todo elemento acima da diagonal principal
					if ((l>k) && (k != l)){
						if (resultado[k][l] == 1.0){
							writer.write("A palavra "+palavras.get(k)+" é equivalente a "+palavras.get(l)+"\n");
							aRemover.add(palavras.get(l));
							
							equivalencias[0] = palavras.get(k);
							equivalencias[1] = palavras.get(l);
							listaReferencias2.add(equivalencias);

						}					
					}
				}
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println();
		for (String s : aRemover) {
			palavras.remove(s);
			// System.out.println("Removendo palavra "+s);
		}

		// return new List[] {palavras, listaReferencias2};
		return listaReferencias2;

	}

	/**
	 * Algoritmo 8 - Remontar Estrutura
	 * Monta um novo documento com base no documento de origem com maior número de blocos
	 * @param Campos consolidados, lista de referências 1 e lista de referências 2
	 * @return Estrutura consolidada
	 * @author Fhabiana Machado
	 * @since 21 de agosto de 2019
	 */
	public static void remontarEstrutura(String jsonDir, String destino, ArrayList<String> palavras, List<String[]> listaReferencias2)
		throws FileNotFoundException, IOException {
		
		//LOG
		System.out.printf("\n\tRemontando estrutura...\n");

		//Escolhe o documento de origem para referência (com maior número de blocos)
		String docReferencia = jsonDir+"\\";
		docReferencia += UtilJSON.arquivoComMaisBlocos(jsonDir);

		//Transforma palavras em campos consolidados
		List<List<ElementoBloco>> camposConsolidados = new ArrayList<>();
		List<ElementoBloco> l = new ArrayList<>();
		
		for (String p : palavras) {
			ElementoBloco e = new ElementoBloco();
			e.setNome(p);
			l.add(e);
                        //System.out.println("Convertendo "+p);
		}
		camposConsolidados.add(l);


		ElementoBloco raiz = new ElementoBloco("RAIZ", ElementoBloco.OBJETO);

        //Verifica se o documento tem um objeto raiz dos demais
        if (UtilJSON.temUnicoObjetoRaiz(docReferencia)) {
            // Se sim, seta o nome do raiz com o nome do primeiro objeto
            FileInputStream fi = new FileInputStream( new File(docReferencia) );
            JsonParser parser = Json.createParser(fi);
			Event evt = null;
			
            while (parser.hasNext()) {
                evt = parser.next();
                if (evt == Event.KEY_NAME) {
                    raiz.setNome(parser.getString());
                    parser.close();
                    fi.close();
                    break;
                }
			}
			
            UtilJSON.montaArvore(raiz, listaReferencias2, camposConsolidados, docReferencia);
        } else {
            // Chama a função recursiva para formar a árvore a partir da raiz dada
            UtilJSON.montaArvore(raiz, listaReferencias2, camposConsolidados, docReferencia);
        }
		
		UtilJSON.gravarEstruturaConsolidada(raiz, destino);

	}

}
