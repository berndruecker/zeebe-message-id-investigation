package io.camunda.zeebe.spring.example;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDaoService {

    @Autowired
    private ZeebeClient zeebe;

    public void publishMessage() {
        System.out.println("GOT HERE!!!");
        zeebe.newPublishMessageCommand()
                .messageName("yes")
                .correlationKey("yes")
                .messageId("121212")
                //.timeToLive(message.getTimeToLive())
                //.variables(message.getVariables())
                .send()
                .whenComplete((c, t) -> {System.out.println("GOT AFTER!!!");})
                .exceptionally(t -> {
                    throw new RuntimeException("Could not hand over record to Zeebe: "+". check nested exception for details: " + t.getMessage());
                });
    }

}