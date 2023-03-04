package pro.sky.telegrambotteamwork.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.exception.UserNotFoundException;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;

import java.util.Collection;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(PetService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser (User user){
        logger.info("Was invoked method for add new user");
        if(user.getPhone().equals(findByPhone(user.getPhone()))){ //проверка что юзер уже добавлен
            throw new RuntimeException("User with this phone has already been added");
        }
        return userRepository.save(user);
    }

    public User deleteUser (User user) {
        userRepository.delete(user);
        logger.info("Was invoked method for delete new user");
        return user;
    }

    public Collection<User> findByPhone(String phone) {
        return userRepository.findAllByPhone(phone);
    }

    public User readInfo (long id) {
        logger.error("There is not user with id = " + id);
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

}
