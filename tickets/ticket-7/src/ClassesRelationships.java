import java.io.*;
import java.lang.reflect.AnnotatedType;

public class ClassesRelationships {

    private static Class<?> aClass;
    private static Class<?> bClass;
    private static String ancestor;

    public static void main(String[] args) {

        if (args == null || args.length != 1 || args[0] == null) {
            System.out.println("Expected 1 non-null argument: <inputFile.name>");
            return;
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] classes = line.split(" ");
                aClass = Class.forName(classes[0]);
                bClass = Class.forName(classes[1]);
                globalCheck();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file " + args[0] + " not found." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error occurred during reading input file." + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error occurred during searching class." + e.getMessage());
        }
    }

    private static void globalCheck() throws ClassNotFoundException {
        System.out.printf("===== Check %s and %s =====%n", aClass.getCanonicalName(), bClass.getCanonicalName());
        ancestor = "";
        checkCoincidence();
        checkSamePackages();
        findAncestor();
        findCommonAncestor();
        System.out.println();
    }

    private static void checkCoincidence() {
        if (aClass.getCanonicalName().equals(bClass.getCanonicalName())) {
            System.out.println("Classes are the same (coincidence)");
        } else {
            System.out.println("Classes are different");
        }
    }

    private static void checkSamePackages() {
        if (aClass.getPackage().equals(bClass.getPackage())) {
            System.out.println("Classes have the same package");
        } else {
            System.out.println("Classes have different packages");
        }
    }

    private static void findAncestor() throws ClassNotFoundException {
        setAncestor(aClass, bClass, findAncestorInterface(aClass, bClass));
        setAncestor(bClass, aClass, findAncestorInterface(bClass, aClass));
        setAncestor(aClass, bClass, findAncestorClass(aClass, bClass));
        setAncestor(bClass, aClass, findAncestorClass(bClass, aClass));
        if (ancestor.isEmpty()) {
            System.out.println("No one is ancestor of " + aClass.getCanonicalName() + " and " + bClass.getCanonicalName());
        }
    }

    private static void setAncestor(Class<?> a, Class<?> b, String classOrInterface) {
        if (ancestor.isEmpty()) {
            ancestor = classOrInterface;
            if (!ancestor.isEmpty()) {
                System.out.println(b.getCanonicalName() + " is ancestor of " + a.getCanonicalName());
            }
        }
    }

    private static String findAncestorInterface(Class<?> a, Class<?> b) throws ClassNotFoundException {
        AnnotatedType[] annotatedInterfaces = a.getAnnotatedInterfaces();
        for (AnnotatedType annotatedType : annotatedInterfaces) {
            Class<?> cur = convertAnnotatedTypeToClass(annotatedType);
            if (cur.getCanonicalName().equals(b.getCanonicalName())) {
                return cur.getCanonicalName();
            }
        }
        for (AnnotatedType annotatedType : annotatedInterfaces) {
            String ans = findAncestorInterface(convertAnnotatedTypeToClass(annotatedType), b);
            if (!ans.isEmpty()) {
                return ans;
            }
        }
        return "";
    }
    
    private static String findAncestorClass(Class<?> a, Class<?> b) throws ClassNotFoundException {
        AnnotatedType annotatedType = a.getAnnotatedSuperclass();
        if (annotatedType != null) {
            Class<?> superClass = convertAnnotatedTypeToClass(annotatedType);
            if (superClass.getCanonicalName().equals(b.getCanonicalName())) {
                return superClass.getCanonicalName();
            } else {
                return findAncestorClass(superClass, b);
            }
        } else {
            return "";
        }
    }

    private static Class<?> convertAnnotatedTypeToClass(AnnotatedType annotatedType) throws ClassNotFoundException {
        int typeParameterPosition;
        if (!annotatedType.getType().getTypeName().contains("<")) {
            typeParameterPosition = annotatedType.getType().getTypeName().length();
        } else {
            typeParameterPosition = annotatedType.getType().getTypeName().indexOf("<");
        }
        return Class.forName(annotatedType.getType().getTypeName().substring(0, typeParameterPosition));
    }

    private static void findCommonAncestor() throws ClassNotFoundException {
        findCommonAncestorInterfaces();
        findCommonAncestorClasses();
    }

    private static void findCommonAncestorInterfaces() throws ClassNotFoundException {
        StringBuilder ans = new StringBuilder();
        String[] interfacesA = findAllAncestorInterfaces(aClass).split(" ");
        String[] interfacesB = findAllAncestorInterfaces(bClass).split(" ");

        for (String fromA : interfacesA) {
            for (String fromB : interfacesB) {
                if (fromA.equals(fromB) && !ans.toString().contains(fromA)) {
                    ans.append(fromA).append(" ");
                }
            }
        }

        commonOutput("interfaces", ans);
    }

    private static void findCommonAncestorClasses() throws ClassNotFoundException {
        StringBuilder ans = new StringBuilder();
        String[] classesA = findAllAncestorClasses(aClass).split(" ");
        String[] classesB = findAllAncestorClasses(bClass).split(" ");

        for (String fromA : classesA) {
            for (String fromB : classesB) {
                if (fromA.equals(fromB) && !ans.toString().contains(fromA)) {
                    ans.append(fromA).append(" ");
                }
            }
        }

        commonOutput("classes", ans);
    }

    private static void commonOutput(String classOrInterface, StringBuilder result) {
        if (result.toString().isEmpty()) {
            System.out.println("No common " + classOrInterface);
        } else {
            System.out.println("Common " + classOrInterface + ": " + result.toString());
        }
    }

    private static String findAllAncestorInterfaces(Class<?> a) throws ClassNotFoundException {
        StringBuilder res = new StringBuilder();
        AnnotatedType[] annotatedClasses = a.getAnnotatedInterfaces();
        for (AnnotatedType annotatedType : annotatedClasses) {
            Class<?> cur = convertAnnotatedTypeToClass(annotatedType);
            res.append(cur.getCanonicalName()).append(" ");
        }
        for (AnnotatedType annotatedType : annotatedClasses) {
            Class<?> cur = convertAnnotatedTypeToClass(annotatedType);
            res.append(findAllAncestorInterfaces(cur));
        }
        return res.toString();
    }

    private static String findAllAncestorClasses(Class<?> a) throws ClassNotFoundException {
        StringBuilder res = new StringBuilder();
        AnnotatedType superClass = a.getAnnotatedSuperclass();
        if (superClass == null) {
            return "";
        }
        Class<?> cur = convertAnnotatedTypeToClass(superClass);
        res.append(cur.getCanonicalName()).append(" ").append(findAllAncestorClasses(cur));
        return res.toString();
    }
}
