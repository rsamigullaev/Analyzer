import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Thread a = new Thread(() -> {
            char letter = 'a';
            maxQty(queue1, letter);
        });
        a.start();

        Thread b = new Thread(() -> {
            char letter = 'b';
            maxQty(queue2, letter);
        });
        b.start();

        Thread c = new Thread(() -> {
            char letter = 'c';
            maxQty(queue3, letter);
        });
        c.start();

        a.join();
        b.join();
        c.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxQtyChar(BlockingQueue<String> queue, char letter) {
        String text;
        int count = 0;
        int max = 0;

        try {
            for (int i = 0; i < 10_000; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) count++;
                }
                if (count > max) max = count;
                count = 0;
            }
        } catch (InterruptedException e) {
            System.out.println("Ошибка!!!");
        }
        return max;
    }

    public static void maxQty(BlockingQueue<String> queue, char letter) {
        System.out.println("Максимальное количество символов " + letter + " - " + maxQtyChar(queue, letter));
    }
}
