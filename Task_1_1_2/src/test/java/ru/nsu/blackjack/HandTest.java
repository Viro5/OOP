package ru.nsu.blackjack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для класса Hand.
 */
public class HandTest {
    private Hand hand;

    @BeforeEach
    public void setUp() {
        hand = new Hand();
    }

    @Test
    public void testAddAndGetCard() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        hand.addCard(card);
        assertEquals(1, hand.size());
        assertEquals(card, hand.getCard(0));
    }

    @Test
    public void testCalculateScoreWithoutAces() {
        hand.addCard(new Card(Suit.CLUBS, Rank.TEN));
        hand.addCard(new Card(Suit.SPADES, Rank.FIVE));
        assertEquals(15, hand.calculateScore());
    }

    @Test
    public void testCalculateScoreWithAcesAdjusted() {
        hand.addCard(new Card(Suit.CLUBS, Rank.ACE));
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));
        assertEquals(21, hand.calculateScore());
    }
}