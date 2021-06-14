public class Main {
    public static void main(String[] args) {
        Logger.Settings fileLogSettings = new Logger.Settings(Logger.Priority.INFO, "log.out", true, true, 2);
        Logger.Settings htmlLogSettings = new Logger.Settings(Logger.Priority.DEBUG, "log.html", true, false, 4);

//        try (Logger logger = new HTMLLogger(htmlLogSettings)) {
//            logger.log("Hello");
//            logger.log("What?");
//            logger.log("What exc?", new RuntimeException("HUI"));
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//
//        try (Logger logger = new FileLogger(fileLogSettings)) {
//            logger.log("KEK");
//            logger.log("chefklarhldjgsdjfgdskfjgdfgjdfj?");
//            logger.log("What is exceptions?", new RuntimeException("HUI"));
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }

//        HTMLLogger НЕ РАБОТАЕТ, Я ХЗ ПОЧЕМУ

        try (Logger logger = new CompositeLogger(fileLogSettings, htmlLogSettings)) {
            logger.log("KEK");
            logger.log("chefklarhldjgsdjfgdskfjgdfgjdfj?");
            logger.log("What is exceptions?", new RuntimeException("HUI"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
