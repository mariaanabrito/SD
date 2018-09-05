/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leilões;


import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Carlos Pereira
 */
public class Utilizadores {
    
    //Variáveis de instância
    
    private Map <String, String> users; //<username, password>

    //Construtores
    
    public Utilizadores () {
         users = new HashMap<>();
    }    
    
    //Métodos de instância
    
    public synchronized void addUser(String usr, String pwrd) throws UserExistsException {
        if (users.containsKey(usr)) {
            throw new UserExistsException("Utilizador já existe");
        } else {
            users.put(usr, pwrd);
           
        }  
    }
    
    
    public void login(String usr, String pwrd) throws LoginFailedException {
        if (users.containsKey(usr)) {
            if (!users.get(usr).equals(pwrd)) {
                throw new LoginFailedException("Login falhou");
            }
        } else {
            throw new LoginFailedException("Login falhou");
        }
    }

//Gets e Sets
    
    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }
    
    public int getUsersSize() {
        return users.size();
    }
    
    //Métodos complementares comuns
    
    
    
    
    
}
