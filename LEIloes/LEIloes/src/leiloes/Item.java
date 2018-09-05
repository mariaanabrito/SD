/*
 * @brief Classe Item. Classe responsável pela manipulação e armazenamento de 
 * informação referente a um leilão.
 * 
 * @author Bruno Pereira      - 75135
 * @author Carlos Pereira     - 61887
 * @author Diogo Silva        - 76407
 * @author Maria Ana de Brito - 73580
 */
package LEIlões;

import java.util.Set;
import java.util.TreeSet;

/*
 * @brief Classe Item. 
 * 
 * @author Bruno Pereira      - 75135
 * @author Carlos Pereira     - 61887
 * @author Diogo Silva        - 76407
 * @author Maria Ana de Brito - 73580
 */
public class Item {
    
    // Variáveis de instância.
    
    private String designacao;        // Designação do item do leilão.
    private final float preco_base;   // Preço mínimo para uma licitação ser válida. 
    private final String vendedor;    // Vendedor do item.
    private String comprador;         // Licitador que está a ganhar o leilão.
    private float licitacao_max;      // Valor da maior licitação.
    private Set <String> licitadores; // Conjunto de todos os licitadores do item.
    
    
    // Construtor parametrizado.
    public Item (String v, String desc, float preco) {
        designacao = desc;
        preco_base = preco;
        vendedor = v;
        comprador = "";
        licitacao_max = 0;
        licitadores = new TreeSet<>();
    }
    
    // Construtor de cópia.
    public Item (Item i) {
        designacao = i.getDesignacao();
        preco_base = i.getPreco_base();
        vendedor = i.getVendedor();
        comprador = i.getComprador();
        licitacao_max = i.getLicitacao_max();
        licitadores = i.getLicitadores();
    }
    
    //Getters e Setters

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public float getPreco_base() {
        return preco_base;
    }

    public String getVendedor() {
        return vendedor;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public float getLicitacao_max() {
        return licitacao_max;
    }

    public void setLicitacao_max(float licitacao_max) {
        this.licitacao_max = licitacao_max;
    }

    public Set<String> getLicitadores() {
        Set<String> ret = new TreeSet<>(licitadores);
        
        return ret;
    }

    public void setLicitadores(Set<String> licitadores) {
        this.licitadores = licitadores;
    }
    
    // Métodos complementares comuns.
    
    /**
     * Método responsável por adicionar uma licitação ao item.
     * 
     * @param usr Utilizador em questão.
     * @param montante Valor da licitação.
     */
    public void addLicitacao(String usr, float montante) {
        licitadores.add(usr);
        
        /* Se a licitação for superior à licitação máxima atual então usr fica
         * a ganhar o leilão.
         */
        if(montante >= getLicitacao_max() && montante > getPreco_base()) {
            setLicitacao_max(montante);
            setComprador(usr);
        }
    }
    
    public Item clone() {
        return new Item(this);
    }
}
