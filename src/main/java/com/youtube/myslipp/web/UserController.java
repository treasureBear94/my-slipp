package com.youtube.myslipp.web;

import com.youtube.myslipp.domain.User;
import com.youtube.myslipp.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/form")
    public String form() {
        return "/user/form";
    }

    @PostMapping("")
    public String create(User user) {
        System.out.println("user = " + user);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "/user/updateForm";
    }
}
