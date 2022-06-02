package matrixbackend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class CloneStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;

    private int count;

}
