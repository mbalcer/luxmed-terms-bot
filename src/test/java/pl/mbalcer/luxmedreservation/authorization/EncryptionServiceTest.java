package pl.mbalcer.luxmedreservation.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        byte[] key = new byte[32];
        for (int i = 0; i < key.length; i++) {
            key[i] = (byte) i;
        }
        String base64Key = Base64.getEncoder().encodeToString(key);

        encryptionService = new EncryptionService(base64Key);
    }

    @Test
    void encryptAndDecrypt_ShouldReturnOriginalValue() {
        String original = "SuperTajneHaslo123!";

        String encrypted = encryptionService.encrypt(original);
        String decrypted = encryptionService.decrypt(encrypted);

        assertThat(decrypted)
                .as("Decrypted value should match original")
                .isEqualTo(original);
    }

    @Test
    void encrypt_ShouldProduceDifferentCiphertextEachTime() {
        String original = "SameText";

        String encrypted1 = encryptionService.encrypt(original);
        String encrypted2 = encryptionService.encrypt(original);

        assertThat(encrypted1)
                .as("First ciphertext should not equal second ciphertext")
                .isNotEqualTo(encrypted2);

        assertThat(encryptionService.decrypt(encrypted1)).isEqualTo(original);
        assertThat(encryptionService.decrypt(encrypted2)).isEqualTo(original);
    }

    @Test
    void decrypt_WithInvalidCiphertext_ShouldThrow() {
        String invalidCiphertext = "InvalidBase64Data";

        assertThatThrownBy(() -> encryptionService.decrypt(invalidCiphertext))
                .as("Decrypting invalid ciphertext should throw RuntimeException")
                .isInstanceOf(EncryptionException.class);
    }

    @Test
    void constructor_WithInvalidKey_ShouldThrow() {
        byte[] shortKey = new byte[16];
        String base64Key = Base64.getEncoder().encodeToString(shortKey);

        assertThatThrownBy(() -> new EncryptionService(base64Key))
                .as("Should throw when key length is not 32 bytes")
                .isInstanceOf(EncryptionException.class);
    }
}
