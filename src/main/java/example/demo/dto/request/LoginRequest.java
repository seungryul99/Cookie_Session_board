package example.demo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    private String loginId;

    private String password;
}