package tech.developerdhairya.DigitBatua.Exception;

public class BadRequestException extends Exception{
    public BadRequestException() {
        super("Invalid Token");
    }
}
