package academy.hangman.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsoleInputTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    private ConsoleInput createConsoleInput(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        return new ConsoleInput(scanner);
    }

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void shouldReturnValidLetterInput() {
        String input = "а\n";
        ConsoleInput consoleInput = createConsoleInput(input);
        char result = consoleInput.readLetter();
        assertThat(result).isEqualTo('а');
        tearDown();
    }

    @Test
    void shouldConvertToLowerCase() {
        String input = "А\n";
        ConsoleInput consoleInput = createConsoleInput(input);
        char result = consoleInput.readLetter();
        assertThat(result).isEqualTo('а');
        tearDown();
    }

    @Test
    void shouldRejectMultipleCharactersAndRetryWithValidInput() {
        // Arrange - сначала вводим несколько символов, потом правильный
        String input = "abc\nа\n";
        ConsoleInput consoleInput = createConsoleInput(input);

        // Act
        char result = consoleInput.readLetter();

        // Assert
        assertThat(result).isEqualTo('а');
        String output = outputStream.toString();
        assertThat(output).contains("введите только одну букву");
        tearDown();
    }

    @Test
    void shouldRejectEmptyInputAndRetryWithValidInput() {
        // Arrange - сначала пустой ввод, потом правильный
        String input = "\nа\n";
        ConsoleInput consoleInput = createConsoleInput(input);

        // Act
        char result = consoleInput.readLetter();

        // Assert
        assertThat(result).isEqualTo('а');
        String output = outputStream.toString();
        assertThat(output).contains("пустой ввод");
        tearDown();
    }

    @Test
    void shouldRejectNonLetterCharactersAndRetryWithValidInput() {
        // Arrange - сначала цифра, потом правильная буква
        String input = "5\nа\n";
        ConsoleInput consoleInput = createConsoleInput(input);

        // Act
        char result = consoleInput.readLetter();

        // Assert
        assertThat(result).isEqualTo('а');
        String output = outputStream.toString();
        assertThat(output).contains("введите букву, а не другой символ");
        tearDown();
    }

    @Test
    void shouldRejectSpecialCharactersAndRetryWithValidInput() {
        // Arrange - сначала спецсимвол, потом правильная буква
        String input = "@\nб\n";
        ConsoleInput consoleInput = createConsoleInput(input);

        // Act
        char result = consoleInput.readLetter();

        // Assert
        assertThat(result).isEqualTo('б');
        String output = outputStream.toString();
        assertThat(output).contains("введите букву, а не другой символ");
        tearDown();
    }

    @Test
    void shouldHandleMultipleInvalidInputsBeforeValidOne() {
        // Arrange - несколько неправильных вводов подряд
        String input = "\n123\n@#$\nвалидно\nв\n";
        ConsoleInput consoleInput = createConsoleInput(input);

        // Act
        char result = consoleInput.readLetter();

        // Assert
        assertThat(result).isEqualTo('в');
        String output = outputStream.toString();
        assertThat(output).contains("пустой ввод");
        assertThat(output).contains("введите только одну букву");
        tearDown();
    }

    @Test
    void shouldReadLineCorrectly() {
        // Arrange
        String input = "тестовая строка\n";
        ConsoleInput consoleInput = createConsoleInput(input);

        // Act
        String result = consoleInput.readLine("Введите строку: ");

        // Assert
        assertThat(result).isEqualTo("тестовая строка");
        tearDown();
    }

    @Test
    void shouldTrimSpacesInReadLine() {
        // Arrange
        String input = "  пробелы  \n";
        ConsoleInput consoleInput = createConsoleInput(input);

        // Act
        String result = consoleInput.readLine("Введите: ");

        // Assert
        assertThat(result).isEqualTo("пробелы");
        tearDown();
    }
}
