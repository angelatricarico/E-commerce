
function getQueryParam(param) {
    const params = new URLSearchParams(window.location.search); 
    return params.get(param);
}

//Funzione per caricare il prodotto
document.addEventListener("DOMContentLoaded", function () {
    function getQueryParam(param) {
        const params = new URLSearchParams(window.location.search);
        return params.get(param);
    }

    const productId = getQueryParam("id");

    if (!productId) {
        document.getElementById("productDetail").innerHTML =
            "<p>Errore: Nessun prodotto specificato.</p>";
    } else {
        fetch("http://localhost:8080/prodotti/" + productId)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Errore nella risposta del server");
                }
                return response.json();
            })
            .then((product) => {
                let imageSrc = product.immagine
                    ? `data:image/jpeg;base64,${product.immagine}`
                    : "/immagini/default.jpg";

                document.getElementById("productDetail").innerHTML = `
                    <div class="image-container">
                        <div class="zoom-lens"></div>
                        <img src="${imageSrc}" id="imgProdotto" class="zoom-image">
                    </div>
                    <div class="card-body">
                        <h1 class="product-title">${product.titolo}</h1> 
                        <p class="description"><strong>Descrizione:</strong> ${product.descrizione}</p>
                        <p class="price">Prezzo: €${product.prezzo}</p>
                    <div class="button-container">
                        <button class="btn btn-dark" id="bottoneCarrello">Aggiungi al Carrello</button>
                        <button class="btn btn-outline-dark" id="bottoneWishlist">Aggiungi alla Lista Desideri</button>
                    </div>                        
                    <p class="small-text"><span>Consegna standard: </span> 2-5 giorni lavorativi</p>
                    </div>
                `;

                document.getElementById("bottoneCarrello").addEventListener("click", aggiungiAlCarrello);
                document.getElementById("bottoneWishlist").addEventListener("click", aggiungiAllaListaDesideri);

                // Funzione per lo zoom
                const img = document.querySelector(".zoom-image");
                const lens = document.querySelector(".zoom-lens");

                // Imposta lo sfondo della lente con l'immagine originale
                lens.style.backgroundImage = `url('${imageSrc}')`;
                lens.style.backgroundSize = `${img.width * 2}px ${img.height * 2}px`;

                img.addEventListener("mousemove", function (e) {
                    zoomImage(e, img, lens);
                });

                lens.addEventListener("mousemove", function (e) {
                    zoomImage(e, img, lens);
                });

                img.addEventListener("mouseleave", function () {
                    lens.style.display = "none";
                });

                img.addEventListener("mouseenter", function () {
                    lens.style.display = "block";
                });

                function zoomImage(event, image, lens) {
                    let bounds = image.getBoundingClientRect();
                    let x = event.clientX - bounds.left;
                    let y = event.clientY - bounds.top;

                    let lensSize = 100; 
                    let lensX = x - lensSize / 2;
                    let lensY = y - lensSize / 2;

                    // Limiti per la lente
                    if (lensX < 0) lensX = 0;
                    if (lensY < 0) lensY = 0;
                    if (lensX > bounds.width - lensSize) lensX = bounds.width - lensSize;
                    if (lensY > bounds.height - lensSize) lensY = bounds.height - lensSize;

                    lens.style.left = lensX + "px";
                    lens.style.top = lensY + "px";
                    lens.style.backgroundPosition = `-${lensX * 2}px -${lensY * 2}px`;
                }
            })
            .catch((error) => {
                document.getElementById("productDetail").innerHTML =
                    "<p>Errore nel recupero del prodotto.</p>";
                console.error("Errore:", error);
            });
    }
});


// Funzioni per aggiungere al carrello e lista desideri
async function aggiungiAlCarrello() {
    const prodottoId = getQueryParam("id");
    const token = localStorage.getItem("authToken"); 

    if (!token) {
        alert("Devi essere loggato per aggiungere prodotti al carrello!");
        window.location.href = "/SerraLana/HTML/registrazione.html";
        return;
    }

    if (!prodottoId) {
        alert("Errore: ID prodotto non trovato.");
        return;
    }

    const requestBody = {
        prodottoId: parseInt(prodottoId),
        quantity: 1
    };
    

    try {
        const response = await fetch(`http://localhost:8080/carrello/aggiungi`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(requestBody)
        });

        if (response.status === 401) {
            alert("Sessione scaduta, effettua di nuovo il login.");
            window.location.href = "/SerraLana/HTML/registrazione.html"; 
            return;
        }

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${await response.text()}`);
        }

        const data = await response.json();
        console.log("Risposta dal server:", data);

        if (data.carrelloId) {
            localStorage.setItem("carrelloId", data.carrelloId);
            console.log("✅ Carrello ID salvato:", data.carrelloId);
        }

        alert("Prodotto aggiunto al carrello con successo!");
    } catch (error) {
        console.error("Errore nell'aggiunta al carrello:", error);
        alert("Errore durante l'aggiunta al carrello.");
    }
}

async function aggiungiAllaListaDesideri() {
    const prodottoId = getQueryParam("id");
    const token = localStorage.getItem("authToken");  

    if (!token) {
        alert("Devi essere loggato per aggiungere prodotti alla lista desideri!");
        window.location.href = "/SerraLana/HTML/registrazione.html";
        return;
    }

    if (!prodottoId) {
        alert("Errore: ID prodotto non trovato.");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/listadesideri/aggiungi-desiderio/${prodottoId}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`  
            }
        });

        if (response.status === 401) {
            alert("Sessione scaduta, effettua di nuovo il login.");
            window.location.href = "/login.html";
            return;
        }

        if (response.status === 409) {
            alert("Prodotto già presente nella lista desideri.");
            return;
        }

        if (!response.ok) {
            throw new Error(`Errore ${response.status}: ${await response.text()}`);
        }

        alert("Prodotto aggiunto alla lista desideri con successo!");
    } catch (error) {
        console.error("Errore nell'aggiunta alla lista desideri:", error);
        alert("Errore durante l'aggiunta alla lista desideri.");
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