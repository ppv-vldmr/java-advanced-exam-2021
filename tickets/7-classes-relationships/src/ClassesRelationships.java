import java.io.*;
import java.lang.reflect.AnnotatedType;

public class ClassesRelationships {

    private static Class<?> aClass;
    private static Class<?> bClass;
    private static boolean same, samePkg;
    private static String ancestor;
    private static String commonInterfaces, commonClasses;

    private static void checkCoincidence() {
        same = aClass.getCanonicalName().equals(bClass.getCanonicalName());
        if (same) {
            System.out.println("Classes are the same (coincidence)");
        }
    }

    private static void checkSamePackages() {
        if (!same) {
            samePkg = aClass.getPackage().equals(bClass.getPackage());
            if (samePkg) {
                System.out.println("Classes have the same package");
            }
        }
    }


    private static void checkAncestor() throws ClassNotFoundException {
        if (!same) {
            if (ancestor.equals("") || ancestor.equals("#ERROR")) {
                ancestor = checkAncestorInterface(aClass, bClass);
                if (!ancestor.equals("") && !ancestor.equals("#ERROR")) {
                    System.out.println(bClass + " is ancestor of " + aClass);
                }
            }
            if (ancestor.equals("") || ancestor.equals("#ERROR")) {
                ancestor = checkAncestorClass(aClass, bClass);
                if (!ancestor.equals("") && !ancestor.equals("#ERROR")) {
                    System.out.println(bClass + " is ancestor of " + aClass);
                }
            }
            if (ancestor.equals("") || ancestor.equals("#ERROR")) {
                ancestor = checkAncestorInterface(bClass, aClass);
                if (!ancestor.equals("") && !ancestor.equals("#ERROR")) {
                    System.out.println(aClass + " is ancestor of " + bClass);
                }
            }
            if (ancestor.equals("") || ancestor.equals("#ERROR")) {
                ancestor = checkAncestorClass(bClass, aClass);
                if (!ancestor.equals("") && !ancestor.equals("#ERROR")) {
                    System.out.println(aClass + " is ancestor of " + bClass);
                }
            }
            if (ancestor.equals("") || ancestor.equals("#ERROR")) {
                System.out.println("No one is ancestor of " + aClass.getCanonicalName() + " and " + bClass.getCanonicalName());
            }
        }
    }

    private static String checkAncestorClass(Class<?> a, Class<?> b) throws ClassNotFoundException {
        AnnotatedType annotatedType = a.getAnnotatedSuperclass();
        if (annotatedType != null) {
            Class<?> superClass = convertAnnotatedTypeToClass(annotatedType);
            if (superClass.getCanonicalName().equals(b.getCanonicalName())) {
                return superClass.getCanonicalName();
            } else {
                return checkAncestorClass(superClass, b);
            }
        } else {
            return "#ERROR";
        }
    }

    private static String checkAncestorInterface(Class<?> a, Class<?> b) throws ClassNotFoundException {
        AnnotatedType[] superClasses = a.getAnnotatedInterfaces();
        for (AnnotatedType annotatedType : superClasses) {
            Class<?> cur = convertAnnotatedTypeToClass(annotatedType);
            if (cur.getCanonicalName().equals(b.getCanonicalName())) {
                return cur.getCanonicalName();
            }
        }
        for (AnnotatedType annotatedType : superClasses) {
            String ans = checkAncestorInterface(convertAnnotatedTypeToClass(annotatedType), b);
            if (!ans.equals("#ERROR")) {
                return ans;
            }
        }
        return "#ERROR";
    }

    private static Class<?> convertAnnotatedTypeToClass(AnnotatedType annotatedType) throws ClassNotFoundException {
        int pos = !annotatedType.getType().getTypeName().contains("<") ? annotatedType.getType().getTypeName().length() : annotatedType.getType().getTypeName().indexOf("<");
        return Class.forName(annotatedType.getType().getTypeName().substring(0, pos));
    }

    private static void findCommonInterfaces() throws ClassNotFoundException {
        StringBuilder ans = new StringBuilder();
        String[] interfacesA = findAllInterfaces(aClass).split(" ");
        String[] interfacesB = findAllInterfaces(bClass).split(" ");
        for (String fromA : interfacesA) {
            for (String fromB : interfacesB) {
                if (fromA.equals(fromB) && !ans.toString().contains(fromA)) {
                    ans.append(fromA).append(" ");
                }
            }
        }
        commonInterfaces = ans.toString();
        if (commonInterfaces.split(" ").length == 0) {
            System.out.println("No common interfaces");
        } else {
            System.out.println("Common interfaces: " + commonInterfaces);
        }
    }

    private static String findAllInterfaces(Class<?> a) throws ClassNotFoundException {
        StringBuilder res = new StringBuilder();
        AnnotatedType[] superClasses = a.getAnnotatedInterfaces();
        for (AnnotatedType annotatedType : superClasses) {
            Class<?> cur = convertAnnotatedTypeToClass(annotatedType);
            res.append(cur.getCanonicalName()).append(" ");
        }
        for (AnnotatedType annotatedType : superClasses) {
            Class<?> cur = convertAnnotatedTypeToClass(annotatedType);
            res.append(findAllInterfaces(cur));
        }
        return res.toString();
    }

    private static String findAllClasses(Class<?> a) throws ClassNotFoundException {
        StringBuilder res = new StringBuilder();
        AnnotatedType superClass = a.getAnnotatedSuperclass();
        if (superClass == null) {
            return "";
        }
        Class<?> cur = convertAnnotatedTypeToClass(superClass);
        res.append(cur.getCanonicalName()).append(" ").append(findAllClasses(cur));
        return res.toString();
    }

    private static void findCommonAncestor() throws ClassNotFoundException {
        findCommonInterfaces();
        findCommonClasses();
    }

    private static void findCommonClasses() throws ClassNotFoundException {
        StringBuilder ans = new StringBuilder();
        String[] classesA = findAllClasses(aClass).split(" ");
        String[] classesB = findAllClasses(bClass).split(" ");
        for (String fromA : classesA) {
            for (String fromB : classesB) {
                if (fromA.equals(fromB) && !ans.toString().contains(fromA)) {
                    ans.append(fromA).append(" ");
                }
            }
        }
        commonClasses = ans.toString();
        if (commonClasses.split(" ").length == 0) {
            System.out.println("No common classes");
        } else {
            System.out.println("Common classes: " + commonClasses);
        }
    }

    private static void globalCheck() throws ClassNotFoundException {
        checkCoincidence();
        checkSamePackages();
        checkAncestor();
        findCommonAncestor();
    }

    public static void main(String[] args) {
        if (args == null || args.length != 1 || args[0] == null) {
            System.out.println("Expected 1 non-null argument: <inputFile.name>");
            return;
        }
        same = samePkg = false;
        ancestor = "";
        commonInterfaces = "";
        commonClasses = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]));
            String[] classes = bufferedReader.readLine().split(" ");
            aClass = Class.forName(classes[0]);
            bClass = Class.forName(classes[1]);
            globalCheck();
        } catch (FileNotFoundException e) {
            System.out.println("Input file " + args[0] + " not found." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error occurred during reading input file." + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error occurred during searching class." + e.getMessage());
        }
    }
}
