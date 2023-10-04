package nl.backend.reparatieservice.repository;

import nl.backend.reparatieservice.model.UploadedPhoto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadedPhotoRepository extends CrudRepository<UploadedPhoto,Long> {
}
