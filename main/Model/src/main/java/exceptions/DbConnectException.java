package exceptions;

public class DbConnectException extends DbException {
    public DbConnectException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("DB_CONNECT").toString();
    }
}
