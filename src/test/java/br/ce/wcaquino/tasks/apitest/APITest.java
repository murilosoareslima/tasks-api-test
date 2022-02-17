package br.ce.wcaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	@Test
	public void deveRetornarTarefas() {
		RestAssured.given().when().get("/todo")// quando der um get nessa rota
				.then().statusCode(200);// deve ter status de retorno 200
	}

	@Test
	public void deveLogarTudoDaRequsicao() {
		RestAssured.given().log().all()// logar a requisi��o
				.when().get("/todo")// quando der um get nessa rota
				.then().log().all();// entao, logar tudo sobre a resposta
	}

	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
			.body("{\"task\" : \"Teste via API\",\"dueDate\" : \"2022-12-20\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.body("{\"task\" : \"Teste via API\",\"dueDate\" : \"2010-12-20\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"));
	}

	@Test
	public void deveRemoverTarefaComSucesso() {
		//inserindo e devolvendo o id gerado.
		Integer id = RestAssured.given()
			.body("{\"task\" : \"Teste via API\",\"dueDate\" : \"2022-12-20\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()				
			.statusCode(201);
			.extract().path("id");

		//remover
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204);
	}
}
