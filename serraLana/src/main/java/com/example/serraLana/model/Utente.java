package com.example.serraLana.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

@Entity
public class Utente {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(mappedBy = "utente") 
    private ListaDesideri listaDesideri;
    
	@OneToMany
	@JoinColumn (name="utente_id")
	@JsonIgnoreProperties({"utente", "prodottoCarrello"})
	private List<Carrello> carrello;
	
	@NotBlank(message = "Nome utente è obbligatorio")
	private String nome;
	
	@NotBlank(message = "Cognome è obbligatorio")
	private String cognome;
	
	@NotBlank(message = "Username è obbligatorio")
	@Column(unique = true)
	private String username;
	
	@NotBlank(message = "Email è obbligatoria")
    @Email(message = "Formato email non valido")
	@Column(unique = true)
    private String email;
    
	private String password;
  

	@Past(message = "Data non valida")
    @NotNull(message = "Data di nascita è obbligatoria")
    private LocalDate dataNascita;
    
    private String indirizzo;
    
    @Column(unique = true)
    @Length(min = 16, max = 16, message = "Il numero della carta di credito deve essere esattamente di 16 cifre")
    private String numeroCarta;

    @Column(nullable = false)
    @Pattern(regexp = "(0[1-9]|1[0-2])/(\\d{2})", message = "La data di scadenza deve essere nel formato MM/YY")
    private String scadenzaCarta;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{3}", message = "Il CVC deve essere composto da 3 cifre")
    private String cvcCarta;

    
    private String ruolo;
	
	@Column(unique = true)
	private String token;
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public List<Carrello> getCarrello() {
		return carrello;
	}

	public void setCarrello(List<Carrello> carrello) {
		this.carrello = carrello;
	}

	public ListaDesideri getListaDesideri() {
		return listaDesideri;
	}

	public void setListaDesideri(ListaDesideri listaDesideri) {
		this.listaDesideri = listaDesideri;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
    public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumeroCarta() {
		return numeroCarta;
	}

	public void setNumeroCarta(String numeroCarta) {
		this.numeroCarta = numeroCarta;
	}

	public String getScadenzaCarta() {
		return scadenzaCarta;
	}

	public void setScadenzaCarta(String scadenzaCarta) {
		this.scadenzaCarta = scadenzaCarta;
	}

	public String getCvcCarta() {
		return cvcCarta;
	}

	public void setCvcCarta(String cvcCarta) {
		this.cvcCarta = cvcCarta;
	}
	
   
	

}
