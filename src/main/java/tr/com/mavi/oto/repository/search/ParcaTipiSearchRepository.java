package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.ParcaTipi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ParcaTipi} entity.
 */
public interface ParcaTipiSearchRepository extends ElasticsearchRepository<ParcaTipi, Long> {
}
