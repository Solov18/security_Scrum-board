package com.example.Scrum.board.controllers.web;

import com.example.Scrum.board.models.Task;
import com.example.Scrum.board.models.TaskStatus;
import com.example.Scrum.board.models.User;
import com.example.Scrum.board.services.TaskRepositoryService;
import com.example.Scrum.board.services.UserRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * SCRUM Board. Управлене задачами
 */
@Controller
@RequiredArgsConstructor
public class ScrumControllerWeb {
    /**
     * Инкапсулируем управление пользователями в БД
     */
    private final UserRepositoryService userRepositoryService;
    /**
     * Инкапсулируем управление задачами в БД
     */
    private final TaskRepositoryService taskRepositoryService;

    /**
     * Получить главную страницу и scrum доску
     *
     * @param model взаимодействие с шаблонизатором
     * @return главная страница html
     */
    @GetMapping("/main")
    public String getScrum(Model model, Principal principal) {
        // Проверяем, что Principal объект не null
        if (principal == null) {
            // Если Principal объект равен null, вернуть страницу с сообщением об ошибке
            return "login";
        }
        // Получаем имя текущего пользователя
        String username = principal.getName();

        // Поиск пользователя по имени
        Optional<User> userOptional = userRepositoryService.findByUsername(username);

        if (userOptional.isPresent()) {
            // Если пользователь найден, получаем его роль и задачи
            User currentUser = userOptional.get();
            String userRole = currentUser.getRoles();
            List<Task> tasks = taskRepositoryService.getTasksByUserRole(userRole);

            model.addAttribute("tasks", tasks);
            return "index";
        } else {
            // Если пользователь не найден, вернуть страницу с сообщением об ошибке
            return "error";
        }
    }


    /**
     * Изменить статус на DOING у задачи
     *
     * @param model     взаимодействие с шаблонизатором
     * @param id        идентификатор задачи
     * @param principal информация о текущем пользователе
     * @return главная страница - scrum доска
     */
    @GetMapping("/task-doing/{id}")
    public String turnToDoing(Model model, @PathVariable("id") int id, Principal principal) {
        // Получаем имя текущего пользователя
        String username = principal.getName();

        // Получаем задачу по идентификатору
        Task task = taskRepositoryService.getById(id);

        if (task != null && task.getUser() != null && task.getUser().getUsername().equals(username)) {
            // Если задача найдена и она назначена текущему пользователю, меняем статус на DOING
            taskRepositoryService.updateStatusById(id, TaskStatus.DOING);
            return "redirect:/main"; // Перенаправляем на главную страницу
        } else {
            // Если задача не найдена или она не назначена текущему пользователю, вернем ошибку или перенаправим на другую страницу
            return "error";
        }
    }

    /**
     * Изменить статус на DONE у задачи
     *
     * @param model     взаимодействие с шаблонизатором
     * @param id        идентификатор задачи
     * @param principal информация о текущем пользователе
     * @return главная страница - scrum доска
     */
    @GetMapping("/task-done/{id}")
    public String turnToDone(Model model, @PathVariable("id") int id, Principal principal) {
        // Получаем текущего пользователя
        Optional<User> currentUserOptional = userRepositoryService.findByUsername(principal.getName());

        // Проверяем, найден ли пользователь
        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();

            // Получаем задачу по ее id
            Task task = taskRepositoryService.getById(id);

            // Проверяем, назначена ли данная задача текущему пользователю
            if (task != null && task.getUser().getId() == currentUser.getId()) {
                // Если задача назначена текущему пользователю, меняем ее статус
                taskRepositoryService.updateStatusById(id, TaskStatus.DONE);
            } else {
                // Если задача не назначена текущему пользователю, можно сгенерировать исключение или вернуть сообщение об ошибке
                return "error";
            }
        } else {
            // Если пользователь не найден, вернуть страницу с сообщением об ошибке
            return "error";
        }

