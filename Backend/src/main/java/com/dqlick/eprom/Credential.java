package com.dqlick.eprom;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mail")
public class Credential {

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String userName;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    private String password;
}
