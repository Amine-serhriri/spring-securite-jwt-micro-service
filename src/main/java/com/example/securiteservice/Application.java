package com.example.securiteservice;

import com.example.securiteservice.entities.AppRole;
import com.example.securiteservice.entities.AppUser;
import com.example.securiteservice.service.AccountServive;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner start(AccountServive accountServive){
        return args -> {
            accountServive.addNewRole(new AppRole(null,"USER"));
            accountServive.addNewRole(new AppRole(null,"ADMIN"));
            accountServive.addNewRole(new AppRole(null,"CUSTOMER_MANAGER"));
            accountServive.addNewRole(new AppRole(null,"PRODUCT_MANAGER"));
            accountServive.addNewRole(new AppRole(null,"BILLS_MANAGER"));

            accountServive.addNewUser(new AppUser(null , "user1","1234",new ArrayList<>()));
            accountServive.addNewUser(new AppUser(null , "admin","1234",new ArrayList<>()));
            accountServive.addNewUser(new AppUser(null , "user2","1234",new ArrayList<>()));
            accountServive.addNewUser(new AppUser(null , "user3","1234",new ArrayList<>()));
            accountServive.addNewUser(new AppUser(null , "user4","1234",new ArrayList<>()));

            accountServive.addRoleToUser("user1","USER");
            accountServive.addRoleToUser("admin","USER");
            accountServive.addRoleToUser("admin","ADMIN");
            accountServive.addRoleToUser("user2","USER");
            accountServive.addRoleToUser("user2","CUSTOMER_MANAGER");
            accountServive.addRoleToUser("user3","USER");
            accountServive.addRoleToUser("user3","PRODUCT_MANAGER");
            accountServive.addRoleToUser("user4","USER");
            accountServive.addRoleToUser("user4","BILLS_MANAGER");
        };
    }
}
