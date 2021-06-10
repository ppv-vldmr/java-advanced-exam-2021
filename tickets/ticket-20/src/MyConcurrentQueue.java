import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyConcurrentQueue<E> {

    List<E> array;
    private final int numberOfElements;
    private int currentNumbersOfElements;
    private int tail;
    private int head;

    MyConcurrentQueue(int numberOfElements) {
        this.numberOfElements = numberOfElements;
        this.array = new ArrayList<>(Collections.nCopies(this.numberOfElements, null));
        currentNumbersOfElements = 0;
        tail = 0;
        head = 0;
    }

    public void add(E element) throws InterruptedException {
        synchronized (this) {
            while (currentNumbersOfElements == numberOfElements) {
                this.wait();
            }
            currentNumbersOfElements++;
            array.set(head, element);
            head = (head + 1) % numberOfElements;
            this.notifyAll();
        }
    }

    public E remove() throws InterruptedException {
        synchronized (this) {
            while (currentNumbersOfElements == 0) {
                this.wait();
            }
            E currentElement = array.get(tail++);
            currentNumbersOfElements--;
            tail %= numberOfElements;
            this.notifyAll();
            return currentElement;
        }
    }

    public int size() {
        synchronized (this) {
            return currentNumbersOfElements;
        }
    }
}
