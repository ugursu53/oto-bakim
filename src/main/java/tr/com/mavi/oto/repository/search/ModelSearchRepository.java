package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Model;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Model} entity.
 */
public interface ModelSearchRepository extends ElasticsearchRepository<Model, Long> {
}
