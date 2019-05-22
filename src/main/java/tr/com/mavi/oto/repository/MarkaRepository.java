package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.Marka;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Marka entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarkaRepository extends JpaRepository<Marka, Long> {

}
