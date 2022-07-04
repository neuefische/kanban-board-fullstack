package de.neuefische.muc.user;

import lombok.Data;

@Data
public class UserCreationDTO {

    private String username;
    private String password;
    private String passwordRepeat;

}
