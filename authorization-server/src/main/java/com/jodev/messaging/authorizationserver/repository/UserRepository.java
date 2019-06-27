package com.jodev.messaging.authorizationserver.repository;

import com.jodev.messaging.authorizationserver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends MongoRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);

}
