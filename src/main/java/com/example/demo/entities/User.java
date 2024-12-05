package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Класс, представляющий пользователя в системе.
 * <p>
 * Каждый пользователь имеет уникальное имя, электронную почту и пароль,
 * а также может иметь несколько ролей, заметок, категорий и тегов.
 * </p>
 *
 * @author VladimirBoss
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Note> notes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Category> categories = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Tag> tags = new ArrayList<>();

    /**
     * Конструктор для создания пользователя с заданным именем, электронной почтой и паролем.
     *
     * @param username Имя пользователя.
     * @param email    Электронная почта пользователя.
     * @param password Пароль пользователя.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Проверяет, имеет ли пользователь указанную роль.
     *
     * @param role Название роли для проверки.
     * @return true, если пользователь имеет указанную роль; иначе false.
     */
    public boolean hasRole(String role) {
        return roles.stream().anyMatch(myRole -> myRole.getName().equalsIgnoreCase(role));
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


}