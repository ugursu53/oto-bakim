package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Iscilik;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Iscilik} entity.
 */
public interface IscilikSearchRepository extends ElasticsearchRepository<Iscilik, Long> {
}
