package za.ac.cput.utils;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:48
 **/
public class EmailUtils {
        public static String getEmailMessage(String name, String host, String token) {
        return "Dear " + name + ",\n\n"
                + "Thank you for registering an account with us. Please click on the link below to confirm your account:\n\n"
                + "Confirmation Link: http://example.com/confirm?token=" + token + "\n\n"
                + "If you did not register an account, please ignore this email.\n\n"
                + "Best regards,\n"
                + "Your App Team, \n" +
                getVerificationUrl(host, token) + "\n\nThe support Team";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/users?token=" + token;
    }
}
