package matrixbackend.dto;

import lombok.*;
import matrixbackend.entity.MatrixTable;
import matrixbackend.entity.TypeMatrix;
import matrixbackend.entity.User;
import matrixbackend.repository.MatrixTableRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatrixTypeDTO {

    @Autowired
    MatrixTableRepository matrixTableRepository;

    private  Long id;

    private String name;

    private int sum;


    private boolean canBuy;

    private boolean IsActive;

    private int count;



    public MatrixTypeDTO(TypeMatrix matrix, int  c) {
        this.id = matrix.getId();
        this.name = matrix.getName();
        this.sum = matrix.getSum();



        this.count= c;
        this.canBuy=true;
        this.IsActive=true;
    }
}
