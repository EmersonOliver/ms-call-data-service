//package com.renemtech.calldataservice;
//
//import com.renemtech.calldataservice.model.dto.CallDataDetailsResponse;
//import io.quarkus.test.junit.QuarkusTest;
//import org.junit.jupiter.api.Test;
//
//import java.util.UUID;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.CoreMatchers.is;
//
//@QuarkusTest
//class CallDataServiceResourceTest {
//    @Test
//    void testGetCallDataServiceDetails() {
//        given()
//          .when().get("/details/123")
//          .then()
//             .statusCode(200)
//             .body(is(mockCallDataDetails()));
//    }
//
//    static CallDataDetailsResponse mockCallDataDetails() {
//        return CallDataDetailsResponse.builder()
//                .callId(UUID.fromString("123"))
//                .receiveNumber("321")
//                .build();
//    }
//
//}