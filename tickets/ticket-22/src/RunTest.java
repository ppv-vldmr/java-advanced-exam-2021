import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTest {
    public static void main(String[] args) {
        System.err.println("Starting tests!");
        Result result =  JUnitCore.runClasses(Tests.class);
        for (Failure failure : result.getFailures()) {
            System.err.println(failure.getTestHeader() + " : " + failure.getMessage());
        }
        if (!result.wasSuccessful()) {
            System.exit(1);
        }
        System.err.println("All tests passed!");
        System.exit(0);
    }
}
