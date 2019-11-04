package com.j25.finalproject.final_project.repository;

import com.j25.finalproject.final_project.model.Account;
import com.j25.finalproject.final_project.model.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {

    Optional<AccountRole> findByName(String name);

    boolean existsByName(String name);

}
