package com.library.step_definitions;

import com.google.protobuf.Api;
import com.google.protobuf.StringValue;
import com.library.pages.BasePage;
import com.library.pages.Books;
import com.library.pages.LibraryPage;
import com.library.utility.ApiUtil;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.openqa.selenium.Keys;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class stepDef {

    RequestSpecification givenPart = RestAssured.given();
    Response response;
    ValidatableResponse thenPart;
    JsonPath jsonPath;
    String expectedValue;
    Map<String, Object> randomDataMap = new HashMap<>();
    LibraryPage page = new LibraryPage();
    BasePage basePage = new BasePage();
    Books books = new Books();
    String token = "";


    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String role) {

        String token = ApiUtil.getTokenByRole(role);
        givenPart.header("x-library-token", token);

    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptType) {

        givenPart.accept(acceptType);

    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(endpoint);
        jsonPath = response.jsonPath();
        thenPart = response.then();
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        thenPart.statusCode(expectedStatusCode);

    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String expectedContentType) {
        thenPart.contentType(expectedContentType);

    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {

        String actualValue = jsonPath.getString(path);
        Assert.assertNotNull(actualValue);

    }

    @Given("Path param is {string}")
    public void path_param_is(String path){

        givenPart.pathParam("id",path);
        expectedValue = path;

    }


    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String id){

        String actualValue = jsonPath.getString(id);
        assertEquals(expectedValue,actualValue);


    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> allField){
        for(String each: allField){
            Assert.assertNotNull(jsonPath.getString(each));
        }



    }
    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentType){

        givenPart.header("Content-Type",contentType);

    }
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String dataType){

        switch (dataType){
            case "book":
                randomDataMap = ApiUtil.createRandomBook();
                break;

            case "user":
                randomDataMap = ApiUtil.createRandomUser();
                break;

            default:
                throw new RuntimeException("Invalid Data type");

        }
        givenPart.formParams(randomDataMap);


    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endPoint){
        response =  givenPart.when().post(endPoint);
        jsonPath= response.jsonPath();
        thenPart= response.then();

    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String value, String expectedText) {

        String actualText = jsonPath.getString(value);
       assertEquals(actualText,expectedText);
        System.out.println("response Body"+response.getBody().asString());


    }

    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String username){

        page.login(username);

    }
    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String page){

        basePage.books.click();

    }
    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match(){

        // ui part
        String expectedBookName = (String) randomDataMap.get("name");
        String actualBookNameInUI = books.findBook(expectedBookName);
        assertEquals(expectedBookName,actualBookNameInUI);

        // DB part

        String bookId = jsonPath.getString("book_id");

        DB_Util.runQuery("Select name from books where id ="+ bookId);

       String actualBookNameInDB =  DB_Util.getFirstRowFirstColumn();
       assertEquals(expectedBookName,actualBookNameInDB);
    }
    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database(){

        String userinfo = jsonPath.getString("id");
        DB_Util.runQuery("select id,full_name,email,status,start_date,end_date,address from users where id="+userinfo);
        Map<String,String> actualUserInfo = DB_Util.getRowMap(1);
        assertEquals(randomDataMap.get("id"), actualUserInfo.get("id"));

    }
    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui(){

        String email =(String) randomDataMap.get("email");
        String password =(String) randomDataMap.get("password");
        page.login(email,password);
    }
    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page(){

        String expectedUserName = (String) randomDataMap.get("full_name");
        String actualUserNameInDashboard = basePage.userName.getText();
        BrowserUtil.waitFor(5);
        assertEquals(expectedUserName,actualUserNameInDashboard);

    }


    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String username, String password) {

       token = ApiUtil.getToken(username,password);
       givenPart.header("x-library-token",token);

    }
    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body(){
        givenPart.formParams("token",token);



    }



}
