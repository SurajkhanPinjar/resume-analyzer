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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/login?error=true";
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return "redirect:/login?error=true";
        }

        // ✅ success
        session.setAttribute("user", user);
        return "redirect:/upload";
    }

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
        usage.setResumeUsed(0);
        usage.setZipUsed(0);
        usage.setUser(user);

        user.setUsage(usage);

        userRepository.save(user);

        session.setAttribute("user", user);
        return "redirect:/upload";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}