package br.ufsm.ppgcc.algoritmos;

import java.io.File;
import java.io.FileInputStream;

import br.ufsm.ppgcc.model.estruturas.ElementoBloco;
import br.ufsm.ppgcc.model.dao.ListasDAO;
import br.ufsm.ppgcc.model.dao.EstruturaConsolidadaDAO;
import br.ufsm.ppgcc.util.InfoJSON;
import br.ufsm.ppgcc.util.UtilJSON;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 * Algoritmo 8 - Remontar Estrutura
 * Monta um novo documento com base no documento de origem com maior número de blocos
 * @param Campos consolidados, lista de referências 1 e lista de referências 2
 * @return Estrutura consolidada
 * @author Ezequiel Ribeiro, Fhabiana Machado
 * @since 21 de agosto de 2019
 */
public class RemontarEstrutura {

    public static void remontar(String jsonDir, String arqLista2, String arqEstruturaUnificada, String arqCamposConsolidados) throws FileNotFoundException, IOException {
        
        ListasDAO l = new ListasDAO();
        List<String[]> listaReferencias2 = l.lerListaReferencias2(arqLista2);
        List<List<ElementoBloco>> listaConsolidada = l.lerListaCamposConsolidados(arqCamposConsolidados);
        List<String> visitados = new ArrayList<>();
        EstruturaConsolidadaDAO e = new EstruturaConsolidadaDAO();

        //Escolhe o documento de origem para referência (com maior número de blocos)
		String docReferencia = jsonDir+"\\";
		docReferencia += UtilJSON.arquivoComMaisBlocos(jsonDir);

        InfoJSON info = new InfoJSON(docReferencia);
        ElementoBloco raiz = new ElementoBloco("RAIZ", ElementoBloco.OBJETO);

        //Verifica se o documento tem um objeto raiz dos demais
        if (info.temUnicoObjetoRaiz()) {
            // Se sim, seta o nome do raiz com o nome do primeiro objeto
            File f = new File(docReferencia);
            FileInputStream fi = new FileInputStream(f);
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

            montaArvore(raiz, listaReferencias2, listaConsolidada, info, visitados);

        } else {
            // Chama a função recursiva para formar a árvore a partir da raiz dada
            montaArvore(raiz, listaReferencias2, listaConsolidada, info, visitados);
        }

        e.gravarEstruturaConsolidada(raiz, arqEstruturaUnificada);
    }

    /**
     * Método auxiliar para remontar a estrutura do JSON segundo um único bloco
     */
    private static void montaArvore(ElementoBloco raiz,
            List<String[]> listaReferencias2,
            List<List<ElementoBloco>> listaConsolidada,
            InfoJSON infoDocOrigem,
            List<String> visitadosConsolidados) throws IOException {

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
                    
                    /*
                    /* Adiciona palavra ao histórico de nodos visitados
                    /* Ou caso a palavra já tenha sido usada, marca o nodo para numa futura varredura avaliar se o exclui
                    /* ou mantém ("sem nome"), numa estrutura que contém o nodo a ser avaliado e seu "pai"
                    */
                    if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                        visitadosConsolidados.add(tPalavra);
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
                            lp = infoDocOrigem.getPalavrasObjeto(atual.getNome());
                        } else if (atual.getTipo() == ElementoBloco.ARR_OBJETO) {
                            lp = infoDocOrigem.getPalavrasObjetoArray(atual.getNome());
                        }

                        if (estaNaLista(palavra, lp)) {

                            atual.addBlocoFilho(novo);
                            
                            /*
                            /* Adiciona palavra ao histórico de nodos visitados
                            /* Ou caso a palavra já tenha sido usada, marca o nodo para numa futura varredura avaliar se o exclui
                            /* ou mantém ("sem nome"), numa estrutura que contém o nodo a ser avaliado e seu "pai"
                            */
                            if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                                visitadosConsolidados.add(tPalavra);
                            } else {
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

            /*
             * Se for Array Objeto 
             */
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
                    
                    if (!estaNaLista(tPalavra, visitadosConsolidados)) {
                        visitadosConsolidados.add(tPalavra);
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
                // Deve empilhar o objeto pai na pilha de abertos setar como atual o novo objeto
                pilhaAbertos.push(atual);
                atual = novo;

            /*
             * Se for Campo
             */
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
        ElementoBloco extra = new ElementoBloco("e_" + gera.nextInt(), ElementoBloco.OBJETO);
        
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

    private static boolean estaNaLista(String termo, List<String> lista) {
        
        for (String elemento : lista) {
            if (elemento.equalsIgnoreCase(termo)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Método auxiliar para busca na lista consolidada
     */
    private static boolean estaNaListaConsolidada(String termo, List<List<ElementoBloco>> listaConsolidados) {
        
        for (List<ElementoBloco> lista : listaConsolidados) {
            for (ElementoBloco elemento : lista) {
                if (elemento.getNome().equalsIgnoreCase(termo)) {
                    return true;
                }
            }
        }
        return false;

    }

}
