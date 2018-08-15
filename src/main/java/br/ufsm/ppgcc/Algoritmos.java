package br.ufsm.ppgcc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;



import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.stream.JsonParser;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.tartarus.martin.Stemmer;

public class Algoritmos {


	/** 
	* Algoritmo 1 - 
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
		
		int i=0;
		int n_arquivos=Util.numArquivosJsonPasta(caminho);
		
	
		try {
			JsonReader reader;

			FileWriter arq = new FileWriter("src/main/resources/artefatos/saidaAlg1.txt");
			PrintWriter gravarArq = new PrintWriter(arq);
			
			for (i=1; i<=n_arquivos; i++) {
				
				if (i!= 1) {
					gravarArq.println("-enddoc");
				}
			
				String file = caminho+"/doc"+i+".json";
//				System.out.println(file);
//				System.exit(0);
				
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
					
/*						case START_ARRAY: {
							gravarArq.println();
							gravarArq.printf("[");
							break;
						}
						case END_ARRAY: {
							gravarArq.printf("]");
							break;
						}
						
						case START_OBJECT: {
							gravarArq.println();
							gravarArq.printf("{");
							break;
						}
						case END_OBJECT: {
							gravarArq.printf("}");
							break;
						}
*/						case KEY_NAME:
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
	 * Algoritmo 2 -
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
		
		try {
			FileWriter arq = new FileWriter("src/main/resources/artefatos/listaRef1.txt");
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
	 * Algoritmo 3 -
	 * Aplica o algoritmo Porter Stemmer em um arquivo texto
	 * 
	 * @param origemArquivo - Caminho do arquivo de origem
	 * @return ArrayList de palavras com apenas o radical
	 * @author Fhabiana Machado
	 * @return 
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
	 * Algoritmo 3 -
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
			System.out.println(palavras.get(i).toString());
		}
		
		return palavras;
	
	}
	
	/**
	 * Algoritmo 3 - 
	 * Gera a matriz do Stemmer, o índice da palavra será a ordem dela no arquivo
	 * 
	 * @param palavras - ArrayList<String> com as palavras já extraídas o radical
	 * @author Fhabiana Machado
	 * @since 19 de fevereiro de 2018
	 * @return Matriz com 0 e 1
	 */
	public static int[][] geraMatrizStemmer(ArrayList<String> palavras){
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
	 * Algoritmo 4 -
	 * Aplica a função de similaridade Levenshtein em um ArrayList<String>
	 * de palavras comparando todas com todas. A linha da palavra será
	 * seu índice.
	 * @param palavras - ArrayList de Strings
	 * @return Matriz float[][] com resultados par a par
	 * @author Fhabiana Machado
	 * @since 02 de março de 2018
	 */
	public static float[][] aplicaLevenshtein(ArrayList<String> palavras) {
		int t = palavras.size();
		float lev[][] = new float[t][t];
		StringMetric metric = StringMetrics.levenshtein();
		
		for (int i = 0; i < palavras.size(); i++) {
            for (int j = 0; j < palavras.size(); j++) {
            	
            	System.out.println("Matriz["+i+"]["+j+"]");
            	
            	if (i==j) {
            		lev[i][j]=1;
            	} else {
            		lev[i][j] = metric.compare(palavras.get(i), palavras.get(j));
            	}
            	
            }
		}
     
		return lev;
	}

}
