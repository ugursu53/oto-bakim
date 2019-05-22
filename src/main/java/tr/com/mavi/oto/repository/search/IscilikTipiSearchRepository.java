package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.IscilikTipi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IscilikTipi} entity.
 */
public interface IscilikTipiSearchRepository extends ElasticsearchRepository<IscilikTipi, Long> {
}
