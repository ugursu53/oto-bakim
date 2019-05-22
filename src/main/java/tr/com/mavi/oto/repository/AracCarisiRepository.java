package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.AracCarisi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AracCarisi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AracCarisiRepository extends JpaRepository<AracCarisi, Long> {

}
