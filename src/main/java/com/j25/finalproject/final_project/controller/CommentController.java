package com.j25.finalproject.final_project.controller;

import com.j25.finalproject.final_project.model.CommentVisit;
import com.j25.finalproject.final_project.service.CommentService;
import com.j25.finalproject.final_project.service.VisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/comment/")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/add")
    public String commentAdd(Model model,
                             CommentVisit comment,
                             @RequestParam(name = "id") Long visitId) {
        comment.setId(null);
        model.addAttribute("comment", comment);
        model.addAttribute("visits", visitId);

        return "comment-add";
    }

    @PostMapping("/add")
    public String commentAdd(CommentVisit comment, Long visitId) {
        commentService.saveComment(comment, visitId);

        return "redirect:/visit/details/" + visitId;
    }

    @GetMapping("/remove")
    public String commentRemove(HttpServletRequest request,
                              @RequestParam(name = "id") Long visitId) {
        String referer = request.getHeader("referer");

        commentService.remove(visitId);

        if (referer != null) {
            return "redirect:" + referer;
        }

        return "redirect:/visit/list";
    }
}
