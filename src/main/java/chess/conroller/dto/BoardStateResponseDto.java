package chess.conroller.dto;

import chess.persistence.dto.BoardStateDto;

import java.time.LocalDateTime;

public class BoardStateResponseDto {
    private Long id;
    private String type;
    private String coordX;
    private String coordY;
    private LocalDateTime regDate;

    public static BoardStateResponseDto from(BoardStateDto dto) {
        BoardStateResponseDto resDto = new BoardStateResponseDto();
        resDto.setId(dto.getId());
        resDto.setCoordX(dto.getCoordX());
        resDto.setCoordY(dto.getCoordY());
        resDto.setType(dto.getType());
        resDto.setRegDate(dto.getRegDate());
        return resDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoordX() {
        return coordX;
    }

    public void setCoordX(String coordX) {
        this.coordX = coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public void setCoordY(String coordY) {
        this.coordY = coordY;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }
}
