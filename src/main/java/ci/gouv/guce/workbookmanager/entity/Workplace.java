package ci.gouv.guce.workbookmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "workplace")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean current;
    private String companyCode;
    private String companyName;
    private String countryCode;
    private String countryName;
    private LocalDate startDate;
    private LocalDate endDate;

    private Integer rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id")
    private Workbook workbook;
}
