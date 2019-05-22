package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.Parca;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Parca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParcaRepository extends JpaRepository<Parca, Long> {

}
