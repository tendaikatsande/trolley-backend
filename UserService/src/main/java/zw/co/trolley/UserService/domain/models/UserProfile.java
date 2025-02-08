package zw.co.trolley.UserService.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<Address> addresses;
}
