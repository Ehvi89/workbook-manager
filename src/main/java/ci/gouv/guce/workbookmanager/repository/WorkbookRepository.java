package ci.gouv.guce.workbookmanager.repository;

import ci.gouv.guce.workbookmanager.entity.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    boolean existsByPassportNumber(String passportNumber);
    boolean existsByEmail(String email);
    boolean existsByPassportNumberAndIdNot(String passportNumber, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
