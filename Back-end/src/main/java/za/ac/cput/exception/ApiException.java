package za.ac.cput.exception;

import com.google.protobuf.Api;

public class ApiException extends RuntimeException{

    public ApiException(String message) { super(message);}
}
