package com.innowise.marketplace.web;

import com.innowise.marketplace.data.UserRepository;
import com.innowise.marketplace.model.User;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute @Valid RegistrationForm form, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "register";
        }
        if (userRepository.findByUsername(form.getUsername()) != null) {
            model.addAttribute("usernameTaken", true);
            return "register";
        }
        User user = new User(form.getUsername(), passwordEncoder.encode(form.getPassword()), form.getPhone());
        userRepository.save(user);
        return "redirect:/login";
    }
}
