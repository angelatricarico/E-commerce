package com.example.serraLana.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Carrello {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "utente_id")
    @JsonBackReference   
    private Utente utente;
    
    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("carrello")  
    @JsonManagedReference
    private List<ProdottoCarrello> prodottoCarrello;

    @OneToOne(mappedBy = "carrello")   
    private Ordine ordine;
    
    public void addProdottoToCarrello(Prodotto prodotto, int quantity) {
        for (ProdottoCarrello prodottoCarrello : prodottoCarrello) {
            if (prodottoCarrello.getProdotto().equals(prodotto)) {
                prodottoCarrello.setQuantity(prodottoCarrello.getQuantity() + quantity); 
                return;
            }
        }

        ProdottoCarrello nuovoProdottoCarrello = new ProdottoCarrello();
        nuovoProdottoCarrello.setProdotto(prodotto);
        nuovoProdottoCarrello.setQuantity(quantity);
        prodottoCarrello.add(nuovoProdottoCarrello);
    }

    public void removeProdottoFromCarrello(Prodotto prodotto) {
        prodottoCarrello.removeIf(prodottoCarrello -> prodottoCarrello.getProdotto().equals(prodotto));
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (ProdottoCarrello prodottoCarrello : prodottoCarrello) {
            totalQuantity += prodottoCarrello.getQuantity();
        }
        return totalQuantity;
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (ProdottoCarrello prodottoCarrello : prodottoCarrello) {
            totalPrice += prodottoCarrello.getProdotto().getPrezzo() * prodottoCarrello.getQuantity();
        }
        return totalPrice;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public List<ProdottoCarrello> getProdottoCarrello() {
		return prodottoCarrello;
	}

	public void setProdottoCarrello(List<ProdottoCarrello> prodottoCarrello) {
		this.prodottoCarrello = prodottoCarrello;
	}

	public Ordine getOrdine() {
		return ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

    
    
}
