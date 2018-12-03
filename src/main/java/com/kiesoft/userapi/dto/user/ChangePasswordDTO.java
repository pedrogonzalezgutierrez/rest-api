package com.kiesoft.userapi.dto.user;

public class ChangePasswordDTO {

    private String email;
    private String password;
    private String newPassword;
    private UserDTO userDTO;

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public static final class Builder {
        private String email;
        private String password;
        private String newPassword;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public ChangePasswordDTO build() {
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
            changePasswordDTO.setEmail(email);
            changePasswordDTO.setPassword(password);
            changePasswordDTO.setNewPassword(newPassword);
            return changePasswordDTO;
        }
    }
}
