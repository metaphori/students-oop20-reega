package reega.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AES {
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    
    private static byte[] getNonceFromPassword(final char[] key, final int numBytes) {
    	final byte[] nonce = new byte[numBytes];
    	for(int i = 0; i<numBytes; i++) {
    		nonce[i] = (byte) key[i%key.length];
    	}
    	return nonce;
    }

    private static SecretKey getAESKeyFromPassword(final char[] password, final byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

    }

    /**
     * @param password: byte password in byte array format
     * @return a base64 encoded AES encrypted password
     * @throws Exception InvalidKeyException, InvalidKeySpecException,
     *                   NoSuchAlgorithmException, NoSuchPaddingException,
     *                   IllegalBlockSizeException, InvalidAlgorithmParameterException,
     *                   BadPaddingException
     */
    public static String encrypt(char[] password) throws Exception {

        // 16 bytes salt
    	final byte[] salt = getNonceFromPassword(password, SALT_LENGTH_BYTE);

        // 12 bytes initial vector
    	final byte[] iv = getNonceFromPassword(password, IV_LENGTH_BYTE);

        // secret key from password
        final SecretKey aesKeyFromPassword = getAESKeyFromPassword(password, salt);

        final Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        // AES-GCM needs GCMParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        final byte[] cipherText = cipher.doFinal(new String(password).getBytes());

        // prefix IV and Salt to cipher text
        final byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        // string representation, base64, send this string to other for decryption.
        return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
    }
}
