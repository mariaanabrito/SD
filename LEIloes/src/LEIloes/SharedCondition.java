/*
 * @brief Classe SharedCondition. Contém métodos para criar, devolver o lock e
 *        fazer lock e unlock de uma variável de condição.
 *
 * @author Bruno Pereira        - A75135
 * @author Carlos Pereira       - A61887
 * @author Diogo Silva          - A76407
 * @author Maria Ana de Brito   - A73580
 */

package LEIloes;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCondition {
    
    
    //Variáveis de instância
    
    private Condition c;        
    private ReentrantLock lock;
    
   
    //Construtores
    
    /**
     * Construtor vazio.
     */
    public SharedCondition() {
        lock = new ReentrantLock();
        c = lock.newCondition();
    }
    
    //Métodos de instância
    
    /**
     * Faz lock à variável lock da variável de condição.
     */
    public void getLock() {
        lock.lock();
    }
    
    /**
     * Faz unlock à variável lock da variável de condição.
     */
    public void releaseLock() {
        lock.unlock();
    }
    
    /**
     * Adormece a thread atual.
     * @throws InterruptedException 
     */
    public void waitCond() throws InterruptedException {
        c.await();
    }
    
    /**
     * Envia um sinal a uma thread adormecida para que ela acorde.
     */
    public void signalCond() {
        c.signal();
    }
}
