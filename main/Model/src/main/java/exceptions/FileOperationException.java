package exceptions;

public class FileOperationException extends DaoException {
    public FileOperationException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }


}
