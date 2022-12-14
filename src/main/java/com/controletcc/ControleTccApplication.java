package com.controletcc;

import com.controletcc.model.entity.User;
import com.controletcc.model.enums.UserType;
import com.controletcc.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class ControleTccApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleTccApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            var admin = new User(null, UserType.ADMIN, "Administrador", "admin", "admin123", true, new ArrayList<>());
            if (!userService.existsByUsername(admin.getUsername())) {
                userService.insert(admin, UserType.ADMIN);
            }
        };
    }

}
