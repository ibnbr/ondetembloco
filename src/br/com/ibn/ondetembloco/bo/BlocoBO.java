package br.com.ibn.ondetembloco.bo;

import java.util.Date;

public class BlocoBO {
	private int id;
	private String nome;
	private String rua;
	private String bairro;
	private Integer lat;
	private Integer lon;
	private Date data;
	private String hora;
	private String destaque;
//	private String favorito;
	
	
//	public String getFavorito() {
//		return favorito;
//	}
//
//	public void setFavorito(String favorito) {
//		this.favorito = favorito;
//	}

	public BlocoBO() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public Integer getLat() {
		return lat;
	}
	public void setLat(Integer lat) {
		this.lat = lat;
	}
	public Integer getLon() {
		return lon;
	}
	public void setLon(Integer lon) {
		this.lon = lon;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getDestaque() {
		return destaque;
	}
	public void setDestaque(String destaque) {
		this.destaque = destaque;
	} 

}
