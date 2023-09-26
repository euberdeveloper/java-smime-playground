import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.security.Security;
import java.security.cert.X509Certificate;

import org.junit.Test;
import org.simplejavamail.utils.mail.smime.SmimeKey;
import org.simplejavamail.utils.mail.smime.SmimeKeyStore;
import org.simplejavamail.utils.mail.smime.SmimeState;
import org.simplejavamail.utils.mail.smime.SmimeUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;

import dev.euber.java_smime_playground.EuberSmimeUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class SimpleUsageTest {
    private SmimeKeyStore aliceKeystore;
    private SmimeKeyStore bobKeystore;
    private MimeMessage mimeExampleMessage;

    private void testSignEncryptAndDecrypt(MimeMessage plainMessage, SmimeKeyStore keyStore, String password)
            throws MessagingException {
        // Get key and certificate from key store
        SmimeKey key = keyStore.getPrivateKey(password, password.toCharArray());
        X509Certificate certificate = key.getCertificate();
        // Sign and encrypt message
        MimeMessage encryptedMessage = EuberSmimeUtils.signAndEncryptMimeMessageWithDefaults(plainMessage, key,
                certificate);
        // Check that it is encrypted
        assertEquals(SmimeUtil.getStatus((encryptedMessage)), SmimeState.ENCRYPTED);
        // Decrypt message
        MimeMessage decryptedMessage = EuberSmimeUtils.decryptMimeMessage(encryptedMessage, key);
        // Check that it is decrypted, that it is signed and that the signature is ok
        assertNotEquals(SmimeUtil.getStatus((decryptedMessage)), SmimeState.ENCRYPTED);
        assertEquals(SmimeUtil.getStatus((decryptedMessage)), SmimeState.SIGNED);
        assertTrue(SmimeUtil.checkSignature(decryptedMessage));
    }

    @Before
    public void setUp() throws MessagingException {
        Security.addProvider(new BouncyCastleProvider());
        this.aliceKeystore = EuberSmimeUtils.getSmimeKeyStore(
                this.getClass().getClassLoader().getResourceAsStream(ResourcesHandler.aliceCertificatePath),
                ResourcesHandler.aliceCertificatePassword);
        this.bobKeystore = EuberSmimeUtils.getSmimeKeyStore(
                this.getClass().getClassLoader().getResourceAsStream(ResourcesHandler.bobCertificatePath),
                ResourcesHandler.bobCertificatePassword);
        this.mimeExampleMessage = EuberSmimeUtils.readMimeMessageFromStream(this.getClass().getClassLoader()
                .getResourceAsStream(ResourcesHandler.mimeExampleMessagePath));
    }

    @Test
    public void testSignEncryptAndDecrypt() throws MessagingException {
        testSignEncryptAndDecrypt(new MimeMessage(this.mimeExampleMessage), this.aliceKeystore,
                ResourcesHandler.aliceCertificatePassword);
        testSignEncryptAndDecrypt(new MimeMessage(this.mimeExampleMessage), this.bobKeystore,
                ResourcesHandler.bobCertificatePassword);
    }

}
