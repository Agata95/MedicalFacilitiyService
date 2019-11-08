package com.j25.finalproject.final_project.model.specification;

import com.j25.finalproject.final_project.model.Account;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AccountSpecification implements Specification<Account> {
    private SearchRequest searchRequest;

    public AccountSpecification(SearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
        Predicate finalPredicate = builder.between(root.get("id"), searchRequest.getFrom(), searchRequest.getUpTo());

        if (searchRequest.getName() != null && !searchRequest.getName().isEmpty()) {
            finalPredicate = builder.and(finalPredicate, builder.like(root.get("name"), searchRequest.getName()));
        }
        if (searchRequest.getSurname() != null && !searchRequest.getSurname().isEmpty()) {
            finalPredicate = builder.and(finalPredicate, builder.like(root.get("surname"), searchRequest.getSurname()));
        }
        if (searchRequest.getSpecialization() != null) {
            finalPredicate = builder.and(finalPredicate, builder.equal(root.get("specialization"), searchRequest.getSpecialization()));
        }
        return finalPredicate;
    }
}
