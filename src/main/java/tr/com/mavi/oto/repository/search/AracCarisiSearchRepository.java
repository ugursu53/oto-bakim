package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.AracCarisi;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AracCarisi} entity.
 */
public interface AracCarisiSearchRepository extends ElasticsearchRepository<AracCarisi, Long> {
}
