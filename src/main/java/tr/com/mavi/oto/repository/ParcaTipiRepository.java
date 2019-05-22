package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.ParcaTipi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ParcaTipi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParcaTipiRepository extends JpaRepository<ParcaTipi, Long> {

}
