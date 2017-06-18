package com.enya.jee.test.service;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/jee-test")
})
public class JMSQueueMDB implements MessageListener {

    @Inject
    @JMSConnectionFactory("java:/ConnectionFactory")
    private JMSContext jmsContext;

    public void onMessage(Message message) {
        try {
            String body = message.getBody(String.class);
            System.out.println("JMSQueueMDB.onMessage(): received message: " + body);
            jmsContext.createProducer().send(message.getJMSReplyTo(), "JMSQueueMDB.onMessage(): processed the message: " + body);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
