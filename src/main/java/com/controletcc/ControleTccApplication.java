package com.controletcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ControleTccApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleTccApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.saveRole(new Role(null, "USER"));
//            userService.saveRole(new Role(null, "ADMIN"));
//            userService.saveUser(new User(null, "Bruno Fonseca", "bruno", "bruno123", true, LocalDateTime.now(), new ArrayList<>()));
//            userService.saveUser(new User(null, "Marta Ravani", "marta", "marta123", true, LocalDateTime.now(), new ArrayList<>()));
//
//            userService.addRoleToUser("bruno", "USER");
//            userService.addRoleToUser("bruno", "ADMIN");
//            userService.addRoleToUser("marta", "USER");
//        };
//    }

}
