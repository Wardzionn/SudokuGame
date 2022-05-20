package exceptions;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class DbException extends SQLException {

    public DbException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    ResourceBundle bundle = ResourceBundle.getBundle("bundles.exceptions");
}
