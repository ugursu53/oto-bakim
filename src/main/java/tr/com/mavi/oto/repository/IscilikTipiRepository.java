package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.IscilikTipi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IscilikTipi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IscilikTipiRepository extends JpaRepository<IscilikTipi, Long> {

}
