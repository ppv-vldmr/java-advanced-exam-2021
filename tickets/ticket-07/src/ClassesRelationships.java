import java.io.*;
import java.util.*;

public class ClassesRelationships {

    private Class<?> aClass, bClass;
    private String ancestor;
    private HashSet<Class<?>> commonInterfaces, commonClasses;

    public ClassesRelationships(String fileName) {
        if (fileName == null) {
            System.err.println("Expected 1 non-null argument: <inputFile.name>");
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] classes = line.split(" ");
                aClass = Class.forName(classes[0]);
                bClass = Class.forName(classes[1]);
                globalCheck();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find classes with given input. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("An error occurred during reading input file " + fileName + ". " + e.getMessage());
        }
    }

    public ClassesRelationships(Class<?> a, Class<?> b) {
        if (a == null || b == null) {
            System.err.println("Expected 2 non-null arguments: full.class1.name full.class2.name");
            return;
        }
        aClass = a;
        bClass = b;
        globalCheck();
    }

    private void globalCheck() {
        System.out.printf("===== Check %s and %s =====%n", aClass.getCanonicalName(), bClass.getCanonicalName());
        ancestor = "";
        commonInterfaces = new HashSet<>();
        commonClasses = new HashSet<>();
        checkCoincidence();
        checkSamePackages();
        findAncestor();
        findCommonAncestors();
        findCommonAncestorInterfaces();
        System.out.println();
    }

    public boolean checkCoincidence() {
        if (aClass.getCanonicalName().equals(bClass.getCanonicalName())) {
            System.out.println("Classes are the same (coincidence)");
        } else {
            System.out.println("Classes are different");
        }
        return aClass.getCanonicalName().equals(bClass.getCanonicalName());
    }

    public boolean checkSamePackages() {
        if (aClass.getPackage().equals(bClass.getPackage())) {
            System.out.println("Classes have the same package");
        } else {
            System.out.println("Classes have different packages");
        }
        return aClass.getPackage().equals(bClass.getPackage());
    }

    public String findAncestor() {
        setAncestor(aClass, bClass, findAncestorInterface(aClass, bClass));
        setAncestor(bClass, aClass, findAncestorInterface(bClass, aClass));
        setAncestor(aClass, bClass, findAncestorClass(aClass, bClass));
        setAncestor(bClass, aClass, findAncestorClass(bClass, aClass));
        if (ancestor.isEmpty()) {
            System.out.println("No one is ancestor of " + aClass.getCanonicalName() + " and " + bClass.getCanonicalName());
        }
        return ancestor;
    }

    private void setAncestor(Class<?> a, Class<?> b, String classOrInterface) {
        if (ancestor.isEmpty()) {
            ancestor = classOrInterface;
            if (!ancestor.isEmpty()) {
                System.out.println(b.getCanonicalName() + " is ancestor of " + a.getCanonicalName());
            }
        }
    }

    private String findAncestorInterface(Class<?> a, Class<?> b) {
        Class<?>[] superInterfaces = a.getInterfaces();
        for (Class<?> superInterface : superInterfaces) {
            if (superInterface.getCanonicalName().equals(b.getCanonicalName())) {
                return superInterface.getCanonicalName();
            }
        }
        for (Class<?> superInterface : superInterfaces) {
            String ans = findAncestorInterface(superInterface, b);
            if (!ans.isEmpty()) {
                return ans;
            }
        }
        return "";
    }
    
    private String findAncestorClass(Class<?> a, Class<?> b) {
        Class<?> superClass = a.getSuperclass();
        if (superClass != null) {
            if (superClass.getCanonicalName().equals(b.getCanonicalName())) {
                return superClass.getCanonicalName();
            } else {
                return findAncestorClass(superClass, b);
            }
        } else {
            return "";
        }
    }

    public HashSet<Class<?>> findCommonAncestors() {
        findCommonAncestorInterfaces();
        findCommonAncestorClasses();
        HashSet<Class<?>> res = new HashSet<>();
        res.addAll(commonInterfaces);
        res.addAll(commonClasses);
        return res;
    }

    public HashSet<Class<?>> findCommonAncestorInterfaces() {
        commonInterfaces = findAllAncestorInterfaces(aClass);
        commonInterfaces.retainAll(findAllAncestorInterfaces(bClass));
        commonOutput("interfaces", commonInterfaces);
        return commonInterfaces;
    }

    private void findCommonAncestorClasses() {
        commonClasses = findAllAncestorClasses(aClass);
        commonClasses.retainAll(findAllAncestorClasses(bClass));
        commonOutput("classes", commonClasses);
    }

    private void commonOutput(String classOrInterface, Set<Class<?>> result) {
        if (result.isEmpty()) {
            System.out.println("No common " + classOrInterface);
        } else {
            System.out.println("Common " + classOrInterface + ": ");
            for (Class<?> item : result) {
                System.out.print(item.getCanonicalName() + " ");
            }
        }
    }

    private HashSet<Class<?>> findAllAncestorInterfaces(Class<?> a) {
        Class<?>[] superInterfaces = a.getInterfaces();
        HashSet<Class<?>> res = new HashSet<>(Arrays.asList(superInterfaces));
        for (Class<?> superInterface : superInterfaces) {
            res.addAll(findAllAncestorInterfaces(superInterface));
        }
        return res;
    }

    private HashSet<Class<?>> findAllAncestorClasses(Class<?> a) {
        HashSet<Class<?>> res = new HashSet<>();
        Class<?> superClass = a.getSuperclass();
        if (superClass == null) {
            return res;
        }
        res.add(superClass);
        res.addAll(findAllAncestorClasses(superClass));
        return res;
    }
}
