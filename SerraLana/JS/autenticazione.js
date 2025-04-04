//Funzione per effettuare log in
function login(username, password) {
    fetch('http://localhost:8080/api/login', { 
        method: 'POST',
        headers: {
          'Content-Type': 'application/json' 
        },
        body: JSON.stringify({ username, password }) 
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Login fallito');
        }
        return response.json();
    })
    .then(data => {
        console.log('Login effettuato:', data);
        window.alert("Login effettuato.");
        location.replace("http://127.0.0.1:3000/SerraLana/HTML/profilo2.html")

        if(data.token) {
          localStorage.setItem("authToken", data.token);
        }
    })
    .catch(error => {
        console.error('Errore nel login:', error);
        window.alert("Login errato.");

    });
  }


    //DOM LOG IN
      document.getElementById('loginForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        login(username, password);
      });


      function getAuthHeaders() {
        const token = localStorage.getItem("authToken");
        return token ? { 'Authorization': 'Bearer ' + token } : {};
      }


    // Funzione per formattare la data in formato YYYY-MM-DD
    function formatDate(date) {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = ("0" + (d.getMonth() + 1)).slice(-2); 
      const day = ("0" + d.getDate()).slice(-2);
      return `${year}-${month}-${day}`; 
    }

// Funzione per registrarsi
function addUser(newUser) {
  newUser.birthday = formatDate(newUser.birthday); 

  fetch('http://localhost:8080/utente', { 
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...getAuthHeaders()
    },
    body: JSON.stringify(newUser)
  })
  .then(response => {
    if (!response.ok) {
      return response.json().then(err => {
        console.error('Errore API:', err); 
        throw new Error(`Errore: ${err.message || 'Errore durante l\'aggiunta dell\'utente'}`);
      });
    }
    return response.json();
  })
  .then(data => {
    console.log('Registrazione effettuata:', data);
    window.alert("Registrazione effettuata.");
    window.location.href = "/SerraLana/HTML/profilo2.html"; 
  })
  .catch(error => {
    console.error('Errore nell\'aggiunta dell\'utente:', error);
    window.alert("Registrazione non andata a buon fine.");
  });
}

// DOM REGISTRAZIONE
document.getElementById('addUserForm').addEventListener('submit', function(event) {
  event.preventDefault();

  const newUser = {
    nome: document.getElementById('newUserName').value.trim(),
    cognome: document.getElementById('newUserSurname').value.trim(),
    username: document.getElementById('newUserName2').value.trim(),
    dataNascita: formatDate(document.getElementById('newUserBirthday').value.trim()), 
    email: document.getElementById('newUserEmail').value.trim(),
    password: document.getElementById('newUserPassword').value.trim(),
    numeroCarta: document.getElementById('cardNumber').value.trim(),
    scadenzaCarta: formatExpiry(document.getElementById('cardExpiry').value.trim()), 
    cvcCarta: document.getElementById('cardCVC').value.trim()
};

function formatExpiry(date) {
    const parts = date.split("-");
    if (parts.length === 2) {
        return `${parts[1]}/${parts[0].slice(-2)}`; 
    }
    return date;
}

console.log("Dati inviati:", newUser);


  if (!newUser.password) {
    alert("Inserisci una password per la registrazione.");
    return;  
  }

  if (!newUser.numeroCarta || !newUser.scadenzaCarta || !newUser.cvcCarta) {
    alert("Inserisci tutte le informazioni relative al metodo di pagamento.");
    return;
}


  addUser(newUser);
});

  



//Navbar
  window.addEventListener('scroll', function() {
    const header = document.getElementById('navbar');
      if (window.scrollY > 50) {
        header.classList.add('scrolled');
      } else {
        header.classList.remove('scrolled');
      }
  });

