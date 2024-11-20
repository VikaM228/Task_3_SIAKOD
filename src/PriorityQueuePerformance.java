import java.util.*;

public class PriorityQueuePerformance {

    // Класс для приоритетной очереди на основе кучи
    static class PriorityQueueHeap {
        private PriorityQueue<Element> heap;

        public PriorityQueueHeap() {
            heap = new PriorityQueue<>((a, b) -> Integer.compare(b.priority, a.priority)); // Сортировка по приоритету (по убыванию)
        }

        // Вставка кучи
        public void insert(String element, int priority) {
            heap.offer(new Element(element, priority));
        }

        // Извлечение кучи
        public String extractMax() {
            if (heap.isEmpty()) {
                return null;
            }
            return heap.poll().element;
        }
    }

    // Класс для элемента с приоритетом
    static class Element {
        String element;
        int priority;

        public Element(String element, int priority) {
            this.element = element;
            this.priority = priority;
        }
    }

    // Класс для приоритетной очереди на основе отсортированного массива
    static class PriorityQueueSortedArray {
        private List<Element> array;

        public PriorityQueueSortedArray() {
            array = new ArrayList<>();
        }

        // Вставка массив
        public void insert(String element, int priority) {
            int index = Collections.binarySearch(array, new Element(element, priority),
                    (a, b) -> Integer.compare(b.priority, a.priority));
            if (index < 0) {
                index = -index - 1;
            }
            array.add(index, new Element(element, priority)); // Вставка в нужное место
        }

        // Извлечение массив
        public String extractMax() {
            if (array.isEmpty()) {
                return null;
            }
            return array.remove(array.size() - 1).element; // Удаление последнего элемента
        }
    }

    // Метод для измерения производительности
    public static long[] measurePerformance(Object queue, int numOperations) {
        long startTime, endTime;

        //  время вставки
        startTime = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            if (queue instanceof PriorityQueueHeap) {
                ((PriorityQueueHeap) queue).insert("element_" + i, i);
            } else if (queue instanceof PriorityQueueSortedArray) {
                ((PriorityQueueSortedArray) queue).insert("element_" + i, i);
            }
        }
        endTime = System.nanoTime();
        long insertTime = endTime - startTime;

        // время извлечения
        startTime = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            if (queue instanceof PriorityQueueHeap) {
                ((PriorityQueueHeap) queue).extractMax();
            } else if (queue instanceof PriorityQueueSortedArray) {
                ((PriorityQueueSortedArray) queue).extractMax();
            }
        }
        endTime = System.nanoTime();
        long extractTime = endTime - startTime;

        return new long[]{insertTime, extractTime};
    }

    public static void main(String[] args) {
        int[] numOperationsList = {50000, 40000, 30000, 20000, 10000};

        System.out.println("Количество операций | Вставка (Куча) | Извлечение (Куча) | Вставка (Массив) | Извлечение (Массив)");


        for (int numOperations : numOperationsList) {

            PriorityQueueHeap heapQueue = new PriorityQueueHeap();
            long[] heapTimes = measurePerformance(heapQueue, numOperations);


            PriorityQueueSortedArray arrayQueue = new PriorityQueueSortedArray();
            long[] arrayTimes = measurePerformance(arrayQueue, numOperations);

            System.out.printf("%17d | %15.6f с | %17.6f с | %16.6f с | %17.6f с\n",
                    numOperations,
                    heapTimes[0] / 1_000_000.0,
                    heapTimes[1] / 1_000_000.0,
                    arrayTimes[0] / 1_000_000.0,
                    arrayTimes[1] / 1_000_000.0);
        }
    }
}
