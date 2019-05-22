package tr.com.mavi.oto.repository;

import tr.com.mavi.oto.domain.IscilikGrubu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IscilikGrubu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IscilikGrubuRepository extends JpaRepository<IscilikGrubu, Long> {

}
