package ci.gouv.guce.workbookmanager.controller;

import ci.gouv.guce.workbookmanager.entity.Workbook;
import ci.gouv.guce.workbookmanager.entity.Workplace;
import ci.gouv.guce.workbookmanager.repository.WorkbookRepository;
import ci.gouv.guce.workbookmanager.service.WorkplaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/workbooks")
public class WorkbookController {

    private final WorkbookRepository workbookRepository;
    private final WorkplaceService workplaceService;

    public WorkbookController(WorkbookRepository workbookRepository, WorkplaceService workplaceService) {
        this.workbookRepository = workbookRepository;
        this.workplaceService = workplaceService;
    }

    // Liste des workbooks
    @GetMapping
    public String listWorkbooks(Model model) {
        model.addAttribute("workbooks", workbookRepository.findAll());
        return "workbook/list";
    }

    // Afficher le formulaire de création
    @GetMapping("/new")
    public String newWorkbookForm(Model model) {
        model.addAttribute("workbook", new Workbook());
        return "workbook/form";
    }

    // sauvegarder un workbook
    @PostMapping
    public String saveWorkbook(@ModelAttribute Workbook workbook) {
        workbookRepository.save(workbook);
        return "redirect:/workbooks";
    }

    // VUE DÉTAIL: Afficher un workbook et ses workplaces
    @GetMapping("/{id}")
    public String viewWorkbook(@PathVariable Long id, Model model) {
        Workbook workbook = workbookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid workbook Id:" + id));

        model.addAttribute("workbook", workbook);
        model.addAttribute("newWorkplace", new Workplace()); // Pour le formulaire d'ajout intégré
        return "workbook/detail";
    }

    // Ajouter un workplace depuis la vue détail
    @PostMapping("/{id}/workplaces")
    public String addWorkplace(@PathVariable Long id, @ModelAttribute Workplace workplace) {
        Workbook workbook = workbookRepository.findById(id).orElseThrow();
        workplaceService.save(workbook, workplace);
        return "redirect:/workbooks/" + id;
    }
}