package org.examples.sb.services;



import lombok.extern.slf4j.Slf4j;
import org.examples.sb.exceptions.AppException;
import org.examples.sb.exceptions.ExceptionType;
import org.examples.sb.exceptions.NotFoundException;
import org.examples.sb.helpers.ModelHelper;
import org.examples.sb.models.Role;
import org.examples.sb.models.User;
import org.examples.sb.repositories.UserRepository;
import org.examples.sb.repositories.entities.RoleEntity;
import org.examples.sb.repositories.entities.UserEntity;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.print.attribute.standard.Destination;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @CachePut(value = "userCache", key = "#user.id", condition="#user.id != null")
    public UserEntity saveUser(User user) {
        UserEntity userEntity = ModelHelper.toUserEntity(user);
        return userRepository.save(userEntity);
    }

    public List<User> getAllUsers() {
        /* 
        Converter<Set<RoleEntity>, List<Role>> convertIdentities = new AbstractConverter<>() {
            protected List<Role> convert(Set<RoleEntity> source) {
                return source.stream()
                        .map(roleEntity -> modelMapper.map(roleEntity, Role.class))
                        .collect(Collectors.toList());
            }
        };
        modelMapper.addConverter(convertIdentities);

        modelMapper.typeMap(UserEntity.class, User.class)
        .addMappings(mapper -> mapper.using(convertIdentities)
                .map(UserEntity::getRoles, User::setRoles));


        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, User.class)).toList();
        */

        return userRepository.findAll().stream()
                .map(ModelHelper::toUser).toList();
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
