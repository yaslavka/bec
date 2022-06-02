package matrixbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matrixbackend.entity.Matrix;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CloneInnerDTO {
    private Long id;
    private String userName;
    private String photo;
    private Long typeId;
    private int place;
    private LocalDateTime date;

    public CloneInnerDTO(Matrix matrix) {
        this.id = matrix.getId();
        this.userName = matrix.getUser().getUsername();
        this.photo = matrix.getUser().getAvatar();
        this.date = matrix.getDate();
    }

    @Override
    public String toString() {
        return "CloneInnerDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", photo='" + photo + '\'' +
                ", typeId=" + typeId +
                ", place=" + place +
                ", date=" + date +
                '}';
    }
}