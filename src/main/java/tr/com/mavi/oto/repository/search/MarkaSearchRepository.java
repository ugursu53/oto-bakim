package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Marka;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Marka} entity.
 */
public interface MarkaSearchRepository extends ElasticsearchRepository<Marka, Long> {
}
