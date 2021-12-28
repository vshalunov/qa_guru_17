package com.github.zlwqa;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void getSingleUser() {

        given()
                .when()
                .get("/api/users/2")
                .then().log().all()
                .statusCode(200)
                .body("data.id", is(2),
                        "data.email", is("janet.weaver@reqres.in"),
                        "data.first_name", notNullValue(),
                        "support.url", is("https://reqres.in/#support-heading"));
    }

    @Test
    void createUser() {

        String data = "{\"name\": \"Vasilii\"," +
                "\"job\": \"QA\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then().log().status().and().log().body()
                .statusCode(201)
                .body("name", is("Vasilii"),
                        "job", is("QA"),
                        "id", notNullValue(),
                        "createdAt", notNullValue());
    }

    @Test
    void updateUserWithPut() {

        String data = "{\"name\": \"Vasilii\"," +
                "\"job\": \"AQA\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("/api/users/2")
                .then().log().all()
                .statusCode(200)
                .body("name", is("Vasilii"),
                        "job", is("AQA"),
                        "updatedAt", notNullValue());
    }

    @Test
    void updateUserWithPatch() {

        String data = "{\"name\": \"Vasilii\"," +
                "\"job\": \"AQA\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("/api/users/2")
                .then().log().all()
                .statusCode(200)
                .body("name", is("Vasilii"),
                        "job", is("AQA"),
                        "updatedAt", notNullValue());
    }

    @Test
    void deleteUser() {

        given()
                .when()
                .delete("/api/users/2")
                .then().log().status()
                .statusCode(204);
    }

}
