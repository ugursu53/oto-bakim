package tr.com.mavi.oto.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PersonelSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PersonelSearchRepositoryMockConfiguration {

    @MockBean
    private PersonelSearchRepository mockPersonelSearchRepository;

}
