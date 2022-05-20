package exceptions;

public class IncorrectValueException extends DaoException {

    public IncorrectValueException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }


}
