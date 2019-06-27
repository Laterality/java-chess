package chess.persistence.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GameSessionDto {
    @Id
    @GeneratedValue
    private Long id;
    private String state;
    private String title;
    @OneToMany(mappedBy = "session")
    private List<BoardStateDto> states = new ArrayList<>();

    public static GameSessionDto of(Long id, String state, String title) {
        GameSessionDto dto = new GameSessionDto();
        dto.setId(id);
        dto.setState(state);
        dto.setTitle(title);
        return dto;
    }

    public static GameSessionDto of(String state, String title) {
        GameSessionDto dto = new GameSessionDto();
        dto.setState(state);
        dto.setTitle(title);
        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<BoardStateDto> getStates() {
        return states;
    }

    public void setStates(List<BoardStateDto> states) {
        this.states = states;
    }
}
