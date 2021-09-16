package com.example.laba3.controller;

import com.example.laba3.models.Requests;
import com.example.laba3.repo.RequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping( "/mainrequests")
public class RequestsController {

    @Autowired
    private RequestsRepository requestsRepository;

    @GetMapping
    public String requests( Model model) {
        Iterable<Requests> requests = requestsRepository.findAll();
        model.addAttribute("requests",requests);
        return "request";
    }

    @GetMapping("/add")
    public String requestadd( Model model)
    {
        return "request-add";
    }

    @PostMapping("/add")
    public String requestsadd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Requests request =new Requests(title,anons,full_text);
        requestsRepository.save(request);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String requestdetails(@PathVariable(value="id") long id, Model model) {
        if(!requestsRepository.existsById(id)) {
            return "redirect:/mainrequests";
        }
        Optional<Requests> request = requestsRepository.findById(id);
        ArrayList<Requests> rev=new ArrayList<>();
        request.ifPresent(rev::add);
        model.addAttribute("request",rev);
        return "request-details";
    }

    @GetMapping("/{id}/edit")
    public String rrequesteviewedit(@PathVariable(value="id") long id, Model model) {
        if(!requestsRepository.existsById(id)) {
            return "redirect:/mainrequests";
        }

        Optional<Requests> request = requestsRepository.findById(id);
        ArrayList<Requests> rev=new ArrayList<>();
        request.ifPresent(rev::add);
        model.addAttribute("request",rev);
        return "request-edit";
    }

    @PostMapping("/{id}/edit")
    public String requestupdate(@PathVariable(value="id") long id,@RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model) {
        Requests request = requestsRepository.findById(id).orElseThrow();
        request.setTitle(title);
        request.setAnons(anons);
        request.setFull_text(full_text);
        requestsRepository.save(request);
        return "redirect:/mainrequests";
    }

    @PostMapping("/{id}/remove")
    public String requestdelete(@PathVariable(value="id") long id) {
        Requests request = requestsRepository.findById(id).orElseThrow();
        requestsRepository.delete(request);
        return "redirect:/mainrequests";
    }

}
