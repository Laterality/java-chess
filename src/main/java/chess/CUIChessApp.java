package chess;

import chess.domain.*;
import chess.view.InputView;
import chess.view.OutputVIew;

import java.util.Arrays;
import java.util.List;

import static chess.domain.Team.*;

public class CUIChessApp {
    private static ChessBoard chessBoard;

    public static void main(String[] args) {
        OutputVIew.printInputGuide();

        ChessPiece empty = EmptyCell.getInstance();

        List<List<ChessPiece>> boardState = Arrays.asList(
                Arrays.asList(Rook.getInstance(BLACK), Knight.getInstance(BLACK), Bishop.getInstance(BLACK), Queen.getInstance(BLACK),
                        King.getInstance(BLACK), Bishop.getInstance(BLACK), Knight.getInstance(BLACK), Rook.getInstance(BLACK)),
                Arrays.asList(Pawn.getInstance(BLACK), Pawn.getInstance(BLACK), Pawn.getInstance(BLACK), Pawn.getInstance(BLACK),
                        Pawn.getInstance(BLACK), Pawn.getInstance(BLACK), Pawn.getInstance(BLACK), Pawn.getInstance(BLACK)),
                Arrays.asList(empty, empty, empty, empty, empty, empty, empty, empty),
                Arrays.asList(empty, empty, empty, empty, empty, empty, empty, empty),
                Arrays.asList(empty, empty, empty, empty, empty, empty, empty, empty),
                Arrays.asList(empty, empty, empty, empty, empty, empty, empty, empty),
                Arrays.asList(Pawn.getInstance(WHITE), Pawn.getInstance(WHITE), Pawn.getInstance(WHITE), Pawn.getInstance(WHITE),
                        Pawn.getInstance(WHITE), Pawn.getInstance(WHITE), Pawn.getInstance(WHITE), Pawn.getInstance(WHITE)),
                Arrays.asList(Rook.getInstance(WHITE), Knight.getInstance(WHITE), Bishop.getInstance(WHITE), Queen.getInstance(WHITE),
                        King.getInstance(WHITE), Bishop.getInstance(WHITE), Knight.getInstance(WHITE), Rook.getInstance(WHITE))
        );

        chessBoard = new ChessBoard(boardState);

        String input = InputView.promptUserSelection();
        while (!handleUserSelection(input)) {
            input = InputView.promptUserSelection();

        }
    }

    private static boolean handleUserSelection(String input) {
        String[] tokens = input.split(" ");
        if (tokens[0].equals("start")) {
            OutputVIew.printBoardState(chessBoard.getBoard());
            return false;
        }
        if (tokens[0].equals("end")) {
            System.exit(0);
            return true;
        }
        if (tokens[0].equals("move")) {
            handleMoveCommand(tokens);
            OutputVIew.printBoardState(chessBoard.getBoard());
            return false;
        }

        throw new IllegalArgumentException("알 수 없는 명령입니다");
    }

    private static void handleMoveCommand(String[] tokens) {
        try {
            chessBoard.move(
                    ChessCoordinate.valueOf(tokens[1])
                            .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 좌표입니다.")),
                    ChessCoordinate.valueOf(tokens[2])
                            .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 좌표입니다."))
            );

        } catch (IllegalArgumentException e) {
            OutputVIew.printError(e.getMessage());
        }
    }
}
