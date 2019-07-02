package chess.persistence.dto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "game_session")
public class GameSessionDto {
    @Id
    @GeneratedValue
    private Long id;
    private String state;
    private String title;
    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER)
    private List<BoardStateDto> pieces = new ArrayList<>();
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    public static GameSessionDto of(Long id, String state, String title) {
        GameSessionDto dto = new GameSessionDto();
        dto.setId(id);
        dto.setState(state);
        dto.setTitle(title);
        dto.setRegDate(LocalDateTime.now());
        return dto;
    }

    public static GameSessionDto of(String state, String title) {
        GameSessionDto dto = new GameSessionDto();
        dto.setState(state);
        dto.setTitle(title);
        dto.setRegDate(LocalDateTime.now());
        return dto;
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

    public List<BoardStateDto> getPieces() {
        return pieces;
    }

    public void setPieces(List<BoardStateDto> pieces) {
        this.pieces = pieces;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return String.format("GameSessionDto { id: %d, state: %s, title: %s, pieces: %s, regDate: %s }",
            id, state, title, pieces, regDate);
    }

    /**
     * 빈 pieces 필드에 대해 pieces.equals로 비교하면 false가 리턴됨. 이유는 불명.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSessionDto that = (GameSessionDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(state, that.state) &&
            Objects.equals(title, that.title) &&
            Objects.equals(regDate, that.regDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, title, regDate);
    }
}
