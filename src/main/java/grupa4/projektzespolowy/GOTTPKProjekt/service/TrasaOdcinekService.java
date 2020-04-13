package grupa4.projektzespolowy.GOTTPKProjekt.service;

import grupa4.projektzespolowy.GOTTPKProjekt.model.Odcinek;
import grupa4.projektzespolowy.GOTTPKProjekt.model.Trasa;
import grupa4.projektzespolowy.GOTTPKProjekt.model.TrasaOdcinek;

import java.util.List;

public interface TrasaOdcinekService {
    List<TrasaOdcinek> getAllOdcinkiByIdTrasa(int idTrasa);
    List<TrasaOdcinek> getAllOdcinkiByTrasa(Trasa trasa);

    List<TrasaOdcinek> getAllTrasybyIdOdcinek(int idOdcinek);
    List<TrasaOdcinek> getAllTrasybyOdcinek(Odcinek odcinek);

    List<TrasaOdcinek> getAllByIdOdcinekAndIdTrasa(int idOdcinek, int idTrasa);

    List<TrasaOdcinek> getAll();
    TrasaOdcinek getOneById(int idTrasaOdcinek);
    void createTrasaOdcinek(TrasaOdcinek trasaOdcinek);
    void removeTrasaOdcinekById(int idTrasaOdcinek);

    void removeAllByTrasa(Trasa trasa);
}
