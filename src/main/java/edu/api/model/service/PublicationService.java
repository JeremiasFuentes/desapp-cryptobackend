package edu.api.model.service;

import edu.api.model.entity.Publication;
import edu.api.model.repository.PublicationRepository;
import edu.api.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PublicationService {

    @Autowired
    PublicationRepository publicationRepository;

    public void save(Publication publication){
        publicationRepository.save(publication);
    }

    public void delete(Publication publication){
        publicationRepository.delete(publication);
    }

    public List<Publication> getAll(){
        return publicationRepository.findAll();
    }

    public Optional<Publication> getByID (int id){return publicationRepository.findById(id);}

    public List<Publication> getAllByUser(User user){
        return publicationRepository.findAllByUser(user);
    }

    public boolean existsById(int id){
        return publicationRepository.existsById(id);
    }

}
