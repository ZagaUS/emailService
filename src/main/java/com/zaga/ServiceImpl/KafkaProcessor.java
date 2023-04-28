package com.zaga.ServiceImpl;

import java.io.File;
import java.io.FileOutputStream;

import javax.activation.FileDataSource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.attachment.DefaultAttachment;
import org.apache.camel.component.mail.MailConstants;

import com.zaga.Entity.MailContent;

@ApplicationScoped
public class KafkaProcessor implements Processor {

   @Inject
   ProducerTemplate producerTemplate;

   @Override
   public void process(Exchange exchange) throws Exception {
      MailContent mailRequest = exchange.getIn().getBody(MailContent.class);

      File file = new File("dummy.pdf");
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(mailRequest.getFileData());
      fos.close();

      // File attachment = new File(file);

      AttachmentMessage in = exchange.getIn(AttachmentMessage.class);
      DefaultAttachment att = new DefaultAttachment(new FileDataSource(file));
      att.addHeader("Content-Description", "some sample content");
      in.addAttachmentObject(mailRequest.getFileName(), att);

      exchange.getIn().setBody(mailRequest.getBody());
      exchange.getIn().setHeader(MailConstants.MAIL_TO, mailRequest.getTo());
      exchange.getIn().setHeader(MailConstants.MAIL_SUBJECT, mailRequest.getSubject());

      System.out.println("Output " + mailRequest);
      System.out.println("to " + mailRequest.getTo());
   }

}
