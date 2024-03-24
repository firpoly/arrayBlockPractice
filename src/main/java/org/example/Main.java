package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread workThread(BlockingQueue<String> text, char letter) {
        return new Thread(() -> {
            int maxSize = 0;
            try {
                String tempText = text.take();
                String textWithOutChar = tempText.replaceAll(String.valueOf(letter), "");
                int countR = tempText.length() - (tempText.length() - textWithOutChar.length());
                if (maxSize <= countR) {
                    maxSize = countR;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(" Максимальное кол-во символов " + letter + " -> " + maxSize);
        });
    }

    public static BlockingQueue<String> textA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> textB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> textC = new ArrayBlockingQueue<>(100);
    public static Thread putText;

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[100000];
        putText = new Thread(() -> {
            for (String text : texts) {
                text = generateText("abc", 100000);
                try {
                    textA.put(text);
                    textB.put(text);
                    textC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        putText.start();
        Thread textAThred = workThread(textA, 'a');
        Thread textBThred = workThread(textB, 'b');
        Thread textCThred = workThread(textC, 'c');

        textAThred.start();
        textBThred.start();
        textCThred.start();

        textAThred.join();
        textBThred.join();
        textCThred.join();

    }
}