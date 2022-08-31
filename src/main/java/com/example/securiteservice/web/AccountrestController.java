package com.example.securiteservice.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.securiteservice.entities.AppRole;
import com.example.securiteservice.entities.AppUser;
import com.example.securiteservice.repositories.AppUserRepository;
import com.example.securiteservice.service.AccountServive;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AccountrestController {
    @Autowired
    private AccountServive accountServive;

    @GetMapping(path = "/users")
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser>appUsers(){
        return accountServive.listUsers();
    }
    @PostMapping(path = "/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    AppUser saveUser(@RequestBody AppUser appUser){
        return accountServive.addNewUser(appUser);
    }

    @PostMapping(path = "/roles")
    @PostAuthorize("hasAuthority('ADMIN')")
    AppRole saveRole(@RequestBody AppRole appRole){
        return accountServive.addNewRole(appRole);
    }
    @PostMapping(path = "/addRoleToUser")
    void AddRoleToUser(@RequestBody RoleUserForm roleUserForm){
         accountServive.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());
    }
    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String authToken=request.getHeader("Authorization");
        if(authToken!=null&& authToken.startsWith("Bearer")){
            try {
                String refreshToken=authToken.substring(7);
                Algorithm algorithm=Algorithm.HMAC256("mySecret1234");
                JWTVerifier jwtVerifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT=jwtVerifier.verify(refreshToken);
                String username=decodedJWT.getSubject();
                AppUser appUser=accountServive.loadUserByUsername(username);
                String jwtAccessToken= JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+5*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> idToken=new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refresh-token",refreshToken);
                response.setContentType("application/json");

                new ObjectMapper().writeValue(response.getOutputStream(),idToken);

            }catch (Exception e){
                response.setHeader("error-message",e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

            }

        }else
            throw new RuntimeException("Refresh-Token required ");

    }

}
@Data
class RoleUserForm{
    private String username;
    private String roleName;
}
