package com.example.flx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "static_url")
public class StaticUrl {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="static_url_id")
	private Integer id;
	
	@Column(name="decode_url")
	private String decodeUrl;
	
	@Column(name="encode_url",unique = true)
	private String encodeUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDecodeUrl() {
		return decodeUrl;
	}

	public void setDecodeUrl(String decodeUrl) {
		this.decodeUrl = decodeUrl;
	}

	public String getEncodeUrl() {
		return encodeUrl;
	}

	public void setEncodeUrl(String encodeUrl) {
		this.encodeUrl = encodeUrl;
	}
}
