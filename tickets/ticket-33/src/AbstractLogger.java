import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractLogger implements Logger {
    protected final ExecutorService workers;
    protected final Settings settings;
    protected final BufferedWriter writer;

    public AbstractLogger(Settings settings) throws IOException {
        this.workers = Executors.newFixedThreadPool(settings.threads);
        this.settings = settings;
        this.writer = new BufferedWriter(new FileWriter(settings.filename));
    }

    private String getTimeOrEmpty() {
        if (settings.isTime) {
            return ZonedDateTime.now().toLocalTime().toString();
        } else {
            return "";
        }
    }

    private String getDateOrEmpty() {
        ZonedDateTime time = ZonedDateTime.now();
        if (settings.isDate) {
            return time.getDayOfMonth() + ":" + time.getMonth() + ":" + time.getYear();
        } else {
            return "";
        }
    }

    private String wrapMessage(String message) {
        return String.format("[%s]:[%s]:[%s]:[%s]%n",
                settings.priority.toString(), getDateOrEmpty(), getTimeOrEmpty(), message);
    }

    private Runnable getWorkerTask(String message) {
        final String finalMessage = wrapMessage(message);
        return () -> {
            try {
                processMessageImpl(finalMessage);
            } catch (IOException e) {
                System.err.printf("Logging error: cause{%s}, log{%s}%n", e.getMessage(), message);
            }
        };
    }

    @Override
    public void log(String message) {
        workers.submit(getWorkerTask(String.format("MESSAGE: %s", message)));
    }

    @Override
    public void log(String message, Throwable cause) {
        workers.submit(getWorkerTask(String.format("MESSAGE: %s; CAUSE: %s", message, cause.getMessage())));
    }

    @Override
    public void close() {
        try {
            endImpl();
        } catch (IOException e) {
            System.err.println("Finalizing error: " + e.getMessage());
        }
        workers.shutdown(); // todo write normal wait
        try {
            if (workers.awaitTermination(5, TimeUnit.SECONDS)) {
                return;
            }
            workers.shutdownNow();
        } catch (InterruptedException e) {
            // ignored
        }
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Writer close error: " + e.getMessage());
        }
    }

    protected abstract void processMessageImpl(String message) throws IOException;

    protected abstract void endImpl() throws IOException;
}
