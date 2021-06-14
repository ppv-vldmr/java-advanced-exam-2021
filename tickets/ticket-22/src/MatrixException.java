public class MatrixException extends Exception {
    public MatrixException(final String message, final Throwable problem) {
        super(message + " ", problem);
    }
}
