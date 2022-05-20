package exceptions;

import java.util.ResourceBundle;

public class DaoException extends Throwable {
    public DaoException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    ResourceBundle bundle = ResourceBundle.getBundle("bundles.exceptions");


}
