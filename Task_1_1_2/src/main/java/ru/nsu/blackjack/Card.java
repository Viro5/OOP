package ru.nsu.blackjack;

public class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        // Убираем (11) из имени карты, так как итоговый счет и так пишется в конце строки: > 12
        return rank.getName() + " of " + suit.getName();
    }
}