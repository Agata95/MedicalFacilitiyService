package com.j25.finalproject.final_project.controller;

import com.j25.finalproject.final_project.model.Visits;
import com.j25.finalproject.final_project.service.VisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(path = "/visit/")
public class VisitsController {

    @Autowired
    private VisitsService visitsService;

    @GetMapping("/add")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String addForm(Model model, Visits visit) {
        model.addAttribute("visit", visit);

        return "visit-form";
    }

    @PostMapping("/add")
    public String addForm(Visits visit, Principal principal) {
        visitsService.addVisit(visit, principal);

        return "redirect:/visit/list";
    }

    @GetMapping("/list")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String visitList(Model model, Principal principal) {
        Set<Visits> visitsSet = visitsService.getAllVisitDoctor(principal.getName());
        model.addAttribute("visits", visitsSet);

        return "visit-list";
    }

    @GetMapping("/patient/list")
    @PreAuthorize(value = "hasAnyRole('PATIENT')")
    public String visitListPatient(Model model, Principal principal) {
        Set<Visits> visitsSet = visitsService.getAllVisitPatient(principal.getName());
        model.addAttribute("visits", visitsSet);

        return "visit-list";
    }

    @GetMapping("/list/archived")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String visitListArchived(Model model, Principal principal) {
        Set<Visits> visitsSet = visitsService.getAllArchived(principal.getName());
        model.addAttribute("visits", visitsSet);

        return "visit-list";
    }

    @GetMapping("/list/patient/archived")
    @PreAuthorize(value = "hasAnyRole('PATIENT')")
    public String visitListArchivedPatient(Model model, Principal principal) {
        Set<Visits> visitsSet = visitsService.getAllArchivedPatient(principal.getName());
        model.addAttribute("visits", visitsSet);

        return "visit-list";
    }


    @GetMapping("/list/current")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String visitListCurrent(Model model, Principal principal) {
        Set<Visits> visitsSet = visitsService.getAllCurrentVisitDoctor(principal.getName());
        model.addAttribute("visits", visitsSet);

        return "visit-list";
    }

    @GetMapping("/book/{id}")
    @PreAuthorize(value = "hasAnyRole('PATIENT')")
    public String setBook(@PathVariable("id") Long id, Principal patient) {
        visitsService.setBook(id, patient.getName());
//        Set<Visits> visitsSet = visitsService.setBook(id, patient.getName());
//        model.addAttribute("visits", visitsSet);

        return "redirect:/visit/patient/list";
    }

    @GetMapping("/archive/{id}")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String setArchive(@PathVariable("id") Long id, Principal principal) {
        visitsService.setArchive(id, principal.getName());

        return "redirect:/visit/list";
    }


    @GetMapping("/listDoctor/{id}")
    public String visitListDoctor(@PathVariable("id") Long id, Model model) {
        Set<Visits> visitsSet = visitsService.getAllDoctorVisit(id);
        model.addAttribute("visits", visitsSet);

        return "visit-list";
    }

    @GetMapping("/remove/{id}")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String visitRemove(@PathVariable("id") Long id) {
        visitsService.removeVisit(id);

        return "redirect:/visit/list";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String visitEdit(Model model,
                              @PathVariable("id") Long id) {
        Optional<Visits> optionalVisit = visitsService.findByVisitId(id);
        if (optionalVisit.isPresent()) {
            model.addAttribute("visit", optionalVisit.get());
            return "visit-form";
        }
        return "redirect:/visit/list";
    }


//    @GetMapping("/details/{id}")
//    public String visitDetails(@PathVariable("id") Long id, Principal principal, Model model) {
//
//        Account doctor = (Account) visitsService.getDoctor(id);
//
//        model.addAttribute("doctor", doctor);
//        visitsService.setArchive(id, principal.getName());
//
//        return "visit-details";
//    }
}
