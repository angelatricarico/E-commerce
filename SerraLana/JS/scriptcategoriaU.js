// Funzioni per ottenere e visualizzare i prodotti filtrati per categoria desiderata

async function getProdottiByCategoria(categoria) {
    const url = `http://localhost:8080/prodotti/categoria/${encodeURIComponent(categoria)}`;
    
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${response.statusText}`);
        }
        
        const prodotti = await response.json();
        renderProdotti(prodotti);
    } catch (error) {
        console.error("Errore durante il recupero dei prodotti:", error);
    }
}


function renderProdotti(prodotti) {
    const container = document.getElementById("prodotti");
    container.innerHTML = ""; 
    
    container.style.padding = "20px"; 
    
    prodotti.forEach(product => {
        const card = document.createElement("div");

        if (product.immagine) { 
            imageSrc = `data:image/jpeg;base64,${product.immagine}`;
        }

        card.innerHTML = `
            <a href="prodotto.html?id=${product.id}" class="card-link">
                <div class="card" style="width: 13rem;">                        
                    <img class="card-img-top" src="${imageSrc}" alt="${product.titolo}">
                    <div class="card-body">
                        <h5 class="card-title">${product.titolo}</h5> 
                        <button class="btn btn-dark" id="vediDettagli">Vedi Dettagli</button>
                    </div>
                </div>
            </a>
        `;
        container.appendChild(card);
    });
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