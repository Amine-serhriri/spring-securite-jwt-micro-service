package com.example.securiteservice.service;

import com.example.securiteservice.entities.AppRole;
import com.example.securiteservice.entities.AppUser;

import java.util.List;

public interface AccountServive {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username,String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser>listUsers();
}
