package exceptions;

public class DbCreateTablesException extends DbException {
    public DbCreateTablesException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("DB_CREATETAB").toString();
    }
}
