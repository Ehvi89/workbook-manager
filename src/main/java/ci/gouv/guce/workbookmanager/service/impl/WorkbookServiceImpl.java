package ci.gouv.guce.workbookmanager.service.impl;

import ci.gouv.guce.workbookmanager.entity.Workbook;
import ci.gouv.guce.workbookmanager.service.WorkbookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkbookServiceImpl implements WorkbookService {
    @Override
    public Workbook save(Workbook d) {
        return null;
    }

    @Override
    public Workbook findOne(Long aLong) {
        return null;
    }

    @Override
    public Workbook update(Workbook d) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public List<Workbook> findAll() {
        return List.of();
    }

    @Override
    public Workbook partialUpdate(Workbook d) {
        return null;
    }

    @Override
    public Boolean existById(Long aLong) {
        return null;
    }
}
