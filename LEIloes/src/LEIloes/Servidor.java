/*
 * @brief Classe Servidor. Aceita ligações com todos os clientes e para cada
 *        cria um objeto LeilaoThread.
 *
 * @author Bruno Pereira        - A75135
 * @author Carlos Pereira       - A61887
 * @author Diogo Silva          - A76407
 * @author Maria Ana de Brito   - A73580
 */

package LEIloes;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    
    /**
     * Cria o ServerSocket, o registo das informações os usernames e as passwords
     * de todos os utilizadores, o registos das informações de todos os leilões,
     * o Map com o username associado às respetivas notificações,o Map com o 
     * username associado à respetiva variável de condição, o Map com o username
     * associado ao respetivo número de notificações por ler.
     * 
     * @param args argumentos
     */
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(6063);    
            Utilizadores users = new Utilizadores();    //registo dos utilizadores
            InfoLeiloes info = new InfoLeiloes();       //informações dos leilões
            
            //username associado às notificações
            Map<String, ArrayList<String>> notes = new HashMap<>();
            
            //username associado à variável de condição
            Map<String, SharedCondition> map = new HashMap<>();
            
            //username associado ao contador de notificações
            Map<String, CounterNotes> counters = new HashMap<>();
            
            //Socket a ser utilizado para a comunicação
            Socket c;

            while((c = s.accept()) != null) { //Caso exista uma ligação no
                                              //ServerSocket para ser aceite
                LeilaoThread ct = new LeilaoThread(c, users, info, notes, map,
                                                   counters);
                ct.start();
            }
        } catch (IOException ex) {  

        }
    }
}
