package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.Personel;
import tr.com.mavi.oto.repository.PersonelRepository;
import tr.com.mavi.oto.repository.search.PersonelSearchRepository;
import tr.com.mavi.oto.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link tr.com.mavi.oto.domain.Personel}.
 */
@RestController
@RequestMapping("/api")
public class PersonelResource {

    private final Logger log = LoggerFactory.getLogger(PersonelResource.class);

    private static final String ENTITY_NAME = "personel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonelRepository personelRepository;

    private final PersonelSearchRepository personelSearchRepository;

    public PersonelResource(PersonelRepository personelRepository, PersonelSearchRepository personelSearchRepository) {
        this.personelRepository = personelRepository;
        this.personelSearchRepository = personelSearchRepository;
    }

    /**
     * {@code POST  /personels} : Create a new personel.
     *
     * @param personel the personel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personel, or with status {@code 400 (Bad Request)} if the personel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personels")
    public ResponseEntity<Personel> createPersonel(@Valid @RequestBody Personel personel) throws URISyntaxException {
        log.debug("REST request to save Personel : {}", personel);
        if (personel.getId() != null) {
            throw new BadRequestAlertException("A new personel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Personel result = personelRepository.save(personel);
        personelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/personels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personels} : Updates an existing personel.
     *
     * @param personel the personel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personel,
     * or with status {@code 400 (Bad Request)} if the personel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personels")
    public ResponseEntity<Personel> updatePersonel(@Valid @RequestBody Personel personel) throws URISyntaxException {
        log.debug("REST request to update Personel : {}", personel);
        if (personel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Personel result = personelRepository.save(personel);
        personelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personel.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /personels} : get all the personels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personels in body.
     */
    @GetMapping("/personels")
    public ResponseEntity<List<Personel>> getAllPersonels(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Personels");
        Page<Personel> page = personelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personels/:id} : get the "id" personel.
     *
     * @param id the id of the personel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personels/{id}")
    public ResponseEntity<Personel> getPersonel(@PathVariable Long id) {
        log.debug("REST request to get Personel : {}", id);
        Optional<Personel> personel = personelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personel);
    }

    /**
     * {@code DELETE  /personels/:id} : delete the "id" personel.
     *
     * @param id the id of the personel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personels/{id}")
    public ResponseEntity<Void> deletePersonel(@PathVariable Long id) {
        log.debug("REST request to delete Personel : {}", id);
        personelRepository.deleteById(id);
        personelSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/personels?query=:query} : search for the personel corresponding
     * to the query.
     *
     * @param query the query of the personel search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/personels")
    public ResponseEntity<List<Personel>> searchPersonels(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Personels for query {}", query);
        Page<Personel> page = personelSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
