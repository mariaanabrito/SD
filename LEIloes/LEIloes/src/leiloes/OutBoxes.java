/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leilões;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Carlos Pereira
 */
public class OutBoxes {
    Map<String, OutBox> outboxes;
    
    public OutBoxes() {
        outboxes = new TreeMap<>();
    }
    
    
    
    public void add(String usr, Condition c, ReentrantLock l) {
        outboxes.put(usr, new OutBox(c, l));
    }
    
    public OutBox get(String usr) {
        return outboxes.get(usr);
    }   
    
    public void inserirMsg(String user, int id)
    {
        outboxes.get(user).putMsgIniciarLeilao(id);
    }
    
    public int size()
    {
        return outboxes.size();
    }
}
