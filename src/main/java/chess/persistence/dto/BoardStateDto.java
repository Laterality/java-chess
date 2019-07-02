package chess.persistence.dto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "board_state")
public class BoardStateDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "loc_x")
    private String coordX;
    @Column(name = "loc_y")
    private String coordY;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id")
    private GameSessionDto session;
    @Column(name = "reg_date")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private LocalDateTime regDate;

    public static BoardStateDto of(Long id, String type, String coordX, String coordY, GameSessionDto sess) {
        BoardStateDto dto = new BoardStateDto();
        dto.setId(id);
        dto.setType(type);
        dto.setCoordX(coordX);
        dto.setCoordY(coordY);
        dto.setSession(sess);
        return dto;
    }

    public static BoardStateDto of(Long id, String type, String coordX, String coordY) {
        return of(id, type, coordX, coordY, null);
    }

    public static BoardStateDto of(String type, String coordX, String coordY, GameSessionDto sess) {
        return of(null, type, coordX, coordY, sess);
    }

    public static BoardStateDto of(String type, String coordX, String coordY) {
        return of(null, type, coordX, coordY);
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

    public GameSessionDto getSession() {
        return session;
    }

    public void setSession(GameSessionDto session) {
        this.session = session;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardStateDto that = (BoardStateDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(coordX, that.coordX) &&
            Objects.equals(coordY, that.coordY) &&
            Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, coordX, coordY, session);
    }
}
