package src;

import java.net.*;
import java.util.Scanner;

import static src.TicTacToeUDPServer.*;

public class TicTacToeUDPClient {
    private final static String YOU = "\nYou are playing ";
    private final static String YOUR_TURN = "\nYour turn\n\nEnter row and column number:\n";
    private final static String WAITING = "\nWaiting for other player's";
    private final static String OTHERS_TURN = WAITING + " turn...";
    private final static String OTHERS_CHOICE = WAITING + " choice...";
    private final static String REFUSAL = "\nOther player don't want to continue\n";
    private final static String ANOTHER_GAME_QUESTION = "Do you want to play another game?\n\nEnter Y or N\n";
    private final static String ANOTHER_GAME =
            "\nAnother game is arranged!\n\nPlayers' figures will be switched\n\nPrepare yourself";
    private final static String END_OF_GAME = "\nEnd of game";
    private final static String INCORRECT_INPUT = "\nInput is incorrect";
    private final static String TRY_AGAIN = "\n\nPlease, try again\n";

    private final static int BEGINNING_OF_MESSAGE = 0;
    private final static int BEGINNING_OF_RESULT = 6;
    private final static int BEGINNING_OF_TURN = 2;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(final String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Usage: <host> <port>");
            return;
        }

        final String host = args[0];

        final int port = Integer.parseInt(args[1]);

        try {
            final SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(host), port);

            try (final DatagramSocket socket = new DatagramSocket()) {
                final BufferedPacket packet = new BufferedPacket(BUFFER_SIZE, socketAddress);

                packet.sendSafe(socket, String.valueOf(42 + Math.random()));

                packet.receiveSafe(socket);

                System.out.println(packet.toString());

                String name = scanner.nextLine();

                packet.sendSafe(socket, name);

                do {
                    packet.receiveSafe(socket);

                    final TicTacToe game = new TicTacToe();

                    final char me = (packet.toString().charAt(BEGINNING_OF_MESSAGE) == '1' ? X : O);

                    System.out.println(YOU + me + '\n');

                    System.out.println(game.toString() + (me == game.getCurrentTurn() ? YOUR_TURN : OTHERS_TURN));

                    while (!socket.isClosed()) {
                        packet.receiveSafe(socket);

                        String message = packet.toString();

                        if (FIRST.toString().equals(message)) {
                            // First turn, nothing to receive
                        } else {
                            final Scanner messageScanner = new Scanner(message.substring(BEGINNING_OF_TURN));

                            final int x = messageScanner.nextInt(), y = messageScanner.nextInt();

                            game.makeMove(x, y);

                            if (message.charAt(BEGINNING_OF_MESSAGE) == MOVE) {
                                System.out.println(game.toString() + YOUR_TURN);
                            } else if (message.charAt(BEGINNING_OF_MESSAGE) == END) {
                                System.out.println(game.toString());
                                System.out.println(message.substring(BEGINNING_OF_RESULT));
                                game.empty();
                                break;
                            } else {
                                throw new TicTacToeException("Client error: " + message);
                            }
                        }

                        int z, w;

                        while (true) {
                            try {
                                if (!scanner.hasNextInt()) {
                                    System.out.println(INCORRECT_INPUT + TRY_AGAIN);
                                    scanner.next();
                                    continue;
                                }

                                z = scanner.nextInt();

                                if (!scanner.hasNextInt()) {
                                    System.out.println(INCORRECT_INPUT + TRY_AGAIN);
                                    scanner.next();
                                    continue;
                                }

                                w = scanner.nextInt();

                                game.isCorrectMove(z, w);

                                break;
                            } catch (TicTacToeException e) {
                                System.out.println(e.getMessage() + TRY_AGAIN);
                            }
                        }

                        packet.sendSafe(socket, z + " " + w);

                        packet.receiveSafe(socket);

                        message = packet.toString();

                        game.makeMove(z, w);

                        if (message.charAt(BEGINNING_OF_MESSAGE) == CORRECT) {
                            System.out.println(game.toString() + OTHERS_TURN);
                        } else if (message.charAt(BEGINNING_OF_MESSAGE) == END) {
                            System.out.println(game.toString());
                            System.out.println(message.substring(BEGINNING_OF_RESULT));

                            game.empty();

                            break;
                        }
                    }

                    System.out.println(ANOTHER_GAME_QUESTION);

                    final char answer1 = Character.toUpperCase(scanner.next().charAt(BEGINNING_OF_MESSAGE));

                    packet.sendSafe(socket, String.valueOf(answer1));

                    if (answer1 == YES) {
                        System.out.println(OTHERS_CHOICE);
                    } else {
                        System.out.println(END_OF_GAME);
                        break;
                    }

                    packet.receiveSafe(socket);

                    final char answer2 = packet.toString().charAt(BEGINNING_OF_MESSAGE);

                    if (answer2 == YES) {
                        System.out.println(ANOTHER_GAME);
                    } else {
                        System.out.println(REFUSAL + END_OF_GAME);
                        break;
                    }
                } while (true);
            } catch (final SocketException e) {
                System.err.println("Error: connection can't be created via socket — " + e.getMessage());
            }
        } catch (final UnknownHostException e) {
            System.err.println("Error: host" + host + "is unknown — " + e.getMessage());
        }
    }
}
