package matrixbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatrixTable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(columnDefinition = "type_matrix_id")
    private TypeMatrix typeMatrix;

    @OneToOne
    @JoinColumn(columnDefinition = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matrix_parent_id")
    private Matrix matrixParent;
    
    private boolean IsActive;

    private boolean canBuy;

    private int count;
}
