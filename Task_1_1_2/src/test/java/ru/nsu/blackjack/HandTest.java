package ru.nsu.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    private Hand hand;

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void testAceValuedAsEleven() {
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.NINE));
        assertEquals(20, hand.calculateScore());
    }

    @Test
    void testAceValuedAsOneOnBust() {
        hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand.addCard(new Card(Suit.CLUBS, Rank.FIVE));
        assertEquals(16, hand.calculateScore());
        assertFalse(hand.isBust());
    }

    @Test
    void testBust() {
        hand.addCard(new Card(Suit.SPADES, Rank.TEN));
        hand.addCard(new Card(Suit.HEARTS, Rank.QUEEN));
        hand.addCard(new Card(Suit.CLUBS, Rank.TWO));
        assertTrue(hand.isBust());
    }
}