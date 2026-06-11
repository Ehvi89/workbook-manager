package ci.gouv.guce.workbookmanager.service;

import ci.gouv.guce.workbookmanager.entity.Workbook;
import ci.gouv.guce.workbookmanager.entity.Workplace;
import ci.gouv.guce.workbookmanager.repository.WorkbookRepository;
import ci.gouv.guce.workbookmanager.repository.WorkplaceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class    WorkbookService {

    private final WorkbookRepository workbookRepository;
    private final WorkplaceRepository workplaceRepository;

    // ── Workbook CRUD ──────────────────────────────────────────────────────────

    public List<Workbook> findAll() {
        return workbookRepository.findAll();
    }

    public Workbook findById(Long id) {
        return workbookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workbook introuvable : " + id));
    }

    public Workbook save(Workbook workbook) {
        validateUniqueness(workbook);
        return workbookRepository.save(workbook);
    }

    public void update(Long id, Workbook updated) {
        Workbook existing = findById(id);
        validateUniquenessForUpdate(updated, id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setBirthdate(updated.getBirthdate());
        existing.setPassportNumber(updated.getPassportNumber());
        existing.setEmail(updated.getEmail());
        workbookRepository.save(existing);
    }

    public void deleteById(Long id) {
        findById(id); // vérifie l'existence
        workbookRepository.deleteById(id);
    }

    private void validateUniqueness(Workbook workbook) {
        if (workbookRepository.existsByPassportNumber(workbook.getPassportNumber())) {
            throw new IllegalArgumentException("Numéro de passeport déjà utilisé : " + workbook.getPassportNumber());
        }
        if (workbookRepository.existsByEmail(workbook.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé : " + workbook.getEmail());
        }
    }

    private void validateUniquenessForUpdate(Workbook workbook, Long id) {
        if (workbookRepository.existsByPassportNumberAndIdNot(workbook.getPassportNumber(), id)) {
            throw new IllegalArgumentException("Numéro de passeport déjà utilisé : " + workbook.getPassportNumber());
        }
        if (workbookRepository.existsByEmailAndIdNot(workbook.getEmail(), id)) {
            throw new IllegalArgumentException("Email déjà utilisé : " + workbook.getEmail());
        }
    }

    // ── Workplace management ───────────────────────────────────────────────────

    public List<Workplace> findWorkplacesByWorkbookId(Long workbookId) {
        return workplaceRepository.findByWorkbookIdOrderByRankAsc(workbookId);
    }

    public Workplace findWorkplaceById(Long id) {
        return workplaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workplace introuvable : " + id));
    }

    public void addWorkplace(Long workbookId, Workplace workplace) {
        Workbook workbook = findById(workbookId);

        if (workplace.isCurrent()) {
            demoteCurrentWorkplace(workbookId, workplace.getStartDate(), null);
        }

        int nextRank = workplaceRepository.countByWorkbookId(workbookId) + 1;
        workplace.setRank(nextRank);
        workplace.setWorkbook(workbook);
        workplaceRepository.save(workplace);
    }

    public void updateWorkplace(Long workbookId, Long workplaceId, Workplace updated) {
        findById(workbookId); // vérifie que le workbook existe
        Workplace existing = findWorkplaceById(workplaceId);

        // Si ce poste devient "actuel", désactiver tout autre poste actuel du workbook
        if (updated.isCurrent() && !existing.isCurrent()) {
            demoteCurrentWorkplace(workbookId, updated.getStartDate(), workplaceId);
        }

        existing.setCurrent(updated.isCurrent());
        existing.setCompanyCode(updated.getCompanyCode());
        existing.setCompanyName(updated.getCompanyName());
        existing.setCountryCode(updated.getCountryCode());
        existing.setCountryName(updated.getCountryName());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());

        // Si rank changé, réorganiser
        if (!existing.getRank().equals(updated.getRank())) {
            reorderOnRankChange(workbookId, existing.getRank(), updated.getRank());
            existing.setRank(updated.getRank());
        }

        workplaceRepository.save(existing);
    }

    public void deleteWorkplace(Long workbookId, Long workplaceId) {
        Workplace workplace = findWorkplaceById(workplaceId);
        int deletedRank = workplace.getRank();
        workplaceRepository.delete(workplace);
        workplaceRepository.decrementRanksAfter(workbookId, deletedRank);
    }

    private void reorderOnRankChange(Long workbookId, int oldRank, int newRank) {
        if (newRank < oldRank) {
            workplaceRepository.incrementRanksFrom(workbookId, newRank);
        } else {
            workplaceRepository.decrementRanksAfter(workbookId, oldRank);
        }
    }

    /**
     * Désactive le poste actuel existant (s'il y en a un) lorsqu'un nouveau poste
     * devient "current". Si le poste désactivé n'a pas de date de fin, on la fixe
     * au startDate du nouveau poste actuel pour éviter tout chevauchement.
     *
     * @param excludeWorkplaceId id du poste à exclure (en cas d'update sur lui-même), peut être null
     */
    private void demoteCurrentWorkplace(Long workbookId, java.time.LocalDate newStartDate, Long excludeWorkplaceId) {
        workplaceRepository.findByWorkbookIdOrderByRankAsc(workbookId).stream()
                .filter(Workplace::isCurrent)
                .filter(w -> !w.getId().equals(excludeWorkplaceId))
                .forEach(w -> {
                    w.setCurrent(false);
                    if (w.getEndDate() == null && newStartDate != null) {
                        w.setEndDate(newStartDate);
                    }
                    workplaceRepository.save(w);
                });
    }
}