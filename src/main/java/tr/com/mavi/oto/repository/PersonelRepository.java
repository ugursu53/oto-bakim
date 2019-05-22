package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.Personel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Personel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonelRepository extends JpaRepository<Personel, Long> {

}
