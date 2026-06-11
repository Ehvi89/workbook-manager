package ci.gouv.guce.workbookmanager.controller;

import ci.gouv.guce.workbookmanager.entity.Workbook;
import ci.gouv.guce.workbookmanager.entity.Workplace;
import ci.gouv.guce.workbookmanager.service.WorkbookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/workbooks")
@RequiredArgsConstructor
public class WorkbookController {

    private final WorkbookService workbookService;

    // ── Workbook list ──────────────────────────────────────────────────────────

    @GetMapping
    public String list(Model model) {
        model.addAttribute("workbooks", workbookService.findAll());
        return "workbook/list";
    }

    // ── Create workbook ────────────────────────────────────────────────────────

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("workbook", new Workbook());
        model.addAttribute("formAction", "/workbooks");
        model.addAttribute("pageTitle", "Nouveau Workbook");
        return "workbook/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Workbook workbook,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/workbooks");
            model.addAttribute("pageTitle", "Nouveau Workbook");
            return "workbook/form";
        }
        try {
            workbookService.save(workbook);
            redirectAttrs.addFlashAttribute("successMessage", "Workbook créé avec succès.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formAction", "/workbooks");
            model.addAttribute("pageTitle", "Nouveau Workbook");
            return "workbook/form";
        }
        return "redirect:/workbooks";
    }

    // ── Workbook detail ────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Workbook workbook = workbookService.findById(id);
        model.addAttribute("workbook", workbook);
        model.addAttribute("workplaces", workbookService.findWorkplacesByWorkbookId(id));
        model.addAttribute("newWorkplace", new Workplace());
        return "workbook/detail";
    }

    // ── Edit workbook ──────────────────────────────────────────────────────────

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("workbook", workbookService.findById(id));
        model.addAttribute("formAction", "/workbooks/" + id + "/edit");
        model.addAttribute("pageTitle", "Modifier le Workbook");
        return "workbook/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Workbook workbook,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/workbooks/" + id + "/edit");
            model.addAttribute("pageTitle", "Modifier le Workbook");
            return "workbook/form";
        }
        try {
            workbookService.update(id, workbook);
            redirectAttrs.addFlashAttribute("successMessage", "Workbook mis à jour.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("formAction", "/workbooks/" + id + "/edit");
            model.addAttribute("pageTitle", "Modifier le Workbook");
            return "workbook/form";
        }
        return "redirect:/workbooks/" + id;
    }

    // ── Delete workbook ────────────────────────────────────────────────────────

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        workbookService.deleteById(id);
        redirectAttrs.addFlashAttribute("successMessage", "Workbook supprimé.");
        return "redirect:/workbooks";
    }

    // ── Workplace: Add ─────────────────────────────────────────────────────────

    @PostMapping("/{workbookId}/workplaces")
    public String addWorkplace(@PathVariable Long workbookId,
                               @Valid @ModelAttribute("newWorkplace") Workplace workplace,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("workbook", workbookService.findById(workbookId));
            model.addAttribute("workplaces", workbookService.findWorkplacesByWorkbookId(workbookId));
            model.addAttribute("addWorkplaceError", true);
            return "workbook/detail";
        }
        workbookService.addWorkplace(workbookId, workplace);
        redirectAttrs.addFlashAttribute("successMessage", "Poste ajouté.");
        return "redirect:/workbooks/" + workbookId;
    }

    // ── Workplace: Edit form ───────────────────────────────────────────────────

    @GetMapping("/{workbookId}/workplaces/{workplaceId}/edit")
    public String editWorkplaceForm(@PathVariable Long workbookId,
                                    @PathVariable Long workplaceId,
                                    Model model) {
        model.addAttribute("workbook", workbookService.findById(workbookId));
        model.addAttribute("workplace", workbookService.findWorkplaceById(workplaceId));
        model.addAttribute("workplaces", workbookService.findWorkplacesByWorkbookId(workbookId));
        model.addAttribute("totalWorkplaces", workbookService.findWorkplacesByWorkbookId(workbookId).size());
        return "workplace/form";
    }

    // ── Workplace: Update ──────────────────────────────────────────────────────

    @PostMapping("/{workbookId}/workplaces/{workplaceId}/edit")
    public String updateWorkplace(@PathVariable Long workbookId,
                                  @PathVariable Long workplaceId,
                                  @Valid @ModelAttribute("workplace") Workplace workplace,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("workbook", workbookService.findById(workbookId));
            model.addAttribute("workplaces", workbookService.findWorkplacesByWorkbookId(workbookId));
            model.addAttribute("totalWorkplaces", workbookService.findWorkplacesByWorkbookId(workbookId).size());
            return "workplace/form";
        }
        workbookService.updateWorkplace(workbookId, workplaceId, workplace);
        redirectAttrs.addFlashAttribute("successMessage", "Poste mis à jour.");
        return "redirect:/workbooks/" + workbookId;
    }

    // ── Workplace: Delete ──────────────────────────────────────────────────────

    @PostMapping("/{workbookId}/workplaces/{workplaceId}/delete")
    public String deleteWorkplace(@PathVariable Long workbookId,
                                  @PathVariable Long workplaceId,
                                  RedirectAttributes redirectAttrs) {
        workbookService.deleteWorkplace(workbookId, workplaceId);
        redirectAttrs.addFlashAttribute("successMessage", "Poste supprimé.");
        return "redirect:/workbooks/" + workbookId;
    }

    // ── Home redirect ──────────────────────────────────────────────────────────

    @GetMapping("/")
    public String home() {
        return "redirect:/workbooks";
    }
}
