package ru.nsu.blackjack;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для класса Deck.
 */
public class DeckTest {

    @Test
    public void testDrawCard() {
        Deck deck = new Deck();
        Card card = deck.drawCard();
        assertNotNull(card);
    }
}