        // После изменения статуса задачи возвращаемся на главную страницу
        return "redirect:/main";
    }

    /**
     * Изменить статус на TO DO у задачи
     *
     * @param model     взаимодействие с шаблонизатором
     * @param id        идентификатор задачи
     * @param principal информация о текущем пользователе
     * @return главная страница - scrum доска
     */
    @GetMapping("/task-todo/{id}")
    public String turnToToDo(Model model, @PathVariable("id") int id, Principal principal) {
        // Получаем текущего пользователя
        Optional<User> currentUserOptional = userRepositoryService.findByUsername(principal.getName());

        // Проверяем, найден ли пользователь
        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();

            // Получаем задачу по ее id
            Task task = taskRepositoryService.getById(id);

            // Проверяем, назначена ли данная задача текущему пользователю
            if (task != null && task.getUser().getId() == currentUser.getId()) {
                // Если задача назначена текущему пользователю, меняем ее статус
                taskRepositoryService.updateStatusById(id, TaskStatus.TO_DO);
            } else {
                // Если задача не назначена текущему пользователю, можно сгенерировать исключение или вернуть сообщение об ошибке
                return "error";
            }
        } else {
            // Если пользователь не найден, вернуть страницу с сообщением об ошибке
            return "error";
        }

        // После изменения статуса задачи возвращаемся на главную страницу
        return "redirect:/main";
    }

    /**
     * Удалить задачу
     */
    @GetMapping("/task-delete/{id}")
    public String turnToDelete(Model model, @PathVariable("id") int id, Principal principal) {
        // Получаем текущего пользователя
        Optional<User> currentUserOptional = userRepositoryService.findByUsername(principal.getName());

        // Проверяем, найден ли пользователь
        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();
            String userRole = currentUser.getRoles();

            // Проверяем, имеет ли текущий пользователь права на удаление задачи
            if (userRole.equals("ADMIN") || userRole.equals("MANAGER")) {
                // Если пользователь имеет права, удаляем задачу
                taskRepositoryService.delTask(id);
            } else {
                // Если у пользователя нет прав на удаление, можно сгенерировать исключение или вернуть сообщение об ошибке
                return "error";
            }
        } else {
            // Если пользователь не найден, вернуть страницу с сообщением об ошибке
            return "error";
        }

        // После удаления задачи возвращаемся на главную страницу
        return "redirect:/main";
    }

    /**
     * Получить форму по редактированию задачи
     *
     * @param task      задача
     * @param principal информация о текущем пользователе
     * @return форма html для редактирования, если пользователь имеет права на редактирование, иначе страница с ошибкой
     */
    @GetMapping("/edit-task/{task}")
    public String formUpdateTask(@PathVariable("task") Task task, Principal principal) {
        // Получаем текущего пользователя
        Optional<User> currentUserOptional = userRepositoryService.findByUsername(principal.getName());

        // Проверяем, найден ли пользователь и имеет ли он права на редактирование
        if (currentUserOptional.isPresent() && (currentUserOptional.get().getRoles().equals("ADMIN") || currentUserOptional.get().getRoles().equals("MANAGER"))) {
            // Если пользователь найден и имеет права на редактирование, отображаем форму редактирования
            return "edit-task";
        } else {
            // Если пользователь не найден или не имеет прав на редактирование, возвращаем страницу с ошибкой
            return "error";
        }
    }

    /**
     * Редактирование задачи в базе данных
     *
     * @param task      принимаемая задача из редакции
     * @param id        айди задачи для редактирования
     * @param principal информация о текущем пользователе
     * @return главная страница - scrum доска, если редактирование выполнено успешно, иначе страница с ошибкой
     */
    @PostMapping("/edit-task/{id}")
    public String updateTask(Task task, @PathVariable("id") int id, Principal principal) {
        // Получаем текущего пользователя
        Optional<User> currentUserOptional = userRepositoryService.findByUsername(principal.getName());

        // Проверяем, найден ли пользователь и имеет ли он права на редактирование
        if (currentUserOptional.isPresent() && (currentUserOptional.get().getRoles().equals("ADMIN") || currentUserOptional.get().getRoles().equals("MANAGER"))) {
            // Если пользователь найден и имеет права на редактирование, обновляем задачу
            taskRepositoryService.updateTask(id, task.getTitle(), task.getDescription());
            return "redirect:/main";
        } else {
            // Если пользователь не найден или не имеет прав на редактирование, возвращаем страницу с ошибкой
            return "error";
        }
    }

    /**
     * Форма создания новой задачи
     *
     * @param task  передаваемая задача
     * @param id    уникальный идентификатор пользователя
     * @param model связь с шаблонизатором
     * @return возвращаем форму html
     */
    @GetMapping("/task-create/{id}")
    public String getCreateTaskForm(Task task, @PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "task-create";
    }

    /**
     * Создание новой задачи
     *
     * @param task  передаваемая задача из браузера
     * @param id    уникальный идентификатор пользователя
     * @param model связь с шаблонизатором
     * @return редирект на главную страницу
     */
    @PostMapping("/task-create/{id}")
    public String createTask(Task task, @PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        taskRepositoryService.taskInsert(task.getTitle(), task.getDescription(), id);
        return "redirect:/main";
    }

    /**
     * Получить задачи в зависимости от роли пользователя.
     *
     * @param model     взаимодействие с шаблонизатором
     * @param principal информация о текущем пользователе
     * @return html страница с задачами
     */
    @GetMapping("/tasks")
    public String getTasksByUserRole(Model model, Principal principal) {
        // Получаем имя текущего пользователя
        String username = principal.getName();

        // Поиск пользователя по имени
        Optional<User> userOptional = userRepositoryService.findByUsername(username);

        if (userOptional.isPresent()) {
            // Если пользователь найден, получаем его роль и задачи
            User currentUser = userOptional.get();
            String userRole = currentUser.getRoles();
            List<Task> tasks = taskRepositoryService.getTasksByUserRole(userRole);

            model.addAttribute("tasks", tasks);
            return "tasks";
        } else {
            // Если пользователь не найден, вернуть страницу с сообщением об ошибке
            return "error";
        }
    }
}
