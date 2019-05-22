package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.IsEmri;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IsEmri} entity.
 */
public interface IsEmriSearchRepository extends ElasticsearchRepository<IsEmri, Long> {
}
