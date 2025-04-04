package com.example.serraLana.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AggiungiProdottoRequest {

    @NotNull(message = "L'ID del prodotto è obbligatorio")
    private Long prodottoId;

    @Min(value = 1, message = "La quantità deve essere almeno 1")
    private int quantity;

    public Long getProdottoId() {
        return prodottoId;
    }

    public void setProdottoId(Long prodottoId) {
        this.prodottoId = prodottoId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
