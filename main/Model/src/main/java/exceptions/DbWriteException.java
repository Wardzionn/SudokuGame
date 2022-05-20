package exceptions;

public class DbWriteException extends DbException {
    public DbWriteException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("DB_WRITE").toString();
    }
}
