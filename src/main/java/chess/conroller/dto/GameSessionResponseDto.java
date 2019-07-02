package chess.conroller.dto;

import chess.persistence.dto.GameSessionDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GameSessionResponseDto {
    private Long id;
    private String state;
    private String title;
    private List<BoardStateResponseDto> pieces;
    private LocalDateTime regDate;

    public static GameSessionResponseDto from(GameSessionDto dto) {
        GameSessionResponseDto resDto = new GameSessionResponseDto();
        resDto.setId(dto.getId());
        resDto.setState(dto.getState());
        resDto.setTitle(dto.getTitle());
        resDto.setPieces(dto.getPieces().stream()
            .map(BoardStateResponseDto::from)
            .collect(Collectors.toList()));
        resDto.setRegDate(dto.getRegDate());
        return resDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BoardStateResponseDto> getPieces() {
        return pieces;
    }

    public void setPieces(List<BoardStateResponseDto> pieces) {
        this.pieces = pieces;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }
}
