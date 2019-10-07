package kuznietsov.gpcparser.parser.exceptions;

public class EofException extends  Exception{

    public EofException() {
    }

    public EofException(String message) {
        super(message);
    }

    public EofException(String message, Throwable cause) {
        super(message, cause);
    }

    public EofException(Throwable cause) {
        super(cause);
    }

    public EofException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
