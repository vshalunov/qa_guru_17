package com.github.zlwqa;

import com.github.zlwqa.lombok.LombokUserData;
import com.github.zlwqa.models.ModelsUserData;
import org.junit.jupiter.api.Test;

import static com.github.zlwqa.specs.Specs.requestSpec;
import static com.github.zlwqa.specs.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests extends TestBase {

    @Test
    void getSingleUserWithModels() {

        ModelsUserData userData = given()
                .spec(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .extract().as(ModelsUserData.class);
/*                .body("data.id", is(2),
                        "data.email", is("janet.weaver@reqres.in"),
                        "data.first_name", notNullValue(),
                        "support.url", is("https://reqres.in/#support-heading"))
                .headers("Content-Type", is("application/json; charset=utf-8"),
                        "access-control-allow-origin", is("*"));*/
        assertEquals(2, userData.getData().getId());
        assertEquals("Janet", userData.getData().getFirstName());
        assertEquals("Weaver", userData.getData().getLastName());
    }

    @Test
    void createUserWithGroovy() {

        String data = "{\"name\": \"Vasilii\"," +
                "\"job\": \"QA\"}";

        given()
                .spec(requestSpec)
                .body(data)
                .when()
                .post("/users")
                .then().log().status().and().log().body()
                .statusCode(201)
                .body("name", is("Vasilii"),
                        "job", is("QA"),
                        "id", notNullValue(),
                        "createdAt", notNullValue());
    }

    @Test
    public void checkEmailWithGroovy() {
        // @formatter:off
        given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
        // @formatter:on
    }

    @Test
    void getListUsersWithLombokModel() {
        LombokUserData data = given()
                .spec(requestSpec)
                .when()
                .get("/users?page=1")
                .then()
                .spec(responseSpec)
                .extract().as(LombokUserData.class);

        assertThat(data.getUser()[5].getId()).isEqualTo(6);
        assertThat(data.getUser()[2].getEmail()).isEqualTo("emma.wong@reqres.in");
        assertThat(data.getUser()[3].getLastName()).isEqualTo("Holt");
        assertThat(data.getUser()[4].getFirstName()).isEqualTo("Charles");
    }

    @Test
    void updateUserWithPut() {

        String data = "{\"name\": \"Vasilii\"," +
                "\"job\": \"AQA\"}";

        given()
                .spec(requestSpec)
                .body(data)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .body("name", is("Vasilii"),
                        "job", is("AQA"),
                        "updatedAt", notNullValue());
    }

    @Test
    void updateUserWithPatch() {

        String data = "{\"name\": \"Vasilii\"," +
                "\"job\": \"AQA\"}";

        given()
                .spec(requestSpec)
                .body(data)
                .when()
                .patch("/users/2")
                .then()
                .spec(responseSpec)
                .body("name", is("Vasilii"),
                        "job", is("AQA"),
                        "updatedAt", notNullValue());
    }

    @Test
    void deleteUser() {

        given()
                .spec(requestSpec)
                .when()
                .delete("/users/2")
                .then().log().status()
                .statusCode(204);
    }

}
