package br.com.jvictorvale.integrationtests.controllers.withjson;

import br.com.jvictorvale.config.TestConfigs;
import br.com.jvictorvale.integrationtests.dto.AccountCredentialsDTO;
import br.com.jvictorvale.integrationtests.dto.PersonDTO;
import br.com.jvictorvale.integrationtests.dto.TokenDTO;
import br.com.jvictorvale.integrationtests.dto.wrappers.json.WrapperPersonDTO;
import br.com.jvictorvale.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDTO person;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(0)
    void signin() {
        AccountCredentialsDTO credentials =
                new AccountCredentialsDTO("leandro", "admin123");

        tokenDto = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEARDER_PARAM_ORIGIN, TestConfigs.ORIGIN_VICTOR)
                .addHeader(TestConfigs.HEARDER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());

    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                    .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Dean", createdPerson.getFirstName());
        assertEquals("Winchester", createdPerson.getLastName());
        assertEquals("Lawrence - Kansas - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("William Winchester");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Dean", createdPerson.getFirstName());
        assertEquals("William Winchester", createdPerson.getLastName());
        assertEquals("Lawrence - Kansas - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Order(3)
    @Test
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Dean", createdPerson.getFirstName());
        assertEquals("William Winchester", createdPerson.getLastName());
        assertEquals("Lawrence - Kansas - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Order(4)
    @Test
    void disableTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Dean", createdPerson.getFirstName());
        assertEquals("William Winchester", createdPerson.getLastName());
        assertEquals("Lawrence - Kansas - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Order(5)
    @Test
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Order(6)
    @Test
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        // PERSON 1

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Amos", personOne.getFirstName());
        assertEquals("Nisby", personOne.getLastName());
        assertEquals("Suite 99", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertFalse(personOne.getEnabled());

        // PERSON 3

        PersonDTO personThree = people.get(2);

        assertNotNull(personThree.getId());
        assertTrue(personThree.getId() > 0);

        assertEquals("Anatol", personThree.getFirstName());
        assertEquals("Rigge", personThree.getLastName());
        assertEquals("Room 1209", personThree.getAddress());
        assertEquals("Male", personThree.getGender());
        assertTrue(personThree.getEnabled());
    }

    @Order(7)
    @Test
    void findByNameTest() throws JsonProcessingException {

        // {{baseUrl}}/api/person/v1/findPeopleByName/and?page=0&size=12&direction=asc
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("firstName", "and")
                .queryParam("page", 0, "size", 12, "direction", "asc")
                .when()
                .get("findPeopleByName/{firstName}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        // PERSON 1

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alejandrina", personOne.getFirstName());
        assertEquals("Hallmark", personOne.getLastName());
        assertEquals("6th Floor", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertTrue(personOne.getEnabled());

        // PERSON 3

        PersonDTO personThree = people.get(2);

        assertNotNull(personThree.getId());
        assertTrue(personThree.getId() > 0);

        assertEquals("Amandy", personThree.getFirstName());
        assertEquals("Jarnell", personThree.getLastName());
        assertEquals("Suite 94", personThree.getAddress());
        assertEquals("Female", personThree.getGender());
        assertFalse(personThree.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Dean");
        person.setLastName("Winchester");
        person.setAddress("Lawrence - Kansas - USA");
        person.setGender("Male");
        person.setEnabled(true);
        person.setProfileUrl("https://pt.wikipedia.org/wiki/Dean_Winchester");
        person.setPhotoUrl("https://pt.wikipedia.org/wiki/Ficheiro:DeanWinchester.png");
    }
}