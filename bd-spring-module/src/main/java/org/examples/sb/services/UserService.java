package org.examples.sb.services;



import lombok.extern.slf4j.Slf4j;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.exceptions.ExceptionType;
import org.examples.sb.exceptions.NotFoundException;
import org.examples.sb.helpers.ModelHelper;
import org.examples.sb.models.User;
import org.examples.sb.repositories.UserRepository;
import org.examples.sb.repositories.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @CachePut(value = "userCache", key = "#user.id", condition="#user.id != null")
    public UserEntity saveUser(User user) {
        UserEntity userEntity = ModelHelper.toUserEntity(user);
        return userRepository.save(userEntity);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(ModelHelper::toUser)
                .toList();
    }

    @Cacheable(value = "userCache", key = "#id",condition = "#id>1")
    public User getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return ModelHelper.toUser(userEntity);
    }

    @CacheEvict(value = "userCache", key = "#id", allEntries = false)
    public Boolean deleteUserById(Long id) throws AppException {
        Boolean result = Boolean.TRUE;
        if (userRepository.findById(id).orElse(null) != null) {
            userRepository.deleteById(id);
        } else {
            result = Boolean.FALSE;
            log.info("User with id {} not exist", id);
            throw new AppException(ExceptionType.SERVICE_EXCEPTION, "");
        }
        return result;
    }

    @Caching(
            cacheable = @Cacheable(value = "userCache", key = "#user.id"),
            put = @CachePut(value = "userCache", key = "#user.id"),
            evict = @CacheEvict(value = "userCache", key = "#user.id")
    )
    public String processData(User user){
        return "";
    }
}
