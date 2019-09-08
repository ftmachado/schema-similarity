package br.ufsm.ppgcc.model.estruturas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ezequielrr
 */
public class NodoEstruturaConsolidada {

    public static final String ZERO_UM = "0:1";
    public static final String ZERO_MUITOS = "0:N";

    private final String nome;
    private boolean raiz;
    private final List<NodoEstruturaConsolidada> filhos;
    private final List<String> relacionamentos;
    private final List<String> atributos;

    public NodoEstruturaConsolidada(String nome) {
        this.nome = nome;
        this.relacionamentos = new ArrayList<>();
        this.filhos = new ArrayList<>();
        atributos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public boolean isRaiz() {
        return raiz;
    }

    public List<NodoEstruturaConsolidada> getFilhos() {
        return filhos;
    }

    public List<String> getRelacionamentos() {
        return relacionamentos;
    }

    public void setRaiz(boolean raiz) {
        this.raiz = raiz;
    }

    public void addRelacionamento(String relacionamento) {
        this.relacionamentos.add(relacionamento);
    }

    public void addFilho(NodoEstruturaConsolidada nodo) {
        filhos.add(nodo);
    }

    public List<String> getAtributos() {
        return atributos;
    }

    public void addAtributo(String atributo) {
        atributos.add(atributo);
    }

    /**
     * Método para obter uma lista contendo o caminho JSONPath para todos os
     * elementos contidos na estrutura consolidada
     *
     * @return Lista contendo os caminhos JSONPath
     */
    public List<String> getListaJsonPath() {
        List<String> jsonPath = new ArrayList<>();
        List<String> busca = new ArrayList<>();
        try {
            criarListaJSONPath(this, jsonPath, busca, "$");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            //Logger.getLogger(AdaptarNotacaoAgregados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonPath;
    }

    /**
     * Método que retorna uma lista com os nomes de todos os elementos presentes
     * na estrutura unificada. Para usar em conjunto com o método
     * getListaJsonPath
     *
     * @return lista contendo o nome de todos os elementos
     */
    public List<String> getListaNomes() {
        List<String> jsonPath = new ArrayList<>();
        List<String> busca = new ArrayList<>();
        criarListaJSONPath(this, jsonPath, busca, "$");
        return busca;
    }

    private void criarListaJSONPath(NodoEstruturaConsolidada n, List<String> listaJsonPath,
            List<String> listaBusca, String prefix) {
        if (n == null) {
            return;
        }
        String strJsonPath = prefix + "." + n.getNome();

        listaJsonPath.add(strJsonPath);
        listaBusca.add(n.getNome());
        for (String attr : n.getAtributos()) {
            if (!attr.equals("att")) {
                listaBusca.add(attr);
                listaJsonPath.add(strJsonPath + "." + attr);
            }
        }
        for (NodoEstruturaConsolidada filho : n.getFilhos()) {
            criarListaJSONPath(filho, listaJsonPath, listaBusca, strJsonPath);
        }
    }

    @Override
    public String toString() {
        if (!this.atributos.isEmpty() && this.atributos.get(0).equalsIgnoreCase("att")) {
            return "";
        }

        String tmp = "-----------------------------------------------------\n";
        if (raiz) {
            tmp += "ROOT\n";
        }
        tmp += "Entidade:" + nome + "\n";

        //Impressão de atributos
        tmp += "Atributos: ";
        for (int i = 0; i < this.atributos.size(); i++) {
            if (i > 0) {
                tmp += ",";
            }
            tmp += this.atributos.get(i);
        }
        tmp += "\n";

        // Imprimir os relacionamentos 0:1
        if (relacionamentos.contains(ZERO_UM)) {
            tmp += "REFI: ";
            int i = 0;
            for (String relacionamento : relacionamentos) {
                if (relacionamento.equalsIgnoreCase(ZERO_UM)) {
                    tmp += ", " + filhos.get(i).getNome();
                }
                i++;
            }
            tmp += "\n";
        }

        // Imprimir os relacionamentos 0:N
        if (relacionamentos.contains(ZERO_MUITOS)) {
            tmp += "EMBED: ";
            int i = 0;
            for (String relacionamento : relacionamentos) {
                if (relacionamento.equalsIgnoreCase(ZERO_MUITOS)) {
                    tmp += ", " + filhos.get(i).getNome();
                }
                i++;
            }
            tmp += "\n";
        }
        tmp += "-----------------------------------------------------\n";
        for (NodoEstruturaConsolidada n : filhos) {
            tmp += n.toString();
        }
        return tmp;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.nome);
        hash = 53 * hash + (this.raiz ? 1 : 0);
        hash = 53 * hash + Objects.hashCode(this.filhos);
        hash = 53 * hash + Objects.hashCode(this.relacionamentos);
        hash = 53 * hash + Objects.hashCode(this.atributos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodoEstruturaConsolidada other = (NodoEstruturaConsolidada) obj;
        if (this.raiz != other.raiz) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.filhos, other.filhos)) {
            return false;
        }
        if (!Objects.equals(this.relacionamentos, other.relacionamentos)) {
            return false;
        }
        if (!Objects.equals(this.atributos, other.atributos)) {
            return false;
        }
        return true;
    }

}
