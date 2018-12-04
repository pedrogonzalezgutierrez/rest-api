package com.kiesoft.userapi.dto.user;

public class EnableUserDTO {

    private String email;
    private Boolean enable;
    private UserDTO userDTO;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public static final class Builder {
        private String email;
        private Boolean enable;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder enable(Boolean enable) {
            this.enable = enable;
            return this;
        }

        public EnableUserDTO build() {
            EnableUserDTO enableUserDTO = new EnableUserDTO();
            enableUserDTO.setEmail(email);
            enableUserDTO.setEnable(enable);
            return enableUserDTO;
        }
    }

}
