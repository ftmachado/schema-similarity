package br.ufsm.ppgcc.algoritmos;

import java.io.File;
import java.io.FileInputStream;

import br.ufsm.ppgcc.model.estruturas.ElementoBloco;
import br.ufsm.ppgcc.model.dao.ListasDAO;
import br.ufsm.ppgcc.model.dao.EstruturaConsolidadaDAO;
import br.ufsm.ppgcc.util.InfoJSON;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 *
 * @author ezequielrr
 */
public class RemontarEstrutura {

    private final Queue<List<ElementoBloco>> filaBlocos;

    public RemontarEstrutura() {
        this.filaBlocos = new LinkedList<>();
    }

    public void remontarUnicoBloco() throws FileNotFoundException, IOException {
        ListasDAO l = new ListasDAO();
        List<String[]> listaReferencias2 = l.lerListaReferencias2();
        List<List<ElementoBloco>> listaConsolidada
                = l.lerListaCamposConsolidados();
        List<String> visitados = new ArrayList<>();
        EstruturaConsolidadaDAO e = new EstruturaConsolidadaDAO();

        String arquivo = arquivoComMaisBlocos();
        InfoJSON info = new InfoJSON(arquivo);
        ElementoBloco raiz = new ElementoBloco("RAIZ", ElementoBloco.OBJETO);

        //Verifica se o documento tem um objeto raiz dos demais
        if (info.temUnicoObjetoRaiz()) {
            // Se sim, seta o nome do raiz com o nome do primeiro objeto
            File f = new File(arquivo);
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
            // Chama a função recursiva para formar a árvore 
            // a partir da raiz dada
            montaArvore(raiz, listaReferencias2, listaConsolidada, info, visitados);
        }
        //System.out.println(info.getPalavrasObjetoArray("Author"));
        e.gravarEstruturaConsolidada(raiz);
    }

    /**
     * Método auxiliar para obter o arquvivo JSON com mais blocos
     */
    private String arquivoComMaisBlocos() throws FileNotFoundException, IOException {
        List<String> arquivos = getListaArquivos();
        int qtdeBlocos = 0;
        int qtdeBlocosMax = 0;
        int iMax = 0;
        if (arquivos.size() > 0) {
            InfoJSON info = new InfoJSON(arquivos.get(0));
            qtdeBlocos = qtdeBlocosMax = info.numeroBlocosDocumento();
            for (int i = 1; i < arquivos.size(); i++) {
                info = new InfoJSON(arquivos.get(i));
                qtdeBlocos = info.numeroBlocosDocumento();
                if (qtdeBlocos > qtdeBlocosMax) {
                    qtdeBlocosMax = qtdeBlocos;
                    iMax = i;
                }
            }
            return arquivos.get(iMax);
        }
        return null;
    }

    /**
     * Método auxiliar para "detectar" os arquivos presentes. o nome dos
     * arquivos para serem encontrados deve ser docX.json, sendo X o número do
     * arquivo
     */
    private List<String> getListaArquivos() {
        int i = 1;
        List<String> listaArquivos = new ArrayList<>();
        while (true) {
            String strNomeArq = "artefatos-entrada/doc" + i + ".json";
            File f = new File(strNomeArq);
            if (!f.exists()) {
                break;
            }
            listaArquivos.add(strNomeArq);
            i++;
        }
        return listaArquivos;
    }

