package br.ufsm.ppgcc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import static javax.json.stream.JsonParser.Event.KEY_NAME;

/**
 * Classe auxiliar para obter informações sobre elementos em um arquivo JSON
 *
 * @author ezequielrr
 */
public class InfoJSON {

    public static final int T_CAMPO = 0;
    public static final int T_OBJETO = 1;
    public static final int T_ARRAY = 2;
    public static final int T_ARRAY_OBJETO = 3;
    public static final int T_NADA = 4;

    private File f;
    private FileInputStream fi;
    private JsonParser parser;
    private String nomeArquivo;

    public InfoJSON(String nomeArquivo) throws FileNotFoundException {
        init(nomeArquivo);
    }

    private void finaliza() throws IOException {
        if (fi != null) {
            fi.close();
            parser.close();
            fi = null;
            parser = null;
        }
    }

    private void init(String nomeArquivo) throws FileNotFoundException {
        this.nomeArquivo = nomeArquivo;
        f = new File(nomeArquivo);
        fi = new FileInputStream(f);
        parser = Json.createParser(fi);
    }

    public void setArquivo(String nomeArquivo) throws
            FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
    }
    
    public int numeroBlocosDocumento() throws FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
        int numBlocos = 0;
        while(parser.hasNext()) {
            Event evt  = parser.next();
            if (evt == Event.START_OBJECT) {
                numBlocos++;
            }
        }
        return numBlocos;
    }

    public void getJSONPathElemento(String elemento) {

        while (parser.hasNext()) {
            Event event = parser.next();

            switch (event) {
                case KEY_NAME: {
                    System.out.print(parser.getString() + "=");
                    break;
                }
                case VALUE_STRING: {
                    System.out.println(parser.getString());
                    break;
                }
                case VALUE_NUMBER: {
                    System.out.println(parser.getString());
                    break;
                }
            }
            //return null;
        }
    }
    
    public void caminhaArquivo() throws FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
        while(parser.hasNext()) {
            Event event = parser.next();
            System.out.println(event);
        }
        finaliza();
    }

    /**
     * Método para detectar casos onde o arquivo contém um objeto único
     * localizado na raiz do documento JSON
     */
    public boolean temUnicoObjetoRaiz() throws FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
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
                        finaliza();
                        return false;
                    } else if (proxEv == Event.START_OBJECT) {
                        nivel++;
                        while (parser.hasNext()) {
                            Event evt = parser.next();
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
                                finaliza();
                                return false;
                            } else {
                                finaliza();
                                return true;
                            }                            
                        } else {
                            finaliza();
                            return false;
                        }
                    }
                }
            }
        }
        finaliza();
        return false;
    }

    public int getTipoElemento(String nomeElemento) throws IOException {
        if (isCampo(nomeElemento)) {
            return T_CAMPO;
        } else if (isObjeto(nomeElemento)) {
            return T_OBJETO;
        } else if (isArray(nomeElemento)) {
            return T_ARRAY;
        } else if (isArrayObject(nomeElemento)) {
            return T_ARRAY_OBJETO;
        }
        return T_NADA;
    }

    public boolean isObjeto(String elemento) throws
            FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == KEY_NAME) {
                if (parser.getString().equals(elemento)) {
                    if (parser.hasNext()) {
                        Event proxEv = parser.next();
                        if (proxEv == Event.START_OBJECT) {
                            finaliza();
                            return true;
                        }
                    }
                }
            }
        }
        finaliza();
        return false;
    }

    public boolean isArray(String elemento) throws
            FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == Event.KEY_NAME) {
                if (parser.getString().equals(elemento)) {
                    if (parser.hasNext()) {
                        Event proxEv = parser.next();
                        if (proxEv == Event.START_ARRAY) {
                            if (parser.hasNext()) {
                                proxEv = parser.next();
                                if (proxEv != Event.START_OBJECT) {
                                    finaliza();
                                    return true;
                                } else {
                                    finaliza();
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        finaliza();
        return false;
    }

    public boolean isArrayObject(String elemento) throws
            FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == Event.KEY_NAME) {
                if (parser.getString().equals(elemento)) {
                    if (parser.hasNext()) {
                        Event proxEv = parser.next();
                        if (proxEv == Event.START_ARRAY) {
                            if (parser.hasNext()) {
                                proxEv = parser.next();
                                if (proxEv == Event.START_OBJECT) {
                                    finaliza();
                                    return true;
                                } else {
                                    finaliza();
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        finaliza();
        return false;
    }

    public boolean isCampo(String elemento) throws
            FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
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
                            finaliza();
                            return true;
                        }
                    }
                }
            }
        }
        finaliza();
        return false;
    }
    
    /**
     * Método que lista todos os nomes de elementos do arquivo
     * (atributos, objetos, arrays)
     * @return Lista com nomes de elementos
     * @throws java.io.IOException
     */
    public List<String> getPalavrasArquivo() throws IOException {
        List<String> listaPalavrasDoc = new ArrayList<>(0);
        finaliza();
        init(nomeArquivo);
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == KEY_NAME) {
                listaPalavrasDoc.add(parser.getString());
            }
        }
        finaliza();
        return listaPalavrasDoc;
    }
    
    /**
     * Método para listar todos os itens pertencentes a um array de objetos
     * trata todos os elementos dos diferentes objetos juntos, ou seja, lista 
     * todos os elementos de todas as posições do array, gerando uma lista com
     * repetições de nomes
     */
    public List<String> getPalavrasObjetoArray(String nomeObjeto) throws IOException {
        List<String> listaPalavrasObj = new ArrayList<>(0);
        finaliza();
        init(nomeArquivo);
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
     * Lista o nome de todos elementos que pertençam a um objeto
     */
    public List<String> getPalavrasObjeto(String nomeObjeto) throws IOException {
        List<String> listaPalavrasObj = new ArrayList<>(0);
        finaliza();
        init(nomeArquivo);
        int nivel = 0;
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == KEY_NAME) {
                // Se o nome conferir com o nome de objeto procurado
                if(parser.getString().equalsIgnoreCase(nomeObjeto)) {
                    event = parser.next();
                    // Se for um objeto
                    if (event == Event.START_OBJECT) {
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
    
    

    public boolean localizarElemento(String elemento)
            throws FileNotFoundException, IOException {
        finaliza();
        init(nomeArquivo);
        while (parser.hasNext()) {
            Event event = parser.next();
            if (event == KEY_NAME) {
                if (parser.getString().equalsIgnoreCase(elemento)) {
                    finaliza();
                    return true;
                }
            }
        }
        finaliza();
        return false;
    }

    @Override
    public String toString() {
        return "InfoJSON{" + "f=" + f + ", fi=" + fi + ", parser=" + parser + ", nomeArquivo=" + nomeArquivo + '}';
    }

}