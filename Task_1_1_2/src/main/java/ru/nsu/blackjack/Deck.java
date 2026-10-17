package ru.nsu.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий колоду из 52 карт.
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Конструктор, создающий и перемешивающий колоду.
     */
    public Deck() {
        reset();
    }

    /**
     * Сбрасывает и заново перемешивает колоду.
     */
    public final void reset() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * Берет верхнюю карту из колоды.
     *
     * @return вытянутая карта
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            reset();
        }
        return cards.remove(cards.size() - 1);
    }
}