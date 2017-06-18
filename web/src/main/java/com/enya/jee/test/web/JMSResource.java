package com.enya.jee.test.web;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("jms")
public class JMSResource {

    @Inject
    @JMSConnectionFactory("java:/ConnectionFactory")
    private JMSContext jmsContext;
    @Resource(lookup = "java:/jms/queue/jee-test")
    private Destination queue;
    @Resource(lookup = "java:/jms/topic/jee-test")
    private Destination topic;

    @GET
    public Response sendMessage(
            @QueryParam("message") final String message
    ) {
        jmsContext.createProducer().setJMSReplyTo(topic).send(queue, message);
        String response = jmsContext.createConsumer(topic).receiveBody(String.class);
        System.out.println("JMSResource.sendMessage(): received message: " + response);
        return Response.ok("received message from the topic: " + response).build();
    }
}
