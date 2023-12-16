package kg.startproject.mobimarket_1.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RegistrtionResponse {
    Long id;
    String userName;
    String jwtToken;
}
