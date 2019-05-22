package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.Iscilik;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Iscilik entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IscilikRepository extends JpaRepository<Iscilik, Long> {

}
