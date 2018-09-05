/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leilões;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Carlos Pereira
 */
public class OutBox {
    
    //Variáveis de instância
    
    private List<String> mensagens;
    private final int limite;
    private Condition put;
    private ReentrantLock lock;
    
    //Construtores
    
    
    public OutBox(Condition c, ReentrantLock l) {
        mensagens = new ArrayList<>();
        limite = 10;
        lock = l;
        put = c;
    }
    
    
    //Métodos de instância
    
    public synchronized int getSize() {
        return mensagens.size();
    }
    
 
    public void putMsgFimLeilao(int leilao, String comprador, float max) {
        lock.lock();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("O leilão " + leilao + " foi ganho por " + comprador +
                  " pelo valor de " + max);
        
        mensagens.add(sb.toString());
        put.signal(); //quem avisa é a leilãothread
        lock.unlock();
    }
    
    public void putMsgIniciarLeilao(int id) {
        lock.lock();
        
        try {
            StringBuilder sb = new StringBuilder();
        
            sb.append("O leilão " + id + " foi iniciado.");
        
            mensagens.add(sb.toString());
            
            put.signal();
        } catch (Exception e) {
            
        } finally {
            lock.unlock();
        }
    }
    
    public synchronized String getMessage() {
        String ret = mensagens.get(0);
        mensagens.remove(0);
        return ret;
    } 
    
    
    public boolean atLimit() {
        return (limite == getSize());
    }
    
}
