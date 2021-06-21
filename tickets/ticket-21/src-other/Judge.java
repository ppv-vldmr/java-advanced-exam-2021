
import java.util.*;

public class Judge {
    // :NOTE: * Модификаторы доступа
    // :NOTE: - codestyle
    int tries, cockroaches, size;

    static List<Integer> generate(int size) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ans.add((int) (Math.random() * size));
        }
        return ans;
    }

    void begin() {
        List<Cockroach> cockroachList = Collections.nCopies(cockroaches, new Cockroach());
        List<Integer> scores = new ArrayList<>(Collections.nCopies(cockroaches, 0));
        for (int t = 0; t < tries; t++) {
            // :NOTE: - \n -> %n системо независимый перевод строки
            System.out.format("Round %d\n", t + 1);
            System.out.println("---------------");
            final List<Thread> threadList = new ArrayList<>();
            final List<Integer> round = generate(size);
            final List<Integer> leaderboard = new ArrayList<>();
            for (int i = 0; i < cockroaches; i++) {
                int finalI = i;
                threadList.add(new Thread(() -> {
                    cockroachList.get(finalI).run(round);
                    synchronized (leaderboard) {
                        leaderboard.add(finalI);
                    }
                }));
            }
            for (Thread thread : threadList) thread.start();
            for (Thread thread : threadList) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.err.format("Can't join thread : \n%s\n", e.getMessage());
                }
            }
            if (leaderboard.size() != cockroaches) {
                System.err.println("Error, uups!");
                return;
            }
            // :NOTE: * Статистика кто на каком месте по сумме раундов, или хотя бы в конце.
            for (int i = 0; i < cockroaches; i++) {
                int num = leaderboard.get(i);
                scores.set(num, scores.get(num) + cockroaches - i);
                System.out.format("Place is %d. Number of cockroach is %d. Score is %d\n",
                                            i + 1, num + 1, scores.get(num));
            }
            System.out.println("---------------");
        }
        HashMap<Integer, Integer> sc = new HashMap<>();
        for (int i = 1; i <= cockroaches; i++) sc.put(i, scores.get(i - 1));
        Comparator<Map.Entry<Integer,Integer>> c = Map.Entry.comparingByValue();
        System.out.println("Results : ");
        sc.entrySet().stream().sorted(c.reversed()).forEach(p -> System.out.format("%d : %d%n", p.getKey(), p.getValue()));

    }

    public Judge(int tries, int cockroaches, int size) {
        this.tries = tries;
        this.cockroaches = cockroaches;
        this.size = size;
    }
}
