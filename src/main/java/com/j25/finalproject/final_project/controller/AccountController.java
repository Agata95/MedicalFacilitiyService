package com.j25.finalproject.final_project.controller;

import com.j25.finalproject.final_project.model.Account;
import com.j25.finalproject.final_project.model.Nationality;
import com.j25.finalproject.final_project.model.Specialization;
import com.j25.finalproject.final_project.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/details")
    public String patientDetails(Model model,
                                 @RequestParam(name = "id") Long id) {
        Optional<Account> optionalAccount = accountService.findById(id);
        if (optionalAccount.isPresent()) {
            model.addAttribute("patient", optionalAccount.get());
            return "patient-details";
        }
        return "redirect:/user/details";
    }

    @GetMapping("/doctor/list")
    public String getDoctorsList(Model model) {
        model.addAttribute("accounts", accountService.getAllDoctors());

        return "doctor-list";
    }

//    @RequestMapping(value = "/doctors", method = RequestMethod.GET)
//    public String showDoctorBySurname(@RequestParam (value = "search", required = false) String surname, Model model, Account account) {
//        model.addAttribute("search", accountService.listDoctorsBySurname(surname));
//        model.addAttribute("account", account);
//        return "doctor-list-search";
//    }


}
