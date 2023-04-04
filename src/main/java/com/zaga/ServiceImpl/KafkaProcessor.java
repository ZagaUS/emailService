package com.zaga.ServiceImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mail.MailConstants;

import com.zaga.Entity.MailContent;

@ApplicationScoped
public class KafkaProcessor implements Processor {

   @Inject
   ProducerTemplate producerTemplate;

   @Override
   public void process(Exchange exchange) throws Exception {
      MailContent mailRequest = exchange.getIn().getBody(MailContent.class);

      exchange.getIn().setBody(mailRequest.getBody());
      exchange.getIn().setHeader(MailConstants.MAIL_TO, mailRequest.getTo());
      exchange.getIn().setHeader(MailConstants.MAIL_SUBJECT, mailRequest.getSubject());

      System.out.println("Output " + mailRequest);
      System.out.println("to " + mailRequest.getTo());
   }

}
