package com.example.Scrum.board.controllers.web;

import com.example.Scrum.board.models.User;
import com.example.Scrum.board.services.UserRepositoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    private final UserRepositoryService userRepositoryService;

    public RegistrationController(UserRepositoryService userRepositoryService) {
        this.userRepositoryService = userRepositoryService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";

    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password) {
        // Проверка, существует ли пользователь с таким именем пользователя или адресом электронной почты
        if (userRepositoryService.existsByUsername(username)) {
            // Если пользователь с таким именем пользователя уже существует,
            // вы можете вернуть сообщение об ошибке
            // или перенаправить его на страницу регистрации с сообщением об ошибке
            return "redirect:/registration?error=usernameExists";  //??????
        }
        if (userRepositoryService.existsByEmail(email)) {
            // Если пользователь с таким адресом электронной почты уже существует, аналогично, вы можете вернуть сообщение об ошибке или перенаправить его на страницу регистрации с сообщением об ошибке
            return "redirect:/registration?error=emailExists"; //??????
        }
        // Создание нового пользователя
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        // Сохранение нового пользователя в базе данных
        userRepositoryService.createUser(newUser);
        // Отправка подтверждающего письма на электронную почту
        //  код для отправки электронного письма с ссылкой для подтверждения регистрации

        // После успешной регистрации перенаправление на страницу входа
        return "login";
    }
}

