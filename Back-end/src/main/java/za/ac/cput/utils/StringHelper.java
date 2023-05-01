package za.ac.cput.utils;

import java.util.UUID;

public class StringHelper {
    public static String generateId(){
        return UUID.randomUUID().toString();
    }

    public static boolean isNullorEmpty(String q){

        if (q==null || q.equals("") || q.isEmpty())
            return true;
        else
            return false;
    }
}
