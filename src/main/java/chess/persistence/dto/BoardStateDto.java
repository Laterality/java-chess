package chess.persistence.dto;

import javax.persistence.*;

@Entity
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
//    private GameSessionDto session;

    public static BoardStateDto of(Long id, String type, String coordX, String coordY) {
        BoardStateDto dto = new BoardStateDto();
        dto.setId(id);
        dto.setType(type);
        dto.setCoordX(coordX);
        dto.setCoordY(coordY);
        return dto;
    }

    public static BoardStateDto of(String type, String coordX, String coordY) {
        BoardStateDto dto = new BoardStateDto();
        dto.setType(type);
        dto.setCoordX(coordX);
        dto.setCoordY(coordY);
        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

//    public GameSessionDto getSession() {
//        return session;
//    }
//
//    public void setSession(GameSessionDto session) {
//        this.session = session;
//    }
}
