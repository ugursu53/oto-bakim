package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.Cari;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Cari entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CariRepository extends JpaRepository<Cari, Long> {

}
