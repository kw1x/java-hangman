package academy.hangman.game;

import academy.hangman.model.GameResult;
import academy.hangman.model.Word;
import java.util.HashSet;
import java.util.Set;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GameSession {
    private final Word word;
    private final int maxAttempts;
    private final Set<Character> guessedLetters;
    private int attemptsMade;

    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")   
    public GameSession(Word word, int maxAttempts) {
        if (word == null) {
            throw new IllegalArgumentException("Word cannot be null");
        }
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Max attempts must be positive");
        }
        this.word = word;
        this.maxAttempts = maxAttempts;
        this.guessedLetters = new HashSet<>();
        this.attemptsMade = 0;
    }

    public boolean makeGuess(char letter) {
        char lowerLetter = Character.toLowerCase(letter);

        if (guessedLetters.contains(lowerLetter)) {
            return false; // Already guessed
        }

        guessedLetters.add(lowerLetter);

        if (!word.contains(lowerLetter)) {
            attemptsMade++;
            return false;
        }

        return true;
    }

    public String getCurrentState() {
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (guessedLetters.contains(letter)) {
                state.append(letter);
            } else {
                state.append('*');
            }
        }
        return state.toString();
    }

    public GameResult getGameResult() {
        if (isWordGuessed()) {
            return GameResult.WON;
        }
        if (attemptsMade >= maxAttempts) {
            return GameResult.LOST;
        }
        return GameResult.IN_PROGRESS;
    }

    private boolean isWordGuessed() {
        for (int i = 0; i < word.length(); i++) {
            if (!guessedLetters.contains(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return getGameResult() != GameResult.IN_PROGRESS;
    }

    public int getAttemptsMade() {
        return attemptsMade;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getRemainingAttempts() {
        return maxAttempts - attemptsMade;
    }

    public Word getWord() {
        return word;
    }

    public Set<Character> getGuessedLetters() {
        return new HashSet<>(guessedLetters);
    }

    public boolean isLetterGuessed(char letter) {
        return guessedLetters.contains(Character.toLowerCase(letter));
    }
}
