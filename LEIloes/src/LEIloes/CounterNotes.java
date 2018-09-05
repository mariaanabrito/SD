/*
 * @brief Classe CounterNotes. Esta classe serve de contador.
 * 
 * @author Bruno Pereira      - 75135
 * @author Carlos Pereira     - 61887
 * @author Diogo Silva        - 76407
 * @author Maria Ana de Brito - 73580
 */

package LEIloes;

public class CounterNotes {
    
    //Variáveis de instância

    private int counter;    //Variável de instância contador.
    
    /**
     * Construtor vazio.
     */
    public CounterNotes() {
        counter = 0;
    }

    /**
     * Método de incrementação de counter, só pode ser acedido por uma thread de cada vez.
     */
    public synchronized void incrementa() {
        counter++;
    }
   
    /**
     * Método de reinicialização de counter, só pode ser acedido por uma thread de cada vez.
     */
    public synchronized void reset() {
        counter = 0;
    }
    
    /**
     * Método do tipo get de counter, só pode ser acedido por uma thread de cada vez.
     * @return valor do contador.
     */
    public synchronized int getCounter() {
        return counter;
    }
}
