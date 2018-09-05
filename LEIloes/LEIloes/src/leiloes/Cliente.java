/*
 * @brief Classe Cliente. Esta classe trata de todas as mensagens enviadas pelo
 * servidor de leilões. Contém todos os métodos necessários ao correcto
 * funcionamento do Cliente.
 * 
 * @author Bruno Pereira      - 75135
 * @author Carlos Pereira     - 61887
 * @author Diogo Silva        - 76407
 * @author Maria Ana de Brito - 73580
 */
package LEIlões;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @brief Definição da Classe Cliente.
 * 
 * @author Bruno Pereira      - 75135
 * @author Carlos Pereira     - 61887
 * @author Diogo Silva        - 76407
 * @author Maria Ana de Brito - 73580
 */
public class Cliente {
   /**
     * Definição das variáveis de instância.
     */
    private static Menu menuInicial;
    private static Menu menuPrincipal;
    private static boolean res; // res = false quando atinge limite de notificações
    private static boolean exit;
    
    /**
     * Método main. Arranca o cliente, responsável por definir e inicializar todas as variáveis
     * relevantes ao correcto funcionamento da classe cliente.
     * 
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Definição e inicialização de todas as variáveis necessárias ao método main.
         */
        try {
            /**
             * Inicialização das variáveis de necessárias à comunicação com o
             * servidor.
             */
            Socket sock = new Socket("localhost", 6063);
            InputStreamReader ir = new InputStreamReader(sock.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
            
            /**
             * Inicialização dos menus usados no ciclo de leitura e escrita para o servidor.
             */
            carregarMenus();
            
            /**
             * Inicialização de variáveis auxiliares.
             */
            String sucesso;
            String linha = "1";
            int op = 0;
            
            /**
             * Inicialização de res como variável de instância.
             */
            res = true;
            
            /**
             * Inicialmente o utilizador não pretende sair do programa.
             */
            exit = false;
            
            /**
             * Ciclo responsável por efectuar a apresentação de menus, leitura das
             * mensagens do servidor assim como a própria resposta para o mesmo.
             */
            while (res && !(linha.equals("0"))) {
                /* Inicialmente o utilizador está a tentar autenticar-se. */
                boolean login = false;
                linha = lerOpcaoMenuInicial();  
                pw.println(linha);
                
                /*
                 * Se o utilizador pretender sair então exit = true.
                 */
                if(exit) {
                    break;
                }
                
                /* Sucesso = true quando o login ou o registo foram efectuados. */
                sucesso = br.readLine();

                if (sucesso.equals("1") && linha.charAt(0) == '2') {
                    login = true;
                } else {
                    if (sucesso.equals("1")) {
                        System.out.println("Registado com sucesso.");
                    } else {
                        System.out.println(sucesso);
                    }
                }
                
                /* Se login foi efectuado com sucesso então entra no ciclo. */
                while (res && login) {
                    while(res && (op = lerOpcaoMenuPrincipal())!= 0) {
                        /* Lembrete que indica o número de notificações por consumir. */
                        getLembrete(pw, br);

                        if (res) {
                            switch(op) {
                                case 1: iniciarLeilao(pw, br);
                                        break;
                                case 2: listarLeiloes(pw, br);
                                        break;
                                case 3: licitarItem(pw, br);
                                        break;
                                case 4: terminarLeilao(pw, br);
                                        break;
                                case 5: lerNotificacoes(pw, br);
                                        break;
                                case 0: res = false;        
                            }
                        }
                    }
                    
                    /* Se utilizador pretender sair, devemos deslogar o mesmo. */
                    if (op == 0) {
                        logout(pw);
                    }
                    login = false;
                }
            }
            
            /* Se saiu do ciclo por res = falso então o cliente foi forçado a terminar. */
            if (!res) {
                System.out.println("O Cliente é lento e foi terminado.");    
            } else {
                System.out.println("Programa terminado.");
                sock.close();
            }
        } catch(IOException e) {
            System.out.println("Um problema ocorreu.");
        }
    }

    /**
     * @brief Método utilizado para carregar os vários menus utilizados pelo cliente.
     */
    private static void carregarMenus() {
        String[] opsIniciais = {"Registar", "Login"};
        String[] opsPrincipais = {"Iniciar Leilão", "Listar Leilões",
                                  "Licitar item", "Terminar Leilão", "Ler Notificações"};
        /* Menu de login e registo. */
        menuInicial = new Menu(opsIniciais);
        /* Menu de leilões. */
        menuPrincipal = new Menu(opsPrincipais);
    }
    
    /**
     * @brief Método utilizado para obter a opção inserida pelo utilizador aquando
     *        a apresentação do menu correspondente.
     *
     * @return Opção lida. 
     */
    private static int lerOpcaoMenuPrincipal() {
        int op;
        
        do {
            menuPrincipal.executarMenu();

            switch(op = menuPrincipal.getOpcao()) {
                
                case 1 : break;
                       
                case 2 : break;
                
                case 3 : break;
                       
                case 4 : break;
                
                case 5 : break;
            }
            break;
        } while(op != 0);
        
        return op;    
    }
        
    /**
     * @brief Método utilizado para ler o username e password do utilizador de
     *        System.in.
     * 
     * @return String resultante da junção do username e password do utilizador. 
     */
    private static String lerUserPass() {
        Scanner read = new Scanner(System.in);
        System.out.println("Insira o nome de utilizador:");
        String usr = read.nextLine();
        System.out.println("Insira a palavra-passe");
        String pwd = read.nextLine();
        return usr + "=" + pwd;
    }
    
