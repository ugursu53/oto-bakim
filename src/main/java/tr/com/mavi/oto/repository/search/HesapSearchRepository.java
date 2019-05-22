package tr.com.mavi.oto.repository.search;

import tr.com.mavi.oto.domain.Hesap;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Hesap} entity.
 */
public interface HesapSearchRepository extends ElasticsearchRepository<Hesap, Long> {
}
