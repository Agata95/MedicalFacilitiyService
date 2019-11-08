package com.j25.finalproject.final_project.model.specification;

import com.j25.finalproject.final_project.model.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String name;
    private String surname;
    private Specialization specialization;
    private Long from = 0L;
    private Long upTo = 10000L;
}
