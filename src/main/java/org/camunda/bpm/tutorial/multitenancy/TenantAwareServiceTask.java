package org.camunda.bpm.tutorial.multitenancy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * Sample service task which invokes a service with the tenant-id of the
 * execution (i.e. process instance).
 */
public class TenantAwareServiceTask implements JavaDelegate {

  //protected static Service service;

  @Override
  public void execute(DelegateExecution execution) throws Exception {

    String tenantId = execution.getTenantId();
    System.out.println("tenantId");
    //service.invoke(tenantId);
  }

}
