document.addEventListener("DOMContentLoaded", function () {
    visualizzaCarrello();
});

// Funzione per visualizzare il carrello
async function visualizzaCarrello() {
    const carrelloId = localStorage.getItem("carrelloId");
    const token = localStorage.getItem("authToken");

    if (!token) {
        alert("Devi essere loggato per visualizzare il carrello!");
        window.location.href = "/SerraLana/HTML/registrazione.html";
        return;
    }

    if (!carrelloId) {
        alert("Il tuo carrello è vuoto.");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/carrello/visualizza/${carrelloId}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${response.statusText}`);
        }

        const carrello = await response.json();
        console.log("Carrello ricevuto dal server:", carrello);

        renderCarrello(carrello);
    } catch (error) {
        console.error("Errore nel recupero del carrello:", error);
        alert("Errore nel recupero del carrello.");
    }
}

// Funzione per aggiornare il totale
function aggiornaTotale(carrello) {
    let subtotale = 0;
    let riepilogoHTML = "";

    carrello.prodottoCarrello.forEach(item => {
        let costoTotale = item.prodotto.prezzo * item.quantity;
        subtotale += costoTotale;
        riepilogoHTML += `<p>${item.quantity}x ${item.prodotto.titolo} - €${costoTotale.toFixed(2)}</p>`;
    });

    let tasse = subtotale * 0.22;
    let totale = subtotale + tasse;

    document.getElementById("riepilogoCarrello").innerHTML = riepilogoHTML;
    document.getElementById("subtotale").textContent = `€${subtotale.toFixed(2)}`;
    document.getElementById("tasse").textContent = `€${tasse.toFixed(2)}`;
    document.getElementById("totaleImporto").textContent = `€${totale.toFixed(2)}`;
}


// Funzione per mostrare i prodotti nel carrello
function renderCarrello(carrello) {
    const container = document.getElementById("prodottiAggiunti");
    container.innerHTML = "";

    if (!carrello.prodottoCarrello || carrello.prodottoCarrello.length === 0) { 
        container.innerHTML = "<p>Il tuo carrello è vuoto.</p>";
        return;
    }

    carrello.prodottoCarrello.forEach(item => {  
        const product = item.prodotto; 
        if (!product) return;

        const card = document.createElement("div");
        card.classList.add("card");
        card.style.width = "18rem";

        let imageSrc = product.immagine ? `data:image/jpeg;base64,${product.immagine}` : "/immagini/default.jpg";

        card.innerHTML = `
            <img class="card-img-top" src="${imageSrc}" alt="${product.titolo}">
            <div class="card-body">
                <h5 class="card-title">${product.titolo}</h5> 
                <p> Prezzo: ${product.prezzo} €</p>
                <p> Quantità: ${item.quantity} </p> 
                <button onclick="rimuoviDalCarrello(${product.id})" class="btn btn-danger">Rimuovi</button>
            </div>
        `;

        container.appendChild(card);
    });

    aggiornaTotale(carrello);
}

// Funzione per rimuovere un prodotto dal carrello
async function rimuoviDalCarrello(prodottoId) {
    const token = localStorage.getItem("authToken");
    const carrelloId = localStorage.getItem("carrelloId");

    if (!token) {
        alert("Devi essere loggato per rimuovere prodotti dal carrello!");
        return;
    }

    if (!carrelloId) {
        alert("Il carrello non esiste!");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/carrello/rimuovi/${prodottoId}/${carrelloId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        const textResponse = await response.text();
        console.log("Risposta del server:", textResponse);

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${textResponse}`);
        }

        alert("Prodotto rimosso dal carrello!");
        visualizzaCarrello();
    } catch (error) {
        console.error("Errore nella rimozione del prodotto:", error);
        alert("Errore nella rimozione del prodotto dal carrello.");
    }
}

// Funzione per creare un ordine
async function creaOrdine() {
    const token = localStorage.getItem("authToken");

    if (!token) {
        alert("Devi essere loggato per effettuare un ordine!");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/ordini/crea", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        const textResponse = await response.text();
        console.log("Risposta del server:", textResponse);

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${textResponse}`);
        }

        alert("Ordine creato con successo!");
        localStorage.removeItem("carrelloId");  
        window.location.reload();  
    } catch (error) {
        console.error("Errore nella creazione dell'ordine:", error);
        alert("Errore nella creazione dell'ordine.");
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