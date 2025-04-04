package com.example.serraLana.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Prodotto {
    
    public static final long Max_file_size = 100000;
    public static List<String> Content_Types = Arrays.asList(
        "image/svg+xml", 
        "image/png", 
        "image/jpeg", 
        "image/jpe", 
        "image/jpg", 
        "application/pdf"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "prodotto_id")
    private List<ProdottoLista> prodottoLista;

    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria; 

    @OneToMany
    @JoinColumn(name = "prodotto_id")
    @JsonIgnore  
    private List<ProdottoCarrello> prodottoCarello;

    @NotBlank(message = "Nome prodotto è obbligatorio")
    private String titolo;

    private String descrizione;

    @NotNull(message = "Prezzo è obbligatorio")
    @Positive(message = "Prezzo deve essere positivo")
    private double prezzo;

    @NotNull(message = "Quantità è obbligatoria")
    @Min(value = 0, message = "Quantità non valida")
    private int quantitaMax; 

    @Lob 
    private byte[] immagine;
    
    private String nomeImg; 


    public Prodotto() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public int getQuantitaMax() { return quantitaMax; }
    public void setQuantitaMax(int quantitaMax) { this.quantitaMax = quantitaMax; }

    public List<ProdottoLista> getProdottoLista() { return prodottoLista; }
    public void setProdottoLista(List<ProdottoLista> prodottoLista) { this.prodottoLista = prodottoLista; }

    public CategoriaEnum getCategoria() { return categoria; }
    public void setCategoria(CategoriaEnum categoria) { this.categoria = categoria; }

	public byte[] getImmagine() {
		return immagine;
	}

	public void setImmagine(byte[] immagine) {
		this.immagine = immagine;
	}


	public String getNomeImg() {
		return nomeImg;
	}

	public void setNomeImg(String nomeImg) {
		this.nomeImg = nomeImg;
	}

	public List<ProdottoCarrello> getProdottoCarello() { return prodottoCarello; }
    public void setProdottoCarello(List<ProdottoCarrello> prodottoCarello) { this.prodottoCarello = prodottoCarello; }

    public Prodotto(Long id, CategoriaEnum categoria, String titolo, String descrizione, double prezzo, int quantitaMax, String nomeImg, String imageUrl) {
        this.id = id;
        this.categoria = categoria;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaMax = quantitaMax;

    }
}