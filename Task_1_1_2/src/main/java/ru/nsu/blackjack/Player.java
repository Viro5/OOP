package ru.nsu.blackjack;

/**
 * Класс, представляющий участника игры в Блэкджек.
 */
public class Player {
    private final String name;
    private final Hand hand;

    /**
     * Конструктор игрока.
     *
     * @param name имя игрока
     */
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    /**
     * Возвращает имя игрока.
     *
     * @return имя игрока
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает руку игрока.
     *
     * @return рука игрока
     */
    public Hand getHand() {
        return hand;
    }
}