import java.io.IOException;

public class CompositeLogger implements Logger {
    private final FileLogger fileLogger;
    private final HTMLLogger htmlLogger;

    public CompositeLogger(Settings fileLoggerSettings, Settings htmlLoggerSettings) throws IOException {
        this.fileLogger = new FileLogger(fileLoggerSettings);
        this.htmlLogger = new HTMLLogger(htmlLoggerSettings);
    }

    @Override
    public void log(String message) {
        fileLogger.log(message);
        htmlLogger.log(message);
    }

    @Override
    public void log(String message, Throwable cause) {
        fileLogger.log(message, cause);
        htmlLogger.log(message, cause);
    }

    @Override
    public void close() throws Exception {
        fileLogger.close();
        htmlLogger.close();
    }
}
