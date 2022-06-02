package matrixbackend.dto;



import lombok.Getter;
import lombok.Setter;
import matrixbackend.entity.User;

@Getter
@Setter
public class InviterResponseDTO {
    private String firstName;
    private String lastName;
    private String avatar;



    public InviterResponseDTO(User user) {
        this.firstName = user.getFirst_name();
        this.lastName = user.getLast_name();
        this.avatar = user.getAvatar();
    }
}

