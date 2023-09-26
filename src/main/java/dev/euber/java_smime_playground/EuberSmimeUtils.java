package dev.euber.java_smime_playground;

import java.io.InputStream;
import java.security.cert.X509Certificate;

import org.simplejavamail.utils.mail.smime.SmimeKey;
import org.simplejavamail.utils.mail.smime.SmimeKeyStore;
import org.simplejavamail.utils.mail.smime.SmimeUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EuberSmimeUtils {
    public static Session getDefaultSession() {
        return Session.getDefaultInstance(System.getProperties());
    }

    public static SmimeKeyStore getSmimeKeyStore(InputStream certificate, String password) {
        return new SmimeKeyStore(certificate, password.toCharArray());
    }

    /**
     * Create a mime message.
     * 
     * @param session The session to use.
     * @param from    The from address.
     * @param to      The to address.
     * @param subject The subject.
     * @param content The content.
     * @param type    The content type (e.g. "text/plain; charset=utf-8")
     * @return The mime message.
     * @throws MessagingException
     */
    public static MimeMessage creatMimeMessage(Session session, String from, String to, String subject, String content,
            String type) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(from));
        mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(content, type);
        return mimeMessage;
    }

    /**
     * Create a sample mime message.
     * 
     * @param from The from address.
     * @param to   The to address.
     * @throws MessagingException
     */
    public static MimeMessage getSampleMimeMessage(String from, String to) throws MessagingException {
        return EuberSmimeUtils.creatMimeMessage(EuberSmimeUtils.getDefaultSession(), from, to, "My example subject",
                "My example content", "text/plain; charset=utf-8");
    }

    /**
     * Read a mime message from a stream.
     * 
     * @param fileInputStream The stream to read from.
     * @return The mime message.
     * @throws MessagingException
     */
    public static MimeMessage readMimeMessageFromStream(InputStream fileInputStream) throws MessagingException {
        return new MimeMessage(getDefaultSession(), fileInputStream);
    }

    public static MimeMessage signMimeMessage(Session session, MimeMessage mimeMessage, SmimeKey key)
            throws MessagingException {
        return SmimeUtil.sign(session, null, mimeMessage, key);
    }

    public static MimeMessage signAndEncryptMimeMessageWithDefaults(MimeMessage mimeMessage, SmimeKey key,
            X509Certificate certificate) throws MessagingException {
        Session session = getDefaultSession();
        return SmimeUtil.encrypt(session, null,
                signMimeMessage(session, mimeMessage, key),
                certificate);
    }

    public static MimeMessage decryptMimeMessage(MimeMessage mimeMessage, SmimeKey key)
            throws MessagingException {
        return SmimeUtil.decrypt(getDefaultSession(), mimeMessage, key);
    }
}