package br.ufsm.ppgcc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ezequielrr
 */
public class ElementoBloco {

    public final static int ATRIBUTO = 0;
    public final static int OBJETO = 1;
    public final static int ARR_OBJETO = 2;
    public final static int ARRAY = 3;

    private String nome;
    private String nomeConsolidado;
    private int tipo;
    private List<ElementoBloco> blocoFilho;

    public ElementoBloco() {
        this.nome = "";
        this.nomeConsolidado = "";
        this.tipo = ATRIBUTO;
        blocoFilho = new ArrayList<>(0);
    }

    public ElementoBloco(String nome, int tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.blocoFilho = new ArrayList<>(0);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<ElementoBloco> getBlocoFilho() {
        return blocoFilho;
    }

    public void setBlocoFilho(List<ElementoBloco> blocoFilho) {
        this.blocoFilho = blocoFilho;
    }

    public void addBlocoFilho(ElementoBloco elem) {
        this.blocoFilho.add(elem);
    }
    
    public void apagaBlocoFilho(ElementoBloco elem) {
        this.blocoFilho.remove(elem);
    }

    public String getNomeConsolidado() {
        return nomeConsolidado;
    }

    public void setNomeConsolidado(String nomeConsolidado) {
        this.nomeConsolidado = nomeConsolidado;
    }

    public String getAbreDelimitador() {
        switch (this.tipo) {
            case ElementoBloco.ARRAY:
                return "[";
            case ElementoBloco.ARR_OBJETO:
                return "[{";
            case ElementoBloco.ATRIBUTO:
                return "";
            case ElementoBloco.OBJETO:
                return "{";
            default:
                return null;
        }
    }

    public String getFechaDelimitador() {
        switch (this.tipo) {
            case ElementoBloco.ARRAY:
                return "]";
            case ElementoBloco.ARR_OBJETO:
                return "}]";
            case ElementoBloco.ATRIBUTO:
                return "";
            case ElementoBloco.OBJETO:
                return "}";
            default:
                return null;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.nome);
        hash = 59 * hash + Objects.hashCode(this.nomeConsolidado);
        hash = 59 * hash + this.tipo;
        hash = 59 * hash + Objects.hashCode(this.blocoFilho);
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
        final ElementoBloco other = (ElementoBloco) obj;
        if (this.tipo != other.tipo) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.nomeConsolidado, other.nomeConsolidado)) {
            return false;
        }
        if (!Objects.equals(this.blocoFilho, other.blocoFilho)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ElementoBloco{" + "nome=" + nome + ", nomeConsolidado=" + nomeConsolidado + ", tipo=" + tipo + ",\n blocoFilho=" + blocoFilho + '}';
    }

}
