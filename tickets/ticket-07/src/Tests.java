import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class Tests {

    private ClassesRelationships classesRelationships;

    @Test
    public void twoLists() throws ClassNotFoundException {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test01");
        Assert.assertTrue(classesRelationships.checkCoincidence());
        Assert.assertTrue(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "");
        Assert.assertEquals(
            classesRelationships.findCommonAncestorInterfaces(),
            Set.of(
                Class.forName("java.util.Collection"),
                Class.forName("java.lang.Iterable")
            )
        );
        Assert.assertEquals(
            classesRelationships.findCommonAncestors(),
            Set.of(
                Class.forName("java.util.Collection"),
                Class.forName("java.lang.Iterable")
            )
        );
    }

    @Test
    public void listAndArrayList() throws ClassNotFoundException {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test02");
        Assert.assertFalse(classesRelationships.checkCoincidence());
        Assert.assertTrue(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "java.util.List");
        Assert.assertEquals(
                classesRelationships.findCommonAncestorInterfaces(),
                Set.of(
                        Class.forName("java.util.Collection"),
                        Class.forName("java.lang.Iterable")
                )
        );
        Assert.assertEquals(
                classesRelationships.findCommonAncestors(),
                Set.of(
                        Class.forName("java.util.Collection"),
                        Class.forName("java.lang.Iterable")
                )
        );
    }

    @Test
    public void arrayListAndList() throws ClassNotFoundException {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test03");
        Assert.assertFalse(classesRelationships.checkCoincidence());
        Assert.assertTrue(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "java.util.List");
        Assert.assertEquals(
                classesRelationships.findCommonAncestorInterfaces(),
                Set.of(
                        Class.forName("java.util.Collection"),
                        Class.forName("java.lang.Iterable")
                )
        );
        Assert.assertEquals(
                classesRelationships.findCommonAncestors(),
                Set.of(
                        Class.forName("java.util.Collection"),
                        Class.forName("java.lang.Iterable")
                )
        );
    }

    @Test
    public void listAndCollection() throws ClassNotFoundException {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test04");
        Assert.assertFalse(classesRelationships.checkCoincidence());
        Assert.assertTrue(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "java.util.Collection");
        Assert.assertEquals(
                classesRelationships.findCommonAncestorInterfaces(),
                Set.of(
                        Class.forName("java.lang.Iterable")
                )
        );
        Assert.assertEquals(
                classesRelationships.findCommonAncestors(),
                Set.of(
                        Class.forName("java.lang.Iterable")
                )
        );
    }

    @Test
    public void listAndAbstractList() throws ClassNotFoundException {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test05");
        Assert.assertFalse(classesRelationships.checkCoincidence());
        Assert.assertTrue(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "java.util.List");
        Assert.assertEquals(
                classesRelationships.findCommonAncestorInterfaces(),
                Set.of(
                        Class.forName("java.lang.Iterable"),
                        Class.forName("java.util.Collection")
                )
        );
        Assert.assertEquals(
                classesRelationships.findCommonAncestors(),
                Set.of(
                        Class.forName("java.lang.Iterable"),
                        Class.forName("java.util.Collection")
                )
        );
    }

    @Test
    public void abstractListAndArrayList() throws ClassNotFoundException {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test06");
        Assert.assertFalse(classesRelationships.checkCoincidence());
        Assert.assertTrue(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "java.util.AbstractList");
        Assert.assertEquals(
                classesRelationships.findCommonAncestorInterfaces(),
                Set.of(
                        Class.forName("java.lang.Iterable"),
                        Class.forName("java.util.Collection"),
                        Class.forName("java.util.List")
                )
        );
        Assert.assertEquals(
                classesRelationships.findCommonAncestors(),
                Set.of(
                        Class.forName("java.lang.Iterable"),
                        Class.forName("java.util.Collection"),
                        Class.forName("java.util.List"),
                        Class.forName("java.lang.Object"),
                        Class.forName("java.util.AbstractCollection")
                )
        );
    }

    @Test
    public void providerAndArrayList() throws ClassNotFoundException {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test07");
        Assert.assertFalse(classesRelationships.checkCoincidence());
        Assert.assertFalse(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "");
        Assert.assertEquals(
                classesRelationships.findCommonAncestorInterfaces(),
                Set.of()
        );
        Assert.assertEquals(
                classesRelationships.findCommonAncestors(),
                Set.of(
                        Class.forName("java.lang.Object")
                )
        );
    }

    @Test
    public void listAndMap() {
        classesRelationships = new ClassesRelationships("tickets/ticket-07/src/testFiles/test08");
        Assert.assertFalse(classesRelationships.checkCoincidence());
        Assert.assertTrue(classesRelationships.checkSamePackages());
        Assert.assertEquals(classesRelationships.findAncestor(), "");
        Assert.assertEquals(
                classesRelationships.findCommonAncestorInterfaces(),
                Set.of()
        );
        Assert.assertEquals(
                classesRelationships.findCommonAncestors(),
                Set.of()
        );
    }
}