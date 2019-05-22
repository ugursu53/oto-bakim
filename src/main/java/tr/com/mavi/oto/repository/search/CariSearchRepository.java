package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Cari;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Cari} entity.
 */
public interface CariSearchRepository extends ElasticsearchRepository<Cari, Long> {
}
