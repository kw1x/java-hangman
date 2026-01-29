package academy.hangman.repository;

import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.Word;
import java.util.List;
import java.util.Optional;

public interface WordRepository {
    Optional<Word> getRandomWord(Category category, Difficulty difficulty);

    Optional<Word> getRandomWord();

    List<Word> getAllWords();
}
