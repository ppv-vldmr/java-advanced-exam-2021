import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Phaser;

public class MergeSort {
    // Сортирует массив в threads потоках
    @SuppressWarnings("unchecked")
    public <T> T[] Sort(T[] a, int threads) {
        // Размер массива
        int n = a.length;

        // Выделяем n доп памяти
        T[] buf = (T[]) Array.newInstance(a[0].getClass(), n);

        // lvl - сколько "слоев" в сортировке слиянием
        // parity - чётность
        // block - размер каждого блока на каждом слое
        int lvl = log2(n - 1);
        int parity = 0;
        int block = 1;

        // Выполняем цикл lvl раз
        while (lvl-- > 0) {
            // nextBlock - Размер блока на след.уровне
            // countDefault - количество блоков размера nextBlock
            int nextBlock = 2 * block;
            int countDefault = n / nextBlock;

            // Всего блоков на след.уровне(в том числа и не размера nextBlock) - нужно для определение макс.числа потоков
            int allBlocks = (countDefault + (n % nextBlock > 0 ? 1 : 0));
            // Число потоков(может быть меньше, чем требовалось изначально
            int allow = Math.min(threads, allBlocks);

            // Мин.количество блоков, которые будет сортироваться одним потоком
            int div = allBlocks / allow;
            // Количество блоков, которые необходимо как-то распределить между потоками (если allBlock не делится нацело на allow)
            int mod = allBlocks % allow;

            // Переменная для того, чтобы не создавать один лишний поток (описано ниже)
            int startMod = (0 < mod ? 1 : 0);
            // Индекс в "исходном" массиве
            int start = (div + startMod) * nextBlock;
            // Для синхронизации
            Phaser phaser = new Phaser(1);
            // Задачи сортировки
            ArrayList<Runnable> voids = new ArrayList<>();
            // Пройдемся столько раз, сколько потоков
            for (int i = 1; i < allow; i++) {
                // У mod потоков необходимо будет вызвать сортировку для div+1 блоков, а не div
                // Сама асинхронная задача
                int finalMod = i < mod ? 1 : 0;
                int finalStart = start;
                int finalParity = parity;
                int finalBlock = block;
                phaser.register();
                voids.add(() -> {
                    // Пройдемся по div+fixMod блокам и отсортируем их последовательно в рамках каждого потока(задачи)
                    for (int j = 0; j < div + finalMod; j++) {
                        // Вызов метода, который сливает два массива в один
                        if (finalParity == 0) {
                            OnRange(a, buf, finalStart + j * nextBlock, finalBlock);
                        } else {
                            OnRange(buf, a, finalStart + j * nextBlock, finalBlock);
                        }
                    }
                    phaser.arriveAndDeregister();
                });
                // Двигаем индекс в исходном массиве
                start += (div + finalMod) * nextBlock;
            }

            // Для каждой задачи вызываем её
            voids.forEach(Runnable::run);

            // Вместо того, чтобы вызывать из текущего потока X потоков, вызовем X-1 поток, а в текущем потоке выполним непосредственно часть работы
            for (int j = 0; j < div + startMod; j++) {
                // Вызов метода, который сливает два массива в один
                if (parity == 0) {
                    OnRange(a, buf, j * nextBlock, block);
                } else {
                    OnRange(buf, a, j * nextBlock, block);
                }
            }

            // После вызова всех задач ждем окончания всех
            phaser.arriveAndAwaitAdvance();

            // Размер блока вдвое больше
            block *= 2;
            // Меняем номера массивов-from и массивов-to
            parity ^= 1;
        }

        if (parity == 1) {
            return buf;
        }
        return a;
    }

    // math.log2()
    private int log2(int n) {
        int res = 0;
        while (n > 0) {
            res++;
            n /= 2;
        }
        return res;
    }

    // Сливает данные из from в to
    // i1 - Указатель на начало левого подмассива в from, i1 + cnt - указатель на начало правого подмассива в from
    // cnt - длины каждого из подмассивов
    // В to[i1...i1+2*cnt-1] будет находится слитый массив
    @SuppressWarnings("unchecked")
    private static <T> void OnRange(T[] from, T[] to, int i1, int cnt) {
        // Предотварщение выход на пределы массива (Math.Min())
        int i2 = i1 + cnt;
        int b1 = Math.min(from.length, i2);
        int b2 = Math.min(from.length, i2 + cnt);
        int i = i1;
        // Стандартная процедура слияния массивов
        while (true) {
            if (i1 < b1 && i2 < b2) {
                if (((Comparable)from[i1]).compareTo(from[i2]) <= 0) {
                    to[i++] = from[i1++];
                }
                else {
                    to[i++] = from[i2++];
                }
            }
            else if (i1 < b1) {
                for (int j = i1; j < b1; j++) {
                    to[i++] = from[j];
                }
                break;
            }
            else if (i2 < b2) {
                for (int j = i2; j < b2; j++) {
                    to[i++] = from[j];
                }
                break;
            }
            else {
                break;
            }
        }
    }
}
