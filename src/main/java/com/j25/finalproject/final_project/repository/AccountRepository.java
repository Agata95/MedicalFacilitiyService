package com.j25.finalproject.final_project.repository;

import com.j25.finalproject.final_project.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Account> findBySurname(String surname);

    List<Account> findAllByNameOrSurnameLike(String name, String surname);
}
