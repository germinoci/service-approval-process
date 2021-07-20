package sk.gov.mou;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(KeycloakQuarkusTestResource.class)
@QuarkusTestResource(value = KafkaQuarkusTestResource.class)
public class ServiceApprovalRestIT {

    private static final String PROCESS_ROOT_URL = "/service_approval/";
    private static final String USER_ADMIN = "admin";
    private static final String USER1 = "referent";
    private static final String ROLE1 = "officer";


    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    protected String keycloakUrl;

    @Test
    public void testStartApprovalUnauthorized() {

        given()
                .body("{}") // serviceData
                .contentType(ContentType.JSON)
                .when()
                .post(PROCESS_ROOT_URL)
                .then()
                .statusCode(401);
    }

    @Test
    public void testStartApprovalAuthorized() {

        // start new service_approval
        String id = given()
                .auth().oauth2(getAccessToken(USER_ADMIN))
                .body("{}")
                .contentType(ContentType.JSON)
                .when()
                .post(PROCESS_ROOT_URL)
                .then()
                .statusCode(201)
                .body("id", notNullValue()).extract().path("id");

        // get all active approvals
        given()
                .auth().oauth2(getAccessToken(USER_ADMIN))
                .accept(ContentType.JSON)
                .when()
                .get(PROCESS_ROOT_URL)
                .then()
                .statusCode(200)
                .body("$.size()", is(1), "[0].id", is(id));

        // get just started approval
        given()
                .auth().oauth2(getAccessToken(USER_ADMIN))
                .accept(ContentType.JSON)
                .when()
                .get(PROCESS_ROOT_URL + id)
                .then()
                .statusCode(200)
                .body("id", is(id));

        // tasks assigned in just started approval
        String taskInfo = given()
                .auth()
                .oauth2(getAccessToken(USER_ADMIN))
                .accept(ContentType.JSON)
                .when()
                .get(PROCESS_ROOT_URL + id + "/tasks?user=" + USER1 + "&group=" + ROLE1)
                .then()
                .statusCode(200)
                .body("$.size()", is(1))
                .body("[0].name", is("serviceSelectionAndAssessment"))
                .extract()
                .path("[0].id");

        String payload = "{}";
        given()
                .auth().oauth2(getAccessToken(USER1))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(PROCESS_ROOT_URL + id + "/serviceSelectionAndAssessment/" + taskInfo + "?user=" + USER1 + "&group=" + ROLE1)
                .then()
                .statusCode(200)
                .body("id", is(id));

        // lastly abort the approval
        given()
                .auth().oauth2(getAccessToken(USER1))
                .accept(ContentType.JSON)
                .when()
                .delete(PROCESS_ROOT_URL + id)
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
