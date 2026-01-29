package academy.hangman.ui;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput() {
        this.scanner = new Scanner(System.in);
    }

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public char readLetter() {
        while (true) {
            System.out.print("\nВведите букву: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка: пустой ввод. Попробуйте снова.");
                continue;
            }

            if (input.length() > 1) {
                System.out.println("Ошибка: введите только одну букву.");
                continue;
            }

            char letter = input.charAt(0);
            if (Character.isLetter(letter)) {
                return Character.toLowerCase(letter);
            }
            System.out.println("Ошибка: введите букву, а не другой символ.");
        }
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Ошибка: число должно быть между " + min + " и " + max);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число.");
            }
        }
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (да/нет): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("да") || input.equals("yes") || input.equals("y") || input.equals("д")) {
                return true;
            }
            if (input.equals("нет") || input.equals("no") || input.equals("n") || input.equals("н")) {
                return false;
            }

            System.out.println("Ошибка: введите 'да' или 'нет'.");
        }
    }

    public void close() {
        scanner.close();
    }
}
