package com.alzaidan.inventory.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ── Constructors ─────────────────────────────────────────────────────────
    public User() {}

    public User(Long id, String username, String password, String name,
                Role role, boolean enabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ── Lifecycle hooks ───────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public Role getRole()                { return role; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setId(Long id)                      { this.id = id; }
    public void setUsername(String username)        { this.username = username; }
    public void setPassword(String password)        { this.password = password; }
    public void setName(String name)                { this.name = name; }
    public void setRole(Role role)                  { this.role = role; }
    public void setEnabled(boolean enabled)         { this.enabled = enabled; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ── UserDetails ───────────────────────────────────────────────────────────
    @Override
    public String getUsername()  { return username; }

    @Override
    public String getPassword()  { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return enabled; }

    // ── Builder ───────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String username;
        private String password;
        private String name;
        private Role role;
        private boolean enabled = true;

        public Builder id(Long id)               { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder name(String name)         { this.name = name; return this; }
        public Builder role(Role role)           { this.role = role; return this; }
        public Builder enabled(boolean enabled)  { this.enabled = enabled; return this; }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.username = this.username;
            user.password = this.password;
            user.name = this.name;
            user.role = this.role;
            user.enabled = this.enabled;
            return user;
        }
    }

    // ── Role enum ─────────────────────────────────────────────────────────────
    public enum Role {
        ADMINISTRATOR, SALES_MANAGER, INVENTORY_OFFICER
    }
}
