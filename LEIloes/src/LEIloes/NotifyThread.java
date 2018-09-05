/*
 * @brief Classe NotifyThread. Aqui adicionadas e guardadas todas as
 *        notificações por ler de um utilizador. Quando uma thread deste
 *        objeto é acordada, escreve no printwriter todas as notificações
 *        guardadas. Quando o número de notificações passar o limite,
 *        a thread termina.
 *
 * @author Bruno Pereira        - A75135
 * @author Carlos Pereira       - A61887
 * @author Diogo Silva          - A76407
 * @author Maria Ana de Brito   - A73580
 */

package LEIloes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class NotifyThread extends Thread {
    
    
    //Variáveis de instância
    
    private final Socket sock;                  //socket
    
    private final SharedCondition sharedCond;   //variável de condição
    
    private final ArrayList<String> notes;      //notificações de um utilizador
    
    private final CounterNotes counter;         //contador de notificações por
                                                //ler
    
    private final int max_notifications;        //número máximo de notificações
    
    
    //Construtores
    
    /**
     * Construtor por parâmetros.
     * 
     * @param socket        socket
     * @param notifications notificações de um utilizador
     * @param condition     variável de condição    
     * @param counter       contador de notificações por ler
     * @param max           número máximo de notificações
     */
    public NotifyThread(Socket socket, ArrayList<String> notifications,
                        SharedCondition condition, CounterNotes counter,
                        int max) {
        this.sock = socket;
        this.sharedCond = condition;
        this.notes = notifications;
        this.counter = counter;
        this.max_notifications = max;
    }
    
    
    @Override
    public void run() { 
        boolean under_max = true;   //flag que indica se o número de
                                    //notificações está abaixo do limite
        
        while(under_max) {
          
            sharedCond.getLock();   //obtém o lock para aceder às notificações
            
            try {
                while(counter.getCounter() == 0) {  
                    //se o número de notificações for igual a 0, adormece até
                    //que seja acordada para escrever as notificações no
                    //printwriter
                    try {
                        sharedCond.waitCond();
                    } catch (InterruptedException ex) {
                    
                    }
                }

                try {
                    PrintWriter ps = new PrintWriter(sock.getOutputStream(),
                                                     true);

                    if (counter.getCounter() >= max_notifications) {
                        //caso o número de notificações passe o limite
                        under_max = false;
                        
                    } else {
                        
                        if (notes.isEmpty()) {
                            //caso não existam notificações, escreve 0 no printwriter
                            ps.println("0");
                        } else {
                            for(;!notes.isEmpty();) {
                                //escreve no printwriter ao mesmo tempo que
                                //elimina da lista
                                ps.println(notes.remove(0)); 
                            }

                            //depois de enviar todas as notificações, faz o reset
                            //do contador
                            counter.reset();
                        } 
                    }
                } catch (IOException ex) {
                
                }
            }
            finally {
                //quando já não necessitar de aceder às notificações, liberta
                //o lock que tinha 
                sharedCond.releaseLock();
            }
        }
    }
}    
