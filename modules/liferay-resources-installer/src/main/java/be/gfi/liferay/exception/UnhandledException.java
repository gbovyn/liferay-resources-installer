package be.gfi.liferay.exception;

public class UnhandledException extends RuntimeException {

    public UnhandledException(final Exception ex) {
        super(ex);
    }
}
