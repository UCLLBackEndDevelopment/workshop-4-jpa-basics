package be.ucll.repository;

import be.ucll.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer {

    private UserRepository userRepository;

    public DbInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initialize() {
        User userJohn = new User("John Doe", 25, "john.doe@ucll.be", "john1234");
        userRepository.save(userJohn);

        User userJane = new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234");
        userRepository.save(userJane);

        User user = new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234");
        userRepository.save(user);

        user = new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234");
        userRepository.save(user);

        user = new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234");
        userRepository.save(user);
    }
}
