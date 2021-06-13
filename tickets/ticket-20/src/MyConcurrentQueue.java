import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyConcurrentQueue<E> {

    private volatile List<E> array;
    private final int numberOfElements;
    private volatile int currentNumbersOfElements;
    private volatile int tail;
    private volatile int head;

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
        return currentNumbersOfElements;
    }
}
