package com.j25.finalproject.final_project.repository;

import com.j25.finalproject.final_project.model.CommentVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentVisit, Long> {
}
