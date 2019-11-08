package com.j25.finalproject.final_project.service;

import com.j25.finalproject.final_project.model.Account;
import com.j25.finalproject.final_project.model.AccountRole;
import com.j25.finalproject.final_project.model.dto.AccountPasswordResetRequest;
import com.j25.finalproject.final_project.model.specification.AccountSpecification;
import com.j25.finalproject.final_project.model.specification.SearchRequest;
import com.j25.finalproject.final_project.repository.AccountRepository;
import com.j25.finalproject.final_project.repository.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRoleService accountRoleService;
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountRoleService accountRoleService, AccountRoleRepository accountRoleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRoleService = accountRoleService;
        this.accountRoleRepository = accountRoleRepository;
    }

    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }
//        szyfrowanie hasła:
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getDefaultRoles());

//        zapis do bazy:
        accountRepository.save(account);

        return true;
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public void toggleLock(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);
            account.setLocked(!account.isLocked());

            accountRepository.save(account);
        }
    }

    //    nie usuwamy kont admin
    public void remove(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            Account account = accountRepository.getOne(accountId);

            if (!account.isAdmin()) {
                accountRepository.delete(account);
            }
        }
    }

    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public void resetPassword(AccountPasswordResetRequest request) {
        if (accountRepository.existsById(request.getAccountId())) {
            Account account = accountRepository.getOne(request.getAccountId());

//            należy pamiętać o zaszyfrowaniu hasła
            account.setPassword(passwordEncoder.encode(request.getResetpassword()));

            accountRepository.save(account);
        }
    }

    public void editRoles(Long accoutId, HttpServletRequest request) {
        if (accountRepository.existsById(accoutId)) {
            Account account = accountRepository.getOne(accoutId);

//            field - działasz na obiekcie, określamy na jakim obiekcie działamy th:object, a
//            field korzysta z gwiazdki (w obiekcie w którym działasz) *{...}
//            name - gdy nie działamy na obiektach (nic sztywnego do przesłania)
//            parametr jest przekazywany pod tą nazwą, nie ma modelu ani th:object, dlatego przekazuje
//            się po nazwie, gdy nie mamy th:object przekazujemy po nazwie
//            $ gdy uzywa się parametr, przekazany w atrybucie w mappingu,
//            @ gdy podajemy adres

//            kluczem w form parameters jest nazwa parametru th:name
            Map<String, String[]> formParameters = request.getParameterMap();
            Set<AccountRole> newCollectionOfRoles = new HashSet<>();

            for (String roleName : formParameters.keySet()) {
                String[] values = formParameters.get(roleName);

                if (values[0].equals("on")) {
                    Optional<AccountRole> accountRoleOptional = accountRoleRepository.findByName(roleName);

                    if (accountRoleOptional.isPresent()) {
                        newCollectionOfRoles.add(accountRoleOptional.get());
                    }
                }
            }

            account.setAccountRoles(newCollectionOfRoles);

            accountRepository.save(account);
        }

    }

    public Page<Account> getPage(Pageable of) {
        return accountRepository.findAll(of);
    }

    public List<Account> getAllDoctors() {
        List<Account> allAccounts = accountRepository.findAll();
        List<Account> doctors = new ArrayList<>();

        for (Account a : allAccounts) {
            if (a.getAccountRoles().stream()
                    .anyMatch(accountRole -> accountRole.getName().equals("DOCTOR"))) {
                doctors.add(a);
            }
        }
        return doctors;
    }


    public List<Account> getAllFromDto(SearchRequest dto) {
        return accountRepository.findAll(new AccountSpecification(dto));

    }

    public Optional<Account> findByUsername(Principal account) {
        return accountRepository.findByUsername(account.getName());
    }
}
