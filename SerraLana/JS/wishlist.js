document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ DOM completamente caricato, avvio visualizzaListaDesideri()");
    visualizzaListaDesideri();
});


//Funzione per caricare la lista desideri
async function visualizzaListaDesideri() {
    const token = localStorage.getItem("authToken");

    if (!token) {
        alert("Devi essere loggato per vedere la lista desideri!");
        window.location.href = "/SerraLana/HTML/registrazione.html";
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/listadesideri/visualizza", {
            method: "GET",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${await response.text()}`);
        }

        const prodotti = await response.json();
        console.log("Lista desideri ricevuta:", prodotti);

        const listaContainer = document.getElementById("prodottiAggiunti");
        listaContainer.innerHTML = "";

        if (prodotti.length === 0) {
            listaContainer.innerHTML = "<h6>La tua lista desideri è vuota.</h6>";
            return;
        }

        prodotti.forEach(prodotto => {
            const card = document.createElement("div");
            card.classList.add("card");
            card.style.width = "18rem";

            let imageSrc = prodotto.immagine
                ? `data:image/jpeg;base64,${prodotto.immagine}`
                : "/immagini/default.jpg";

            card.innerHTML = `
                <img class="card-img-top" src="${imageSrc}" alt="${prodotto.nome}">
                <div class="card-body">
                    <h5 class="card-title">${prodotto.nome}</h5>
                    <p>Prezzo: €${prodotto.prezzo.toFixed(2)}</p>
                    <p>Quantità: ${prodotto.quantity}</p>
                   <div class="btn-container">
                        <button onclick="rimuoviDaListaDesideri(${prodotto.id})" class="btn btn-danger">Rimuovi</button>
                        <button onclick="aggiungiDalDesiderioAlCarrello(${prodotto.id})" class="btn btn-success">Aggiungi al Carrello</button>
                    </div>
                </div>
            `;

            listaContainer.appendChild(card);
        });

    } catch (error) {
        console.error("Errore nel recupero della lista desideri:", error);
        alert("Errore durante il recupero della lista desideri.");
    }
}

//Funzione per rimuovere il prodotto dalla lista desideri
async function rimuoviDaListaDesideri(prodottoId) {
    const token = localStorage.getItem("authToken");

    if (!token) {
        alert("Devi essere loggato per rimuovere prodotti dalla lista desideri!");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/listadesideri/rimuovi-desiderio/${prodottoId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${await response.text()}`);
        }

        alert("Prodotto rimosso dalla lista desideri!");
        visualizzaListaDesideri();  

    } catch (error) {
        console.error("Errore nella rimozione:", error);
        alert("Errore durante la rimozione del prodotto.");
    }
}


//Funzione per aggiungere dalla lista desideri al carrello
async function aggiungiDalDesiderioAlCarrello(prodottoId) {
    const token = localStorage.getItem("authToken");
    if (!token) {
        alert("Devi essere loggato per aggiungere prodotti al carrello!");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/listadesideri/aggiungi-dal-desiderio-al-carrello/${prodottoId}`, {
            method: "POST",
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${await response.text()}`);
        }

        alert("Prodotto aggiunto al carrello con successo!");
        visualizzaListaDesideri();  
    } catch (error) {
        console.error("Errore durante l'aggiunta al carrello:", error);
        alert("Errore durante l'aggiunta del prodotto al carrello.");
    }
}


//Navbar
window.addEventListener('scroll', function() {
    const header = document.getElementById('navbar');
        if (window.scrollY > 50) {
        header.classList.add('scrolled');
        } else {
    header.classList.remove('scrolled');
    }
});
