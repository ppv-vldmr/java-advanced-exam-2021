public class Main {
    public static void main(String[] args) {
        MergeSort ms = new MergeSort();
        Integer[] array = {5, 1, 4, 0, 3, 2, 11, 6, 20, 18, 19};
        Integer[] arraySort = ms.Sort(array, 3);
        for (int i : arraySort) {
            System.out.print(i + " ");
        }
    }
}
