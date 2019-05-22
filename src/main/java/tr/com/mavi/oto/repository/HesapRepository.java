package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.Hesap;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Hesap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HesapRepository extends JpaRepository<Hesap, Long> {

}
