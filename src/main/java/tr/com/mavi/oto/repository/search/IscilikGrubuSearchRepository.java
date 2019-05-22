package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.IscilikGrubu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IscilikGrubu} entity.
 */
public interface IscilikGrubuSearchRepository extends ElasticsearchRepository<IscilikGrubu, Long> {
}
