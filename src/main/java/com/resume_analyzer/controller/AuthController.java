package com.resume_analyzer.controller;

import com.resume_analyzer.entity.Usage;
import com.resume_analyzer.entity.User;
import com.resume_analyzer.entity.SubscriptionPlan;
import com.resume_analyzer.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* ---------------- LOGIN ---------------- */

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        session.setAttribute("user", user);
        return "redirect:/upload";
    }

    /* ---------------- SIGNUP ---------------- */

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        if (userRepository.existsByEmail(email)) {
            return "redirect:/signup?error=email_exists";
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setPlan(SubscriptionPlan.STARTER);
        user.setApiKey(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());

        Usage usage = new Usage();
        usage.setUser(user);

        user.setUsage(usage);

        userRepository.save(user); // Cascade saves usage

        userRepository.save(user);
        session.setAttribute("user", user);

        return "redirect:/upload";
    }

    /* ---------------- LOGOUT ---------------- */

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}