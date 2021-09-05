package rs.fon.njt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    protected String username;
    protected String password;
    @Column(name = "first_name")
    protected String firstName;
    @Column(name = "last_name")
    protected String lastName;
    protected String email;
    protected String role;
}
