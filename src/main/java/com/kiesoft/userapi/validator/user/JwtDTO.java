package com.kiesoft.userapi.validator.user;

public class JwtDTO {

    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public static final class Builder {
        private String jwt;

        public Builder jwt(String jwt) {
            this.jwt = jwt;
            return this;
        }

        public JwtDTO build() {
            JwtDTO jwtDTO = new JwtDTO();
            jwtDTO.setJwt(jwt);
            return jwtDTO;
        }
    }

}
