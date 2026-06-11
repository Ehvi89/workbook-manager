package ci.gouv.guce.workbookmanager;

import ci.gouv.guce.workbookmanager.entity.Workbook;
import ci.gouv.guce.workbookmanager.entity.Workplace;
import ci.gouv.guce.workbookmanager.service.WorkbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final WorkbookService workbookService;

    @Override
    public void run(String... args) {
        log.info("Initialisation des données de démo...");

        // Workbook 1
        Workbook wb1 = Workbook.builder()
                .firstName("Jean")
                .lastName("Dupont")
                .birthdate(LocalDate.of(1985, 3, 15))
                .passportNumber("FR1234567")
                .email("jean.dupont@example.com")
                .build();
        wb1 = workbookService.save(wb1);

        workbookService.addWorkplace(wb1.getId(), Workplace.builder()
                .companyCode("ACME")
                .companyName("Acme Corporation")
                .countryCode("FR")
                .countryName("France")
                .startDate(LocalDate.of(2015, 1, 10))
                .endDate(LocalDate.of(2019, 12, 31))
                .current(false)
                .build());

        workbookService.addWorkplace(wb1.getId(), Workplace.builder()
                .companyCode("TECH")
                .companyName("TechVision SAS")
                .countryCode("FR")
                .countryName("France")
                .startDate(LocalDate.of(2020, 2, 1))
                .current(true)
                .build());

        // Workbook 2
        Workbook wb2 = Workbook.builder()
                .firstName("Marie")
                .lastName("Martin")
                .birthdate(LocalDate.of(1992, 7, 22))
                .passportNumber("FR9876543")
                .email("marie.martin@example.com")
                .build();
        wb2 = workbookService.save(wb2);

        workbookService.addWorkplace(wb2.getId(), Workplace.builder()
                .companyCode("GLOB")
                .companyName("Global Consulting")
                .countryCode("BE")
                .countryName("Belgique")
                .startDate(LocalDate.of(2018, 9, 1))
                .current(true)
                .build());

        log.info("Données de démo initialisées : {} workbooks", 2);
    }
}
