package br.ufsm.ppgcc.model.estruturas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ezequielrr
 */
public class MatrizResultados {
    private List<List<Integer>> matriz;
    private List<String> camposLinha;
    private List<String> camposColuna;

    public MatrizResultados(List<List<Integer>> matriz, 
            List<String> camposLinha, List<String> camposColuna) {
        this.matriz = matriz;
        this.camposLinha = camposLinha;
        this.camposColuna = camposColuna;
    }

    public MatrizResultados() {
        camposLinha = new ArrayList<>();
        camposColuna = new ArrayList<>();
        matriz = new ArrayList<>();
    }

    public List<List<Integer>> getMatriz() {
        return matriz;
    }

    public void setMatriz(List<List<Integer>> matriz) {
        this.matriz = matriz;
    }

    public List<String> getCamposLinha() {
        return camposLinha;
    }

    public void setCamposLinha(List<String> camposLinha) {
        this.camposLinha = camposLinha;
    }

    public List<String> getCamposColuna() {
        return camposColuna;
    }

    public void setCamposColuna(List<String> camposColuna) {
        this.camposColuna = camposColuna;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.matriz);
        hash = 67 * hash + Objects.hashCode(this.camposLinha);
        hash = 67 * hash + Objects.hashCode(this.camposColuna);
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
        final MatrizResultados other = (MatrizResultados) obj;
        if (!Objects.equals(this.matriz, other.matriz)) {
            return false;
        }
        if (!Objects.equals(this.camposLinha, other.camposLinha)) {
            return false;
        }
        if (!Objects.equals(this.camposColuna, other.camposColuna)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MatrizResultados{" + "matriz=" + matriz + ", camposLinha=" + camposLinha + ", camposColuna=" + camposColuna + '}';
    }
    
}
