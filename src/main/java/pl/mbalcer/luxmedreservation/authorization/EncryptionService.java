package pl.mbalcer.luxmedreservation.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH_BYTES = 12; // recommended for GCM

    private final byte[] keyBytes;
    private final SecretKeySpec keySpec;
    private final SecureRandom secureRandom = new SecureRandom();

    public EncryptionService(@Value("${ENCRYPTION_KEY}") String base64Key) {
        if (base64Key == null || base64Key.isBlank()) {
            throw new EncryptionException("ENCRYPTION_KEY must be set in environment");
        }
        this.keyBytes = Base64.getDecoder().decode(base64Key);
        if (this.keyBytes.length != 32) {
            throw new EncryptionException("ENCRYPTION_KEY must decode to 32 bytes (AES-256 key)");
        }
        this.keySpec = new SecretKeySpec(this.keyBytes, "AES");
    }

    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            ByteBuffer bb = ByteBuffer.allocate(iv.length + ciphertext.length);
            bb.put(iv);
            bb.put(ciphertext);
            return Base64.getEncoder().encodeToString(bb.array());
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    public String decrypt(String encodedText) {
        try {
            byte[] all = Base64.getDecoder().decode(encodedText);
            ByteBuffer bb = ByteBuffer.wrap(all);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            bb.get(iv);
            byte[] ciphertext = new byte[bb.remaining()];
            bb.get(ciphertext);

            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            byte[] plain = cipher.doFinal(ciphertext);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Decryption failed", e);
        }
    }
}
