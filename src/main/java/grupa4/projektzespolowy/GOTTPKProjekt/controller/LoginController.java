package grupa4.projektzespolowy.GOTTPKProjekt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController 
{
	@GetMapping("/logowanie")
	public String showLogForm()
	{
		return "uzytkownik/logowanieForm";
	}
}
