package com.j25.finalproject.final_project.controller;

import com.j25.finalproject.final_project.model.Account;
import com.j25.finalproject.final_project.model.Nationality;
import com.j25.finalproject.final_project.model.Specialization;
import com.j25.finalproject.final_project.model.Visits;
import com.j25.finalproject.final_project.model.dto.AccountPasswordResetRequest;
import com.j25.finalproject.final_project.model.specification.SearchRequest;
import com.j25.finalproject.final_project.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/user/")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String registrationForm(Model model, Account account) {
        account.setNationality(Nationality.POLISH);
        model.addAttribute("newAccount", account);
        model.addAttribute("nationalities", Nationality.values());

        return "registration-form-patient";
    }

    @PostMapping("/register")
//    po @Valid musi być BindingResult
    public String register(@Valid Account account,
                           BindingResult result,
                           String passwordConfirm,
                           Model model) {

        return messagesError(account, result, passwordConfirm, model);
    }

    private String messagesError(@Valid Account account, BindingResult result, String passwordConfirm, Model model) {
        if (result.hasErrors()) {
            return registrationError(model, account, "Wrong " + result.getFieldError().getField() + ": " +
                    result.getFieldError().getDefaultMessage());
        }
//        todo: tworzenie konta
        if (!account.getPassword().equals(passwordConfirm)) {
            return registrationError(model, account, "Password do not match.");
        }

        if (!accountService.register(account)) {
            return registrationError(model, account, "User with given username already exists. Please enter a different username.");
        }

        return registrationSuccessfully(model, account, "Your patient has been successfully created. Please login.");
    }

    @GetMapping("/doctor/register")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String registrationForm2(Model model, Account account) {

        account.setNationality(Nationality.POLISH);
        account.setSpecialization(Specialization.CARDIOLOGIST);

        model.addAttribute("newAccount", account);
        model.addAttribute("nationalities", Nationality.values());
        model.addAttribute("specializations", Specialization.values());

        return "registration-form-doctor";
    }

    @PostMapping("/doctor/register")
    @PreAuthorize(value = "hasAnyRole('ADMIN') ")
    public String register2(@Valid Account account,
                            BindingResult result,
                            String passwordConfirm,
                            Model model) {

        return messagesError(account, result, passwordConfirm, model);
    }

    private String registrationError(Model model, Account account, String s) {
        model.addAttribute("newAccount", account);
        model.addAttribute("errorMessage", s);

        return "registration-form-patient";
    }

    private String registrationSuccessfully(Model model, Account account, String s) {
        model.addAttribute("newAccount", account);
        model.addAttribute("errorMessage", s);

        return "login-form";
    }

    @GetMapping("/doctor/list")
    public String getDoctorsList(Model model) {
        model.addAttribute("accounts", accountService.getAllDoctors());

        return "doctor-list";
    }

    @GetMapping("/search")
    public String searchForm(Model model, SearchRequest dto) {
        model.addAttribute("searchDto", dto);
        model.addAttribute("categories", Specialization.values());

        return "search-form";
    }

    @PostMapping("/search")
    public String searchProducts(SearchRequest dto, Model model, Principal principal) {
        List<Account> accounts = accountService.getAllFromDto(dto);
        if (!accounts.isEmpty()) {
            model.addAttribute("accounts", accounts);
            model.addAttribute("ownerName", principal.getName());

            return "doctor-list";
        }
        return "redirect:/";
    }


    @GetMapping("/account/details")
    @PreAuthorize(value = "hasAnyRole('PATIENT')")
    public String accountPatientDetails(Model model, Principal principal) {
        Optional<Account> optionalAccount = accountService.findByUsername(principal);
        if (optionalAccount.isPresent()) {
            model.addAttribute("patient", optionalAccount.get());
            return "patient-details";
        }
        return "redirect:/user/details";
    }

    @GetMapping("/account/doctor/details")
    @PreAuthorize(value = "hasAnyRole('DOCTOR')")
    public String accountDoctorDetails(Model model, Principal principal) {
        Optional<Account> optionalAccount = accountService.findByUsername(principal);
        if (optionalAccount.isPresent()) {
            model.addAttribute("doctor", optionalAccount.get());
            return "doctor-details";
        }
        return "redirect:/user/details";
    }

    @GetMapping("/details")
    public String patientDetails(Model model,
                                 @RequestParam(name = "id") Long id) {
        Optional<Account> optionalAccount = accountService.findById(id);
        if (optionalAccount.isPresent()) {
            model.addAttribute("patient", optionalAccount.get());
            return "patient-details";
        }
        return "redirect:/visit/list";
    }


    @GetMapping("/resetPassword")
    public String resetPassword(Model model, @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);

        if (accountOptional.isPresent()) {
            model.addAttribute("account", accountOptional.get());
            return "account-passwordreset";
        }
        return "redirect:/";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(AccountPasswordResetRequest request) {
        accountService.resetPassword(request);

        return "redirect:/";
    }


    @GetMapping("/account/edit")
    public String patientEdit(Model model, Principal principal) {
        Optional<Account> optionalAccount = accountService.findByUsername(principal);
        if (optionalAccount.isPresent()) {
            model.addAttribute("patient", optionalAccount.get());
            model.addAttribute("nationalities", Nationality.values());
            return "patient-form";
        }
        return "redirect:/user/details";
    }

}
