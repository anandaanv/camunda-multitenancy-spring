package com.growth99.camundademo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn")
@CrossOrigin(origins = "*")
public class CamundaController {

  @Autowired
  private RepositoryService repositoryService;

  @Autowired
  private RuntimeService runtimeService;

  @PostMapping("/deploy")
  public void deployBpmnApplication(String bpmnXml, String tenantId) {
    repositoryService
        .createDeployment()
        .tenantId(tenantId)
        .addString("process.bpmn", bpmnXml)
        .deploy();


    runtimeService
        .createProcessInstanceByKey("process")
        .processDefinitionTenantId(tenantId)
        .execute();

    runtimeService.startProcessInstanceByKey("process");
  }

  @GetMapping("/get-process-definition/{tenantId}")
  @ResponseBody
  public String getDeployment(@PathVariable String tenantId) {
    ProcessDefinition process = repositoryService
        .createProcessDefinitionQuery()
        .tenantIdIn(tenantId)
        .latestVersion()
        .active()
        .singleResult();
    InputStream resource = repositoryService
        .getResourceAsStream(process.getDeploymentId(), "process.bpmn");

    BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
    return reader.lines().collect(Collectors.joining());
  }

}
