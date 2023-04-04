package com.zaga.Entity;

public class MailContent {

   private String subject;
   private String from;
   private String to;
   private String body;

   public String getSubject() {
      return subject;
   }

   public void setSubject(String subject) {
      this.subject = subject;
   }

   public String getFrom() {
      return from;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public String getTo() {
      return to;
   }

   public void setTo(String to) {
      this.to = to;
   }

   public String getBody() {
      return body;
   }

   public void setBody(String body) {
      this.body = body;
   }

   public MailContent() {
   }

   public MailContent(String subject, String from, String to, String body) {
      this.subject = subject;
      this.from = from;
      this.to = to;
      this.body = body;
   }

   @Override
   public String toString() {
      return "MailContent [subject=" + subject + ", from=" + from + ", to=" + to + ", body=" + body + "]";
   }

}
