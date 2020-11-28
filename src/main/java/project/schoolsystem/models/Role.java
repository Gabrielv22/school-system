package project.schoolsystem.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private String name;
//
//    public enum Name {
//        ROLE_USER,
//        ROLE_STUDENT,
//        ROLE_TEACHER
//    }

}
