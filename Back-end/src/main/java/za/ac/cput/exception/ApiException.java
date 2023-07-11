package za.ac.cput.exception;

 /**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/06/05
 * @Time : 08:00
 **/
public class ApiException extends RuntimeException{

    public ApiException(String message) { super(message);}
}
