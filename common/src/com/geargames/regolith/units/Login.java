package com.geargames.regolith.units;

/**
 * User: mkutuzov
 * Date: 14.05.12
 */
public class Login extends Entity {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return super.toString() + "; name='" + getName() + "'";
    }

}
