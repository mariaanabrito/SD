/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leilões;

/**
 *
 * @author Carlos Pereira
 */
public class UserExistsException extends Exception {

    /**
     *
     * @param msg the detail message.
     */
    public UserExistsException(String msg) {
        super(msg);
    }
}
