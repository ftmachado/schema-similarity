/*
* CLASSE PRINCIPAL - CHAMA AS ANÁLISES RADICAL, LEV E LIN DE CADA BLOCO,
* REALIZA O CALCULO DA EQUIVALENCIA (ALGORITMO 6) E GRAVA CADA BLOCO EM UM ARQUIVO CSV
*/
package br.ufsm.ppgcc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Principal {
    
/* Atributos utilizados */
    public static int i,j,k,l,m,n_linhas,cont=0, pesoA=1, pesoB=3, pesoC=2;  
    public static double ponto_corte=0.7, ponto_corte_AHP=0.5,Aux, media_ponderada=0;

//método principal 
    public static void main(String[] args) throws IOException {
        
//instancia as matrizes das analises
    //AnaliseRad radical = new AnaliseRad();
    //AnaliseLev lev = new AnaliseLev();
    AnaliseLin analise_lin = new AnaliseLin(); //instancia os métodos para realizar o calculo de lin
    CarregaLin lin = new CarregaLin();

//instancia o vetor de palavras
    Palavras palavras = new Palavras();
    
//realiza calculo do Algoritmo 5
    analise_lin.CalculoLin();

/*Abre a pasta que contem os arquivos com cada bloco de palavras para ver 
 *quantas vezes é preciso gerar cada analise por meio do for
*/
    File pasta = new File("C:\\Users\\renat\\Desktop\\Mestrado\\1º Sem\\Topicos BD\\Arq_aplicacao\\Bloco_Palavras");
    File[] arquivo = pasta.listFiles();
    for (m=0;m<arquivo.length;m++){
        
//mostra palavras do arquivo txt
    System.out.println("\nPalavras do Bloco "+m+":");
    palavras.List_Plv(m);
 
//mostra as matrizes Rad, Lev e Lin
  
    System.out.println("\n Matriz Rad - Radical"); 
    radical.MatrizRad(m); 
    System.out.println("\n");
    System.out.println("\n Matriz Lev - Caractere");
    lev.MatrizLev(m);
    System.out.println("\n");
    System.out.println("\n Matriz Lin - Conhecimento");
    lin.MatrizLin(m);        
    System.out.println("\n");     

//verifica se o tamanho das matrizes sao iguais
    //if ((radical.aux_tam[m] == lev.aux_tam[m]) && (radical.aux_tam[m] == lin.aux_tam[m]) )
    //{
        n_linhas = lin.aux_tam[m];
    //}  
        
// Matriz de resultado do tamanho das matrizes das analises
    double Resultado[][]= new double[n_linhas][n_linhas]; 

//inicio do calculo de equivalencia (Algoritmo 6)
    System.out.println("\n Calculo de Equivalencia - Algoritmo 6 (Detalhado) \n");
    for (i=0; i<n_linhas; i++)
    { //inicio for linha
        for (j=i; j<n_linhas; j++)
        { // inicio for coluna
           
            System.out.println(">>>>>Elemento "+"["+i+"] ["+j+"]");
            
            //Atende 1º caso
            // Se pelo menos um dos 3 for igual a 1 considera equivalente
            if ((lev.Lev[i][j] == 1.0) || (radical.Rad[i][j] == 1.0) || (lin.Lin[i][j] == 1.0)) 
            {
                //Considera equivalente
		Resultado[i][j] = 1;
		System.out.println("Caso 1: Resultado["+i+"]["+j+"] = 1");
            } 
            else
            {
                // Atende 2º caso
                // Todos igual a 0 considera diferente
                    if (lev.Lev[i][j] == 0 && radical.Rad[i][j] == 0 && lin.Lin[i][j] == 0)
                    {
                        //Considera diferente
			Resultado[i][j] = 0;
			System.out.println("Caso 2: Resultado["+i+"]["+j+"] = 0");
                    } 
                    else
                    {
                        cont=0;
                        //Atende 3º caso
                            if (lev.Lev[i][j] >0.0 && lev.Lev[i][j] < 1.0) 
                            {
                                Aux = lev.Lev[i][j];
                                cont++;
                            } 
                           
                                if (radical.Rad[i][j] >0.0 && radical.Rad[i][j] < 1.0)
                                {
                                    Aux = radical.Rad[i][j];
                                    cont++;
                                }
                               
                                if (lin.Lin[i][j] >0.0 && lin.Lin[i][j] < 1.0)
                                {
                                    Aux = lin.Lin[i][j];
                                    cont++;
                                }
                      
                                
                            if (cont==1)
                            {
                                //se valor maior que ponto de corte(0.7)
                                if (Aux > ponto_corte)
                                {
                                    Resultado[i][j]=1;
                                    System.out.println("Caso 3: Resultado["+i+"]["+j+"] = 1");
                                }
                                else
                                {
                                    Resultado[i][j]=0;
                                    System.out.println("Caso 3: Resultado["+i+"]["+j+"] = 0");
                                }
                            }else
                            {
                        if (cont>1){
                        //Atende ao 4º caso    
			//mais de um com valores entre 0 < x < 1 
                        //aplica a media ponderada
			media_ponderada = (pesoA*radical.Rad[i][j] + pesoB*lev.Lev[i][j] + pesoC*lin.Lin[i][j])/6;//soma dos pesos
                	//Se o resultado for maior que ponto de corte = 0.5
			if (media_ponderada > ponto_corte_AHP)
                        {
                            Resultado[i][j] = 1;
                            System.out.println("Caso 4: Resultado["+i+"]["+j+"] = 1");
                        }
                        else
                        {
                            Resultado[i][j] = 0;
                            System.out.println("Caso 4: Resultado["+i+"]["+j+"] = 0");
                        }
                        }
                    }
                    }
            }
	} // fim for coluna
    } // fim for linha
//fim do calculo de equivalencia (Algoritmo 6)

//grava em um arquivo o resultado do algoritmo 6
    
	try {
            FileWriter writer = new FileWriter("C:\\Users\\renat\\Desktop\\Mestrado\\1º Sem\\Topicos BD\\Arq_aplicacao\\Saida\\"+m+".csv");
            for(k=0; k<n_linhas; k++){  
                for (l=0;l<n_linhas;l++){
                    if (k == l){
                    writer.write(1.0+";");
                    }else{
                    if (l<k){
                        writer.write("| - ;");
                    } else{
                        writer.write(Resultado[k][l]+";");
                    }}
                      } 
                        writer.write("\n"); 
            }
			writer.close();
		} catch (IOException exc) {
		}

//Mostra matriz resultados (Algotimo 6)
       // System.out.println("\n");
        System.out.println("\nMatriz resultante do Calculo de Equivalencia (Algoritmo 6)");
	for (k=0; k<n_linhas; k++){
		System.out.println("");
		for (l=0;l<n_linhas;l++){
                    //diagonal principal
                    if (k == l)
                    {
                        Resultado[k][l] = 1;
                    }
                    // restante da matriz
            
			if (l<k){
				System.out.print("| - |");
                        }
			else
				System.out.print("|"+Resultado[k][l]+"|");
		}
	}
        
        
//Mostra palavras equivalentes
        System.out.println("\n"); 
        System.out.println("\nPalavras equivalentes encontradas (Algoritmo 6):\n");
	for (k=0; k<n_linhas; k++){
            for (l=0;l<n_linhas;l++){
            
			if ((l>k) && (k != l)){
                            if (Resultado[k][l] == 1.0){
				System.out.println(palavras.Plv[k]+" - "+ palavras.Plv[l]);}
                        }
            }
	}
        System.out.println("-");
        
    }
}
}