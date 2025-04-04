package com.example.serraLana;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.serraLana.model.CategoriaEnum;
import com.example.serraLana.model.Prodotto;
import com.example.serraLana.repository.ProdottoRepository;

@Component 
public class DataLoader implements CommandLineRunner {

    private final ProdottoRepository prodottoRepository;
    
    private static final String IMAGE_DIR = "C:/images";


    public DataLoader(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Prodotto> prodottiEsistenti = prodottoRepository.findAll();
        if (prodottiEsistenti.isEmpty()) {
            caricaProdottiPredefiniti();
        } else {
            System.out.println("I prodotti sono già presenti nel database.");
        }
    }

	private void caricaProdottiPredefiniti() {
	    List<Prodotto> prodotti = new ArrayList<>();
	
	    prodotti.add(creaProdotto("MAGLIETTA CON RICAMO",
	            "Maglietta relaxed fit. Collo rotondo e maniche corte. Ricamo sul davanti combinato a contrasto con stampa sulla schiena.",
	            22.95, 100, CategoriaEnum.MASCHILE, "prodotto3_maschile.jpg"));
	
	    prodotti.add(creaProdotto("FELPA RELAXED FIT A CONTRASTO",
	            "Felpa relaxed fit. Collo con cappuccio regolabile e maniche lunghe. Dettaglio di profilo combinato a contrasto. Finiture a coste.",
	            45.95, 100, CategoriaEnum.MASCHILE, "prodotto2_maschile.jpg"));
	
	    prodotti.add(creaProdotto("POLO CON STAMPA A RIGHE",
	            "Polo relaxed fit realizzata in tessuto compatto con elasticità. Collo polo con chiusura frontale a bottoni. Manica lunga.",
	            39.95, 100, CategoriaEnum.MASCHILE, "prodotto4_maschile.jpg"));
	
	    prodotti.add(creaProdotto("CAMICIA DENIM CON TASCHE",
	            "Camicia relaxed fit realizzata in denim di cotone. Colletto a revers e maniche lunghe con polsini con bottoni. Tasche applicate con patta sul petto. Effetto lavato. Chiusura frontale con abbottonatura.",
	            59.95, 100, CategoriaEnum.MASCHILE, "prodotto1_maschile.jpg"));
	
	    prodotti.add(creaProdotto("STIVALE CON IMBRAGATURA",
	            "Stivale realizzato in pelle di alta qualità. Imbragatura decorativa all'altezza della caviglia. Fodera interna e soletta in pelle. Forma squadrata. Suola in tinta con leggero tacco.",
	            279.95, 100, CategoriaEnum.MASCHILE, "prodotto5_maschile.jpg"));
	
	    prodotti.add(creaProdotto("TRENCH CORTO CON CAPPUCCIO",
	            "Trench corto con collo a revers, cappuccio e maniche lunghe rifinite con linguetta e bottone. Tasche anteriori a filetto. Chiusura anteriore incrociata con bottoni.",
	            49.99, 100, CategoriaEnum.FEMMINILE, "prodotto4_femminile.jpg"));
	
	    prodotti.add(creaProdotto("VESTITO CON SCOLLO DORATO",
	            "Vestito morbido senza maniche con applicazione dorata sulla spalla. Pieghe laterali in vita.",
	            39.99, 100, CategoriaEnum.FEMMINILE, "prodotto5_femminile.jpg"));
	
	    prodotti.add(creaProdotto("SANDALI CON TACCO IN PELLE",
	            "Sandalo con tacco in pelle. Listini incrociati sulla parte anteriore. Tacco largo alto. Chiusura con cinturino posteriore con fibbia laterale. Punta tonda.",
	            69.99, 100, CategoriaEnum.FEMMINILE, "prodotto3_femminile.jpg"));
	
	    prodotti.add(creaProdotto("GONNA MIDI A CAPPA",
	            "Gonna a vita alta con cintura e pieghe anteriori. Fondo svasato. Chiusura posteriore con cerniera nascosta nella cucitura.",
	            79.99, 100, CategoriaEnum.FEMMINILE, "prodotto1_femminile.jpg"));
	
	    prodotti.add(creaProdotto("ORECCHINI CON FIORE IN RILIEVO",
	            "Orecchini metallici a forma di fiore in rilievo a colori. Chiusura con farfallina e clip.",
	            19.99, 100, CategoriaEnum.FEMMINILE, "prodotto2_femminile.jpg"));
	
	    prodottoRepository.saveAll(prodotti);
	    System.out.println("Prodotti predefiniti caricati nel database.");
	}


	private Prodotto creaProdotto(String titolo, String descrizione, double prezzo, int quantita, CategoriaEnum categoria, String nomeImg) {
	    Prodotto prodotto = new Prodotto();
	    prodotto.setTitolo(titolo);
	    prodotto.setDescrizione(descrizione);
	    prodotto.setPrezzo(prezzo);
	    prodotto.setQuantitaMax(quantita);
	    prodotto.setCategoria(categoria);
	    prodotto.setNomeImg(nomeImg);
	
	    try {
	        Path imagePath = Paths.get(IMAGE_DIR, nomeImg);
	        byte[] imageBytes = Files.readAllBytes(imagePath);
	        prodotto.setImmagine(imageBytes);
	    } catch (IOException e) {
	        prodotto.setImmagine(new byte[0]); 
	        System.err.println("Errore nel caricamento dell'immagine: " + nomeImg + " -> " + e.getMessage());
	    }
	
	    return prodotto;
	}
}
