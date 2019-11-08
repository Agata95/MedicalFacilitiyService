package com.j25.finalproject.final_project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String newComment;


    @ToString.Exclude
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "visit_id")
    private Visits visits;

    @Override
    public String toString() {
        return newComment;
    }
}
