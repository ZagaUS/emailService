package com.zaga.ServiceImpl;

import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
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
                        .log("Email sent successfully");

            from("direct:sendMail")
                        .log("I'm in")
                        .setHeader(MailConstants.MAIL_TO, header("To"))
                        .setHeader(MailConstants.MAIL_SUBJECT, header("Subject"))
                        .setBody(body())
                        .to("smtps://smtp.gmail.com?username=hariduke001@gmail.com&password=jjwkenhimqehxvpz")
                        .log("Email sent successfully");
      }
}
