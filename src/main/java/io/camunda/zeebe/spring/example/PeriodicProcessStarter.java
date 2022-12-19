package io.camunda.zeebe.spring.example;

import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class PeriodicProcessStarter {

  private static Logger log = LoggerFactory.getLogger(ExampleApplication.class);

  @Autowired
  private ZeebeClientLifecycle client;

  @Autowired
  private MessageDaoService messageDaoService;

  @Scheduled(fixedRate = 5000L)
  public void startProcesses() {
    if (!client.isRunning()) {
      return;
    }

    final ProcessInstanceEvent event =
      client
        .newCreateInstanceCommand()
        .bpmnProcessId("demoProcess")
        .latestVersion()
        .variables("{\"a\": \"" + UUID.randomUUID().toString() + "\",\"b\": \"" + new Date().toString() + "\"}")
        .send()
        .join();

    messageDaoService.publishMessage();

    log.info("started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
      event.getProcessDefinitionKey(), event.getBpmnProcessId(), event.getVersion(), event.getProcessInstanceKey());
  }
}
