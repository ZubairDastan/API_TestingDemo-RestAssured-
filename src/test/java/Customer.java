import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Customer {
    Properties props = new Properties();
    FileInputStream file = new FileInputStream("./src/test/resources/config.properties");

    public Customer() throws FileNotFoundException {
    }

    public void callingCustomerLoginAPI() throws ConfigurationException, IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseUrl");
        Response response =
                given()
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"username\":\"salman\",\n" +
                                "    \"password\":\"salman1234\"\n" +
                                "}")
                .when()
                        .post("/customer/api/v1/login")
                .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath resObj = response.jsonPath();
        String token = resObj.get("token");
        Utils.setEnvVariable("token", token);
        System.out.println(token);

    }

    public void callingCustomerListAPI() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseUrl");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                .when()
                        .get("/customer/api/v1/list")
                .then()
                        .assertThat().statusCode(200).extract().response();

        System.out.println(response.asString());
        JsonPath jsonObj = response.jsonPath();
        int id = jsonObj.get("Customers[0].id");
        Assert.assertEquals(101, id);
    }

    public void searchCustomer() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseUrl");
        Response response =
                given()
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                .when()
                        .get("/customer/api/v1/get/101")
                .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonObj = response.jsonPath();
        String name = jsonObj.get("name");
        System.out.println(name);
        Assert.assertEquals("Mr. Kamal", name);
        System.out.println(response.asString());

    }

    public void creatCustomer() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseUrl");
        Response response =
                given()
                        .contentType("application/json").header("Authorization", props.getProperty("token"))
                        .body("{\n" +
                                "    \"id\":1002,\n" +
                                "    \"name\":\"Test1\",\n" +
                                "    \"email\":\"test1000@test.test\",\n" +
                                "    \"address\":\"Test address 1\",\n" +
                                "    \"phone_number\":\"0123548796636\"\n" +
                                "}")
                .when()
                        .post("/customer/api/v1/create")
                .then()
                        .assertThat().statusCode(201).extract().response();

        JsonPath jsonObj = response.jsonPath();
        String message = jsonObj.get("message");
        System.out.println(message);
        Assert.assertEquals("Success", message);
        System.out.println(response.asString());
    }

    public void createExistingCustomer() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseUrl");
        Response response =
                given()
                        .contentType("application/json").header("Authorization", props.getProperty("token"))
                        .body("{\n" +
                                "    \"id\":1002,\n" +
                                "    \"name\":\"Test1\",\n" +
                                "    \"email\":\"test1000@test.test\",\n" +
                                "    \"address\":\"Test address 1\",\n" +
                                "    \"phone_number\":\"0123548796636\"\n" +
                                "}")
                .when()
                        .post("/customer/api/v1/create")
                .then()
                        .assertThat().statusCode(208).extract().response();

        JsonPath jsonObj = response.jsonPath();
        String message = jsonObj.get("error.message");
        System.out.println(message);
        System.out.println(response.asString());
        Assert.assertEquals("Customer already exists", message);
    }

    public void updateCustomer() throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseUrl");
        Response response =
                given()
                        .contentType("application/json").header("Authorization", props.getProperty("token"))
                        .body("{\n" +
                                "    \"id\": 102,\n" +
                                "    \"name\": \"Mr. Jamal Islam\",\n" +
                                "    \"email\": \"mrjamal@test.com\",\n" +
                                "    \"address\": \"Gazipur,Dhaka\",\n" +
                                "    \"phone_number\": \"01502020110\"\n" +
                                "}")
                .when()
                        .put("/customer/api/v1/update/102")
                .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonObj = response.jsonPath();
        System.out.println(response.asString());
        String match = jsonObj.get("Customers.address");
        Assert.assertEquals("Gazipur,Dhaka", match);
    }

    public void deleteCustomer () throws IOException {
        props.load(file);
        RestAssured.baseURI = props.getProperty("baseUrl");
        Response response =
                given()
                        .contentType("application/json").header("Authorization", props.getProperty("token"))
                .when()
                        .delete("/customer/api/v1/delete/1002")
                .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonObj = response.jsonPath();
        System.out.println(response.asString());
        String message = jsonObj.get("message");
        Assert.assertEquals("Customer deleted!", message);
    }
}
