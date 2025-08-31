package org.todo.todorails.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Maneja solo los errores genéricos (404, 500, etc.)
     * IMPORTANTE: No redirigimos a /login ni /dashboard,
     * porque los errores de autenticación ya los maneja Spring Security.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Retorna la vista "error.html" en templates
        return "error";
    }
}
