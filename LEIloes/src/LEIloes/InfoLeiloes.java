/*
 * @brief Classe InfoLeiloes. Esta classe trata do armazenamento e manipulação
 * de todos os leilões no servidor.
 * 
 * @author Bruno Pereira      - 75135
 * @author Carlos Pereira     - 61887
 * @author Diogo Silva        - 76407
 * @author Maria Ana de Brito - 73580
 */

package LEIloes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InfoLeiloes {
    
    /**
     * Variáveis de instância.
     */
    private int total;                          /* Número total de leilões */
    private Map<Integer, Item> leiloes;         /* A Key é o id do leilão e o
                                                 * Value é o objecto Item. */
    private Map<Integer, ReentrantLock> locks;  /* A Key é o id do leilão e o
                                                 * Value, o lock correspondente. */
    
    
    /* Construtor vazio. */
    public InfoLeiloes() {
        total = 0;
        leiloes = new HashMap<>();
        locks = new HashMap<>();
    }
    
    
    /* Métodos de instância. */
    
    /**
     * Método responsável por enviar os leilões para um cliente específico.
     * 
     * @param username String que é o username em questão.
     * @return lista de leilões.
     */
    public String listLeiloes(String username) {
        StringBuilder sb = new StringBuilder();
        
         
        /* Efectua um lock sequência ao objecto que está a ser utilizado. 
         * Permitindo a consulta e alteração dos outros objectos do Map. */

        for(Integer i : leiloes.keySet()) {
            /* Acquire do Lock. */
            locks.get(i).lock();
            
            try {
                Item aux = leiloes.get(i);
                sb.append(i).append(" ");
                /* Se o utilizador que efectuou o pedidor for o vendedor. */
                if(aux.getVendedor().equals(username)) {
                    sb.append("* ");
                }
                /* Se o utilizador que efectuou o pedido se tratar de um comprador. */
                if(aux.getComprador().equals(username)) {
                    sb.append("+ ");
                }
                sb.append("=");
            } finally {
                /* Release do Lock */
                locks.get(i).unlock();
            }
        }
        /* Lista de leilões devidamente assinalados para o utilizador em questão. */
        return sb.toString();
    }
    
    /**
     *  Método responsável por adicionar um novo leilão.
     * 
     * @param user O utilizador que pretende adicionar o leilão.
     * @param desc Descrição do leilão.
     * @param base Valor mínimo para uma licitação do leilão.
     * @return 
     */
    public int addLeilao(String user, String desc, float base) {
        
        /* Efectuar lock aos leilões. */
        for(ReentrantLock l : locks.values()) {
            l.lock();
        }
        
        /* Criar o novo lock para o novo leilão. */
        ReentrantLock lock = new ReentrantLock();
        
        /* Acquire do Lock do novo leilão. */
        lock.lock();
        
        total++;
        Item i = new Item(user, desc, base);
        
        /* É seguro visto que mais ninguém pode estar a efectuar alterações nos leilões. */
        leiloes.put(total, i);
        locks.put(total, lock);
        
        /* Libertar a estrutura de locks. */
        for(ReentrantLock l : locks.values()) {
            l.unlock();
        }
        return total;
    }
    
    /**
     * Método responsável por efectuar uma licitação a um dado leilão.
     * 
     * @param usr      Utilizador em questão.
     * @param id       ID do leilão.
     * @param montante Montate licitado.
     */
    public void addLicitacao(String usr, int id, float montante) {
        locks.get(id).lock();
        
        try {
            Item i = leiloes.get(id);
            i.addLicitacao(usr, montante);
        } finally {
            locks.get(id).unlock();
        }
    }
    
    /**
     * Método que indica se um dado leilão existe.
     * 
     * @param id ID do leilão.
     * @return true se leilão existe, false caso contrário.
     */
    public boolean contemLeilao(int id) {
        return leiloes.containsKey(id);
    }
    
    /**
     * Método responsável por terminar um leilão.
     * 
     * @param id
     * @return Leilão removido.
     */
    public Item terminaLeilao(int id) {
        Item ret;
        locks.get(id).lock();
        try {
            ret = leiloes.get(id).clone();
            
            /* Remove o leilão em questão. */
            leiloes.remove(id);
        } finally {
            locks.get(id).unlock();
            locks.remove(id);
        }
        return ret;
    }
    
    
    /* Getters e Setters */
    
    public synchronized int getTotal() {
        return leiloes.size();
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<Integer, Item> getLeiloes() {
        return leiloes;
    }

    public void setLeiloes(Map<Integer, Item> leiloes) {
        this.leiloes = leiloes;
    }

    public Map<Integer, ReentrantLock> getLocks() {
        return locks;
    }

    public void setLocks(Map<Integer, ReentrantLock> locks) {
        this.locks = locks;
    }  
}
