import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.Test;

import dev.euber.java_smime_playground.EuberSmimeUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class SimpleUtilsTest {
    private void testGetSampleMimeMessage(String from, String to) throws MessagingException, IOException {
        MimeMessage message = EuberSmimeUtils.getSampleMimeMessage(from, to);
        assertNotNull(message);
        assertEquals(from, message.getFrom()[0].toString());
        assertEquals(to, message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("My example subject", message.getSubject());
        assertEquals("My example content", message.getContent());
        assertEquals("text/plain", message.getContentType());
    }

    @Test
    public void getSampleMimeMessage() throws MessagingException, IOException {
        this.testGetSampleMimeMessage("euberdeveloper@gmail.com", "euberspam@gmail.com");
        this.testGetSampleMimeMessage("pippo@disney.com", "pluto@disney.com");
    }

    @Test
    public void createMimeMessage() throws MessagingException, IOException {
        MimeMessage message = EuberSmimeUtils.creatMimeMessage(EuberSmimeUtils.getDefaultSession(), "from@gmail.com",
                "to@gmail.com", "oggetto", "<b>contenuto</b>", "text/plain");
        assertNotNull(message);
        assertEquals("from@gmail.com", message.getFrom()[0].toString());
        assertEquals("to@gmail.com", message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals("oggetto", message.getSubject());
        assertEquals("<b>contenuto</b>", message.getContent());
        assertEquals("text/plain", message.getContentType());
    }
}
