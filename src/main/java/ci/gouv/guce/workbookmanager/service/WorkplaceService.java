package ci.gouv.guce.workbookmanager.service;

import ci.gouv.guce.workbookmanager.entity.Workplace;
import ci.gouv.guce.workbookmanager.entity.Workbook;

public interface WorkplaceService extends CrudService<Workplace, Long> {
    public Workplace save(Workbook workbook, Workplace newWorkplace);
}
