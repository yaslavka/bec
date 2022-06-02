package matrixbackend.dto;

import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserDTO {

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String last_name;

    @NotBlank
    @NotNull
    private String first_name;

    @NotBlank
    @NotNull
    private String email;

    @NotNull
    private String referral;

    @NotBlank
    @NotNull
    private String password;

    @NotBlank
    @NotNull
    private String phone;





}
