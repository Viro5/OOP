package ru.nsu.blackjack;

import java.util.Scanner;

public class Game {
    private final Deck deck;
    private final Player player;
    private final Player dealer;
    private int playerScore;
    private int dealerScore;
    private int roundNumber;

    public Game() {
        this.deck = new Deck();
        this.player = new Player("You", false);
        this.dealer = new Player("Dealer", true);
        this.playerScore = 0;
        this.dealerScore = 0;
        this.roundNumber = 0;
    }

    public void start(Scanner scanner) {
        System.out.println("Welcome to Blackjack!");

        boolean continueGame = true;
        while (continueGame) {
            roundNumber++;
            playRound(scanner);

            System.out.println("\nDo you want to play another round? (1 - Yes, 0 - No)");
            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                continueGame = false;
            }
        }

        System.out.println("\nThanks for playing! Final score: " + playerScore + ":" + dealerScore);
    }

    public void playRound(Scanner scanner) {
        System.out.println("\nRound " + roundNumber);
        player.resetHand();
        dealer.resetHand();

        player.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());
        player.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());

        System.out.println("Dealer dealt cards");
        printState(false);

        boolean playerBj = player.getHand().isBlackjack();
        boolean dealerBj = dealer.getHand().isBlackjack();

        if (playerBj || dealerBj) {
            System.out.println("\n--- Round Result ---");
            printState(true);

            if (playerBj && dealerBj) {
                System.out.println("Push! Both players have Blackjack.");
            } else if (playerBj) {
                System.out.println("Blackjack! You win the round!");
                playerScore++;
            } else {
                System.out.println("Dealer has Blackjack! Dealer wins the round!");
                dealerScore++;
            }
            printTotalScore();
            return;
        }

        boolean playerBust = handlePlayerTurn(scanner);

        if (playerBust) {
            System.out.println("Bust! You lose the round.");
            dealerScore++;
            printTotalScore();
            return;
        }

        handleDealerTurn();
        determineWinner();
        printTotalScore();
    }

    private boolean handlePlayerTurn(Scanner scanner) {
        System.out.println("\nYour Turn\n-------");
        while (true) {
            System.out.print("Type \"1\" to Hit, or \"0\" to Stand: ");
            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                Card drawn = deck.drawCard();
                player.getHand().addCard(drawn);
                System.out.println("You drew: " + drawn);
                printState(false);

                if (player.getHand().isBust()) {
                    return true;
                }
            } else if ("0".equals(choice)) {
                break;
            } else {
                System.out.println("Invalid input! Please enter 1 or 0.");
            }
        }
        return false;
    }

    private void handleDealerTurn() {
        System.out.println("\nDealer's Turn\n-------");
        System.out.println("Dealer reveals hidden card: " + dealer.getHand().getCards().get(1));
        printState(true);

        while (dealer.getHand().calculateScore() < 17) {
            Card drawn = deck.drawCard();
            dealer.getHand().addCard(drawn);
            System.out.println("Dealer draws: " + drawn);
            printState(true);
        }
    }

    private void determineWinner() {
        int pScore = player.getHand().calculateScore();
        int dScore = dealer.getHand().calculateScore();

        System.out.println("\n--- Round Results ---");
        if (dealer.getHand().isBust()) {
            System.out.println("Dealer busted! You win the round!");
            playerScore++;
        } else if (pScore > dScore) {
            System.out.println("You win the round!");
            playerScore++;
        } else if (dScore > pScore) {
            System.out.println("Dealer wins the round!");
            dealerScore++;
        } else {
            System.out.println("It's a tie!");
        }
    }

    private void printState(boolean revealDealerCard) {
        System.out.println("Your cards: " + player.getHand().toString(true)
                + " > " + player.getHand().calculateScore());
        System.out.println("Dealer cards: " + dealer.getHand().toString(revealDealerCard)
                + (revealDealerCard ? " > " + dealer.getHand().calculateScore() : ""));
    }

    private void printTotalScore() {
        System.out.println("Score " + playerScore + ":" + dealerScore + " ("
                + (playerScore >= dealerScore ? "Player leads" : "Dealer leads") + ").");
    }
}