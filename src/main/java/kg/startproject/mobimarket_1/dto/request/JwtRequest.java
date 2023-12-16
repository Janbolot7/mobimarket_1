package kg.startproject.mobimarket_1.dto.request;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
