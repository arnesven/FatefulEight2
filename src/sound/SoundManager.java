package sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class SoundManager {
    public static String getSoundAsBase64(String rest) {
        String path = "resources/sound/" + rest + ".mp3";
        try {
            File f = new File(path);
            InputStream is;
            if (f.exists()) {
                is = new FileInputStream(path);
            } else {
                String path2 = path.replace("resources/", "");
                is = SoundManager.class.getClassLoader().getResourceAsStream(path2);
            }

            if (is == null) {
                System.out.println("Tried getting resource " + path);
            }
            byte[] byteArr = is.readAllBytes();
            String result = Base64.getEncoder().encodeToString(byteArr);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeAsBase64(String result) {
        return Base64.getDecoder().decode(result);
    }
}
