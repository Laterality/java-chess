package chess.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static chess.domain.PieceType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChessScoreCountTest {

    @Test
    void calculate() {

        ChessScoreCount scoreCount = new ChessScoreCount(Arrays.asList(
                ROOK_BLACK,
                BISHOP_BLACK,
                QUEEN_BLACK,
                QUEEN_WHITE,
                PAWN_WHITE,
                KNIGHT_WHITE
        ));

        assertThat(scoreCount.getScore(Team.BLACK)).isEqualTo(17);
        assertThat(scoreCount.getScore(Team.WHITE)).isEqualTo(12.5);
    }
}