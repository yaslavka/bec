package matrixbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matrixbackend.entity.Matrix;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CloneDTO {
    private String name;
    private Long id;

    public CloneDTO(Matrix matrix) {
        this.name = matrix.getUser()
                +"/"+ matrix.getId()+" "
                +matrix.getDate();
        this.id = matrix.getId();
    }
}