    /**
     * @brief Método responsável por fazer o tratamento do menuInicial.
     * 
     * @return String resultante do tratamento da opção inserida pelo utilizador.
     */
    private static String lerOpcaoMenuInicial() {
        StringBuilder sb = new StringBuilder();
        int op;
        
        do {
            menuInicial.executarMenu();
            
            switch(op = menuInicial.getOpcao()) {
                case 1 : sb.append("1=").append(lerUserPass());
                         op = 0;
                         break;
                case 2 : sb.append("2=").append(lerUserPass());
                         op = 0;
                         break;         
            }
            break;
        } while(op != 0);
        
        if(sb.length() == 0) {
            sb.append("0=");
            exit = true;
        }
        
        return sb.toString();    
    }
    
    /**
     * @brief Este método é responsável por deslogar o utilizador enviando ao
     *        servidor essa mesma indicação.
     * 
     * @param pw 
     */
    private static void logout(PrintWriter pw) {
        pw.println("9=");
    }
    
    /**
     * @brief Método responsável pelo tratamento da inicialização de um leilão.
     *        Envia os dados referentes ao leilão ao servidor.
     * 
     * @param pw PrintWriter para enviar a descrição do item ao servidor.
     * @param br BufferedReader para ler a resposta do servidor.
     */
    private static void iniciarLeilao(PrintWriter pw, BufferedReader br) {
        try {
            Scanner read = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();
           
            System.out.println("Insira a descrição do item:");
            String desc = read.nextLine();
            
            System.out.println("Insira valor base do item:");
            float f = read.nextFloat();
            
            sb.append("3=").append(desc).append("=").append(f);
            
            pw.println(sb.toString());
            
            String output = br.readLine();
            System.out.println(output);
            
        } catch(Exception e) {
            System.out.println("Input inválido.");
        }
    }
    
    /**
     * @brief Método responsável por listar todos os leilões a decorrer aquando
     *        o chamamento do mesmo. Denote-se que apresenta ao utilizador a
     *        a lista desses provenientes do servidor.
     * 
     * @param pw PrintWriter para enviar a opção de listar leilões ao servidor.
     * @param br BufferedReader para ler a lista de leilões.
     */
    private static void listarLeiloes(PrintWriter pw, BufferedReader br){
        try {
            Scanner read = new Scanner(System.in);
            StringBuilder sb = new StringBuilder();

            /* Opção de listar leilões para o servidor. */
            sb.append("4");
            pw.println(sb.toString());
            
            String output = br.readLine();
            System.out.println(output.replace("=", "\n"));
            
        } catch(Exception e) {
            System.out.println("Input inválido.");
        }
    }
    
    /**
     * @brief Método responsável pelo tratamento de uma licitação por parte do cliente.
     * 
     * @param pw PrintWriter para enviar a informação referida à licitação ao servidor.
     * @param br BufferedReader para obter a resposta do servidor após o envio da informação.
     */
    private static void licitarItem(PrintWriter pw, BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        Scanner read = new Scanner(System.in);
        
        /* Opção de licitar item para o servidor. */
        sb.append("5=");
        System.out.print("Insira id do leilão: ");
        sb.append(read.nextInt()).append("=");
        
        System.out.print("Insira montante que deseja licitar: ");
        pw.println(sb.append(read.nextFloat()).toString());
       
        try {
            /* Obter resposta do servidor. */
            System.out.println(br.readLine());
        } catch (IOException ex) {
        }
    }
    
    /**
     * @brief Método responsável pelo tratamento do termino de um leilão.
     * 
     * @param pw PrintWriter para indicar que o cliente pretende encerrar.
     * @param br BufferedReader para obter a resposta do servidor.
     */
    private static void terminarLeilao(PrintWriter pw, BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        Scanner read = new Scanner(System.in);
       
        sb.append("6=");
        System.out.println("Insira id do leilão a terminar:");
        sb.append(read.nextInt());
        pw.println(sb.toString());
        
        try {
            System.out.println(br.readLine());
        } catch (IOException ex) {
        }
    }
    
    /**
     * Método para a leitura de todas as notificações prontas para serem recebidas
     * por parte do servidor.
     * 
     * @param pw PrintWriter para enviar a opção pretendida.
     * @param br BufferedReader para ler todas as notificações até o momento.
     */
    private static void lerNotificacoes(PrintWriter pw, BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        sb.append("7=");
        pw.println(sb.toString());
        
        try {
            String read = br.readLine();
            int num_notificacoes = Integer.parseInt(read);
            if (num_notificacoes > 0) {
                for(int i = 0; i < num_notificacoes;i++) {
                    System.out.println(br.readLine());
                }
            } else {
                System.out.println("Não existem notificações a apresentar.");
            }
        } catch (IOException ex) {
            System.out.println("Não leu do socket");
        }
    }
    
    /**
     * Método responsável por pedir um lembrete ao servidor do número de notificações
     * sem estarem consumidas por parte do cliente.
     * 
     * @param pw PrintWriter para enviar a opção pretendida ao servidor.
     * @param br BufferedReader para verificar o número de notificações por ler.
     */
    private static void getLembrete(PrintWriter pw, BufferedReader br) {
        String ret = "";
        pw.println("8=");

        try {
           ret = br.readLine();
        } catch (IOException ex) {
        }

        if (ret == null) {
            res = false;
        } else {
             System.out.println(ret);
        }
    }
}
