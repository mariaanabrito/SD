/*
 * @brief Classe LeilaoThread. Classe responsável por receber comandos de um
 * utilizador através de um socket e comunicar respostas pelo mesmo.
 * 
 * @author Bruno Pereira      - 75135
 * @author Carlos Pereira     - 61887
 * @author Diogo Silva        - 76407
 * @author Maria Ana de Brito - 73580
 */

package LEIloes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class LeilaoThread extends Thread {

    // Variáveis de instância
    
    private final Socket sock;                    // Socket para comunicação. 
    private Utilizadores users;                   // Utilizadores no sistema.
    private String username;                      // Utilizador em questão.
    private NotifyThread nt;                      // Thread de notificação.
    private InfoLeiloes info;                     // Conjunto de todos os leilões existentes.
    private Map<String, ArrayList<String>> notes; // Outboxes.
    private Map<String, SharedCondition> shared;  // Variáveis de condição.
    private Map<String, CounterNotes> counters;   // Número de notificações para um cliente.
    private int max_notifications = 15;           // Número máximo de notificações
 
    
    /**
     * Construtor por parâmetros.
     * 
     * @param socket    socket
     * @param users     utilizadores
     * @param info      informações sobre os leilões
     * @param notes     notificações dos utilizadores
     * @param shared    variáveis de condição
     * @param counters  contadores de notificações
     */
    public LeilaoThread(Socket socket, Utilizadores users, InfoLeiloes info,
                        Map<String, ArrayList<String>> notes,
                        Map<String, SharedCondition> shared,
                        Map<String, CounterNotes> counters) {
        this.sock = socket;
        this.users = users;
        this.info = info;
        this.notes = notes;
        this.shared = shared;
        this.counters = counters;
        username = "";
    }
    
    /**
     * Método de start da thread.
     */
    @Override
    public void run() {
        try {
            InputStreamReader ir = new InputStreamReader(sock.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            String message = "";
   
            /* Ciclo para efectuar a leitura de mensagens do cliente e efectuar o tratamento das mesmas. */
            while ((message = br.readLine()) != null) {
                String[] componentes;
                componentes = message.split("=");
                
                /* Opção enviada pelo cliente. */
                int op = Integer.parseInt(componentes[0]);
                
                /* LeilaoThread responde dependendo do comando do cliente. */
                switch(op) {
                    case 0: sock.close();
                            break;
                    case 1: registarCliente(componentes[1], componentes[2],
                                            pw);
                            break;
                    case 2: loginCliente(componentes[1], componentes[2], pw);
                            break;
                    case 3: iniciarLeilao(componentes[1],
                                          Float.parseFloat(componentes[2]),
                                          pw);
                            break;
                    case 4: listarLeiloes(pw);
                            break;
                    case 5: licitarItem(Integer.parseInt(componentes[1]), 
                                        Float.parseFloat(componentes[2]),
                                        pw);
                            break;
                    case 6: terminarLeilao(Integer.parseInt(componentes[1]),
                                           pw);
                            break;
                    case 7: dumpNotifications(pw);
                            break;
                    case 8: getLembrete(pw);
                            break;
                    case 9: logoutCliente();
                            break;
                    default: pw.println("Opção inválida."); 
                            break;
                }
             }
        } catch (IOException e) {
        }    
    }
    
    /**
     * Método responsável pelo novo registo de um cliente.
     * 
     * @param user Utilizador em questção.
     * @param pass Password do utilizador.
     * @param pw   Enviar a resposta de sucesso ou insucesso ao cliente.
     */
    private void registarCliente(String user, String pass, PrintWriter pw) {
        try {
            users.addUser(user, pass);
            pw.println(1);
        } catch (UserExistsException e) {
            pw.println(e.getMessage());
        }
    }
    
    /**
     * Método responsável por efectuar o login de um cliente.
     * É enviado uma mensagem de sucesso ao cliente caso o login tenha sido
     * efectuado corretamente.
     * 
     * @param user Utilizador em questão.
     * @param pass Password do utilizador.
     * @param pw   Canal de comunicação com o cliente.
     */
    private void loginCliente(String user, String pass, PrintWriter pw) {
        try {
            users.login(user, pass);
            username = user;
            // Sucesso.
            pw.println(1);
        } catch(LoginFailedException e) {
            pw.println(e.getMessage());
        }
        
        notes.put(username, new ArrayList<>());
        SharedCondition sha = new SharedCondition();
        CounterNotes c = new CounterNotes();

        counters.put(username, c);
        shared.put(username, sha);
        
        // Inicialização da thread de notificações para o cliente.
        nt = new NotifyThread(sock, notes.get(username), sha, c, max_notifications);
        nt.start();
        
    }
    
    /**
     * Método que efectua o logout de um cliente.
     */
    private void logoutCliente() {
        users.logout(username);
    }
   
    /**
     * Método responsável por iniciar um leilão.
     * 
     * @param desc Descrição do item.
     * @param base Preço base do item.
     * @param pw   Canal de comunicação para enviar o id do item ao cliente.
     */
    private void iniciarLeilao(String desc, Float base, PrintWriter pw) {
        int id;
        id = info.addLeilao(username, desc, base);
        
        // Enviar id do item.
        pw.println("Número: " + id);
    }
    
    /**
     * Método responsável por listar os leilões para o respectivo user.
     * 
     * @param pw PrintWriter para indicar a lista de leilões.
     */
    private void listarLeiloes(PrintWriter pw) {
        String str = "";
        
        if (info.getTotal() == 0) {
            str = "Não existem leilões ativos.";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Leilões ativos:=")
              .append(info.listLeiloes(username));
            str = sb.toString();
        }
        pw.println(str);
    }
    
    /**
     * Método responsável por efectua a licitação a um item do seu user.
     * 
     * @param id       ID do leilão.
     * @param montante Valor a ser licitado.
     * @param pw       PrintWriter para enviar uma resposta de sucesso ou insucesso ao cliente.
     */
    private void licitarItem(int id, float montante, PrintWriter pw) {
        if (info.contemLeilao(id)) {
            info.addLicitacao(username, id, montante);
            pw.println("Licitação efetuada.");
            
        } else {
            pw.println("O leilão não existe.");
        }   
    }
  
    /**
     * Método responsável por terminar um leilão do seu user.
     * 
     * @param id ID do leilão.
     * @param pw PrintWriter para enviar uma resposta ao cliente.
     */
    private void terminarLeilao(int id, PrintWriter pw) {
        
        if (info.contemLeilao(id)) {
            String msm = "O leilão " + id + " foi terminado.";
            Item leilao = info.terminaLeilao(id);
            
            if(leilao.getVendedor().equals(username)) {
                Set<String> licitadores = leilao.getLicitadores();
                // Caso existam licitadores.
                if (!licitadores.isEmpty()) {
                    String comprador = leilao.getComprador();
                    licitadores.remove(comprador);
                    String vencedor = "Parabéns. Venceu o leilão " + id +
                                      " com a licitação de " +
                                      leilao.getLicitacao_max();
                    
                    shared.get(comprador).getLock();
                                                        
                    try {
                        // Adicionar a mensagem de término e vencimento de um leilão ao comprador.
                        notes.get(comprador).add(vencedor);
                        counters.get(comprador).incrementa();
                    } finally {
                        shared.get(comprador).releaseLock();
                    }

                    for(String li : licitadores) {
                        shared.get(li).getLock();       
                        try {
                            // Adicionar a mensagem de término nas outboxes dos licitadores.
                            notes.get(li).add(msm);
                            counters.get(li).incrementa();
                        } finally {
                            shared.get(li).releaseLock();   //liberta o lock
                        }
                     }

                    pw.println(msm);
                } else {
                    pw.println("Leilão terminado e ninguém licitou.");
                }
            }
            else {
                pw.println("Não tem permissão");
            }
        } else {
            pw.println("O leilão não existe.");
        }
    }
    
    /**
     * Método responsável por enviar as notificações para o user.
     * 
     * @param pw Canal de comunicação com o servidor.
     */
    private void dumpNotifications(PrintWriter pw) {
        int num = counters.get(username).getCounter();
        pw.println(num);
        shared.get(username).getLock();
        
        try {
            // Sinaliza a variável de condição.
            shared.get(username).signalCond();
        } finally {
            shared.get(username).releaseLock();
        }

    }
    
    /**
     * Método que devolve o número de notificações por ler do user.
     * 
     * @param pw PrintWriter para enviar a mensagem ao cliente.
     */
    private void getLembrete(PrintWriter pw) {
        int num = counters.get(username).getCounter();  //número de notificações
                                                        //por ler
        StringBuilder sb = new StringBuilder();
        
        if (num < max_notifications) {
            sb.append("--- Tem ").append(num)
              .append(" mensagens por ler ---");

              pw.println(sb.toString());    //envio do número de notificações
                                            //ao Cliente
        } else {
            //caso o número de notificações exceda o limite, a ligação com o 
            //cliente é forçadamente encerrada
            try {
                sock.close();
            } catch (IOException ex) {
                
            }
        }
    }
}
