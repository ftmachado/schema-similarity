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
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.json.Json;
import javax.json.stream.JsonParser;
import static javax.json.stream.JsonParser.Event.KEY_NAME;
import static javax.json.stream.JsonParser.Event;

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

	/**
	 * Obtem o número de blocos de um documento
	 * @param file - caminho do arquivo
	 * @return numBlocos - total de blocos do arquivo
	 */
	public static int numeroBlocosDocumento(String file) throws FileNotFoundException, IOException {
		int numBlocos = 0;

		FileInputStream fi = new FileInputStream( new File(file) );
		JsonParser parser = Json.createParser(fi);
		
		while(parser.hasNext()) {
			JsonParser.Event evt  = parser.next();
			
            if (evt == Event.START_OBJECT) {
                numBlocos++;
            }
		}
		
		fi.close();
		parser.close();
        return numBlocos;
	}

	/**
	 * Para um determinado diretório, faz um parse e retorna o nome do arquivo com maior número de blocos
	 * @param jsonDir - diretório com arquivos JSON
	 * @return arqMax - nome do arquivo com máximo de blocos
	 */
	public static String arquivoComMaisBlocos(String jsonDir) throws FileNotFoundException, IOException {

		String[] arquivos = new File(jsonDir).list();

        int qtdeBlocos = 0;
        int qtdeBlocosMax = 0;
		String arqMax ="";
		
        if (arquivos.length > 0) {
			
			// Inicializa com o primeiro doc
			qtdeBlocos = qtdeBlocosMax = Util.numeroBlocosDocumento(jsonDir+"\\"+arquivos[0]);
			
			for (String x : arquivos) {
				qtdeBlocos = Util.numeroBlocosDocumento(jsonDir+"\\"+x);
				if (qtdeBlocos > qtdeBlocosMax) {
                    qtdeBlocosMax = qtdeBlocos;
                    arqMax = x;
                }
			}

			// System.out.println("Máximo de blocos "+qtdeBlocosMax);
			return arqMax;
		}
        return null;
	}
	
	/**
     * Método para detectar casos onde o arquivo contém um objeto único
     * localizado na raiz do documento JSON
     */
	public static boolean temUnicoObjetoRaiz(String file) throws FileNotFoundException, IOException {
		
		FileInputStream fi = new FileInputStream( new File(file) );
		JsonParser parser = Json.createParser(fi);

        int nivel = 0;
        if (parser.hasNext()) {
            Event event = parser.next();
            event = parser.next();
            if (event == KEY_NAME) {
                if (parser.hasNext()) {
                    Event proxEv = parser.next();
                    if (proxEv == Event.VALUE_STRING
                            || proxEv == Event.VALUE_NUMBER
                            || proxEv == Event.VALUE_FALSE
                            || proxEv == Event.VALUE_NULL
                            || proxEv == Event.VALUE_TRUE
                            || proxEv == Event.START_ARRAY) {
						fi.close();
						parser.close();
                        return false;
					
					} else if (proxEv == Event.START_OBJECT) {
                        nivel++;
                        while (parser.hasNext()) {
                            JsonParser.Event evt = parser.next();
                            if (evt == Event.START_OBJECT) {
                                nivel++;
                            } else if (evt == Event.END_OBJECT) {
                                nivel--;
                            }
                            if (nivel == 0) {
                                proxEv = parser.next();
                                break;
                            }
                        }
                        if(nivel == 0 && proxEv == Event.END_OBJECT) {
                            if (parser.hasNext()) {
                                fi.close();
								parser.close();
                                return false;
                            } else {
                                fi.close();
								parser.close();
                                return true;
                            }                            
                        } else {
                            fi.close();
							parser.close();
                            return false;
                        }
                    }
                }
            }
		}
		
        fi.close();
		parser.close();
		return false;
		
	}
	
	/**
     * Grava a estrutura consolidada a partir de uma estrutura remontada
	 * @author ezequielrr
	 */
	public static void gravarEstruturaConsolidada(ElementoBloco elemento, String destino) throws IOException {
		
		FileWriter arq = new FileWriter(destino);
		PrintWriter gravarArq = new PrintWriter(arq);
		
		String str = "";
		String escNome = "";
		
		if (elemento.getNomeConsolidado() != null && !elemento.getNomeConsolidado().equals("")) {
			escNome = elemento.getNomeConsolidado();
		} else {
			escNome = elemento.getNome();
		}
		
        str += "" + escNome + ";" + elemento.getAbreDelimitador()
                + Util.getStringEstruturaConsolidada(elemento.getBlocoFilho())
                + elemento.getFechaDelimitador() + "";

        gravarArq.println(str);
        gravarArq.close();
		arq.close();
		
	}
	
	/**
     * Método auxiliar usado para tornar a lista remontada em uma string
     * de uma estrutura consolidada
	 * @author ezequielrr
     */
    private static String getStringEstruturaConsolidada(List<ElementoBloco> elementos) {
        String str = "";
        String escNome = "";

		for (ElementoBloco e : elementos) {

			escNome = ( e.getNomeConsolidado() != null && !e.getNomeConsolidado().equals("") ? e.getNomeConsolidado() : e.getNome() );

            if (e.getTipo() != ElementoBloco.OBJETO && e.getTipo()!= ElementoBloco.ARR_OBJETO) {
                str += escNome + ";" + e.getAbreDelimitador()+ e.getFechaDelimitador();
			
			} else {
                str += escNome + ";" + e.getAbreDelimitador()
                        + getStringEstruturaConsolidada(e.getBlocoFilho())
                        + e.getFechaDelimitador();
            }
		}
		
        return str;
	}

	/**
     * Método auxiliar para remontar a estrutura do JSON segundo um único bloco
	 * @author ezequielrr
     */
	private static void montaArvore(ElementoBloco raiz,	List<String[]> listaReferencias2,
	    List<List<ElementoBloco>> listaConsolidada,	List<String> visitadosConsolidados) throws IOException {

        Stack<ElementoBloco> pilhaAbertos = new Stack<>();
        Stack<ElementoBloco> pilhaFechados = new Stack<>();
        ElementoBloco atual = raiz;
        List<String> palavrasDoArquivo = infoDocOrigem.getPalavrasArquivo();
        List<ElementoBloco[]> avaliar = new ArrayList<>();

        for (String palavra : palavrasDoArquivo) {
            // Para "pular" o nodo raiz
            if (palavra.equalsIgnoreCase(raiz.getNome())) {
                continue;
            }
            String tPalavra = getTermoConsolidado(palavra, listaConsolidada, listaReferencias2);

            //Adiciona os objetos
            if (infoDocOrigem.getTipoElemento(palavra) == InfoJSON.T_OBJETO) {
                List<String> lp = new ArrayList<>();
                if (atual.getTipo() == ElementoBloco.OBJETO) {
                    lp = infoDocOrigem.getPalavrasObjeto(atual.getNome());
                } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                    lp = infoDocOrigem.getPalavrasObjetoArray(atual.getNome());
                }
                ElementoBloco novo = new ElementoBloco(palavra, ElementoBloco.OBJETO);
                novo.setNomeConsolidado(tPalavra);
                if (estaNaLista(palavra, lp)) {
                    atual.addBlocoFilho(novo);
                    // Adiciona palavra ao histórico de nodos visitados
                    if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                        visitadosConsolidados.add(tPalavra);
                    } else {
                        // Caso a palavra já tenha sido usada, marca o nodo para
                        // numa futura varredura avaliar se exclui o nodo ou mantém
                        // ("sem nome"), numa estrutura que contém o
                        // nodo a ser avaliado e seu "pai"
                        ElementoBloco[] avalia = {atual, novo};
                        avaliar.add(avalia);
                    }
                } else {
                    //Verifica antes se o objeto atual não tem atributos
                    // Caso este não tenha, é alterado para Atributo antes de
                    // Ser "descartado"
                    if (atual.getBlocoFilho().isEmpty()) {
                        atual.setTipo(ElementoBloco.ATRIBUTO);
                    }
                    // Vai descendo de nivel ate encontrar o nivel
                    // que contenha o elemento
                    while (!pilhaAbertos.empty()) {
                        // "Descarta" o nodo recém setado como atributo
                        if (atual.getTipo() != ElementoBloco.ATRIBUTO) {
                            pilhaFechados.push(atual);
                        }
                        atual = pilhaAbertos.pop();
                        if (atual.getTipo() == ElementoBloco.OBJETO) {
                            lp = infoDocOrigem.getPalavrasObjeto(atual.getNome());
                        } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                            lp = infoDocOrigem.getPalavrasObjetoArray(atual.getNome());
                        }
                        if (estaNaLista(palavra, lp)) {
                            atual.addBlocoFilho(novo);
                            // Adiciona palavra ao histórico de nodos visitados
                            if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                                visitadosConsolidados.add(tPalavra);
                            } else {
                                // Caso a palavra já tenha sido usada, marca o nodo para
                                // numa futura varredura avaliar se exclui o nodo ou mantém
                                // ("sem nome"), numa estrutura que contém o
                                // nodo a ser avaliado e seu "pai"
                                ElementoBloco[] avalia = {atual, novo};
                                avaliar.add(avalia);
                            }
                            break;
                        }
                    }
                }
                // Deve empilhar o objeto pai na pilha de abertos
                // Setar como atual o novo objeto
                pilhaAbertos.push(atual);
                atual = novo;
            } else if (infoDocOrigem.getTipoElemento(palavra) == InfoJSON.T_ARRAY_OBJETO) {
                List<String> lp = new ArrayList<>();
                if (atual.getTipo() == ElementoBloco.OBJETO) {
                    lp = infoDocOrigem.getPalavrasObjeto(atual.getNome());
                } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                    lp = infoDocOrigem.getPalavrasObjetoArray(atual.getNome());
                }
                ElementoBloco novo = new ElementoBloco(palavra, ElementoBloco.ARR_OBJETO);
                novo.setNomeConsolidado(tPalavra);
                if (estaNaLista(palavra, lp)) {
                    atual.addBlocoFilho(novo);
                    // Adiciona palavra ao histórico de nodos visitados
                    if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                        visitadosConsolidados.add(tPalavra);
                    }
                } else {
                    //Verifica antes se o objeto atual não tem atributos
                    // Caso este não tenha, é alterado para Atributo antes de
                    // Ser "descartado"
                    if (atual.getBlocoFilho().isEmpty()) {
                        atual.setTipo(ElementoBloco.ATRIBUTO);
                    }

                    // Vai descendo de nivel ate encontrar o nivel
                    // que contenha o elemento
                    while (!pilhaAbertos.empty()) {
                        // "Descarta" o nodo recém setado como atributo
                        if (atual.getTipo() != ElementoBloco.ATRIBUTO) {
                            pilhaFechados.push(atual);
                        }
                        atual = pilhaAbertos.pop();
                        if (atual.getTipo() == ElementoBloco.OBJETO) {
                            lp = infoDocOrigem.getPalavrasObjeto(atual.getNome());
                        } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                            lp = infoDocOrigem.getPalavrasObjetoArray(atual.getNome());
                        }
                        if (estaNaLista(palavra, lp)) {
                            atual.addBlocoFilho(novo);
                            // Adiciona palavra ao histórico de nodos visitados
                            if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                                visitadosConsolidados.add(tPalavra);
                            }

                            break;
                        }
                    }
                }
                // Deve empilhar o objeto pai na pilha de abertos
                // Setar como atual o novo objeto
                pilhaAbertos.push(atual);
                atual = novo;
            } else if (infoDocOrigem.getTipoElemento(palavra) == InfoJSON.T_CAMPO) {
                if (estaNaLista(tPalavra, visitadosConsolidados)) {
                    continue;
                }
                List<String> lp = new ArrayList<>();
                if (atual.getTipo() == ElementoBloco.OBJETO) {
                    lp = infoDocOrigem.getPalavrasObjeto(atual.getNome());
                } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                    lp = infoDocOrigem.getPalavrasObjetoArray(atual.getNome());
                }
                ElementoBloco novo = new ElementoBloco(palavra, ElementoBloco.ATRIBUTO);
                novo.setNomeConsolidado(tPalavra);
                if (estaNaLista(palavra, lp)) {
                    if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                        atual.addBlocoFilho(novo);
                        visitadosConsolidados.add(tPalavra);
                    }
                } else {
                    //Verifica antes se o objeto atual não tem atributos
                    // Caso este não tenha, é alterado para Atributo antes de
                    // Ser "descartado"
                    if (atual.getBlocoFilho().isEmpty()) {
                        atual.setTipo(ElementoBloco.ATRIBUTO);
                    }

                    // Vai descendo de nivel ate encontrar o nivel
                    // que contenha o elemento
                    while (!pilhaAbertos.empty()) {
                        // "Descarta" o nodo recém setado como atributo
                        if (atual.getTipo() != ElementoBloco.ATRIBUTO) {
                            pilhaFechados.push(atual);
                        }

                        atual = pilhaAbertos.pop();
                        if (atual.getTipo() == ElementoBloco.OBJETO) {
                            lp = infoDocOrigem.getPalavrasObjeto(atual.getNome());
                        } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                            lp = infoDocOrigem.getPalavrasObjetoArray(atual.getNome());
                        }
                        if (estaNaLista(palavra, lp)) {
                            atual.addBlocoFilho(novo);
                            visitadosConsolidados.add(tPalavra);
                            break;
                        }
                    }
                }
            }
        }

        // É feita uma varredura final
        for (ElementoBloco[] eb : avaliar) {
            // remover os nodos excedentes (convertidos em atributos mas são
            // repetidos
            if (eb[1].getTipo() == ElementoBloco.ATRIBUTO) {
                eb[0].apagaBlocoFilho(eb[1]);
            } else if (eb[1].getNomeConsolidado().equalsIgnoreCase(eb[0].getNomeConsolidado())) {
                // filhos com o mesmo nome do pai
                // Transferência para o pai dos filhos do filho
                List<ElementoBloco> tFilhos = eb[1].getBlocoFilho();
                List<ElementoBloco> tPai = eb[0].getBlocoFilho();
                tPai.addAll(tFilhos);
                //apagar o filho
                eb[0].apagaBlocoFilho(eb[1]);
            } else {
                // renomear os casos de filhos que tenham filhos consolidados
                Random gera = new Random();
                eb[1].setNomeConsolidado("e_" + gera.nextInt());
            }
        }

        // Adicionar todos os termos consolidados que não estejam na estrutura
        // formada em um nó anexado à raiz
        // Criação do nó
        Random gera = new Random();
        ElementoBloco extra = new ElementoBloco("e_" + gera.nextInt(),
                ElementoBloco.OBJETO);
        for (List<ElementoBloco> bloco : listaConsolidada) {
            for (ElementoBloco elem : bloco) {
                // Verifica se não está na lista de palavras utilizadas 
                if (!estaNaLista(elem.getNome(), visitadosConsolidados)) {
                    elem.setTipo(ElementoBloco.ATRIBUTO);
                    //Não incluir bloco raiz
                    if (!elem.getNome().equalsIgnoreCase(raiz.getNome())) {
                        extra.addBlocoFilho(elem);
                    }
                }
            }
        }
        //Anexa ao nodo raiz
        raiz.addBlocoFilho(extra);
}
	
}
