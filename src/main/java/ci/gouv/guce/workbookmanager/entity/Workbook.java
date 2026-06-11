package ci.gouv.guce.workbookmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workbook")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "La date de naissance est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birthdate;

    @NotBlank(message = "Le numéro de passeport est obligatoire")
    @Column(nullable = false, unique = true)
    private String passportNumber;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "workbook", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("rank ASC")
    @Builder.Default
    private List<Workplace> workplaces = new ArrayList<>();

    @Transient
    public Integer getAge() {
        if (birthdate == null) return null;
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    public void addWorkplace(Workplace workplace) {
        workplace.setWorkbook(this);
        workplaces.add(workplace);
    }

    public void removeWorkplace(Workplace workplace) {
        workplaces.remove(workplace);
        workplace.setWorkbook(null);
    }
}
