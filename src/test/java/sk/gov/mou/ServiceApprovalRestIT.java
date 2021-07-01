package sk.gov.mou;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(KeycloakQuarkusTestResource.class)
@QuarkusTestResource(value = KafkaQuarkusTestResource.class)
public class ServiceApprovalRestIT {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    private String keycloakUrl;

    @Test
    public void testStartApprovalUnauthorized() {

        given()
                .body("{}") // serviceData
                .contentType(ContentType.JSON)
                .when()
                .post("/service_approval")
                .then()
                .statusCode(401);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testStartApprovalAuthorized() {

        // start new approval
        String id = given()
                .auth().oauth2(getAccessToken("mary"))
                .body("{}")
                .contentType(ContentType.JSON)
                .when()
                .post("/service_approval")
                .then()
                .statusCode(201)
                .body("id", notNullValue()).extract().path("id");
        // get all active approvals
        given()
                .auth().oauth2(getAccessToken("admin"))
                .accept(ContentType.JSON)
                .when()
                .get("/service_approval")
                .then()
                .statusCode(200)
                .body("$.size()", is(1), "[0].id", is(id));

        // get just started approval
        given()
                .auth().oauth2(getAccessToken("referent"))
                .accept(ContentType.JSON)
                .when()
                .get("/service_approval/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id));

        // tasks assigned in just started approval

        String taskInfo = given()
                .auth()
                .oauth2(getAccessToken("manazer"))
                .accept(ContentType.JSON)
                .when()
                .get("/service_approval/" + id + "/tasks?user=manazer&group=manager")
                .then()
                .statusCode(200)
                .body("$.size", is(1))
                .body("[0].name", is("firstLineApproval"))
                .extract()
                .path("[0].id");

        String payload = "{}";
        given()
                .auth().oauth2(getAccessToken("referent"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post("/service_approval/" + id + "/firstLineApproval/" + taskInfo + "?user=referent&group=officer")
                .then()
                .statusCode(200)
                .body("id", is(id));

        // lastly abort the approval
        given()
                .auth().oauth2(getAccessToken("referent"))
                .accept(ContentType.JSON)
                .when()
                .delete("/service_approval/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id));
    }

    private String getAccessToken(String userName) {
        return given()
                .param("grant_type", "password")
                .param("username", userName)
                .param("password", userName)
                .param("client_id", "kogito-app")
                .param("client_secret", "secret")
                .when()
                .post(keycloakUrl + "/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getToken();
    }
}
