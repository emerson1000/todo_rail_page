package org.todo.todorails.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    // Handler for home (index) page
    @GetMapping("/")
    public String showIndexPage() {
        return "index";  // Returns the index.html page
    }

    // Handler for login page
    @GetMapping("/login")
    public String showLoginPage(RedirectAttributes redirectAttributes) {
        return "login";  // Returns the login.html page
    }

    // Handler for terms and conditions page
    @GetMapping("/terms")
    public String showTermsPage() {
        return "terms";
    }

    // Handler for testing authentication
    @GetMapping("/test-auth")
    public String testAuth(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            model.addAttribute("username", auth.getName());
            model.addAttribute("authorities", auth.getAuthorities());
            model.addAttribute("authenticated", true);
            System.out.println("✅ Usuario autenticado: " + auth.getName());
            System.out.println("🔐 Roles: " + auth.getAuthorities());
        } else {
            model.addAttribute("authenticated", false);
            System.out.println("❌ Usuario NO autenticado");
        }
        
        return "test-auth";
    }
}
