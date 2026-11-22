package ru.nsu.blackjack;

import java.util.Scanner;

/**
 * Класс, управляющий основным игровым циклом Блэкджека.
 */
public class Game {
    private final Deck deck;
    private final Player player;
    private final Player dealer;
    private final Scanner scanner;

    /**
     * Конструктор игры.
     */
    public Game() {
        this.deck = new Deck();
        this.player = new Player("Player");
        this.dealer = new Player("Dealer");
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает игровой процесс.
     */
    public void start() {
        System.out.println("Welcome to Blackjack!");

        int round = 1;
        while (true) {
            System.out.println("\n--- Round " + round + " ---");
            playRound();

            System.out.print("\nDo you want to play again? (1 - yes, 0 - no): ");
            int choice = scanner.nextInt();
            if (choice != 1) {
                break;
            }
            round++;
        }

        System.out.println("Thanks for playing!");
    }

    /**
     * Проводит один раунд игры.
     */
    private void playRound() {
        player.getHand().clear();
        dealer.getHand().clear();

        player.getHand().addCard(deck.drawCard());
        player.getHand().addCard(deck.drawCard());

        dealer.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());

        System.out.println("You were dealt: " + player.getHand().getCard(0)
                + " and " + player.getHand().getCard(1));
        System.out.println("Dealer's cards: " + dealer.getHand().getCard(0) + " and <hidden card>");

        // Ход игрока
        while (true) {
            System.out.println("Your score: " + player.getHand().calculateScore());
            if (player.getHand().calculateScore() >= 21) {
                break;
            }

            System.out.print("Enter 1 to hit, or 0 to stand: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                Card card = deck.drawCard();
                player.getHand().addCard(card);
                System.out.println("You drew: " + card);
            } else {
                break;
            }
        }

        int playerScore = player.getHand().calculateScore();
        if (playerScore > 21) {
            System.out.println("Bust! You lost.");
            return;
        }

        // Ход дилера
        System.out.println("\nDealer reveals hidden card: " + dealer.getHand().getCard(1));
        while (dealer.getHand().calculateScore() < 17) {
            Card card = deck.drawCard();
            dealer.getHand().addCard(card);
            System.out.println("Dealer draws: " + card);
        }

        int dealerScore = dealer.getHand().calculateScore();
        System.out.println("Dealer's score: " + dealerScore);

        if (dealerScore > 21 || playerScore > dealerScore) {
            System.out.println("You win!");
        } else if (playerScore < dealerScore) {
            System.out.println("Dealer wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }
}