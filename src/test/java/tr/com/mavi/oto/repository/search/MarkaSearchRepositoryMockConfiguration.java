package tr.com.mavi.oto.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MarkaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MarkaSearchRepositoryMockConfiguration {

    @MockBean
    private MarkaSearchRepository mockMarkaSearchRepository;

}
