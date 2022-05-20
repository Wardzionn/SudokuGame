package exceptions;

public class WriteToFileException extends FileOperationException {
    public WriteToFileException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("WRITE_TO_FILE").toString();
    }
}
