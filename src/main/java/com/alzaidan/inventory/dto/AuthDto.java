package com.alzaidan.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    // ── Login Request ─────────────────────────────────────────────────────────
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;
        @NotBlank(message = "Password is required")
        private String password;

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
    }

    // ── Login Response ────────────────────────────────────────────────────────
    public static class LoginResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String name;
        private String role;

        public LoginResponse() {}

        public String getToken()    { return token; }
        public String getType()     { return type; }
        public Long getId()         { return id; }
        public String getUsername() { return username; }
        public String getName()     { return name; }
        public String getRole()     { return role; }

        public void setToken(String token)       { this.token = token; }
        public void setType(String type)         { this.type = type; }
        public void setId(Long id)               { this.id = id; }
        public void setUsername(String username) { this.username = username; }
        public void setName(String name)         { this.name = name; }
        public void setRole(String role)         { this.role = role; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private String token;
            private String type = "Bearer";
            private Long id;
            private String username;
            private String name;
            private String role;

            public Builder token(String token)       { this.token = token; return this; }
            public Builder type(String type)         { this.type = type; return this; }
            public Builder id(Long id)               { this.id = id; return this; }
            public Builder username(String username) { this.username = username; return this; }
            public Builder name(String name)         { this.name = name; return this; }
            public Builder role(String role)         { this.role = role; return this; }

            public LoginResponse build() {
                LoginResponse r = new LoginResponse();
                r.token = this.token; r.type = this.type; r.id = this.id;
                r.username = this.username; r.name = this.name; r.role = this.role;
                return r;
            }
        }
    }
}
