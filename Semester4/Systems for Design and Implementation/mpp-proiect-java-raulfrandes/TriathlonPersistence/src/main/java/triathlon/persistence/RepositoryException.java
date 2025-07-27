package triathlon.persistence;

public class RepositoryException extends RuntimeException{
    public RepositoryException() {
    }
    public RepositoryException(String message) {
        super(message);
    }
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause, boolean enableSuppresion, boolean writableStackTrace) {
        super(message, cause, enableSuppresion, writableStackTrace);
    }
}
