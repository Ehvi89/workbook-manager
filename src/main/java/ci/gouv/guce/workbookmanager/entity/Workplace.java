package ci.gouv.guce.workbookmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "workplace", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workbook_id", "rank"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private boolean current = false;

    @NotBlank(message = "Le code entreprise est obligatoire")
    @Column(nullable = false)
    private String companyCode;

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Column(nullable = false)
    private String companyName;

    @NotBlank(message = "Le code pays est obligatoire")
    @Column(nullable = false)
    private String countryCode;

    @NotBlank(message = "Le nom du pays est obligatoire")
    @Column(nullable = false)
    private String countryName;

    @NotNull(message = "La date de début est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    private Workbook workbook;
}
