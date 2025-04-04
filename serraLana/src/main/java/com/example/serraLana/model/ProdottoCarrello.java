package com.example.serraLana.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class ProdottoCarrello {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "prodotto_id")
    @JsonIgnoreProperties({"prodottoCarrello", "prodottoLista"})   
    private Prodotto prodotto;
    
    @ManyToOne
    @JoinColumn(name = "carrello_id")
    @JsonIgnoreProperties("prodottoCarrello") 
    @JsonBackReference
    private Carrello carrello;
    
    @ManyToOne
    @JoinColumn(name = "ordine_id")
    private Ordine ordine; 
    
    @NotNull(message = "Quantità è obbligatoria")
    @Min(value = 1, message = "La quantità deve essere maggiore di zero")
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {  
        this.ordine = ordine;
    }

    public void incrementQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
    }

    public void decrementQuantity(int quantityToReduce) {
        if (this.quantity - quantityToReduce >= 0) {
            this.quantity -= quantityToReduce;
        }
    }
}
