package ci.gouv.guce.workbookmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workbook")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate birthdate;

    @Column(unique = true, nullable = false)
    private String passportNumber;

    @Column(unique = true, nullable = false)
    private String email;

    // Relation 1-n. "orphanRemoval = true" supprime un workplace s'il est retiré de cette liste.
    @OneToMany(mappedBy = "workbook", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("rank ASC")
    private List<Workplace> workplaces = new ArrayList<>();

    // Champ calculé qui n'est pas persisté en BDD
    @Transient
    public Integer getAge() {
        if (birthdate != null) {
            return Period.between(birthdate, LocalDate.now()).getYears();
        }
        return null;
    }
}