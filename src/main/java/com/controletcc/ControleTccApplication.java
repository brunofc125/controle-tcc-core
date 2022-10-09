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
//            var uAdmin = new User(null, "Administrador", "admin", "admin123", true, new ArrayList<>());
//            var uSupervisor = new User(null, "Supervisor", "supervisor", "supervisor123", true, new ArrayList<>());
//            var uProfessor = new User(null, "Professor", "professor", "professor123", true, new ArrayList<>());
//            var uAluno = new User(null, "Aluno", "aluno", "aluno123", true, new ArrayList<>());
//
//            userService.insert(uAdmin, UserType.ADMIN);
//            userService.insert(uSupervisor, UserType.SUPERVISOR);
//            userService.insert(uProfessor, UserType.PROFESSOR);
//            userService.insert(uAluno, UserType.ALUNO);
//        };
//    }
}
