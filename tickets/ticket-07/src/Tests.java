import org.junit.Assert;
import org.junit.Test;

public class Tests {

    private ClassesRelationships classesRelationships;

    private void prepare(String testFile) {
        classesRelationships = new ClassesRelationships();
        classesRelationships.logger = new StringBuilder();
        classesRelationships.main(new String[]{"tickets/ticket-07/src/" + testFile});
    }

    @Test
    public void twoLists() {
        prepare("testFiles/test01");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are the same (coincidence)\n" +
                "Classes have the same package\n" +
                "No one is ancestor of java.util.List and java.util.List\n" +
                "Common interfaces: java.util.Collection java.lang.Iterable \n" +
                "No common classes\n"
        );
    }

    @Test
    public void listAndArrayList() {
        prepare("testFiles/test02");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are different\n" +
                "Classes have the same package\n" +
                "java.util.List is ancestor of java.util.ArrayList\n" +
                "Common interfaces: java.util.Collection java.lang.Iterable \n" +
                "No common classes\n"
        );
    }

    @Test
    public void arrayListAndList() {
        prepare("testFiles/test03");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are different\n" +
                "Classes have the same package\n" +
                "java.util.List is ancestor of java.util.ArrayList\n" +
                "Common interfaces: java.util.Collection java.lang.Iterable \n" +
                "No common classes\n"
        );
    }

    @Test
    public void listAndCollection() {
        prepare("testFiles/test04");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are different\n" +
                "Classes have the same package\n" +
                "java.util.Collection is ancestor of java.util.List\n" +
                "Common interfaces: java.lang.Iterable \n" +
                "No common classes\n"
        );
    }

    @Test
    public void listAndAbstractList() {
        prepare("testFiles/test05");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are different\n" +
                "Classes have the same package\n" +
                "java.util.List is ancestor of java.util.AbstractList\n" +
                "Common interfaces: java.util.Collection java.lang.Iterable \n" +
                "No common classes\n"
        );
    }

    @Test
    public void abstractListAndArrayList() {
        prepare("testFiles/test06");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are different\n" +
                "Classes have the same package\n" +
                "java.util.AbstractList is ancestor of java.util.ArrayList\n" +
                "Common interfaces: java.util.List java.util.Collection java.lang.Iterable \n" +
                "Common classes: java.util.AbstractCollection java.lang.Object \n"
        );
    }

    @Test
    public void providerAndArrayList() {
        prepare("testFiles/test07");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are different\n" +
                "Classes have different packages\n" +
                "No one is ancestor of java.security.Provider and java.util.ArrayList\n" +
                "No common interfaces\n" +
                "Common classes: java.lang.Object \n"
        );
    }

    @Test
    public void listAndMap() {
        prepare("testFiles/test08");
        Assert.assertEquals(
                classesRelationships.logger.toString(),
                "Classes are different\n" +
                "Classes have the same package\n" +
                "No one is ancestor of java.util.List and java.util.Map\n" +
                "No common interfaces\n" +
                "No common classes\n"
        );
    }
}