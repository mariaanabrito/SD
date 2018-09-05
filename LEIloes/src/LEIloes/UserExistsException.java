/*
 * @author Bruno Pereira        - A75135
 * @author Carlos Pereira       - A61887
 * @author Diogo Silva          - A76407
 * @author Maria Ana de Brito   - A73580
 */

package LEIloes;


public class UserExistsException extends Exception {

    /**
     * Excepção atirada quando se falha no registo.
     * 
     * @param msg mesagem sobre a excepção
     */
    public UserExistsException(String msg) {
        super(msg);
    }
}
