package ru.nsu.blackjack;

/**
 * Точка входа в приложение.
 */
public class Main {
    /**
     * Главный метод, запускающий игру Блэкджек.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}