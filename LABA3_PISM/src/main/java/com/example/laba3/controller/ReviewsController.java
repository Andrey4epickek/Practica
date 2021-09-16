package com.example.laba3.controller;

import com.example.laba3.models.Reviews;
import com.example.laba3.repo.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping( "/mainreviews")
public class ReviewsController {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @GetMapping
    public String reviews( Model model) {
        Iterable<Reviews> reviews = reviewsRepository.findAll();
        model.addAttribute("reviews",reviews);
        return "review";
    }

    @GetMapping("/add")
    public String reviewadd( Model model)
    {
        return "review-add";
    }

    @PostMapping("/add")
    public String reviewsadd(@RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model) {
        Reviews review =new Reviews(title,anons,full_text);
        reviewsRepository.save(review);
        return "redirect:/mainreviews";
    }

    @GetMapping("/{id}")
    public String reviewdetails(@PathVariable(value="id") long id, Model model) {
        if(!reviewsRepository.existsById(id)) {
            return "redirect:/mainreviews";
        }
        Optional<Reviews> review = reviewsRepository.findById(id);
        ArrayList<Reviews> rev=new ArrayList<>();
        review.ifPresent(rev::add);
        model.addAttribute("review",rev);
        return "review-details";
    }

    @GetMapping("/{id}/edit")
    public String reviewedit(@PathVariable(value="id") long id, Model model) {
        if(!reviewsRepository.existsById(id)) {
            return "redirect:/mainreviews";
        }

        Optional<Reviews> review = reviewsRepository.findById(id);
        ArrayList<Reviews> rev=new ArrayList<>();
        review.ifPresent(rev::add);
        model.addAttribute("review",rev);
        return "review-edit";
    }

    @PostMapping("/{id}/edit")
    public String reviewsupdate(@PathVariable(value="id") long id,@RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model) {
        Reviews review = reviewsRepository.findById(id).orElseThrow();
        review.setTitle(title);
        review.setAnons(anons);
        review.setFull_text(full_text);
        reviewsRepository.save(review);
        return "redirect:/mainreviews";
    }

    @PostMapping("/{id}/remove")
    public String reviewsdelete(@PathVariable(value="id") long id) {
        Reviews review = reviewsRepository.findById(id).orElseThrow();
        reviewsRepository.delete(review);
        return "redirect:/mainreviews";
    }

}
