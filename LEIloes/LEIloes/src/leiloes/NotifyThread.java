/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leilões;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos Pereira
 */
public class NotifyThread extends Thread {
    
    
    //Variáveis de instância
    
    private final Condition put;
    private final ReentrantLock lock;
    private final Socket sock;
    private OutBox out;
    

    //Construtores
    
    public NotifyThread(Socket s, OutBoxes outboxes, String usr) {
        sock = s;
        lock = new ReentrantLock();
        put = lock.newCondition();
        outboxes.add(usr, put, lock);
        out = outboxes.get(usr);
    }
    
    
    @Override
    public void run() {
        while(!out.atLimit()) {
            lock.lock();
            try
            {
                while(out.getSize() == 0) {
                    try {
                        put.await();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NotifyThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            String message = out.getMessage();
            
            try {
                PrintWriter ps = new PrintWriter(sock.getOutputStream(), true);
                ps.println(message);
            } catch (IOException ex) {
                Logger.getLogger(NotifyThread.class.getName()).log(Level.SEVERE, null, ex);
            } 
            } catch(Exception e)
            {
                
            }
            finally
            {
                lock.unlock();
            }
        }
        
        try {
            sock.close();
        } catch (IOException ex) {
            Logger.getLogger(NotifyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
