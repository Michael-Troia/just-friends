package com.example.justfriends.Controllers;

import com.example.justfriends.Models.User;
import com.example.justfriends.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    public UserRepo userRepo;

    public UserController(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @GetMapping("/sign-up")
    public String showSignUp(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute User user,
                               Model viewModel){
        User dbUser = userRepo.save(user);
        viewModel.addAttribute("user", dbUser);
        return  "redirect:/show/"+dbUser.getId();
    }

    @GetMapping("/show/{id}")
    public String showUser(Model model,
                           @PathVariable long id){
        model.addAttribute(userRepo.getOne(id));
        return "show";
    }
}
