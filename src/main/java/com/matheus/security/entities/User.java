package com.matheus.security.entities;

import jakarta.persistence.*;

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
}