    /**
     * Método auxiliar para remontar a estrutura do JSON segundo um único bloco
     */
    private void montaArvore(ElementoBloco raiz,
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

    private String getTermoConsolidado(String entrada,
            List<List<ElementoBloco>> listaConsolidada,
            List<String[]> listaReferencias2) {
        if (!estaNaListaConsolidada(entrada, listaConsolidada)) {
            String nomeT = obterTermoEquivalente(entrada, listaReferencias2);
            return nomeT;
        }
        return entrada;
    }

    /**
     * Método auxiliar para obter o termo equivalente na lista de referências 2,
     * usada quando o termo não é encontrado na lista consolidada. Caso o termo
     * não seja localizado, retorna null.
     */
    private String obterTermoEquivalente(String termo, List<String[]> listaReferencias2) {
        for (String[] item : listaReferencias2) {
            if (item[0].equalsIgnoreCase(termo)) {
                return item[1];
            } else if (item[1].equalsIgnoreCase(termo)) {
                return item[0];
            }
        }
        return null;
    }

    private boolean estaNaLista(String termo, List<String> lista) {
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
    private boolean estaNaListaConsolidada(String termo,
            List<List<ElementoBloco>> listaConsolidados) {
        for (List<ElementoBloco> lista : listaConsolidados) {
            for (ElementoBloco elemento : lista) {
                if (elemento.getNome().equalsIgnoreCase(termo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void remontarPorBlocos() {
        try {
            //Carrega os artefatos de entrada
            ListasDAO l = new ListasDAO();
            //Lista de referencias 1
            List<List<String>> listaRef1 = l.lerListaReferencias1();
            //Lista de referencias 2
            List<String[]> listaRef2 = l.lerListaReferencias2();
            //Configuracoes especialista
            List<List<String>> listaEspecialista = l.lerListaEspecialista();
            //Lista de campos consolidados
            List<List<ElementoBloco>> blocosCamposConsolidados = l.lerListaCamposConsolidados();
            
            // Remonta a estrutura
            List<ElementoBloco> blocoPrincipal = remontarPorBlocos(blocosCamposConsolidados, listaRef2, listaRef1, listaEspecialista);
            
            // Grava a estrutura em arquivo, como estrutura consolidada
            EstruturaConsolidadaDAO est = new EstruturaConsolidadaDAO();
            est.gravarEstruturaConsolidada(blocoPrincipal);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RemontarEstrutura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RemontarEstrutura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<ElementoBloco> remontarPorBlocos(
            List<List<ElementoBloco>> blocosCamposConsolidados,
            List<String[]> listaRef2, List<List<String>> listaRef1,
            List<List<String>> listaEspecialista) throws IOException {

        int i;
        for (i = blocosCamposConsolidados.size() - 1; i >= 0; i--) {
            List<ElementoBloco> elementos = blocosCamposConsolidados.get(i);
            for (int j = elementos.size() - 1; j >= 0; j--) {
                ElementoBloco eb = elementos.get(j);
                atualizaElementoBloco(eb, listaRef1, listaEspecialista, i);
                if (eb.getTipo() == ElementoBloco.OBJETO
                        || eb.getTipo() == ElementoBloco.ARR_OBJETO) {

                    if (!filaBlocos.isEmpty()) {
                        eb.setBlocoFilho(filaBlocos.remove());
                    } else {
                        eb.setTipo(ElementoBloco.ATRIBUTO);
                    }

                }
            }
            filaBlocos.add(elementos);
        }
        // Seta o primeiro nó como o raiz do objeto
        return filaBlocos.poll();
    }

    private void atualizaElementoBloco(ElementoBloco elem,
            List<List<String>> listaRef1, List<List<String>> especialista,
            int numeroBloco) throws IOException {

        int tipoElemento = this.getTipoBloco(elem.getNome(), listaRef1);
        if (tipoElemento == -1) {
            tipoElemento = this.getTipoBlocoEspecialista(elem.getNome(),
                    especialista, numeroBloco);
        }

        switch (tipoElemento) {
            case InfoJSON.T_ARRAY:
                elem.setTipo(ElementoBloco.ARRAY);
                break;
            case InfoJSON.T_ARRAY_OBJETO:
                elem.setTipo(ElementoBloco.ARR_OBJETO);
                break;
            case InfoJSON.T_CAMPO:
                elem.setTipo(ElementoBloco.ATRIBUTO);
                break;
            case InfoJSON.T_OBJETO:
                elem.setTipo(ElementoBloco.OBJETO);
                break;
            default:
                // Fazer a busca na estrutura que representa as escolhas
                // do especialista e descobrir o tipo do elemento em um dos
                // documentos apontados(fazer)
                break;
        }
    }

    private int getTipoBlocoEspecialista(String nomeElem,
            List<List<String>> listaEspecialista, int numeroBloco)
            throws FileNotFoundException, IOException {
        int tipoDado = 0;
        List<String> documentos = listaEspecialista.get(numeroBloco);
        for (String documento : documentos) {
            InfoJSON info = new InfoJSON("artefatos-entrada/"+documento);
            tipoDado = info.getTipoElemento(nomeElem);
            if (tipoDado != InfoJSON.T_NADA) {
                return tipoDado;
            }
        }
        return -1;
    }

    /**
     *
     */
    private int getTipoBloco(String nomeElem,
            List<List<String>> listaRef1) throws FileNotFoundException, IOException {
        for (List<String> referencia : listaRef1) {
            if (referencia.get(0).equalsIgnoreCase(nomeElem)) {
                InfoJSON info = new InfoJSON("artefatos-entrada/"+referencia.get(1));
                int tipo = info.getTipoElemento(nomeElem);
                return tipo;
            }
        }
        return -1;
    }

    private String getStringFile(ElementoBloco elemento) {
        String str = "";
        str += "{" + elemento.getNome() + ";" + elemento.getAbreDelimitador()
                + getStringArquivo(elemento.getBlocoFilho())
                + elemento.getFechaDelimitador() + "}";
        return str;
    }

    private String getStringArquivo(List<ElementoBloco> elementos) {
        String str = "";
        for (ElementoBloco e : elementos) {
            if (e.getTipo() != ElementoBloco.OBJETO && e.getTipo()
                    != ElementoBloco.ARR_OBJETO) {
                str += e.getNome() + ";" + e.getAbreDelimitador()
                        + e.getFechaDelimitador();
            } else {
                str += e.getNome() + ";" + e.getAbreDelimitador()
                        + getStringArquivo(e.getBlocoFilho())
                        + e.getFechaDelimitador();
            }
        }
        return str;
    }

}
