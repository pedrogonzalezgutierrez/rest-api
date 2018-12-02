package com.kiesoft.userapi.dto.user;

public class CreateUserDTO {

    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final class Builder {
        private String name;
        private String email;
        private String password;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email= email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public CreateUserDTO build() {
            CreateUserDTO createUserDTO = new CreateUserDTO();
            createUserDTO.setName(name);
            createUserDTO.setEmail(email);
            createUserDTO.setPassword(password);
            return createUserDTO;
        }
    }

}
