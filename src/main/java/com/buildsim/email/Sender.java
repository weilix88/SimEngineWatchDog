package main.java.com.buildsim.email;

import com.sendgrid.*;
import main.java.com.buildsim.init.WatchDogConfig;
import main.java.com.buildsim.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Sender {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void sendEmail(String subject,
                          String htmlContent,
                          String dest,
                          String attachmentPath,
                          String attachmentName,
                          String attachmentType) throws UnsupportedEncodingException {
        Email from = new Email("buildsimhub@gmail.com");
        Email to = new Email(dest);
        Content emailContent = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, emailContent);

        if(!StringUtil.isNullOrEmpty(attachmentPath)){
            File file = new File(attachmentPath);
            byte[] fileData = null;
            try {
                fileData = IOUtils.toByteArray(new FileInputStream(file));
            } catch (IOException ex) {
            }

            Attachments attachment = new Attachments();
            Base64 x = new Base64();
            String imageDataString = x.encodeAsString(fileData);
            attachment.setContent(imageDataString);
            attachment.setType(attachmentType);
            attachment.setFilename(attachmentName);
            attachment.setDisposition("attachment");
            attachment.setContentId("Banner");
            mail.addAttachments(attachment);
        }

        String apiKey = WatchDogConfig.readProperty("SendGridKey");
        if(apiKey==null) {
            apiKey = "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";
        }

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        }catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
