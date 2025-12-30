package academy.hangman.repository;

import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.Word;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryWordRepository implements WordRepository {
    private final List<Word> words;
    private final Random random;

    public InMemoryWordRepository() {
        this.random = new Random();
        this.words = initializeWords();
    }

    private List<Word> initializeWords() {
        List<Word> wordList = new ArrayList<>();

        // Животные
        wordList.add(new Word("кот", Category.ANIMALS, Difficulty.EASY, "Домашний питомец, мурлычет"));
        wordList.add(new Word("собака", Category.ANIMALS, Difficulty.EASY, "Лучший друг человека"));
        wordList.add(new Word("слон", Category.ANIMALS, Difficulty.EASY, "Самое большое наземное животное"));
        wordList.add(new Word("жираф", Category.ANIMALS, Difficulty.MEDIUM, "Самое высокое животное"));
        wordList.add(new Word("бегемот", Category.ANIMALS, Difficulty.MEDIUM, "Живет в воде, очень тяжелый"));
        wordList.add(new Word("крокодил", Category.ANIMALS, Difficulty.HARD, "Зеленый хищник, живет в реках"));

        // Фрукты
        wordList.add(new Word("яблоко", Category.FRUITS, Difficulty.EASY, "Красный или зеленый, растет на дереве"));
        wordList.add(new Word("банан", Category.FRUITS, Difficulty.EASY, "Желтый, длинный фрукт"));
        wordList.add(new Word("апельсин", Category.FRUITS, Difficulty.MEDIUM, "Оранжевый цитрус"));
        wordList.add(new Word("ананас", Category.FRUITS, Difficulty.MEDIUM, "Тропический фрукт с колючей кожурой"));
        wordList.add(new Word("гранат", Category.FRUITS, Difficulty.MEDIUM, "Красный, много зерен внутри"));
        wordList.add(new Word("маракуйя", Category.FRUITS, Difficulty.HARD, "Экзотический фрукт"));

        // Страны
        wordList.add(new Word("россия", Category.COUNTRIES, Difficulty.EASY, "Самая большая страна в мире"));
        wordList.add(new Word("китай", Category.COUNTRIES, Difficulty.EASY, "Великая стена"));
        wordList.add(new Word("италия", Category.COUNTRIES, Difficulty.MEDIUM, "Родина пиццы"));
        wordList.add(new Word("франция", Category.COUNTRIES, Difficulty.MEDIUM, "Эйфелева башня"));
        wordList.add(new Word("бразилия", Category.COUNTRIES, Difficulty.HARD, "Карнавал в Рио"));
        wordList.add(new Word("аргентина", Category.COUNTRIES, Difficulty.HARD, "Танго и футбол"));

        // Профессии
        wordList.add(new Word("врач", Category.PROFESSIONS, Difficulty.EASY, "Лечит людей"));
        wordList.add(new Word("учитель", Category.PROFESSIONS, Difficulty.EASY, "Учит детей в школе"));
        wordList.add(new Word("повар", Category.PROFESSIONS, Difficulty.EASY, "Готовит еду"));
        wordList.add(new Word("инженер", Category.PROFESSIONS, Difficulty.MEDIUM, "Создает технику"));
        wordList.add(new Word("архитектор", Category.PROFESSIONS, Difficulty.HARD, "Проектирует здания"));
        wordList.add(new Word("программист", Category.PROFESSIONS, Difficulty.HARD, "Пишет код"));

        return wordList;
    }

    @Override
    public Optional<Word> getRandomWord(Category category, Difficulty difficulty) {
        List<Word> filtered = words.stream()
            .filter(w -> w.category() == category && w.difficulty() == difficulty)
            .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(filtered.get(random.nextInt(filtered.size())));
    }

    @Override
    public Optional<Word> getRandomWord() {
        if (words.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(words.get(random.nextInt(words.size())));
    }

    @Override
    public List<Word> getAllWords() {
        return new ArrayList<>(words);
    }
}
