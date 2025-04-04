package com.example.serraLana.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Ordine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime dataCreazione;
    
    private String status;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
    
    @OneToOne
    @JoinColumn(name = "carrello_id") 
    private Carrello carrello;
    
    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProdottoCarrello> prodotti = new ArrayList<>();
    
    private String statusPagamento;

    public Ordine() {
        this.dataCreazione = LocalDateTime.now();  
        this.status = "CONFERMATO"; 
        this.statusPagamento = "COMPLETATO";  

    }

     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
    
    public String getStatus() {
    	return status;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<ProdottoCarrello> getProdotti() {
        return prodotti;
    }

    public void setProdotti(List<ProdottoCarrello> prodotti) {
        this.prodotti = prodotti;
    }
    
    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}
