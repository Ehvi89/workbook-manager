package ci.gouv.guce.workbookmanager.service.impl;

import ci.gouv.guce.workbookmanager.repository.WorkbookRepository;
import ci.gouv.guce.workbookmanager.repository.WorkplaceRepository;
import ci.gouv.guce.workbookmanager.entity.Workbook;
import ci.gouv.guce.workbookmanager.entity.Workplace;
import ci.gouv.guce.workbookmanager.service.WorkplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkplaceServiceImpl implements WorkplaceService {
    @Autowired
    private WorkplaceRepository workplaceRepository;
    @Autowired
    private WorkbookRepository workbookRepository;

    @Override
    public Workplace save(Workbook workbook, Workplace newWorkplace) {
        newWorkplace.setWorkbook(workbook);
        // Si aucun rank n'est spécifié, on le place à la fin
        int newRank = workbook.getWorkplaces().size() + 1;
        newWorkplace.setRank(newRank);
        workplaceRepository.save(newWorkplace);
        return newWorkplace;
    }

    @Override
    public Workplace save(Workplace d) {
        return workplaceRepository.save(d);
    }

    @Override
    public Workplace findOne(Long aLong) {
        if (workplaceRepository.existsById(aLong)) {
            final Optional<Workplace> wp = workplaceRepository.findById(aLong);
            if (wp.isPresent()) {
                return wp.get();
            }
        }
        return null;
    }

    @Override
    public Workplace update(Workplace d) {
        if (workplaceRepository.existsById(d.getId())) {
            return workplaceRepository.save(d);
        }
        return null;
    }

    @Override
    public void delete(Long aLong) {
        if (workplaceRepository.existsById(aLong)) {
            Optional<Workplace> wp = workplaceRepository.findById(aLong);
            if (wp.isPresent()) {
                Workplace workplace = wp.get();
                Workbook workbook = workplace.getWorkbook();
                workplaceRepository.deleteById(aLong);
                // On retire l'élément de la liste en mémoire pour réindexer le reste
                workbook.getWorkplaces().removeIf(w -> w.getId().equals(aLong));
                reindexRanks(workbook);
            }
        }
    }

    @Override
    public List<Workplace> findAll() {
        return workplaceRepository.findAll();
    }

    @Override
    public Workplace partialUpdate(Workplace d) {
        return null;
    }

    @Override
    public Boolean existById(Long aLong) {
        return workplaceRepository.existsById(aLong);
    }

    private void reindexRanks(Workbook workbook) {
        List<Workplace> workplaces = workbook.getWorkplaces();
        int currentRank = 1;
        for (Workplace wp : workplaces) {
            wp.setRank(currentRank++);
            workplaceRepository.save(wp);
        }
    }
}
