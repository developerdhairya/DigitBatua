package tech.developerdhairya.DigitBatua.DTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateSuccessResponse(Object data ,HttpStatus status){
        Map<String,Object> map=new HashMap<>();
        map.put("message","Success");
        map.put("data",data);
        return new ResponseEntity<Object>(map,status);
    }

    public static ResponseEntity<Object> generateErrorResponse(HttpStatus status,String message){
        Map<String,Object> map=new HashMap<>();
        map.put("message",message);
        map.put("data",null);
        return new ResponseEntity<Object>(map,status);
    }

}
