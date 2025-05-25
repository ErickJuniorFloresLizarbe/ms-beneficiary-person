package pe.edu.vallegrande.beneficiary.controllerTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class PersonControllerTest {

     @BeforeAll

    public static void setup() {
        RestAssured.baseURI = "https://curly-train-j6xg495x9rp3pr69-8085.app.github.dev";
    }

    @Test
    public void BeneficiaryGetTest() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/persons/filter?typeKinship=HIJO&state=A") 
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void BeneficiaryDeleteTest() {
        int beneficiaryId = 6;
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("/api/persons/" + beneficiaryId + "/delete")
        .then()
            .statusCode(200);
    }

    @Test
    public void BeneficiaryRestoreTest() {
        int beneficiaryId = 6;
        given()
            .contentType(ContentType.JSON)
        .when()
            .put("/api/persons/" + beneficiaryId + "/restore")
        .then()
            .statusCode(200);
    }

    @Test
    public void BeneficiaryUpdateTest() {
        int beneficiaryId = 6;
        String requestBody = "{\n" +
            "    \"idPerson\": " + beneficiaryId + ",\n" +
            "    \"name\": \"Laura Maria\",\n" +
            "    \"surname\": \"Ramírez\",\n" +
            "    \"age\": 22,\n" +
            "    \"birthdate\": \"2001-07-10\",\n" +
            "    \"typeDocument\": \"DNI\",\n" +
            "    \"documentNumber\": \"1232232\",\n" +
            "    \"typeKinship\": \"Hijo\",\n" +
            "    \"sponsored\": \"SI\",\n" +
            "    \"state\": \"A\",\n" +
            "    \"familyId\": 2\n" +
            "}";
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .put("/api/persons/" + beneficiaryId + "/update-person")
        .then()
            .statusCode(200);
    }

    @Test
    public void BeneficiaryRegisterTest() {
        String requestBody = "{\n" +
            "    \"name\": \"Pablo\",\n" +
            "    \"surname\": \"Flores\",\n" +
            "    \"age\": 7,\n" +
            "    \"birthdate\": \"2018-12-12\",\n" +
            "    \"typeDocument\": \"DNI\",\n" +
            "    \"documentNumber\": \"12345678\",\n" +
            "    \"typeKinship\": \"Hijo\",\n" +
            "    \"sponsored\": \"SI\",\n" +
            "    \"state\": \"A\",\n" +
            "    \"familyId\": 2,\n" +
            "    \"education\": [],\n" +
            "    \"health\": []\n" +
            "}";
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/persons/register")
        .then()
            .statusCode(200);
    }
}
