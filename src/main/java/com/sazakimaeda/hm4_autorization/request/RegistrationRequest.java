package com.sazakimaeda.hm4_autorization.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "VALIDATION.REGISTRATION.EMAIL.NOT_BLANK")
    @Email(message = "VALIDATION.REGISTRATION.EMAIL.FORMAT")
    private String email;

    @NotBlank(message = "VALIDATION.REGISTRATION.PASSWORD.NOT_BLANK")
    @Size(
            min = 3,
            max = 255,
            message = "VALIDATION.REGISTRATION.PASSWORD.SIZE" )
    private String password;

    @NotBlank(message = "VALIDATION.REGISTRATION.PASSWORD.NOT_BLANK")
    @Size(
            min = 1,
            max = 255,
            message = "VALIDATION.REGISTRATION.PASSWORD.SIZE" )
    private String confirmPassword;

    @NotBlank(message = "VALIDATION.REGISTRATION.FIRSTNAME.NOT_BLANK")
    @Size(
            min = 1,
            max = 255,
            message = "VALIDATION.REGISTRATION.FIRSTNAME.SIZE" )

    private String firstName;

    @NotBlank(message = "VALIDATION.REGISTRATION.LASTNAME.NOT_BLANK")
    @Size(
            min = 1,
            max = 255,
            message = "VALIDATION.REGISTRATION.LASTNAME.SIZE" )
    private String lastName;
}
