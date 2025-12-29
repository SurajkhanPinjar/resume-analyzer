package com.resume_analyzer.controller;

import com.resume_analyzer.entity.SubscriptionPlan;
import com.resume_analyzer.entity.Usage;
import com.resume_analyzer.entity.User;
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

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {

        if (userRepository.existsByEmail(email)) {
            return "redirect:/signup?error";
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setPlan(SubscriptionPlan.STARTER);
        user.setApiKey(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());

        Usage usage = new Usage();
        usage.setResumeUsed(0);
        usage.setZipUsed(0);
        user.setUsage(usage);

        userRepository.save(user);

        // ✅ Auto-login
        session.setAttribute("user", user);

        return "redirect:/upload";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null ||
                !passwordEncoder.matches(password, user.getPasswordHash())) {
            return "redirect:/login?error";
        }

        session.setAttribute("user", user);
        return "redirect:/upload";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}