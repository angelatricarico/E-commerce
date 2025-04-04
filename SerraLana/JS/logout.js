document.addEventListener("DOMContentLoaded", function () {
    getUserInfo();

    const updateButton = document.getElementById("updateButton");
    if (updateButton) {
        updateButton.addEventListener("click", handleUpdateButtonClick);
    }

    const deleteButton = document.getElementById("deleteAccountButton");
    if (deleteButton) {
        deleteButton.addEventListener("click", deleteAccount);
    }

    const logoutButton = document.getElementById("logoutButton");
    if (logoutButton) {
        logoutButton.addEventListener("click", logout);
    }

    const togglePasswordButton = document.getElementById("togglePassword");
    if (togglePasswordButton) {
        togglePasswordButton.addEventListener("click", togglePasswordVisibility);
    }

    const ordersButton = document.getElementById("ordersButton");
    if (ordersButton) {
        ordersButton.addEventListener("click", function () {
            window.location.href = "/SerraLana/HTML/ordini.html";
        });
    }
});


// Funzione per caricare l'account
function getUserInfo() {
    const token = localStorage.getItem("authToken");

    if (!token) {
        alert("Devi essere loggato per visualizzare il profilo!");
        window.location.href = "/SerraLana/HTML/registrazione.html";
        return;
    }

    fetch('http://localhost:8080/utente', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => response.ok ? response.json() : Promise.reject("Errore nel recupero delle informazioni"))
    .then(data => {
        document.getElementById("username").value = data.username;
        document.getElementById("email").value = data.email;
        document.getElementById("nome").value = data.nome;
        document.getElementById("cognome").value = data.cognome;
        document.getElementById("indirizzo").value = data.indirizzo;
        document.getElementById("dataNascita").value = data.dataNascita;
        document.getElementById("numeroCarta").value = data.numeroCarta || "";
        document.getElementById("scadenzaCarta").value = data.scadenzaCarta || "";
        document.getElementById("cvcCarta").value = "•••";  
        

        const passwordInput = document.getElementById("password");
        passwordInput.value = "********";
        passwordInput.setAttribute("data-original", data.password);
    })
    .catch(error => {
        console.error(error);
        window.location.href = "/SerraLana/HTML/registrazione.html";
    });
}

// Alterna la visibilità della password
function togglePasswordVisibility() {
    const passwordInput = document.getElementById("password");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        passwordInput.value = passwordInput.getAttribute("data-original");
        this.classList.replace("fa-eye", "fa-eye-slash");
    } else {
        passwordInput.type = "password";
        passwordInput.value = "********";
        this.classList.replace("fa-eye-slash", "fa-eye");
    }
}

// Funzione per abilitare/disabilitare i campi input
function toggleFormFields(enable) {
    document.querySelectorAll("#profileForm input").forEach(input => {
        if (input.id !== "email") {   
            input.disabled = !enable;
            if (enable) {
                input.style.backgroundColor = "#333";  
                input.style.color = "white";  
            } else {
                input.style.backgroundColor = "";  
                input.style.color = "";  
            }
        }
    });
}


// Variabile per controllare lo stato del pulsante
let isEditing = false;

// Funzione per abilitare il bottone Modifica o Salva
function handleUpdateButtonClick() {
    const updateButton = document.getElementById("updateButton");

    if (!isEditing) {
        toggleFormFields(true);
        updateButton.innerText = "SALVA";
        isEditing = true;
    } else {
        updateUserInfo().then(success => {
            if (success) {
                toggleFormFields(false);
                updateButton.innerText = "MODIFICA";
                isEditing = false;
            }
        });
    }
}

// Funzione per modificare e salvare i campi
function updateUserInfo() {
    return new Promise((resolve, reject) => {
        const token = localStorage.getItem("authToken");
        if (!token) {
            console.error("Nessun token trovato nel localStorage");
            reject(false);
            return;
        }

        const passwordField = document.getElementById("password");
        const passwordValue = passwordField.value !== "********" ? passwordField.value.trim() : "";

        const userData = {
            username: document.getElementById("username").value.trim(),
            email: document.getElementById("email").value.trim(),
            nome: document.getElementById("nome").value.trim(),
            cognome: document.getElementById("cognome").value.trim(),
            indirizzo: document.getElementById("indirizzo").value.trim(),
            dataNascita: document.getElementById("dataNascita").value || null
        };

        if (passwordValue !== "") {
            userData.password = passwordValue;
        }

        const numeroCarta = document.getElementById("numeroCarta").value.trim();
        const scadenzaCarta = document.getElementById("scadenzaCarta").value.trim();
        const cvcCarta = document.getElementById("cvcCarta").value.trim();

        // Aggiunge i dati della carta solo se tutti i campi sono compilati (e il CVC non è "•••")
        if (numeroCarta && scadenzaCarta) {
            userData.numeroCarta = numeroCarta;
            userData.scadenzaCarta = scadenzaCarta;
            // Aggiungi il CVC solo se è stato cambiato (non "•••")
            if (cvcCarta !== "•••" && cvcCarta !== "") {
                userData.cvcCarta = cvcCarta;
            }
        }

        Object.keys(userData).forEach(key => {
            if (!userData[key]) delete userData[key];
        });

        console.log("Dati inviati al server:", userData);

        fetch('http://localhost:8080/utente', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(userData)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    console.error("Errore JSON dal server:", err);
                    throw new Error("Errore durante l'aggiornamento");
                });
            }
            return response.json();
        })
        .then(() => {
            alert('Utente aggiornato con successo');
            resolve(true);
        })
        .catch(error => {
            console.error('Errore durante aggiornamento:', error);
            alert("Errore durante l'aggiornamento.");
            reject(false);
        });
    });
}



// Funzione per eliminare l'account
function deleteAccount() {
    const token = localStorage.getItem("authToken");
    if (!token) return;

    if (!confirm("Sei sicuro di voler eliminare il tuo account? Questa azione è irreversibile!")) {
        return;
    }

    fetch('http://localhost:8080/utente', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (response.ok) {
            localStorage.removeItem("authToken");
            alert("Account eliminato con successo.");
            location.replace("/SerraLana/HTML/home.html");
        } else {
            return response.json();
        }
    })
    .then(data => {
        if (data && data.message) {
            alert("Errore: " + data.message);
        }
    })
    .catch(error => {
        console.error("Errore durante l'eliminazione dell'account:", error);
        alert("Errore durante l'eliminazione dell'account.");
    });
}

// Funzione per effettuare il log out
function logout() {
    const token = localStorage.getItem("authToken");
    if (!token) return;

    fetch('http://localhost:8080/api/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Logout fallito");

        localStorage.removeItem("authToken");
        alert("Logout effettuato.");
        location.replace("/SerraLana/HTML/home.html");
    })
    .catch(error => {
        console.error("Errore durante il logout:", error);
        alert("Errore durante il logout.");
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

