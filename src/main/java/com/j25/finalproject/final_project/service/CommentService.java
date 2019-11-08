package com.j25.finalproject.final_project.service;

import com.j25.finalproject.final_project.model.CommentVisit;
import com.j25.finalproject.final_project.model.Visits;
import com.j25.finalproject.final_project.repository.CommentRepository;
import com.j25.finalproject.final_project.repository.VisitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VisitsRepository visitsRepository;

    public void saveComment(CommentVisit comment, Long visitId) {
        if (visitsRepository.existsById(visitId)) {
            Optional<Visits> visitsOptional = visitsRepository.findById(visitId);
            comment.setVisits(visitsOptional.get());
            commentRepository.save(comment);
        } else {
            throw new EntityNotFoundException("Visit not found.");
        }
    }

    public void remove(Long visitId) {
        Optional<Visits> optionalVisits = visitsRepository.findById(visitId);
        commentRepository.deleteAll(optionalVisits.get().getComment());
    }
}
