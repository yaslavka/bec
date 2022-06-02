package matrixbackend.dto;

import lombok.*;
import matrixbackend.entity.TypeMatrix;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatrixTypeDTO {

    private  Long id;

    private String name;

    private int sum;


    private boolean canBuy;

    private boolean IsActive;

    private int count;



    public MatrixTypeDTO(TypeMatrix matrix) {
        this.id = matrix.getId();
        this.name = matrix.getName();
        this.sum = matrix.getSum();
        this.count=5;
        this.canBuy=true;
        this.IsActive=true;
    }
}
