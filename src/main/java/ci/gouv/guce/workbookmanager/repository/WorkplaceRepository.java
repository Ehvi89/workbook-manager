package ci.gouv.guce.workbookmanager.repository;

import ci.gouv.guce.workbookmanager.entity.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    List<Workplace> findByWorkbookIdOrderByRankAsc(Long workbookId);

    int countByWorkbookId(Long workbookId);

    @Modifying
    @Query("UPDATE Workplace w SET w.rank = w.rank - 1 WHERE w.workbook.id = :workbookId AND w.rank > :rank")
    void decrementRanksAfter(@Param("workbookId") Long workbookId, @Param("rank") int rank);

    @Modifying
    @Query("UPDATE Workplace w SET w.rank = w.rank + 1 WHERE w.workbook.id = :workbookId AND w.rank >= :rank")
    void incrementRanksFrom(@Param("workbookId") Long workbookId, @Param("rank") int rank);
}
