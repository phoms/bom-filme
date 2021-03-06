package br.com.infnet.bomfilme.model;

/**
 * Representa um endere�o completo.
 * 
 * @author Pedro Henrique
 */
public class Endereco {
	private String rua;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	private String estado;

	public Endereco() {
	}

	public Endereco(String rua, String complemento, String bairro, String cep,
			String cidade, String estado) {
		this.rua = rua;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(rua);
		
		if(complemento != null)
			builder.append(", ").append(complemento);
		
		builder.append(", ").append(bairro);
		builder.append(" - ").append(cidade);
		
		return builder.toString();
	}
}
