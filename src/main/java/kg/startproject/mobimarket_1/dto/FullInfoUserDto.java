package kg.startproject.mobimarket_1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//New class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullInfoUserDto {
    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String avatar;
    String phoneNumber;
    LocalDate birthDate;
}
