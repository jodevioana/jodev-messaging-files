package com.jodev.messaging.authorizationserver;

import com.google.common.collect.Lists;
import com.jodev.messaging.authorizationserver.repository.PermissionRepository;
import com.jodev.messaging.authorizationserver.repository.RoleRepository;
import com.jodev.messaging.authorizationserver.repository.UserRepository;
import com.jodev.messaging.authorizationserver.model.Permission;
import com.jodev.messaging.authorizationserver.model.Role;
import com.jodev.messaging.authorizationserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMongoAuditing
@EnableDiscoveryClient
public class AuthorizationServerApplication implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PermissionRepository permissionRepository;

  public static void main(String[] args) {
    SpringApplication.run(AuthorizationServerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Permission can_delete_user_permission = new Permission();
    can_delete_user_permission.setName("can_delete_user");

    Permission can_create_user_permission = new Permission();
    can_create_user_permission.setName("can_create_user");

    Permission can_update_user_permission = new Permission();
    can_update_user_permission.setName("can_update_user");

    Permission can_read_user_permission = new Permission();
    can_read_user_permission.setName("can_read_user");

    permissionRepository.deleteAll();
    permissionRepository.save(can_delete_user_permission);
    permissionRepository.save(can_create_user_permission);
    permissionRepository.save(can_update_user_permission);
    permissionRepository.save(can_read_user_permission);

    Role admin = new Role();
    admin.setName("ROLE_ADMIN");
    admin.setPermissions(Lists.newArrayList(can_create_user_permission, can_delete_user_permission, can_update_user_permission, can_read_user_permission));

    Role user = new Role();
    user.setName("ROLE_USER");
    user.setPermissions(Lists.newArrayList(can_read_user_permission));

    roleRepository.deleteAll();
    roleRepository.save(admin);
    roleRepository.save(user);


    User adminUser = new User("a@a.com", "admin", passwordEncoder.encode("admin"), true, false, false, false, Lists.newArrayList(admin, user));
    User userUser = new User("b@b.com", "user", passwordEncoder.encode("user"), true, false, false, false, Lists.newArrayList(user));

    userRepository.deleteAll();
    userRepository.save(adminUser);
    userRepository.save(userUser);
  }
}
