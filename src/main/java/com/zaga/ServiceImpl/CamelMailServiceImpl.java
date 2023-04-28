package com.zaga.ServiceImpl;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.camel.attachment.Attachment;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.attachment.DefaultAttachment;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mail.MailComponent;
import org.apache.camel.component.mail.MailConstants;

import com.zaga.Entity.MailContent;

public class CamelMailServiceImpl extends RouteBuilder {

      @Inject
      KafkaProcessor kafkaProcessor;

      @Override
      public void configure() throws Exception {
            from("kafka:{{kafka.topic.name}}?" +
                        "brokers={{kafka.bootstrap.servers}}")
                        .routeId("kafka-consumer")
                        .unmarshal().json(MailContent.class)
                        .process(new KafkaProcessor())
                        .log("I'm in")
                        .to("smtps://smtp.gmail.com?username=hariduke001@gmail.com&password=jjwkenhimqehxvpz")
                        .log("Email sent successfully")
                        .onException(Exception.class)
                        .log("Kafka failed to send email")
                        .end();

            from("direct:sendMail")
                        .log("I'm in")
                        .setHeader(MailConstants.MAIL_TO, header("To"))
                        .setHeader(MailConstants.MAIL_SUBJECT, header("Subject"))
                        .setBody(body())
                        .process(exchange -> {
                              // Retrieve the attachment from the headers
                              File attachment = exchange.getIn().getHeader("attachment",
                                          File.class);
                              String fileName = (String) exchange.getIn().getHeader("fileName");
                              AttachmentMessage in = exchange.getIn(AttachmentMessage.class);
                              in.setBody("Hello World");
                              DefaultAttachment att = new DefaultAttachment(new FileDataSource(attachment));
                              att.addHeader("Content-Description", "some sample content");
                              in.addAttachmentObject(fileName, att);
                        })
                        .to("smtps://smtp.gmail.com?username=hariduke001@gmail.com&password=jjwkenhimqehxvpz")
                        .log("Email sent successfully")
                        .end();
      }
}
