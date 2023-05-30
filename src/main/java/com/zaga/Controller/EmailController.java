package com.zaga.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.ProducerTemplate;

@Path("/emailNotification")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmailController {

    @Inject
    ProducerTemplate producerTemplate;

    @POST
    @Path("/sendEmailWithoutAttachment")
    public Response sendMailWithoutAttachment(@QueryParam("to") String to, @QueryParam("from") String from,
            @QueryParam("subject") String subject, @QueryParam("body") String body)
            throws Exception {

        // Create the message headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("To", to);
        headers.put("From", from);
        headers.put("Subject", subject);

        try {
            producerTemplate.sendBodyAndHeaders("direct:sendMailWithoutAttachment", body, headers);
            return Response.ok(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.ok("Failed to send email").build();
        }
    }

    @POST
    @Path("/sendEmailWithAttachment")
    @Consumes("application/octet-stream")
    public Response sendMail(@QueryParam("to") String to, @QueryParam("from") String from,
            @QueryParam("subject") String subject, @QueryParam("body") String body,
            @QueryParam("fileName") String fileName, InputStream inputStream)
            throws Exception {
        // Create the attachment object
        byte[] data = inputStream.readAllBytes();
        File file = new File("dummy.pdf");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.close();

        // Create the message headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("To", to);
        headers.put("From", from);
        headers.put("Subject", subject);
        headers.put("attachment", file);
        headers.put("fileName", fileName);

        try {
            producerTemplate.sendBodyAndHeaders("direct:sendMail", body, headers);
            return Response.ok("Email sent successfully").build();
        } catch (Exception e) {
            return Response.ok("Failed to send email").build();
        }

    }

}