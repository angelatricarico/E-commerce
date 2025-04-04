document.addEventListener("DOMContentLoaded", visualizzaOrdini);

// Funzione per visualizzare tutti gli ordini dell'utente
async function visualizzaOrdini() {
    const token = localStorage.getItem("authToken");

    if (!token) {
        alert("Devi essere loggato per visualizzare gli ordini!");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/ordini", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${response.statusText}`);
        }

        const textResponse = await response.text();   
        console.log("Risposta del server:", textResponse);  

        try {
            const ordini = JSON.parse(textResponse);   
            renderOrdini(ordini);
        } catch (error) {
            console.error("Errore nel parsing JSON:", error);
            alert("La risposta del server non è un JSON valido.");
        }

    } catch (error) {
        console.error("Errore nel recupero degli ordini:", error);
        alert("Errore nel recupero degli ordini.");
    }
}


// Funzione per mostrare gli ordini
function renderOrdini(ordini) {
    const container = document.getElementById("listaOrdini");
    container.innerHTML = "";

    if (ordini.length === 0) {
        container.innerHTML = "<p>Nessun ordine trovato.</p>";
        return;
    }

    ordini.forEach(ordine => {
        const ordineDiv = document.createElement("div");
        ordineDiv.classList.add("card", "mb-3", "p-3");

        ordineDiv.innerHTML = `
            <h5>Ordine #${ordine.id}</h5>
            <p>Data: ${new Date(ordine.dataCreazione).toLocaleString()}</p>
            <p>Stato: <strong>${ordine.status}</strong></p>
            <ul>
                <li>${ordine.titoli}</li>
            </ul>
        `;

        container.appendChild(ordineDiv);
    });
}





