public interface Logger extends AutoCloseable {
    enum Priority {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    class Settings {
        final Priority priority;
        final boolean isDate;
        final boolean isTime;
        final int threads;
        final String filename;

        public Settings(Priority priority, String filename, boolean isDate, boolean isTime, int threads) {
            this.priority = priority;
            this.filename = filename;
            this.isDate = isDate;
            this.isTime = isTime;
            this.threads = threads;
        }
    }

    void log(String message);
    void log(String message, Throwable cause);
}
