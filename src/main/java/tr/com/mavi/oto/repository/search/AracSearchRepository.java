package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Arac;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Arac} entity.
 */
public interface AracSearchRepository extends ElasticsearchRepository<Arac, Long> {
}
