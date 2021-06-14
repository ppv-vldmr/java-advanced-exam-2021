import java.io.IOException;

public class HTMLLogger extends AbstractLogger {

    public HTMLLogger(Settings settings) throws IOException {
        super(settings);
        writer.write("<html>");
    }

    @Override
    protected void processMessageImpl(String message) throws IOException {
        writer.write("<div>" + message + "</div>"); // todo implement time/date formatting with html
    }

    @Override
    protected void endImpl() throws IOException {
        writer.write("</html>");
    }
}
