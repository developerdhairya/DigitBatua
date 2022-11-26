package tech.developerdhairya.DigitBatua.Entity;

import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import java.util.Date;

public interface Token {
    AuthenticationUtil util=new AuthenticationUtil();

    int EXPIRATION_TIME=10;

    Date getExpirationTime();
}
