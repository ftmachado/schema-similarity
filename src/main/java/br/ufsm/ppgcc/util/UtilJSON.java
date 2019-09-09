package br.ufsm.ppgcc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.stream.JsonParser;
import static javax.json.stream.JsonParser.Event.KEY_NAME;
import static javax.json.stream.JsonParser.Event;

import br.ufsm.ppgcc.model.estruturas.ElementoBloco;


public class UtilJSON {

    public static final int T_CAMPO = 0;
    public static final int T_OBJETO = 1;
    public static final int T_ARRAY = 2;
    public static final int T_ARRAY_OBJETO = 3;
    public static final int T_NADA = 4;

    public static FileInputStream fi;
    public static JsonParser parser;

    /**
	 * Obtem o número de blocos de um documento
     * @param <FileInputStream>
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
			qtdeBlocos = qtdeBlocosMax = UtilJSON.numeroBlocosDocumento(jsonDir+"\\"+arquivos[0]);
			
			for (String x : arquivos) {
				qtdeBlocos = UtilJSON.numeroBlocosDocumento(jsonDir+"\\"+x);
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
                + UtilJSON.getStringEstruturaConsolidada(elemento.getBlocoFilho())
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
     * Método que lista todos os nomes de elementos do arquivo
     * (atributos, objetos, arrays)
     * @return Lista com nomes de elementos
     * @throws java.io.IOException
     */
    public static List<String> getPalavrasArquivo(String file) throws IOException {
        
        List<String> listaPalavrasDoc = new ArrayList<>(0);
        FileInputStream fi = new FileInputStream( new File(file) );
		JsonParser parser = Json.createParser(fi);

        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == KEY_NAME) {
                listaPalavrasDoc.add(parser.getString());
            }
        }
        
        fi.close();
        parser.close();

        return listaPalavrasDoc;
    }

    /**
     * Inicializa objetos
     */
    public static void init(String nomeArquivo) throws FileNotFoundException {
        fi = new FileInputStream( new File(nomeArquivo) );
        parser = Json.createParser(fi);
    }

    /**
     * Finaliza
     * @throws IOException
     */
    public static void finaliza() throws IOException {
        if (fi != null) {
            fi.close();
            parser.close();
            fi = null;
            parser = null;
        }
    }

    /**
     * Método auxiliar para retornar o tipo do elemento
     */
    public static int getTipoElemento(String elemento, String arquivo) throws IOException {

        init(arquivo);        
        int tipo = T_NADA;

        while (parser.hasNext()) {
            Event event = parser.next();
            
            if (event == KEY_NAME) {

                if (parser.getString().equals(elemento)) {
                    if (parser.hasNext()) {
                        Event proxEv = parser.next();

                        if (proxEv == Event.VALUE_STRING
                                || proxEv == Event.VALUE_NUMBER
                                || proxEv == Event.VALUE_FALSE
                                || proxEv == Event.VALUE_NULL
                                || proxEv == Event.VALUE_TRUE) {
                            
                            tipo = T_CAMPO;

                        } else if (proxEv == Event.START_OBJECT) {
                            
                            tipo = T_OBJETO;

                        } else if (proxEv == Event.START_ARRAY) {
                            
                            if (parser.hasNext()) {
                                proxEv = parser.next();
                                if (proxEv == Event.START_OBJECT) {
                                    tipo = T_ARRAY_OBJETO;
                                } else {
                                    tipo = T_ARRAY;
                                }
                            }

                        }
                    }
                }

            }

        }

        finaliza();
        return tipo;

    }

    /**
     * Lista o nome de todos elementos que pertençam a um objeto
     * @author ezequielr
     */
    public static List<String> getPalavrasObjeto(String nomeObjeto, String arquivo) throws IOException {
        
        finaliza();
        init(arquivo);
        
        List<String> listaPalavrasObj = new ArrayList<>(0);
//        System.out.println("Procurando "+nomeObjeto);

        int nivel = 0;
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == KEY_NAME) {
//                System.out.println("N0 "+event);
//                System.out.println("Elem "+parser.getString());
                // Se o nome conferir com o nome de objeto procurado
                if(parser.getString().equalsIgnoreCase(nomeObjeto)) {
                    event = parser.next();
//                    System.out.println("N1 "+event);
                    // Se for um objeto
                    if (event == Event.START_OBJECT) {
                        while (parser.hasNext()) {
                            event = parser.next();
//                            System.out.println("N2 "+event);
                            // Se encontrar outro objeto aninhado, eleva o nível
                            if(event == Event.START_OBJECT) {
                                nivel++;
                            } else if (event == KEY_NAME) {
                                // Adiciona apenas as palavras pertencentes
                                // ao mesmo nível do objeto (nivel == 0)
                                if(nivel == 0) {
                                    listaPalavrasObj.add(parser.getString());
                                }
                            } else if (event == Event.END_OBJECT) {
                                //Encerra apenas se for o fim do nível
                                if (nivel == 0) {
                                    finaliza();
                                    return listaPalavrasObj;
                                } else {
                                    // Senão, reduz a variável e segue
                                    // a leitura
                                    nivel--;
                                }
                            }
                        }
                    }
                }
            }
        }
        finaliza();
        return null;

    }

    /**
     * Método para listar todos os itens pertencentes a um array de objetos
     * trata todos os elementos dos diferentes objetos juntos, ou seja, lista 
     * todos os elementos de todas as posições do array, gerando uma lista com
     * repetições de nomes
     */
    public static List<String> getPalavrasObjetoArray(String nomeObjeto, String arquivo) throws IOException {
        
        init(arquivo);
        
        List<String> listaPalavrasObj = new ArrayList<>(0);
        
        int nivel = 0;
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == KEY_NAME) {
                // Se o nome conferir com o nome de objeto procurado
                if(parser.getString().equalsIgnoreCase(nomeObjeto)) {
                    event = parser.next();
                    if(event == Event.START_ARRAY) {
                        event = parser.next();
                        if(event == Event.START_OBJECT) {
                            while (parser.hasNext()) {
                                event = parser.next();
                                // Se encontrar outro objeto aninhado, eleva o nível
                                if(event == Event.START_OBJECT) {
                                    nivel++;
                                } else if (event == KEY_NAME) {
                                    // Adiciona apenas as palavras pertencentes
                                    // ao mesmo nível do objeto (nivel == 0)
                                    if(nivel == 0) {
                                        listaPalavrasObj.add(parser.getString());
                                    }
                                } else if (event == Event.END_OBJECT) {
                                    //Encerra apenas se for o fim do nível
                                    if (nivel == 0) {
                                        event = parser.next();
                                        if(event == Event.END_ARRAY) {
                                            finaliza();
                                            return listaPalavrasObj;
                                        } else if (event == Event.START_OBJECT) {
                                            continue;
                                        }
                                    } else {
                                        // Senão, reduz a variável e segue
                                        // a leitura
                                        nivel--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Método auxiliar para busca na lista consolidada
     */
    public static boolean estaNaListaConsolidada(String termo,List<List<ElementoBloco>> listaConsolidados) {
        for (List<ElementoBloco> lista : listaConsolidados) {
            for (ElementoBloco elemento : lista) {
                if (elemento.getNome().equalsIgnoreCase(termo)) {
                    return true;
                }
            }
        }
        return false;
    }

	/**
     * Método auxiliar para remontar a estrutura do JSON segundo um único bloco
     * @param raiz, listaReferencias2, listaConsolidada, docReferencia
	 * @author ezequielrr
     */
    public static void montaArvore(ElementoBloco raiz, List<String[]> listaReferencias2,
        List<List<ElementoBloco>> listaConsolidada, String docReferencia) throws IOException {

        List<String> visitados = new ArrayList<>();
        Stack<ElementoBloco> pilhaAbertos = new Stack<>();
        Stack<ElementoBloco> pilhaFechados = new Stack<>();
        ElementoBloco atual = raiz;
        List<String> palavrasDoArquivo = getPalavrasArquivo(docReferencia);
        List<ElementoBloco[]> avaliar = new ArrayList<>();

        //init(docReferencia);
        int totalcontador = palavrasDoArquivo.size();
        int contador = 1;
        System.out.println("Total de palavras no doc = total de iterações "+totalcontador);

        for (String palavra : palavrasDoArquivo) {
                        
            // Para "pular" o nodo raiz
            if (palavra.equalsIgnoreCase(raiz.getNome())) {                
                continue;
            }
            
            /*
            /* Verifica se a palavra está consolidada senão procura o termo equivalente
            */
            String tPalavra = null;

            if (!estaNaListaConsolidada(palavra, listaConsolidada)) {
                
                for (String[] item : listaReferencias2) {
                    if (item[0].equalsIgnoreCase(palavra)) {
                        tPalavra = item[1];
                    } else if (item[1].equalsIgnoreCase(palavra)) {
                        tPalavra = item[0];
                    }
                }

            } else {
                tPalavra = palavra;
            }

            /*
             * Se for Objeto 
             */
            if (getTipoElemento(palavra, docReferencia) == T_OBJETO) {

                List<String> lp = new ArrayList<>();
                
                // Testa se for a raiz
                if (atual.getNome().equalsIgnoreCase(raiz.getNome())){
                    
                    if (atual.getTipo() == ElementoBloco.OBJETO) {
                        lp = getPalavrasObjeto(palavra, docReferencia);
                    } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                        lp = getPalavrasObjetoArray(palavra, docReferencia);
                    }
                    
                } else {
                    
                    if (atual.getTipo() == ElementoBloco.OBJETO) {
                        lp = getPalavrasObjeto(atual.getNome(), docReferencia);
                    } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                        lp = getPalavrasObjetoArray(atual.getNome(), docReferencia);
                    }

                }
                                
//                System.out.println("LP depois"+lp.toString());

                ElementoBloco novo = new ElementoBloco(palavra, ElementoBloco.OBJETO);
                novo.setNomeConsolidado(tPalavra);
                
                
                if (lp.contains(palavra)) {

                    atual.addBlocoFilho(novo);

                    /*
                    /* Adiciona palavra ao histórico de nodos visitados
                    /* Ou caso a palavra já tenha sido usada, marca o nodo para numa futura varredura avaliar se o exclui
                    /* ou mantém ("sem nome"), numa estrutura que contém o nodo a ser avaliado e seu "pai"
                    */
                    if (!visitados.contains(tPalavra)) {
                        visitados.add(tPalavra);
                    } else {
                        ElementoBloco[] avalia = {atual, novo};
                        avaliar.add(avalia);
                    }

                } else {
                    
                    /*
                    /* Verifica antes se o objeto atual não tem atributos
                    /* Caso este não tenha, é alterado para Atributo antes de ser "descartado"
                    */
                    if (atual.getBlocoFilho().isEmpty()) {
                        atual.setTipo(ElementoBloco.ATRIBUTO);
                    }

                    // Vai descendo de nivel ate encontrar o nivel que contenha o elemento
                    while (!pilhaAbertos.empty()) {

                        // "Descarta" o nodo recém setado como atributo
                        if (atual.getTipo() != ElementoBloco.ATRIBUTO) {
                            pilhaFechados.push(atual);
                        }
                        atual = pilhaAbertos.pop();

                        if (atual.getTipo() == ElementoBloco.OBJETO) {
                            lp = getPalavrasObjeto(atual.getNome(), docReferencia);
                        } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                            lp = getPalavrasObjetoArray(atual.getNome(), docReferencia);
                        }

                        if (lp.contains(palavra)) {

                            atual.addBlocoFilho(novo);

                            /*
                            /* Adiciona palavra ao histórico de nodos visitados
                            /* Ou caso a palavra já tenha sido usada, marca o nodo para numa futura varredura avaliar se o exclui
                            /* ou mantém ("sem nome"), numa estrutura que contém o nodo a ser avaliado e seu "pai"
                            */
                            if (!visitados.contains(tPalavra)) {
                                visitados.add(tPalavra);
                            } else {
                                ElementoBloco[] avalia = {atual, novo};
                                avaliar.add(avalia);
                            }
                            break;

                        }
                    }
                }

                // Deve empilhar o objeto pai na pilha de abertos e setar como atual o novo objeto
                pilhaAbertos.push(atual);
                atual = novo;

            /*
             * Se for Array Objeto 
             */
            } else if (getTipoElemento(palavra, docReferencia) == T_ARRAY_OBJETO) {

                List<String> lp = new ArrayList<>();
                if (atual.getTipo() == ElementoBloco.OBJETO) {
                    lp = getPalavrasObjeto(atual.getNome(), docReferencia);
                } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                    lp = getPalavrasObjetoArray(atual.getNome(), docReferencia);
                }

                ElementoBloco novo = new ElementoBloco(palavra, ElementoBloco.ARR_OBJETO);
                novo.setNomeConsolidado(tPalavra);
                
                if (lp.contains(palavra)) {
                    atual.addBlocoFilho(novo);
                    
                    if (!visitados.contains(tPalavra)) {
                        visitados.add(tPalavra);
                    }

                } else {
                    /*
                    /* Verifica antes se o objeto atual não tem atributos
                    /* Caso este não tenha, é alterado para Atributo antes de ser "descartado"
                    */
                    if (atual.getBlocoFilho().isEmpty()) {
                        atual.setTipo(ElementoBloco.ATRIBUTO);
                    }

                    // Vai descendo de nivel ate encontrar o nivel que contenha o elemento
                    while (!pilhaAbertos.empty()) {
                        
                        // "Descarta" o nodo recém setado como atributo
                        if (atual.getTipo() != ElementoBloco.ATRIBUTO) {
                            pilhaFechados.push(atual);
                        }
                        atual = pilhaAbertos.pop();
                        
                        // Se for a raiz finaliza
                        if (atual.getNome().equalsIgnoreCase(raiz.getNome())) {
                            System.out.println("RAIZ break "+contador+"/"+totalcontador);
                            contador++;
//                            pilhaAbertos.clear();
                            break;
                        }
                        
                        if (atual.getTipo() == ElementoBloco.OBJETO) {
                            lp = getPalavrasObjeto(atual.getNome(), docReferencia);
                        } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                            lp = getPalavrasObjetoArray(atual.getNome(), docReferencia);
                        }
                        
                        if (!lp.isEmpty()) {
                            if (lp.contains(palavra)) {
                                atual.addBlocoFilho(novo);

                                if (!visitados.contains(tPalavra)) {
                                    visitados.add(tPalavra);
                                }

                                break;
                            }
                        }
                        
                    }
                }
                // Deve empilhar o objeto pai na pilha de abertos setar como atual o novo objeto
                pilhaAbertos.push(atual);
                atual = novo;
                
            /*
             * Se for Campo
             */
            } else if (getTipoElemento(palavra, docReferencia) == T_CAMPO) {
                
                if (visitados.contains(palavra)) {
                    continue;
                }

                List<String> lp = new ArrayList<>();
                
                if (atual.getTipo() == ElementoBloco.OBJETO) {
                    lp = getPalavrasObjeto(atual.getNome(), docReferencia);
                } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                    lp = getPalavrasObjetoArray(atual.getNome(), docReferencia);
                }

                ElementoBloco novo = new ElementoBloco(palavra, ElementoBloco.ATRIBUTO);
                novo.setNomeConsolidado(tPalavra);

                if (!lp.isEmpty()) {
                    
                    if (lp.contains(palavra)) {

                        if (!visitados.contains(tPalavra)) {
                            atual.addBlocoFilho(novo);
                            visitados.add(tPalavra);
                        }
                    }

                } else {
                    /*
                    /* Verifica antes se o objeto atual não tem atributos
                    /* Caso este não tenha, é alterado para Atributo antes de ser "descartado"
                    */
                    if (atual.getBlocoFilho().isEmpty()) {
                        atual.setTipo(ElementoBloco.ATRIBUTO);
                    }

                    // Vai descendo de nivel ate encontrar o nivel que contenha o elemento
                    while (!pilhaAbertos.empty()) {

                        // "Descarta" o nodo recém setado como atributo
                        if (atual.getTipo() != ElementoBloco.ATRIBUTO) {
                            pilhaFechados.push(atual);
                        }
                        atual = pilhaAbertos.pop();

                        if (atual.getTipo() == ElementoBloco.OBJETO) {
                            lp = getPalavrasObjeto(atual.getNome(), docReferencia);
                        } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                            lp = getPalavrasObjetoArray(atual.getNome(), docReferencia);
                        }

                        if (lp.contains(palavra)) {
                            atual.addBlocoFilho(novo);
                            visitados.add(tPalavra);
                            break;
                        }

                    }
                }
            }
        }

        /*
        /* É feita uma varredura final
        */
        for (ElementoBloco[] eb : avaliar) {

            // remover os nodos excedentes (convertidos em atributos mas são repetidos
            if (eb[1].getTipo() == ElementoBloco.ATRIBUTO) {

                eb[0].apagaBlocoFilho(eb[1]);
            
            } else if (eb[1].getNomeConsolidado().equalsIgnoreCase(eb[0].getNomeConsolidado())) {

                // filhos com o mesmo nome do pai - Transferência para o pai dos filhos do filho
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

        /*
        /* Adicionar todos os termos consolidados que não estejam na estrutura
        /* formada em um nó anexado à raiz
        */
        
        // Criação do nó
        Random gera = new Random();
        ElementoBloco extra = new ElementoBloco("e_" + gera.nextInt(),ElementoBloco.OBJETO);
        
        for (List<ElementoBloco> bloco : listaConsolidada) {
            for (ElementoBloco elem : bloco) {
                // Verifica se não está na lista de palavras utilizadas 
                if (!visitados.contains(elem.getNome())) {
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