package za.ac.cput.enumeration;
 /**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/06/06
 * @Time : 11:00
 **/
public enum VerificationType {
    ACCOUNT("ACCOUNT"), PASSWORD("PASSWORD");

    private final String type;

    VerificationType(String type){ this.type  = type;}

    public String getType(){
        return this.type.toLowerCase();
    }
}

