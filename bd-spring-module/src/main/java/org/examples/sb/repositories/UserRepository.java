/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.examples.sb.repositories;

import org.examples.sb.repositories.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 *
 * @author brijeshdhaker
 */
@Repository("userRepository")
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    
}
