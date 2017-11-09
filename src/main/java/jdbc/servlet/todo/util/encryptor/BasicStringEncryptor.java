package jdbc.servlet.todo.util.encryptor;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class BasicStringEncryptor implements Encryptor<String> {


    @Override
    public String encrypt(String s) {
        return encrypt(s, BasicStringEncryptor::SHA_BASE64);
    }

    @Override
    public String encrypt(String s, Encryptor.EncryptionStrategy<String> strategy) {
        return strategy.encrypt(s);
    }

    public static String SHA_BASE64(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
