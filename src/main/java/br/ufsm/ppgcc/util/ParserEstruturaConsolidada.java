/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsm.ppgcc.util;

import br.ufsm.ppgcc.model.estruturas.NodoEstruturaConsolidada;
import java.util.Stack;

/**
 * Classe para carregar e interpretar a estrutura consolidada
 * aplica as regras definidas, gerando uma árvore de n-filhos
 * 
 * @author ezequielrr
 */
public class ParserEstruturaConsolidada {

    private final static char ESPACO = ' ';
    private final static char FIM_PALAVRA = ';';
    private final static char INI_CLASSE = '{';
    private final static char FECHA_CLASSE = '}';
    private final static char INI_ARRAY = '[';
    private final static char FECHA_ARRAY = ']';
    private final static char INI_ARRAY_OBJ = '?';
    private final static char FECHA_ARRAY_OBJ = '*';
    private final static char PALAVRA = '~';

    private final static int NODOS_ABERTOS = 0;
    private final static int NODOS_FECHADOS = 1;

    private boolean leituraPalavra;
    private final Stack<String> bufferPalavras;
    private char bufferSimbolos;
    private StringBuilder palavraAtual;
    private final Stack[] pilha;
    private NodoEstruturaConsolidada nodoExibicao;
    private NodoEstruturaConsolidada nodoAtual;

    public ParserEstruturaConsolidada() {
        pilha = new Stack[2];
        pilha[NODOS_ABERTOS] = new Stack<>();
        pilha[NODOS_FECHADOS] = new Stack<>();
        nodoExibicao = null;
        palavraAtual = new StringBuilder();
        bufferPalavras = new Stack<>();
    }

    public void lerCaractere(char c) {

        switch (c) {
            case ESPACO:
                if (leituraPalavra) {
                    bufferSimbolos = PALAVRA;
                    palavraAtual.append(c);
                }
                break;
            case '\n':
                break;
            case FIM_PALAVRA:
                if (leituraPalavra) {
                    bufferSimbolos = FIM_PALAVRA;
                    leituraPalavra = false;
                    adaptarNotacao(FIM_PALAVRA);
                }
                break;
            case INI_CLASSE:
                if (bufferSimbolos == INI_ARRAY) {
                    adaptarNotacao(INI_ARRAY_OBJ);
                } else {
                    adaptarNotacao(INI_CLASSE);
                }
                break;
            case FECHA_CLASSE:
                bufferSimbolos = FECHA_CLASSE;
                adaptarNotacao(FECHA_CLASSE);
                break;
            case INI_ARRAY:
                bufferSimbolos = INI_ARRAY;
                break;
            case FECHA_ARRAY:
                if (bufferSimbolos != FECHA_CLASSE) {
                    adaptarNotacao(INI_ARRAY);
                    adaptarNotacao(FECHA_ARRAY);
                }
                break;
            default:
                leituraPalavra = true;
                bufferSimbolos = PALAVRA;
                palavraAtual.append(c);
                break;
        }
    }

    private void adaptarNotacao(char simbolo) {
        NodoEstruturaConsolidada novoNodo = null;
        switch (simbolo) {
            case FIM_PALAVRA:
                if (bufferPalavras.empty()) {
                    bufferPalavras.push(palavraAtual.toString().trim());
                } else {
                    nodoAtual.addAtributo(bufferPalavras.pop());
                    bufferPalavras.push(palavraAtual.toString().trim());
                }
                palavraAtual = new StringBuilder();
                break;
            case INI_CLASSE:
                novoNodo = new NodoEstruturaConsolidada(bufferPalavras.pop());
                if (nodoAtual != null) {
                    nodoAtual.addFilho(novoNodo);
                    nodoAtual.addRelacionamento(NodoEstruturaConsolidada.ZERO_UM);
                    pilha[NODOS_ABERTOS].push(nodoAtual);
                } else {
                    novoNodo.setRaiz(true);
                }
                nodoAtual = novoNodo;
                break;
            case FECHA_CLASSE:
                if (!bufferPalavras.empty()) {
                    nodoAtual.addAtributo(bufferPalavras.pop());
                }
                if (!pilha[NODOS_ABERTOS].empty()) {
                    pilha[NODOS_FECHADOS].push(nodoAtual);
                    nodoAtual = (NodoEstruturaConsolidada) pilha[NODOS_ABERTOS].pop();
                } else {
                    pilha[NODOS_FECHADOS].push(nodoAtual);
                }
                break;
            case INI_ARRAY:
                novoNodo = new NodoEstruturaConsolidada(bufferPalavras.pop());
                novoNodo.addAtributo("att");
                nodoAtual.addFilho(novoNodo);
                nodoAtual.addRelacionamento(NodoEstruturaConsolidada.ZERO_MUITOS);
                break;
            case FECHA_ARRAY:
                break;
            case INI_ARRAY_OBJ:
                novoNodo = new NodoEstruturaConsolidada(bufferPalavras.pop());
                nodoAtual.addFilho(novoNodo);
                nodoAtual.addRelacionamento(NodoEstruturaConsolidada.ZERO_MUITOS);
                pilha[NODOS_ABERTOS].push(nodoAtual);
                nodoAtual = novoNodo;
                break;
            case FECHA_ARRAY_OBJ:
                pilha[NODOS_FECHADOS].push(nodoAtual);
                nodoAtual = (NodoEstruturaConsolidada) pilha[NODOS_ABERTOS].pop();
                break;
            default:
                break;
        }
    }

    public NodoEstruturaConsolidada getNodoEstruturaConsolidada() throws Exception {
        if (pilha[NODOS_ABERTOS].empty() && nodoExibicao == null) {
            nodoExibicao = (NodoEstruturaConsolidada) pilha[NODOS_FECHADOS].pop();
        } else {
            if (!pilha[NODOS_ABERTOS].empty()) {
                throw new Exception("Processo ainda não concluído!");
            }
        }

        return this.nodoExibicao;
    }
}
