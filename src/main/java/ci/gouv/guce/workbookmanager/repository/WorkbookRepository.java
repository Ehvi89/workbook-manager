package ci.gouv.guce.workbookmanager.repository;

import ci.gouv.guce.workbookmanager.entity.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkbookRepository extends JpaRepository<Workbook,  Long> {
}
