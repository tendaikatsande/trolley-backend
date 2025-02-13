package zw.co.trolley.AuthService.domain.models;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tr_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String roles ="ROLE_CUSTOMER";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    private String refreshToken;

}
