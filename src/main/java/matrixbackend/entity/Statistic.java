package matrixbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table()
@Getter
@Setter
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int allPlanet;

    private int myPlanet;

    private int allComet;

    private  int myComet;

    private int firstPlanet;

    private int structurePlanet;

    private int myInventoryIncome;


}