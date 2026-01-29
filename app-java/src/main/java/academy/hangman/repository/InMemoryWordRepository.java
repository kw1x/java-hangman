package academy.hangman.repository;

import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.Word;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryWordRepository implements WordRepository {
    private final List<Word> words = new ArrayList<>();
    private final Random random;

    {
        // Животные
        words.add(new Word("кот", Category.ANIMALS, Difficulty.EASY, "Домашний питомец, мурлычет"));
        words.add(new Word("собака", Category.ANIMALS, Difficulty.EASY, "Лучший друг человека"));
        words.add(new Word("слон", Category.ANIMALS, Difficulty.EASY, "Самое большое наземное животное"));
        words.add(new Word("жираф", Category.ANIMALS, Difficulty.MEDIUM, "Самое высокое животное"));
        words.add(new Word("бегемот", Category.ANIMALS, Difficulty.MEDIUM, "Живет в воде, очень тяжелый"));
        words.add(new Word("крокодил", Category.ANIMALS, Difficulty.HARD, "Зеленый хищник, живет в реках"));

        // Фрукты
        words.add(new Word("яблоко", Category.FRUITS, Difficulty.EASY, "Красный или зеленый, растет на дереве"));
        words.add(new Word("банан", Category.FRUITS, Difficulty.EASY, "Желтый, длинный фрукт"));
        words.add(new Word("апельсин", Category.FRUITS, Difficulty.MEDIUM, "Оранжевый цитрус"));
        words.add(new Word("ананас", Category.FRUITS, Difficulty.MEDIUM, "Тропический фрукт с колючей кожурой"));
        words.add(new Word("гранат", Category.FRUITS, Difficulty.MEDIUM, "Красный, много зерен внутри"));
        words.add(new Word("маракуйя", Category.FRUITS, Difficulty.HARD, "Экзотический фрукт"));

        // Страны
        words.add(new Word("россия", Category.COUNTRIES, Difficulty.EASY, "Самая большая страна в мире"));
        words.add(new Word("китай", Category.COUNTRIES, Difficulty.EASY, "Великая стена"));
        words.add(new Word("италия", Category.COUNTRIES, Difficulty.MEDIUM, "Родина пиццы"));
        words.add(new Word("франция", Category.COUNTRIES, Difficulty.MEDIUM, "Эйфелева башня"));
        words.add(new Word("бразилия", Category.COUNTRIES, Difficulty.HARD, "Карнавал в Рио"));
        words.add(new Word("аргентина", Category.COUNTRIES, Difficulty.HARD, "Танго и футбол"));

        // Профессии
        words.add(new Word("врач", Category.PROFESSIONS, Difficulty.EASY, "Лечит людей"));
        words.add(new Word("учитель", Category.PROFESSIONS, Difficulty.EASY, "Учит детей в школе"));
        words.add(new Word("повар", Category.PROFESSIONS, Difficulty.EASY, "Готовит еду"));
        words.add(new Word("инженер", Category.PROFESSIONS, Difficulty.MEDIUM, "Создает технику"));
        words.add(new Word("архитектор", Category.PROFESSIONS, Difficulty.HARD, "Проектирует здания"));
        words.add(new Word("программист", Category.PROFESSIONS, Difficulty.HARD, "Пишет код"));
    }

    public InMemoryWordRepository() {
        this.random = new Random();
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
