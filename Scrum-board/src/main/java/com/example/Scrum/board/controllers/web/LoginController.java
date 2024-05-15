/*
package com.example.Scrum.board.controllers.web;

import com.example.Scrum.board.services.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
   // private final UserRepositoryService userRepositoryService;

*/
/*
    @Autowired
    public LoginController(UserRepositoryService userRepositoryService) {
        this.userRepositoryService = userRepositoryService;
    }
*//*


    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
}
    */
/*@PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("email") String email,
                            @RequestParam("password") String password) {
        // Проверяем аутентификацию пользователя
        boolean isAuthenticated = userRepositoryService.authenticate(username,email, password);

        if (isAuthenticated) {
            // Если аутентификация успешна, перенаправляем пользователя на главную страницу
            return "/index";
        } else {
            // Если аутентификация не удалась, возвращаем пользователя на страницу входа с сообщением об ошибке
            return "redirect:/login?error"; // Можно добавить параметр для передачи сообщения об ошибке, например, "?error=authenticationFailed"
        }
    }
}
*/
