package tr.com.mavi.oto.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CariSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CariSearchRepositoryMockConfiguration {

    @MockBean
    private CariSearchRepository mockCariSearchRepository;

}
