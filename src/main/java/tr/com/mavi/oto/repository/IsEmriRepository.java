package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.IsEmri;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IsEmri entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IsEmriRepository extends JpaRepository<IsEmri, Long> {

}
