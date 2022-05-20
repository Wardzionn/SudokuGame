package exceptions;

public class LoadFromFileException extends FileOperationException {
    public LoadFromFileException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return bundle.getObject("LOAD_FROM_FILE").toString();
    }
}
