/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leilões;

import java.net.*;

/**
 *
 * @author Carlos Pereira
 */
public class Servidor {
    
    public static void main(String[] args) throws Exception {
        Utilizadores users = new Utilizadores();
        ServerSocket s = new ServerSocket(6063);
        OutBoxes outboxes = new OutBoxes(); //outbox de cada user
        InfoLeiloes info = new InfoLeiloes();
        Socket c;
        
        System.out.println("SERVIDOR");
        while((c = s.accept()) != null) {        
            LeilaoThread ct = new LeilaoThread(c, users, outboxes, info);
            ct.start();
        }
    }
}
