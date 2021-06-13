package src;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

public class TicTacToeUDPServer {
    final static Character X = 'X';
    final static Character O = 'O';
    final static Character MOVE = 'M';
    final static Character END = 'E';
    final static Character CORRECT = 'C';
    final static Character INCORRECT = 'I';
    final static Character FIRST = 'F';
    final static Character YES = 'Y';

    private final static String ENTER_NAME = "Please, enter your name:";

    private final static String YOU_WON = " Your won!\n";
    private final static String YOU_LOST = " Your lost!\n";
    private final static String DRAW = " Draw!";

    static final int BUFFER_SIZE = 4096;

    private static DatagramSocket socket;

    private static Result move(final BufferedPacket packetP1, final BufferedPacket packetP2, final TicTacToe game) {
        final Scanner sc = new Scanner(packetP1.toString());
        final int x = sc.nextInt(), y = sc.nextInt();

        while (true) {
            try {
                final Result result = game.makeMove(x, y);

                String currentMove = ":" + x + " " + y;

                switch (result) {
                    case WIN:
                        packetP1.sendSafe(socket, END + currentMove + YOU_WON);
                        packetP2.sendSafe(socket, END + currentMove + YOU_LOST);

                        break;
                    case DRAW:
                        packetP1.sendSafe(socket, END + currentMove + DRAW);
                        packetP2.sendSafe(socket, END + currentMove + DRAW);

                        break;
                    case UNKNOWN:
                        packetP1.sendSafe(socket, CORRECT.toString());
                        packetP2.sendSafe(socket, MOVE + currentMove);
                        break;
                }

                return result;
            } catch (final Exception e) {
                try {
                    packetP1.send(socket, INCORRECT + ":" + e.getMessage());
                } catch (final IOException e1) {
                    System.err.println("Error: message can't be sent " + e1.getMessage());
                }
            }
        }
    }

    private static void connectPlayer(final BufferedPacket packet, final int id) {
        packet.receiveSafe(socket);
        System.out.println("Player " + id + " connected\n");

        packet.sendSafe(socket, ENTER_NAME);
        packet.receiveSafe(socket);

        String name = packet.toString();

        System.out.println("Player " + id + " is " + name + "\n");
    }

    public static void main(final String[] args) {
        if (args == null || args.length != 1) {
            System.err.println("Usage: <port>");
            return;
        }

        final int port = Integer.parseInt(args[0]);

        try {
            socket = new DatagramSocket(port);
        } catch (final SocketException e) {
            System.err.println("Error: socket can't be created on port " + port + ": " + e.getMessage());
            return;
        }

        BufferedPacket player1packet = new BufferedPacket(BUFFER_SIZE), player2packet = new BufferedPacket(BUFFER_SIZE);

        connectPlayer(player1packet, 1);
        connectPlayer(player2packet, 2);

        final int ONE = 1, TWO = 2;

        do {
            player1packet.sendSafe(socket, Integer.toString(ONE));
            player2packet.sendSafe(socket, Integer.toString(TWO));

            boolean firstTurn = true;

            final TicTacToe game = new TicTacToe();

            while (!socket.isClosed()) {
                if (firstTurn) {
                    player1packet.sendSafe(socket, FIRST.toString());
                }

                player1packet.receiveSafe(socket);

                Result result = move(player1packet, player2packet, game);

                if (result == Result.WIN || result == Result.DRAW) {
                    game.empty();
                    break;
                }

                player2packet.receiveSafe(socket);

                result = move(player2packet, player1packet, game);

                if (result == Result.WIN || result == Result.DRAW) {
                    game.empty();
                    break;
                }

                firstTurn = false;
            }

            player1packet.receiveSafe(socket);

            player2packet.receiveSafe(socket);

            final char answer1 = player1packet.toString().charAt(0), answer2 = player2packet.toString().charAt(0);

            player1packet.sendSafe(socket, String.valueOf(answer2));
            player2packet.sendSafe(socket, String.valueOf(answer1));

            if (answer1 != YES || answer2 != YES) {
                System.out.println("Game over");
                break;
            }

            BufferedPacket temp = player1packet;
            player1packet = player2packet;
            player2packet = temp;
        } while (true);
    }
}
