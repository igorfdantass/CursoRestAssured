package br.cra.tests;

public class Movimentacoes {
	private Integer conta_id;
	private String descricao;
	private String envolvido;
	private TipoMovimentacao tipo; 
	private String data_transacao;
	private String data_pagamento;
	private Float valor;
	private Boolean status;
	
	public Integer getConta_id() {
		return conta_id;
	}
	public void setConta_id(Integer conta_id) {
		this.conta_id = conta_id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getEnvolvido() {
		return envolvido;
	}
	public void setEnvolvido(String envolvido) {
		this.envolvido = envolvido;
	}
	public TipoMovimentacao getTipo() {
		return tipo;
	}
	public void setTipo(TipoMovimentacao tipo) {
		this.tipo = tipo;
	}
	public String getData_transacao() {
		return data_transacao;
	}
	public void setData_transacao(String data_transacao) {
		this.data_transacao = data_transacao;
	}
	public String getData_pagamento() {
		return data_pagamento;
	}
	public void setData_pagamento(String data_pagamento) {
		this.data_pagamento = data_pagamento;
	}
	public Float getValor() {
		return valor;
	}
	public void setValor(Float valor) {
		this.valor = valor;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	
	
	
}
