package com.matheus.security.entities;

import com.matheus.security.controller.dto.LoginRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userID; //id de forma aleatoria

    @Column(unique = true) //Nao existem usernames duplicados
    private String username;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //forma de trazer do banco de dados
    // Um usuario pode ter varias roles, e varias roles estao associadas a varios usuarios
    @JoinTable(
            name = "tb_users_roles", //tabela associativa
            joinColumns = @JoinColumn(name = "user_id"), //coluna da chave do user_id
            inverseJoinColumns = @JoinColumn(name = "role_id") // coluna da chave do role_id
    )
    private Set<Role> roles; //para nao terem dados repetidos

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(loginRequest.password(), this.password); //senha da request e senha da db
    }

}
