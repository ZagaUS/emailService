package com.zaga.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.camel.ProducerTemplate;

import com.zaga.Entity.MailContent;

@Path("/emailNotification")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmailController {

    @Inject
    ProducerTemplate producerTemplate;

    @POST
    @Path("/sendEmail")
    public Response sendMail(MailContent mailRequest) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("To", mailRequest.getTo());
        headers.put("From", mailRequest.getFrom());
        headers.put("Subject", mailRequest.getSubject());

        producerTemplate.sendBodyAndHeaders("direct:sendMail", mailRequest.getBody(), headers);
        return Response.ok("Email sent successfully").build();
    }

}