package br.cra.tests;

import static br.cra.tests.TipoMovimentacao.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.cra.core.BaseTest;

public class ProjectTests extends BaseTest {
	private String TOKEN;
	private Integer CONTA_ID; 

	@Before
	public void Before() {
		coletaToken();
		criaConta("contaPadrão");
		
	}
	
	/*
	 * @After private void After() { removeContas(); }
	 */
	
	
	/*
	 * private void removeContas() { ArrayList<Object> lst = listaContas();
	 * while(lst.ha)
	 * 
	 * }
	 */

	/*
	 * private ArrayList<Object> listaContas() { ArrayList<Object> listaContas =
	 * given() .header("Authorization", "JWT " + TOKEN) .when() .get("/contas")
	 * .then() .statusCode(200) .extract().path(""); return listaContas; }
	 */
	
	private void coletaToken() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "igor@projeto");
		login.put("senha", "projeto123");
		TOKEN = 
				given()
					.body(login)
				.when()
					.post("/signin")
				.then()
					.statusCode(200)
					.extract().path("token")
					;
	}
	
	private void criaConta(String nome) {
		Conta novaConta = new Conta(nome);
		CONTA_ID =  given()
					.header("Authorization", "JWT " + TOKEN)
					.body(novaConta)
				.when()
					.post("/contas")
				.then()
					.statusCode(201)
					.body("nome", is(novaConta.getNome()))
					.extract().path("id");
		
	}	

	@Test
	public void falhaAoAcessarSemToken() {
		given()
		.when()	
			.get("/contas")
		.then()
			.statusCode(401);
	}

	@Test
	public void deveAcessarComToken() {
		given()
			.header("Authorization", "JWT " + TOKEN)
		.when()
			.get("/contas")
		.then()
			.statusCode(200);
	}

	@Test
	public void deveIncluirConta() {
		criaConta("contaTest");
	}

	@Test
	public void naoDeveIncluirSemCampoObrigatorio() {
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body("{ \"\": \"\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(400).body("msg[0]", is("Nome é um campo obrigatório"));
	}

	@Test
	public void deveAlterarConta() {
		Conta conta = new Conta("conta1_alterada");
		given()
			.header("Authorization", "JWT " + TOKEN)
			.pathParam("id", CONTA_ID)
			.body(conta)
		.when()
			.put("/contas/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is("conta1_alterada"));
	}
	
	@Test
	public void naoDeveAlterarContaInexistente() {				
		Conta conta = new Conta("NOMEQUALQUER");
		given()
			.header("Authorization", "JWT " + TOKEN)
			.pathParam("id", "00000")
			.body(conta)
		.when()
			.put("/contas/{id}")
		.then()
			.log().all()
			.statusCode(404);
	}

	@Test
	public void naoDeveIncluirContaRepetida() {
		Conta conta = new Conta("conta1");
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body(conta)
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"));;
	}
	
	@Test
	public void deveInserirMovimentacaoDespesaStatusFalse() {
		Movimentacoes mov = new Movimentacoes();
		mov.setConta_id(CONTA_ID);
		mov.setData_transacao("19/04/2022");
		mov.setData_pagamento("20/04/2022");
		mov.setDescricao("trans1");
		mov.setEnvolvido("zezin da padaria");
		mov.setStatus(false);
		mov.setTipo(DESP);
		mov.setValor(100f);
		
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
			.log().all()
			.body("tipo", is("DESP"))
			.body("status", is(false))
			;
	}
	
	@Test
	public void deveInserirMovimentacaoReceitaStatusTrue() {
		Movimentacoes mov = new Movimentacoes();
		mov.setConta_id(CONTA_ID);
		mov.setData_transacao("20/04/2022");
		mov.setData_pagamento("21/04/2022");
		mov.setDescricao("trans2");
		mov.setEnvolvido("zezin da padaria");
		mov.setStatus(true);
		mov.setTipo(REC);
		mov.setValor(200f);
		
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
			.body("tipo", is("REC"))
			.body("status", is(true))
			.log().all()
			;
	}
	
	@Test
	public void naoDeveInserirMovimentacaoComDatasInvalidas() {
		Movimentacoes mov = new Movimentacoes();
		mov.setConta_id(CONTA_ID);
		mov.setData_transacao("20/04/2028");
		mov.setData_pagamento("25042028");
		mov.setDescricao("trans2");
		mov.setEnvolvido("zezin da padaria");
		mov.setStatus(true);
		mov.setTipo(REC);
		mov.setValor(200f);
		
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.log().all()
			.body("", hasSize(2))
			.body("param[0]", is("data_transacao"))
			.body("msg[0]", is("Data da Movimentação deve ser menor ou igual à data atual"))
			.body("param[1]", is("data_pagamento"))
			.body("msg[1]", is("Data do pagamento inválida (DD/MM/YYYY)"));
	}
	
	@Test
	public void naoDeveInserirMovimentacaoVazia() {
		Movimentacoes mov = new Movimentacoes();
			
		given()
			.header("Authorization", "JWT " + TOKEN)
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("", hasSize(8))
			.body("msg", hasItems(
					"Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"
					));
	}
	
	@Test
	public void naoDeveRemoverContaComMovimentacoes() {
		deveInserirMovimentacaoReceitaStatusTrue();
		given()
			.header("Authorization", "JWT " + TOKEN)
			.pathParam("contaID", CONTA_ID)
		.when()
			.delete("/contas/{contaID}")
		.then()
			.statusCode(500)
			.log().all()
			.body("constraint", is("transacoes_conta_id_foreign"));
	}
	
	@Test
	public void deveRemoverConta() {
		given()
			.header("Authorization", "JWT " + TOKEN)
			.pathParam("contaID", CONTA_ID)
		.when()
			.delete("/contas/{contaID}")
		.then()
			.statusCode(204);
	}
	
	@Test
	public void naoDeveRemoverContaInexistente() {
		given()
			.header("Authorization", "JWT " + TOKEN)
			.pathParam("contaID", 00000)
		.when()
			.delete("/contas/{contaID}")
		.then()
			.statusCode(404);
	}
	
	@Test
	public void deveCalcularSaldoComUmaMovimentacaoEmUmaConta() {
		criaConta("contaComMovimentação");
		deveInserirMovimentacaoReceitaStatusTrue();
		given()
			.header("Authorization", "JWT " + TOKEN)
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("", hasSize(1))
			.body("saldo.findAll{it = 200.00}.size()", is(1));
	}
	
	@Test
	public void deveCalcularSaldoComUmaMovimentacaoEmMaisDeUmaConta() {
		deveInserirMovimentacaoReceitaStatusTrue();
		criaConta("contaComMovimentação");
		deveInserirMovimentacaoReceitaStatusTrue();
		given()
			.header("Authorization", "JWT " + TOKEN)
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("", hasSize(2))
			.body("saldo.findAll{it = 200.00}.size()", is(2));
	}
	
	@Test
	public void naoDeveCalcularSaldoSemMovimentacoes() {
		given()
			.header("Authorization", "JWT " + TOKEN)
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("", hasSize(0));
	}
	
	@Test
	public void deveRemoverTransacao() {
		deveInserirMovimentacaoReceitaStatusTrue();
		given()
			.header("Authorization", "JWT " + TOKEN)
			.pathParam("contaID", CONTA_ID)
		.when()
			.delete("/transacoes/{contaID}")
		.then()
			.statusCode(204);
	}
}
