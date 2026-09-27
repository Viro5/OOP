package ru.nsu.blackjack;

public class Player {
    private final String name;
    private final Hand hand;
    private final boolean isDealer;

    public Player(String name, boolean isDealer) {
        this.name = name;
        this.hand = new Hand();
        this.isDealer = isDealer;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void resetHand() {
        hand.clear();
    }
}