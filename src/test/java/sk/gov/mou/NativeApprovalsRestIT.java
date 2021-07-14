package sk.gov.mou;

import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;


@NativeImageTest
@QuarkusTestResource(KeycloakQuarkusTestResource.class)
@QuarkusTestResource(value = KafkaQuarkusTestResource.class)
public class NativeApprovalsRestIT extends ServiceApprovalRestIT {
    /**
     *  run the same tests only against native image
     *  for simple click test in intellij idea `/target/service-approval-process-runner` must be generated
     *  use command : mvn package -Pnative or mvn package -Pnative -Dquarkus.native.container-build=true
     *
     */
}