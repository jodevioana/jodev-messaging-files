package com.jodev.messaging.authorizationserver.repository;

import com.jodev.messaging.authorizationserver.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RoleRepository extends MongoRepository<Role, Long> {

}
