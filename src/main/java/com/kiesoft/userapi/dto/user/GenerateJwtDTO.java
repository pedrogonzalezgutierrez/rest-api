package com.kiesoft.userapi.dto.user;

public class GenerateJwtDTO {

    private String email;
    private String password;
    private String jwt;

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

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public static final class Builder {
        private String email;
        private String password;
        private String jwt;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder jwt(String jwt) {
            this.jwt = jwt;
            return this;
        }

        public GenerateJwtDTO build() {
            GenerateJwtDTO generateJwtDTO = new GenerateJwtDTO();
            generateJwtDTO.setEmail(email);
            generateJwtDTO.setPassword(password);
            generateJwtDTO.setJwt(jwt);
            return generateJwtDTO;
        }
    }

}
