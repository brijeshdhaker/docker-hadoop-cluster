package org.examples.sb.services;



import org.examples.sb.exceptions.AppException;
import org.examples.sb.exceptions.ExceptionType;
import org.examples.sb.exceptions.NotFoundException;
import org.examples.sb.helpers.ModelHelper;
import org.examples.sb.models.User;
import org.examples.sb.repositories.UserRepository;
import org.examples.sb.repositories.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "userCache", key = "#id",condition = "#id>1")
    public User getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return ModelHelper.toUser(userEntity);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id : " + id);
        }
        userRepository.deleteById(id);
    }
}
