package exceptions;

public class DbInsertBoardException extends DbException {
    public DbInsertBoardException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("DB_INSERTB").toString();
    }
}
