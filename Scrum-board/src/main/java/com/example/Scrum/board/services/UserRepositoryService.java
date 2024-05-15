package com.example.Scrum.board.services;

import com.example.Scrum.board.models.User;
import com.example.Scrum.board.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserRepositoryService {
    /**
     * Управление пользователями в БД
     */
    private final UserRepository userRepository;

    /**
     * Создание пользователя
     *
     * @param user новый пользователь
     * @return пользователь
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Получить всех пользователей
     *
     * @return список пользователей
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Получить пользователя по ID
     *
     * @param id уникальный идентификатор пользователя
     * @return пользователь
     */
    public User getById(int id) {
        return userRepository.getById(id);
    }

    /**
     * Обновить пользователя
     *
     * @param user пользователь
     */
    public void update(User user) {
        userRepository.updateUser(user.getFirstName(), user.getEmail(), user.getId());
    }

    /**
     * Удалить пользователя
     *
     * @param user пользователь
     */
    public void deleteUser(User user) {
        userRepository.delete(user);
    }


    /**
     * Ищет пользователя по его имени пользователя
     *
     * @param username
     * @return
     */
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;

    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;

    }

    /**
     * Проверяет аутентификацию пользователя по имени пользователя или email и паролю.
     *

     * @param password пароль
     * @return true, если аутентификация успешна, в противном случае - false
     */

    public boolean authenticate(String username, String email, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(email);
        }
        // Если пользователь найден, проверяем совпадение пароля
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }

        // Если пользователь не найден, возвращаем false
        return false;
    }
    /**
     * Получить пользователя по имени пользователя
     *
     * @param username имя пользователя
     * @return пользователь
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }




}

