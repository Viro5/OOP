package ru.nsu.blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий руку игрока или дилера (набор карт).
 */
public class Hand {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Добавляет карту в руку.
     *
     * @param card карта для добавления
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Возвращает карту из руки по индексу.
     *
     * @param index индекс карты
     * @return карта по указанному индексу
     */
    public Card getCard(int index) {
        return cards.get(index);
    }

    /**
     * Возвращает количество карт в руке.
     *
     * @return количество карт
     */
    public int size() {
        return cards.size();
    }

    /**
     * Вычисляет суммарное количество очков карт в руке с учетом Тузов.
     *
     * @return сумма очков
     */
    public int calculateScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }

        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

    /**
     * Очищает руку от карт.
     */
    public void clear() {
        cards.clear();
    }
}