package grupa4.projektzespolowy.GOTTPKProjekt.controller;

import grupa4.projektzespolowy.GOTTPKProjekt.model.Odcinek;
import grupa4.projektzespolowy.GOTTPKProjekt.model.Trasa;
import grupa4.projektzespolowy.GOTTPKProjekt.model.TrasaOdcinek;
import grupa4.projektzespolowy.GOTTPKProjekt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TrasaOdcinekController {

    @Autowired
    private TrasaServiceImpl trasaService;

    @Autowired
    private OdcinekServiceImpl odcinekService;

    @Autowired
    private TrasaOdcinekServiceImpl trasaOdcinekService;

    @GetMapping("/trasa_odcinki/konfiguruj/{idTrasa}")
    public String konfigurujOdcinkiTrasy(@PathVariable int idTrasa,
                                         Model model,
                                         Authentication authentication) {

        List<TrasaOdcinek> odcinkiTrasy = trasaOdcinekService.getAllOdcinkiByIdTrasa(idTrasa);
        Trasa trasa = trasaService.getOneById(idTrasa);
        int dubelTras = trasa.getDubel();

        List<Odcinek> odcinki;
        if (odcinkiTrasy.size() == 0) {
            odcinki = odcinekService.getAllByOtwartyAndIdPasmo(1, trasa.getPasmo().getIdPasmo());
        } else {
            if (odcinkiTrasy.get(odcinkiTrasy.size() - 1).getPowrot() == 1) {
                odcinki = odcinekService.getAllByPunktPoczatkowyOrPunktKoncowyAndOtwarty(odcinkiTrasy.get(odcinkiTrasy.size() - 1).getOdcinek().getPunktPoczatkowy(),
                        odcinkiTrasy.get(odcinkiTrasy.size() - 1).getOdcinek().getPunktPoczatkowy(), 1);
            } else {
                odcinki = odcinekService.getAllByPunktPoczatkowyOrPunktKoncowyAndOtwarty(odcinkiTrasy.get(odcinkiTrasy.size() - 1).getOdcinek().getPunktKoncowy(),
                        odcinkiTrasy.get(odcinkiTrasy.size() - 1).getOdcinek().getPunktKoncowy(), 1);
                List<Odcinek> odcinkiToRemove = odcinekService.getAllByIdPunktKoncowyAndPunktyDo(odcinkiTrasy.get(odcinkiTrasy.size() - 1).getOdcinek().getPunktKoncowy(), 0);
                // Filtracja odcinków do których nie da się "zejść" z naszego punktu końcowego
                odcinki = odcinki.stream().filter(aObject ->
                        !odcinkiToRemove.contains(aObject)).collect(Collectors.toList());
            }
        }
        //odcinkiTrasy.get(0).getOdcinek().getPunktKoncowy().getIdpunkt();

        model.addAttribute("odcinkiTrasy", odcinkiTrasy);
        model.addAttribute("trasa", trasa);
        model.addAttribute("dubelTras", dubelTras);
        model.addAttribute("odcinki", odcinki);
        model.addAttribute("LoggedUser", authentication);

        return "trasa/odcinkiTrasy";
    }

//    @PostMapping({"/trasa_odcinki/dodaj/{idLastPunktKon}", "/trasa_odcinki/dodaj/"})
//    public String createTrasaOdcinek(@ModelAttribute TrasaOdcinek trasaOdcinek,
//                                     @PathVariable(required = false) Integer idLastPunktKon,
//                                     HttpServletRequest request) {
//
//        // zrobic pole powrot, cos sie pierdoli jak za pierwszym razem wybierzemy dwa te same odcinki, potem ok?
//        Odcinek pierwszyOdcinekTrasy = null;
//        List<TrasaOdcinek> odcinkiTejTrasy = trasaOdcinekService.getAllOdcinkiByIdTrasa(trasaOdcinek.getTrasa().getIdTrasa());
//        if (odcinkiTejTrasy.size() > 0) {
//            pierwszyOdcinekTrasy = odcinkiTejTrasy.get(0).getOdcinek();
//        }
//
//        int wystapienTejTrasy = (trasaOdcinekService.getAllByIdOdcinekAndIdTrasa(trasaOdcinek.getOdcinek().getIdOdcinek(), trasaOdcinek.getTrasa().getIdTrasa())).size();
//        if (wystapienTejTrasy > 2)
//            trasaOdcinek.getTrasa().setDubel(1);
//
//        if (idLastPunktKon == null) {
//            trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyOd());
//        } else {
//
//            if ((Integer) idLastPunktKon == (Integer) trasaOdcinek.getOdcinek().getPunktPoczatkowy().getIdpunkt()) {
//                if (wystapienTejTrasy % 2 == 0) {
//                    System.out.println(wystapienTejTrasy + " PP punkty od");
//                    trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyOd());
//                } else {
//                    System.out.println(wystapienTejTrasy + " PP punkty do");
//                    trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyDo());
//                }
//            } else {
//                trasaOdcinek.setPowrot(1);
//                //if (pierwszyOdcinekTrasy == trasaOdcinek.getOdcinek()) {
//                //    wystapienTejTrasy++;
//                //}
//                //else if((wystapienTejTrasy == 2) && (pierwszyOdcinekTrasy == trasaOdcinek.getOdcinek())){
//                //    trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyOd());
//                //}
//
//                System.out.println("PK: " + wystapienTejTrasy);
//                if (wystapienTejTrasy % 2 == 0) {
//                    System.out.println(wystapienTejTrasy + " PK punkty do " + trasaOdcinek.getOdcinek().getPunktyDo());
//                    trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyDo());
//                } else {
//                    System.out.println(wystapienTejTrasy + " PK punkty od " + trasaOdcinek.getOdcinek().getPunktyOd());
//                    trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyOd());
//                }
//
//            }
//        }
//
//        trasaOdcinekService.createTrasaOdcinek(trasaOdcinek);
//
//        return "redirect:" + request.getHeader("Referer");
//    }

    @PostMapping({"/trasa_odcinki/dodaj/{idLastPunktKon}", "/trasa_odcinki/dodaj/"})
    public String createTrasaOdcinek(@ModelAttribute TrasaOdcinek trasaOdcinek,
                                     @PathVariable(required = false) Integer idLastPunktKon,
                                     HttpServletRequest request) {

        if ((trasaOdcinekService.getAllOdcinkiByTrasa(trasaOdcinek.getTrasa())).size() == 0) {
            trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyOd());
            trasaOdcinek.getTrasa().setSuma_punktow(trasaOdcinek.getTrasa().getSuma_punktow() + trasaOdcinek.getOdcinek().getPunktyOd());
            trasaOdcinekService.createTrasaOdcinek(trasaOdcinek);
            return "redirect:" + request.getHeader("Referer");
        }

        if (idLastPunktKon == trasaOdcinek.getOdcinek().getPunktPoczatkowy().getIdpunkt()) {
            trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyOd());
            trasaOdcinek.getTrasa().setSuma_punktow(trasaOdcinek.getTrasa().getSuma_punktow() + trasaOdcinek.getOdcinek().getPunktyOd());
        } else {
            trasaOdcinek.setPunkty(trasaOdcinek.getOdcinek().getPunktyDo());
            trasaOdcinek.getTrasa().setSuma_punktow(trasaOdcinek.getTrasa().getSuma_punktow() + trasaOdcinek.getOdcinek().getPunktyDo());
            trasaOdcinek.setPowrot(1);
        }

        int wystapienTejTrasy = (trasaOdcinekService.getAllByIdOdcinekAndIdTrasa(trasaOdcinek.getOdcinek().getIdOdcinek(), trasaOdcinek.getTrasa().getIdTrasa())).size();
        if (wystapienTejTrasy+1 > 2){
            System.out.println("Ustawiam dubel na 1");
            trasaOdcinek.getTrasa().setDubel(1);
        }

        trasaOdcinekService.createTrasaOdcinek(trasaOdcinek);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/trasa_odcinki/usun/{idTrasaOdcinek}")
    public String removeTrasaOdcinek(@PathVariable int idTrasaOdcinek,
                                     HttpServletRequest request) {

        TrasaOdcinek trasaOdcinek = trasaOdcinekService.getOneById(idTrasaOdcinek);

        int wystapienTejTrasy = (trasaOdcinekService.getAllByIdOdcinekAndIdTrasa(trasaOdcinek.getOdcinek().getIdOdcinek(), trasaOdcinek.getTrasa().getIdTrasa())).size();
        System.out.println("Po usunieciu: " + (wystapienTejTrasy - 1));
        if (wystapienTejTrasy - 1 <= 2) {
            trasaOdcinek.getTrasa().setDubel(0);
            System.out.println("Wchodze w petle i dubel = 0: " + trasaOdcinek.getTrasa().getDubel());
        }

        trasaOdcinek.getTrasa().setSuma_punktow(trasaOdcinek.getTrasa().getSuma_punktow() - trasaOdcinek.getPunkty());

        trasaOdcinekService.removeTrasaOdcinekById(idTrasaOdcinek);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/trasa_odcinki/usun_wszystko/{idTrasa}")
    public String removeAllTrasaOdcinek(@PathVariable int idTrasa,
                                        HttpServletRequest request){

        Trasa trasa = trasaService.getOneById(idTrasa);
        trasaOdcinekService.removeAllByTrasa(trasa);

        return "redirect:" + request.getHeader("Referer");
    }

}
