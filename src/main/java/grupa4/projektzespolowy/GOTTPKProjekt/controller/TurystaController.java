package grupa4.projektzespolowy.GOTTPKProjekt.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import grupa4.projektzespolowy.GOTTPKProjekt.model.Rola;
import grupa4.projektzespolowy.GOTTPKProjekt.model.Turysta;
import grupa4.projektzespolowy.GOTTPKProjekt.model.Uzytkownik;
import grupa4.projektzespolowy.GOTTPKProjekt.service.RolaService;
import grupa4.projektzespolowy.GOTTPKProjekt.service.TurystaService;
import grupa4.projektzespolowy.GOTTPKProjekt.service.UzytkownikService;
import grupa4.projektzespolowy.GOTTPKProjekt.service.TurystaServiceImpl;

@RestController
public class TurystaController 
{
	@Autowired
	private TurystaServiceImpl turystaServiceImpl;
	
	@Autowired
	private UzytkownikService uzytkownikService;
	
	@Autowired
	private RolaService rolaService;
	
	@GetMapping("/turysci")
    public ModelAndView getAllProduct() {

        ModelAndView modelAndView = new ModelAndView("turysta/turysta");
        modelAndView.addObject("turysci", turystaServiceImpl.getAllTurysta());
        return modelAndView;
    }
	
	 
	  @GetMapping("/turysta/addForm")
	    public ModelAndView addformTurysta() 
	  	{

	        ModelAndView modelAndView = new ModelAndView("turysta/addForm");
	        return modelAndView;
	    }
	  
	  @GetMapping("/turysta/updateForm/?")
	    public ModelAndView updateformTurysta() 
	  	{

	        ModelAndView modelAndView = new ModelAndView("turysta/updateForm");
	        return modelAndView;
	    }
	 
	  @PostMapping("/turysta/dodaj")
	    public void createTurysta(@RequestParam(value="imie") String imie,
	                                  @RequestParam(value="nazwisko") String nazwisko,
	                                  @RequestParam(value="opis") String opis,
	                                  @RequestParam(value="punkty") int punkty,
	                                  @RequestParam(value="telefon") String telefon,
	                                  @RequestParam(value="login") String login,
	                                  @RequestParam(value="haslo") String haslo,
	                                  @RequestParam(value="email") String email,
	                                  HttpServletResponse httpResponse ) throws IOException {

	        Rola rola = rolaService.getOneByName("Turysta");
	        Uzytkownik uzytkownik = new Uzytkownik(login, haslo, email,rola); // tworze użytkownika z referencją do pobranej roli
	        rola.getUzytkownicy().add(uzytkownik); // dodaj użytkownika do roli (relacja jeden do wielu)
	        Turysta turysta = new Turysta(imie, nazwisko,opis,uzytkownik,telefon,punkty); // stwórz turyste z utworzonym użytkownikiem

	        turystaServiceImpl.createTurysta(turysta); // puść inserta do bazy
	        // UWAGA! kolejność operacji jest ważna.


	        //redirectAttributes.addFlashAttribute("WIADOMOSC", "wiadomosc");
	        httpResponse.sendRedirect("/turysci");
	    }
	  
	   @PostMapping("/turysta/update/{id_turysta}")
	    public ModelAndView updateTurysta(@RequestParam(value="imie") String imie,
	                                 @RequestParam(value="nazwisko") String nazwisko,
	                                 @RequestParam(value="telefon") String telefon,
	                                 @RequestParam(value="login") String login,
	                                 @RequestParam(value="haslo", required = false) String haslo,
	                                 @RequestParam(value="email") String email,
	                                 @PathVariable Integer id_turysta,
	                                 HttpServletResponse httpResponse ) throws IOException {

	        Turysta turysta = turystaServiceImpl.getOneById(id_turysta);

	        turysta.setImie(imie);
	        turysta.setNazwisko(nazwisko);
	        turysta.setTelefon(telefon);
	        turysta.getUzytkownik().setLogin(login);
	        turysta.getUzytkownik().setHaslo(haslo);
	        turysta.getUzytkownik().setEmail(email);

	       turystaServiceImpl.createTurysta(turysta);

	        httpResponse.sendRedirect("/turysci");
        ModelAndView modelAndView = new ModelAndView("turysta");
        modelAndView.addObject("turysta", turystaServiceImpl.getAllTurysta());
        return modelAndView;
    }
	
	 @PostMapping("/turysta")
	    public ResponseEntity <Turysta> createProduct(@RequestBody Turysta turysta) {
	        return ResponseEntity.ok().body(this.turystaServiceImpl.createTurysta(turysta));
	    }
	
}
