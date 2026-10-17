package ru.nsu.blackjack;

/**
 * Класс, представляющий игровую карту.
 */
public class Card {
    private final Suit suit;
    private final Rank rank;

    /**
     * Конструктор карты.
     *
     * @param suit масть карты
     * @param rank достоинство карты
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Возвращает достоинство карты.
     *
     * @return достоинство карты
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает стоимость карты в очках.
     *
     * @return количество очков
     */
    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}