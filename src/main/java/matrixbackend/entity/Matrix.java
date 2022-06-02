package matrixbackend.entity;

import lombok.*;
import matrixbackend.MatrixEssenceEnum;
import matrixbackend.SideMatrix;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matrix {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;



    @ManyToOne
    @JoinColumn(columnDefinition = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        this.date = LocalDateTime.now();
    }


    @CreatedDate
    @Column(name = "date")
    private LocalDateTime date;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="parent_id",nullable=true)
    private Matrix parentMatrix;


    @Enumerated
    private SideMatrix sideMatrix;



    @Column(name = "matrix_essence",columnDefinition = "int")
    private MatrixEssenceEnum matrixEssenceEnum;




}
