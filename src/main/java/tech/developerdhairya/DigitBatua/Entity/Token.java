package tech.developerdhairya.DigitBatua.Entity;

import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public interface Token {

    Instant getExpirationTime();

}
