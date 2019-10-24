package com.j25.finalproject.final_project.controller;


import com.j25.finalproject.final_project.model.Account;
import com.j25.finalproject.final_project.model.dto.AccountPasswordResetRequest;
import com.j25.finalproject.final_project.service.AccountRoleService;
import com.j25.finalproject.final_project.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(path = "/admin/account/")
// dostęp chroniony tylko regułą w cudzysłowiu
public class AdminAccountController {

    private AccountService accountService;
    private AccountRoleService accountRoleService;

    @Autowired
    public AdminAccountController(AccountService accountService, AccountRoleService accountRoleService) {
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
    }

    @GetMapping("/list")
    @PreAuthorize(value = "hasAnyRole('ACCOUNT_MANAGER', 'ADMIN') ")
    public String getUserList(Model model) {
        model.addAttribute("accounts", accountService.getAll());
        return "account-list";
    }

    @GetMapping("/toggleLock")
    @PreAuthorize(value = "hasAnyRole('ACCOUNT_MANAGER', 'ADMIN')")
    public String toggleLock(@RequestParam(name = "accountId") Long accountId) {
        accountService.toggleLock(accountId);

        return "redirect:/admin/account/list";
    }

    @GetMapping("/remove")
    @PreAuthorize(value = "hasAnyRole('ACCOUNT_REMOVER', 'ADMIN')")
    public String remove(@RequestParam(name = "accountId") Long accountId) {
        accountService.remove(accountId);

        return "redirect:/admin/account/list";
    }

    @GetMapping("/resetPassword")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String resetPassword(Model model, @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);

        if (accountOptional.isPresent()) {
            model.addAttribute("account", accountOptional.get());
            return "account-passwordreset";
        }
        return "redirect:/admin/account/list";
    }

    @PostMapping("/resetPassword")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String resetPassword(AccountPasswordResetRequest request) {
        accountService.resetPassword(request);

        return "redirect:/admin/account/list";
    }

    @GetMapping("/editRoles")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String editRoles(Model model, @RequestParam(name = "accountId") Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (accountOptional.isPresent()) {
            model.addAttribute("roles", accountRoleService.getAll());
            model.addAttribute("account", accountOptional.get());

            return "account-roles";
        }
        return "redirect:/admin/account/list";
    }

    @PostMapping("/editRoles")

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public String editRoles(Long accountId, HttpServletRequest request) {
        accountService.editRoles(accountId, request);

        return "redirect:/admin/account/list";
    }

    @GetMapping("/list/getPage")
    public String getPage(Model model,
                          @RequestParam(name = "pageNumber", defaultValue = "0") int page) {
        PageRequest pageable = PageRequest.of(page - 1, 15);
        Page<Account> accountPage = accountService.getPage(pageable);
        int totalPages = accountPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);

            return "account-list";
        }
        model.addAttribute("activeAccountList", true);
        model.addAttribute("accountList", accountPage.getContent());


        return "redirect:/account/list/page/" + page;
    }
}
