package exceptions;

public class DbInsertFieldException extends DbException {
    public DbInsertFieldException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("DB_INSERTF").toString();
    }
}
