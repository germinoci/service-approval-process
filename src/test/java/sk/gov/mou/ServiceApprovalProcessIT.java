package sk.gov.mou;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.jbpm.process.instance.impl.humantask.HumanTaskTransition;
import org.jbpm.process.instance.impl.humantask.phases.Claim;
import org.jbpm.process.instance.impl.workitem.Complete;
import org.junit.jupiter.api.Test;
import org.kie.kogito.Model;
import org.kie.kogito.auth.IdentityProviders;
import org.kie.kogito.auth.SecurityPolicy;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;
import org.kie.kogito.process.WorkItem;
import org.kie.kogito.testcontainers.quarkus.InfinispanQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(KeycloakQuarkusTestResource.class)
@QuarkusTestResource(value = InfinispanQuarkusTestResource.class)
@QuarkusTestResource(value = KafkaQuarkusTestResource.class)
public class ServiceApprovalProcessIT {

    @Named("service_approval")
    @Inject
    Process<? extends Model> serviceApprovalProcess;

    private static final String USER1 = "referent";
    private static final String ROLE1 = "officer";

    private static final String USER2 = "manazer";
    private static final String ROLE2 = "manager";


    private static final String TASK_ASSIGMENT_TO_MANAGER = "assignToManager";
    private static final String TASK_ASSIGMENT_APPROVE = "approveThisService";
    private static final String TASK_ASSIGMENT_PUBLISH = "publishThisService";

    private static final String PROCESS_DATA_CLASS = "serviceData";

    private static final String TARGET_ASSIGMENT_TO_MANAGER = "isAssigned";
    private static final String TARGET_ASSIGMENT_APPROVE = "isApproved";
    private static final String TARGET_ASSIGMENT_PUBLISH = "isPublished";

    @Test
    public void serviceApprovalProcessExists() {
        assertNotNull(serviceApprovalProcess);
    }

    @Test
    public void testServiceApprovalProcessFlow() {

        assertNotNull(serviceApprovalProcess);

        Map<String, Object> results = new HashMap<>();

        ProcessInstance<?> processInstance = getServiceApprovalProcess();

        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());


        // user referent (role officer) as owner of the task
        SecurityPolicy policy = SecurityPolicy.of(IdentityProviders.of(USER1, Arrays.asList(ROLE1)));
        processInstance.workItems(policy);
        List<WorkItem> workItems = processInstance.workItems(policy);
        assertEquals(1, workItems.size());

        // user referent (role officer) complete his task
        results.put(TASK_ASSIGMENT_TO_MANAGER, true);
        processInstance.completeWorkItem(workItems.get(0).getId(), results, policy);
        workItems = processInstance.workItems(policy);
        assertEquals(0, workItems.size());
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());

        // user manazer (role manager) as owner of the task
        policy = SecurityPolicy.of(IdentityProviders.of(USER2, Arrays.asList(ROLE2)));
        workItems = processInstance.workItems(policy);
        assertEquals(1, workItems.size());
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());

        // user manazer (role manager) complete his task
        results.put(TASK_ASSIGMENT_APPROVE, true);
        processInstance.completeWorkItem(workItems.get(0).getId(), results, policy);
        workItems = processInstance.workItems(policy);
        assertEquals(0, workItems.size());
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());

        // user referent (role officer) as owner of the task
        policy = SecurityPolicy.of(IdentityProviders.of(USER1, Arrays.asList(ROLE1)));
        workItems = processInstance.workItems(policy);
        assertEquals(1, workItems.size());
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());

        // user referent (role officer) complete his task
        results.put(TASK_ASSIGMENT_PUBLISH, true);
        processInstance.completeWorkItem(workItems.get(0).getId(), results, policy);
        workItems = processInstance.workItems(policy);
        assertEquals(0, workItems.size());

        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_COMPLETED, processInstance.status());

        Model processVariables = (Model) processInstance.variables();
        assertEquals(4, processVariables.toMap().size());
        assertEquals(processVariables.toMap().get(TARGET_ASSIGMENT_TO_MANAGER), true);
        assertEquals(processVariables.toMap().get(TARGET_ASSIGMENT_APPROVE), true);
        assertEquals(processVariables.toMap().get(TARGET_ASSIGMENT_PUBLISH), true);
        assertEquals(processVariables.toMap().get(PROCESS_DATA_CLASS), null);
    }

    @Test
    public void testServiceApprovalProcessFlowViaPhases() {

        assertNotNull(serviceApprovalProcess);

        ProcessInstance<?> processInstance = getServiceApprovalProcess();

        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());

        // user referent (role officer) as owner of the task
        SecurityPolicy policy = SecurityPolicy.of(IdentityProviders.of(USER1, Arrays.asList(ROLE1)));
        processInstance.workItems(policy);
        List<WorkItem> workItems = processInstance.workItems(policy);
        assertEquals(1, workItems.size());

        // change human task via transition
        processInstance.transitionWorkItem(workItems.get(0).getId(), new HumanTaskTransition(Claim.ID, null, policy));
        processInstance.transitionWorkItem(workItems.get(0).getId(), new HumanTaskTransition(Complete.ID, Collections.singletonMap(TASK_ASSIGMENT_TO_MANAGER, true), policy));
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());
        workItems = processInstance.workItems(policy);
        assertEquals(0, workItems.size());

        // user manazer (role manager) as owner of the task
        policy = SecurityPolicy.of(IdentityProviders.of(USER2, Arrays.asList(ROLE2)));
        processInstance.workItems(policy);
        workItems = processInstance.workItems(policy);
        assertEquals(1, workItems.size());

        // change human task via transition
        processInstance.transitionWorkItem(workItems.get(0).getId(), new HumanTaskTransition(Claim.ID, null, policy));
        processInstance.transitionWorkItem(workItems.get(0).getId(), new HumanTaskTransition(Complete.ID, Collections.singletonMap(TASK_ASSIGMENT_APPROVE, true), policy));
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_ACTIVE, processInstance.status());

        // user referent (role officer) as owner of the task
        policy = SecurityPolicy.of(IdentityProviders.of(USER1, Arrays.asList(ROLE1)));
        processInstance.workItems(policy);
        workItems = processInstance.workItems(policy);
        assertEquals(1, workItems.size());

        // change human task via transition
        processInstance.transitionWorkItem(workItems.get(0).getId(), new HumanTaskTransition(Claim.ID, null, policy));
        processInstance.transitionWorkItem(workItems.get(0).getId(), new HumanTaskTransition(Complete.ID, Collections.singletonMap(TASK_ASSIGMENT_PUBLISH, true), policy));

        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_COMPLETED, processInstance.status());

        Model processVariables = (Model) processInstance.variables();
        assertEquals(4, processVariables.toMap().size());
        assertEquals(processVariables.toMap().get(TARGET_ASSIGMENT_TO_MANAGER), true);
        assertEquals(processVariables.toMap().get(TARGET_ASSIGMENT_APPROVE), true);
        assertEquals(processVariables.toMap().get(TARGET_ASSIGMENT_PUBLISH), true);
        assertEquals(processVariables.toMap().get(PROCESS_DATA_CLASS), null);
    }

    private ProcessInstance<?> getServiceApprovalProcess() {
        Model m = serviceApprovalProcess.createModel();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(PROCESS_DATA_CLASS, new ServiceData("random", ServiceStatus.STARTED));
        m.fromMap(parameters);

        ProcessInstance<?> processInstance = serviceApprovalProcess.createInstance(m);
        processInstance.start();
        return processInstance;
    }
}
