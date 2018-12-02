package com.kiesoft.userapi.dto.user;

public class GenerateJwtDTO {

    private String email;
    private String password;

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
        private String email;
        private String password;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public GenerateJwtDTO build() {
            GenerateJwtDTO generateJwtDTO = new GenerateJwtDTO();
            generateJwtDTO.setEmail(email);
            generateJwtDTO.setPassword(password);
            return generateJwtDTO;
        }
    }

}
