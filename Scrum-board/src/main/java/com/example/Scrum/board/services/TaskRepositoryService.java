package com.example.Scrum.board.services;

import com.example.Scrum.board.models.Task;
import com.example.Scrum.board.models.TaskStatus;
import com.example.Scrum.board.models.User;
import com.example.Scrum.board.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskRepositoryService {
    /**
     * Управление задачами через базу данных
     */
    private final TaskRepository taskRepository;

    /**
     * Получение списка всех задач
     */
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    /**
     * Создание новой задачи
     */
    public void create(Task task, User user) {
        task.setUser(user);
        taskRepository.save(task);
    }

    public void taskInsert(String title, String description, int id) {
        taskRepository.insertTask(title, description, id);
    }

    /**
     * Получить список задач по названию
     */
    public List<Task> getByTitle(String title) {
        return taskRepository.getByTitle(title);
    }

    /**
     * Получить список задач по статусу задачи
     */
    public List<Task> getByStatus(TaskStatus taskStatus) {
        return taskRepository.getByTaskStatus(taskStatus);
    }

    /**
     * Удалить задачу
     */
    public void delTask(Task task) {
        taskRepository.delete(task);
    }

    /**
     * Удалить задачу по ID
     */
    public void delTask(int id) {
        taskRepository.delete(getById(id));
    }

    /**
     * Получить задачу по ID
     */
    public Task getById(int id) {
        return taskRepository.getById(id);
    }

    /**
     * Обновить задачу
     */
    public void updateTask(Task task) {
        taskRepository.updateTask(task.getTitle(), task.getDescription(), task.getTaskStatus(), task.getId());
    }

    /**
     * Обновить задачу по ограниченным параметрам
     */
    public void updateTask(int id, String title, String desc) {
        Task task = taskRepository.getById(id);
        if (task != null) {
            task.setTitle(title);
            task.setDescription(desc);
            taskRepository.save(task);
        } else {
            throw new RuntimeException("error");
        }
    }

    /**
     * Обновить статус задачи по ID
     */
    public void updateStatusById(int id, TaskStatus taskStatus) {
        Task task = getById(id);
        task.setTaskStatus(taskStatus);
        updateTask(task);
    }

    public List<Task> getTasksByUserRole(String userRoles) {
        if ("ADMIN".equals(userRoles)) {
            return taskRepository.findAll();
        } else if ("MANAGER".equals(userRoles)) {
            List<Task> managerTasks = taskRepository.findByUserRoles("MANAGER");
            List<Task> gagerTasks = taskRepository.findByUserRoles("GAGER");
            managerTasks.addAll(gagerTasks);
            return managerTasks;
        } else if ("GAGER".equals(userRoles)) {
            return taskRepository.findByUserRoles("GAGER");
        }
        return List.of();
    }
}

