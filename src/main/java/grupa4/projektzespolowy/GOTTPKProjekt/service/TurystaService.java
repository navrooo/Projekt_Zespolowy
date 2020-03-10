package grupa4.projektzespolowy.GOTTPKProjekt.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import grupa4.projektzespolowy.GOTTPKProjekt.model.Turysta;
import grupa4.projektzespolowy.GOTTPKProjekt.repository.TurystaRepository;

@Service
@Transactional
public class TurystaService 
{
	@Autowired
    private TurystaRepository turystaRepository;
	
	public Turysta createTurysta(Turysta turysta) {
        return turystaRepository.save(turysta);
    }

    public List<Turysta> getAllProduct() {
        return this.turystaRepository.findAll();
    }
}
