package matrixbackend.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "referral_id")
    private User referral;


    @Column(name = "first_name")
    private String first_name;


    @Column(name = "last_name")
    private String last_name;

    @OneToOne
    @JoinColumn(name = "statistic_id")
    private Statistic statistic;

    @NotNull
    @NotBlank
    private String phone;


    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String username;

    @Column(nullable = false)
    private String password;

    private String financePassword;

    private String instagram;

    private String telegram;

    private String vkontakte;

    private String refLink;


    private  boolean hasFinPassword;

    private int income;

    private boolean firstEnter;

    private boolean showInviter;

    private String tgKey;

    private String locale;

    private boolean canCreateComment;


    private int activePartners;


    private int firstLines;





    @NotNull
    @Column(columnDefinition = "integer default 0")
    private Integer balance;

    private String avatar;

    @Column(columnDefinition = "boolean default false")
    private boolean isVerified;


    @Column(columnDefinition = "integer default 0")
    private int userOnLinks;

    @PrePersist
    public void prePersist() {
        this.registrationDate = LocalDateTime.now();
    }


    @CreatedDate
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    private Date activationDate;


    @ManyToMany(cascade = {CascadeType.DETACH , CascadeType.PERSIST,
            CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="roles_id")})
    private List<Roles> roles;
}
