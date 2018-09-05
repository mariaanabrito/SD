/*
 * @brief Classe Utilizadores. Contém métodos para registar um novo utilizador
 *        no sistema, fazer o login e o logout de um utilizador já existente.
 *
 * @author Bruno Pereira        - A75135
 * @author Carlos Pereira       - A61887
 * @author Diogo Silva          - A76407
 * @author Maria Ana de Brito   - A73580
 */

package LEIloes;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Utilizadores {
    
    //Variáveis de instância
    
    private Map <String, String> users;
    private Map <String, Boolean> logged;
    private Map <String, ReentrantLock> locks;
    
    
    //Construtores
    
    /**
     * Construtor vazio.
     */
    public Utilizadores () {
         users = new HashMap<>();   //Usernames e password associadas
         logged = new HashMap<>();  //Username e flag a indicar se tem o
                                    //login feito
         locks = new HashMap<>();   //Username e locks correspondentes
    }    
    
    
    //Métodos de instância
    
    /**
     * Adiciona um utilizador ao sistema com o username dado, associando-lhe
     * a password fornecida. Caso já exista um utilizador com o mesmo
     * username, é lançada a excepção apropriada.
     * 
     * @param usr                   username do utilizador
     * @param pwrd                  password do utilizador
     * @throws UserExistsException  excepção lançada quando se tenta registar
     *                              um utilizador já existente
     */
    public synchronized void addUser(String usr, String pwrd) throws UserExistsException {
        if (users.containsKey(usr)) {
            throw new UserExistsException("Utilizador já existe");
        } else {
            users.put(usr, pwrd); 
            logged.put(usr, false);
            locks.put(usr, new ReentrantLock());
        }
    }
    
    /**
     * Efetua o login de um utilizador. Começa por verificar se o utilizador
     * já se encontra registado no sistema. Caso esteja registado, verifica
     * se a password fornecida é igual à que está associada a ele no sistema.
     * Se os dados fornecidos forem iguais aos que estão guardados no sistema,
     * a flag que indica ter feito o login passa de falsa a verdadeira.
     * É lançada a excepção adequada quando não o login não é feito.
     * 
     * @param usr                   username do utilizador
     * @param pwrd                  password do utilizador
     * @throws LoginFailedException excepção lançada quando os parâmetros de
     *                              entrada não são iguais aos que estão 
     *                              guardados no servidor
     */
    public void login(String usr, String pwrd) throws LoginFailedException {
        if (users.containsKey(usr)) {
            if (!users.get(usr).equals(pwrd)) {
                //caso o utilizador não exista no sistema
                throw new LoginFailedException("Login falhou.");
            } 
            
            locks.get(usr).lock();
            
            if (logged.get(usr) == true) {
                //caso o utilizador já tenha o login feito
                locks.get(usr).unlock();
                throw new LoginFailedException("O utilizador já se encontra autenticado.");
            } else {
                logged.put(usr, true);  //a flag a indicar que tem o login feito
                                        //passa a verdadeira
                locks.get(usr).unlock();
            }
        } else {
            throw new LoginFailedException("O utilizador não existe.");
        }
    }
    
    /**
     * Efetua o logout do utilizador. Passa a flag que indica o login estar feito
     * a falsa.
     * 
     * @param usr username do utilizador
     */
    public void logout(String usr) {
        locks.get(usr).lock();
        logged.put(usr, false);
        locks.get(usr).unlock();
    }

    
    //Getters e Setters
    
    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }
    
    public int getUsersSize() {
        return users.size();
    }
}
