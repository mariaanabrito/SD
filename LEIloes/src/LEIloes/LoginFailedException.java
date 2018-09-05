/*
 * @author Bruno Pereira        - A75135
 * @author Carlos Pereira       - A61887
 * @author Diogo Silva          - A76407
 * @author Maria Ana de Brito   - A73580
 */

package LEIloes;

public class LoginFailedException extends Exception {

    /**
     * Excepção atirada quando se falha no login.
     * 
     * @param msg mensagem sobre a excepção
     */
    public LoginFailedException(String msg) {
        super(msg);
    }
}
