package com.library.utility;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ApiUtil {


    static Faker faker = new Faker();

    public static String getToken(String email, String password) {


        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);
        JsonPath jsonPath = RestAssured.given().accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .formParams(credentials)
                .when().post("/login")
                .then()
                .extract().jsonPath();

        String token = jsonPath.getString("token");

        return token;
    }


    public static Map<String, String> getCredentials(String role) {

        String email = "";
        String password = "";

        switch (role) {

            case "librarian":
                email = System.getenv("LIBRARIAN_USERNAME");
                password = System.getenv("LIBRARIAN_PASSWORD");
                break;

            case "student":
                email = System.getenv("STUDENT_USERNAME");
                password = System.getenv("STUDENT_PASSWORD");
                break;

            default:
                throw new RuntimeException("Invalid role");

        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);
        return credentials;
    }

    public static String getTokenByRole(String role) {

        Map<String, String> credentials = getCredentials(role);
        String email = credentials.get("email");
        String password = credentials.get("password");
        return getToken(email, password);
    }


    public static Map<String, Object> createRandomBook() {

        Map<String, Object> book = new HashMap<>();
        book.put("name", "Challenge yourself");
        book.put("isbn", faker.ancient().hero());
        book.put("year", faker.number().numberBetween(2020, 2025));
        book.put("author", faker.book().author());
        book.put("book_category_id", 7);
        book.put("description", faker.chuckNorris().fact());

        return book;
    }

    public static Map<String, Object> createRandomUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("full_name", faker.name().fullName());
        user.put("email", faker.internet().emailAddress());
        user.put("password", faker.internet().password(5, 8));
        user.put("user_group_id", faker.number().numberBetween(1,3));
        user.put("status","active"); // Example: static value
        user.put("start_date","2020-10-14");
        user.put("end_date","2021-10-12");
        user.put("address",faker.address().streetAddress());
        return user;
    }

    public static void login (String username, String password){
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username",username );
        credentials.put("password", password);
    }


}
