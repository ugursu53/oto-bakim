package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Parca;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Parca} entity.
 */
public interface ParcaSearchRepository extends ElasticsearchRepository<Parca, Long> {
}
