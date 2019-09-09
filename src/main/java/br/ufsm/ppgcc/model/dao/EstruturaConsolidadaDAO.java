package br.ufsm.ppgcc.model.dao;

import br.ufsm.ppgcc.model.estruturas.ParserEstruturaConsolidada;
import br.ufsm.ppgcc.model.estruturas.ElementoBloco;
import br.ufsm.ppgcc.model.estruturas.NodoEstruturaConsolidada;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Ezequiel Ribeiro
 */
public class EstruturaConsolidadaDAO {
    
    public NodoEstruturaConsolidada lerEstruturaConsolidada(String arqEstruturaUnificada) 
            throws FileNotFoundException, IOException, Exception {
        
        ParserEstruturaConsolidada notacoes = new ParserEstruturaConsolidada();
        FileReader arq = new FileReader(arqEstruturaUnificada);
        BufferedReader lerArq = new BufferedReader(arq);
        int c;

        while ((c = lerArq.read()) != -1) {
            notacoes.lerCaractere((char)c);
        }

        lerArq.close();
        arq.close();
        
        return notacoes.getNodoEstruturaConsolidada();
    }
    
    /**
     * Grava a estrutura consolidada a partir de uma estrutura remontada
	 * @author Fhabiana Machado
	 */
    public void gravarEstruturaConsolidada(ElementoBloco elemento, String arqEstruturaUnificada) throws IOException {
        
        FileWriter arq = new FileWriter(arqEstruturaUnificada);
		PrintWriter gravarArq = new PrintWriter(arq);
		
		String str = "";
		String escNome = "";
		
		if (elemento.getNomeConsolidado() != null && !elemento.getNomeConsolidado().equals("")) {
			escNome = elemento.getNomeConsolidado();
		} else {
			escNome = elemento.getNome();
		}
		
        str += "" + escNome + ";" + elemento.getAbreDelimitador()
                + getStringEstruturaConsolidada(elemento.getBlocoFilho())
                + elemento.getFechaDelimitador() + "";

        gravarArq.println(str);
        gravarArq.close();
        arq.close();

    }
    
    /**
     * Grava o esquema conceitual a partir da estrutura consolidada
     */
    public void gravarEsquemaConceitual(NodoEstruturaConsolidada estrutura, String arqEsquemaConceitual) 
            throws IOException {
        
        FileWriter arq = new FileWriter(arqEsquemaConceitual);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.println(estrutura.toString());
        gravarArq.close();
        arq.close();        

    }
    
    /**
     * Método auxiliar usado para tornar a lista remontada em uma string
     * de uma estrutura consolidada
     */
    private String getStringEstruturaConsolidada(List<ElementoBloco> elementos) {
        String str = "";
        String escNome = "";
        for (ElementoBloco e : elementos) {
            if (e.getNomeConsolidado() != null && !e.getNomeConsolidado().equals("")) {
                escNome = e.getNomeConsolidado();
            } else {
                escNome = e.getNome();
            }
            if (e.getTipo() != ElementoBloco.OBJETO && e.getTipo()
                    != ElementoBloco.ARR_OBJETO) {
                str += escNome + ";" + e.getAbreDelimitador()
                        + e.getFechaDelimitador();
            } else {
                str += escNome + ";" + e.getAbreDelimitador()
                        + getStringEstruturaConsolidada(e.getBlocoFilho())
                        + e.getFechaDelimitador();
            }
        }
        return str;
    }
    
}
