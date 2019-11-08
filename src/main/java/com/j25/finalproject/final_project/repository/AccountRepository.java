package com.j25.finalproject.final_project.repository;

import com.j25.finalproject.final_project.model.Account;
import com.j25.finalproject.final_project.model.Specialization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Account> findBySurname(String surname);

    List<Account> getAllByNameContaining(String name);

    List<Account> getAllBySurnameContaining(String surname);

    List<Account> getAllBySpecialization(Specialization specialization);

    List<Account> getAllByNameOrSurnameOrSpecializationAndId(String name, String surname, Specialization specialization, Long Id);


}
