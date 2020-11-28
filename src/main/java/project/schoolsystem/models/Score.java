package project.schoolsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "scores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private Double value;

    @Column(name = "date")
    private LocalDate date;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private User user;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Course course;
}
