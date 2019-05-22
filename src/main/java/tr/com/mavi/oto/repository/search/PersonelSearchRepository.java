package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Personel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Personel} entity.
 */
public interface PersonelSearchRepository extends ElasticsearchRepository<Personel, Long> {
}
