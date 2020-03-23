package grupa4.projektzespolowy.GOTTPKProjekt.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import grupa4.projektzespolowy.GOTTPKProjekt.model.Rola;
import grupa4.projektzespolowy.GOTTPKProjekt.model.Turysta;
import grupa4.projektzespolowy.GOTTPKProjekt.model.Uzytkownik;
import grupa4.projektzespolowy.GOTTPKProjekt.service.RolaService;
import grupa4.projektzespolowy.GOTTPKProjekt.service.UzytkownikServiceImpl;
import grupa4.projektzespolowy.GOTTPKProjekt.service.TurystaServiceImpl;

@Controller
public class TurystaController
{
	@Autowired
	private TurystaServiceImpl turystaServiceImpl;

	@Autowired
	private UzytkownikServiceImpl uzytkownikServiceImpl;

	@Autowired
	private RolaService rolaService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/turysci")
	public ModelAndView getAllProduct(Authentication authentication) {

		ModelAndView modelAndView = new ModelAndView("turysta/turysta");
		modelAndView.addObject("turysci", turystaServiceImpl.getAllTurysta());
		modelAndView.addObject("LoggedUser", authentication);

		return modelAndView;
	}


	@GetMapping({"/turysci/addForm", "/rejestracja/turysta"})
	public ModelAndView addformTurysta(Authentication authentication)
	{
		ModelAndView modelAndView = new ModelAndView("turysta/addForm");
		modelAndView.addObject("LoggedUser", authentication);
		return modelAndView;
	}

	@GetMapping("/turysci/updateForm")
	public String updateformTurysta(@RequestParam(value="id") Integer idTurysta,
									Model model,
									Authentication authentication)
	{

		model.addAttribute("LoggedUser", authentication);

		Turysta turysta = turystaServiceImpl.getOneById(idTurysta);
		Uzytkownik uzytkownik = turysta.getUzytkownik();
		model.addAttribute("turysta", turysta);
		model.addAttribute("uzytkownik", uzytkownik);

		return "turysta/updateForm";
	}

	@PostMapping("/turysci/dodaj")
	public String createTurysta(  @RequestParam(value="imie") String imie,
								  @RequestParam(value="nazwisko") String nazwisko,
								  @RequestParam(value="opis") String opis,
								  @RequestParam(value="punkty") int punkty,
								  @RequestParam(value="telefon") String telefon,
								  @RequestParam(value="login") String login,
								  @RequestParam(value="haslo") String haslo,
								  @RequestParam(value="email") String email,
								  RedirectAttributes redirectAttributes,
								  Authentication authentication) {

		Rola rola = rolaService.getOneByName("ROLE_turysta");
		Uzytkownik uzytkownik = new Uzytkownik(login, passwordEncoder.encode(haslo), email,rola); // tworze użytkownika z referencją do pobranej roli
		rola.getUzytkownicy().add(uzytkownik); // dodaj użytkownika do roli (relacja jeden do wielu)
		Turysta turysta = new Turysta(imie, nazwisko,telefon,uzytkownik,opis,punkty); // stwórz turyste z utworzonym użytkownikiem

		turystaServiceImpl.createTurysta(turysta); // puść inserta do bazy
		// UWAGA! kolejność operacji jest ważna.


		redirectAttributes.addFlashAttribute("wiadomosc", "Dodane turyste pomyślnie");
		if(authentication != null)
			return "redirect:/turysci";
		else
			return "redirect:/login";
	}

	@PostMapping("/turysci/update/{idTurysta}")
	public String updateTurysta(@RequestParam(value="imie") String imie,
								@RequestParam(value="nazwisko") String nazwisko,
								@RequestParam(value="telefon") String telefon,
								@RequestParam(value="login") String login,
								@RequestParam(value="opis") String opis,
								@RequestParam(value="punkty") int punkty,
								@RequestParam(value="haslo", required = false) String haslo,
								@RequestParam(value="email") String email,
								@PathVariable Integer idTurysta,
								RedirectAttributes redirectAttributes) throws IOException {

		Turysta turysta = turystaServiceImpl.getOneById(idTurysta);

		turysta.setImie(imie);
		turysta.setNazwisko(nazwisko);
		turysta.setTelefon(telefon);
		turysta.setOpis(opis);
		turysta.setPunkty(punkty);
		turysta.getUzytkownik().setLogin(login);
		if(haslo != "")
			turysta.getUzytkownik().setHaslo(passwordEncoder.encode(haslo));
		turysta.getUzytkownik().setEmail(email);

		turystaServiceImpl.createTurysta(turysta);
		redirectAttributes.addFlashAttribute("wiadomosc", "Zmiany zostały przyjęte pomyślnie");
		return "redirect:/turysci";
	}

	@GetMapping("/turysci/usun/{idTurysta}") // usuń turyste wraz z jego kontem użytkownika
	public String removeTurysta(@PathVariable Integer idTurysta,
								RedirectAttributes redirectAttributes) throws IOException {

		Turysta turysta = turystaServiceImpl.getOneById(idTurysta); // pobieram turyste po odebranym id
		Uzytkownik uzytkownik = turysta.getUzytkownik(); // pobieram uzytkownika przypisanego do turysty
		turysta.setUzytkownik(null); // usuwam referencje do rodzica

		uzytkownikServiceImpl.removeUzytkownik(uzytkownik.getIdUzytkownik()); // usuwam użytkownika i od razu kaskadowo usuwa się turysta.
		turystaServiceImpl.removeTurysta(idTurysta);

		redirectAttributes.addFlashAttribute("wiadomosc", "Usunięto turyste pomyślnie");
		return "redirect:/turysci";
	}

}
