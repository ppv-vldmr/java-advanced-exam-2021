import java.io.IOException;

public class FileLogger extends AbstractLogger {

    public FileLogger(Settings settings) throws IOException {
        super(settings);
    }

    @Override
    protected void processMessageImpl(String message) throws IOException {
        writer.write(message);
    }

    @Override
    protected void endImpl() {

    }
}
