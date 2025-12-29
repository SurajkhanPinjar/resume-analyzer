package com.resume_analyzer.utils;

import com.resume_analyzer.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthUtil {

    public static User currentUser(HttpServletRequest request) {
        Object user = request.getAttribute("user");
        if (user instanceof User) {
            return (User) user;
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }
}