package lk.ijse.productservice.util;

import java.util.Base64;

public class AppUtil {
    public static String imageBase64(byte[] imageBytes){
        return Base64.getEncoder()
                .encodeToString(imageBytes);
    }
}
