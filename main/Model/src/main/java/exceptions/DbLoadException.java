package exceptions;

public class DbLoadException extends DbException {
    public DbLoadException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("DB_LOAD").toString();
    }
}
