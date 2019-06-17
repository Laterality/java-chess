package chess.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static chess.domain.BoardCellState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class ChessBoardTest {

    @Test
    void initBoard() {
        ChessBoard chessBoard = new ChessBoard();
        List<List<BoardCellState>> boardState = Arrays.asList(
                Arrays.asList(ROOK_BLACK, KNIGHT_BLACK, BISHOP_BLACK, QUEEN_BLACK, KING_BLACK, BISHOP_BLACK, KNIGHT_BLACK, ROOK_BLACK),
                Arrays.asList(PAWN_BLACK, PAWN_BLACK, PAWN_BLACK, PAWN_BLACK, PAWN_BLACK, PAWN_BLACK, PAWN_BLACK, PAWN_BLACK),
                Arrays.asList(NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE),
                Arrays.asList(NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE),
                Arrays.asList(NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE),
                Arrays.asList(NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE),
                Arrays.asList(PAWN_WHITE, PAWN_WHITE, PAWN_WHITE, PAWN_WHITE, PAWN_WHITE, PAWN_WHITE, PAWN_WHITE, PAWN_WHITE),
                Arrays.asList(ROOK_WHITE, KNIGHT_WHITE, BISHOP_WHITE, QUEEN_WHITE, KING_WHITE, BISHOP_WHITE, KNIGHT_WHITE, ROOK_WHITE)
        );
        assertThat(chessBoard.getBoard()).isEqualTo(boardState);
    }
}