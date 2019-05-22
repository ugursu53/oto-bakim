package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.Marka;
import tr.com.mavi.oto.repository.MarkaRepository;
import tr.com.mavi.oto.repository.search.MarkaSearchRepository;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link tr.com.mavi.oto.domain.Marka}.
 */
@RestController
@RequestMapping("/api")
public class MarkaResource {

    private final Logger log = LoggerFactory.getLogger(MarkaResource.class);

    private static final String ENTITY_NAME = "marka";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarkaRepository markaRepository;

    private final MarkaSearchRepository markaSearchRepository;

    public MarkaResource(MarkaRepository markaRepository, MarkaSearchRepository markaSearchRepository) {
        this.markaRepository = markaRepository;
        this.markaSearchRepository = markaSearchRepository;
    }

    /**
     * {@code POST  /markas} : Create a new marka.
     *
     * @param marka the marka to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marka, or with status {@code 400 (Bad Request)} if the marka has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/markas")
    public ResponseEntity<Marka> createMarka(@RequestBody Marka marka) throws URISyntaxException {
        log.debug("REST request to save Marka : {}", marka);
        if (marka.getId() != null) {
            throw new BadRequestAlertException("A new marka cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Marka result = markaRepository.save(marka);
        markaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/markas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /markas} : Updates an existing marka.
     *
     * @param marka the marka to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marka,
     * or with status {@code 400 (Bad Request)} if the marka is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marka couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/markas")
    public ResponseEntity<Marka> updateMarka(@RequestBody Marka marka) throws URISyntaxException {
        log.debug("REST request to update Marka : {}", marka);
        if (marka.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Marka result = markaRepository.save(marka);
        markaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marka.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /markas} : get all the markas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of markas in body.
     */
    @GetMapping("/markas")
    public ResponseEntity<List<Marka>> getAllMarkas(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Markas");
        Page<Marka> page = markaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /markas/:id} : get the "id" marka.
     *
     * @param id the id of the marka to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marka, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/markas/{id}")
    public ResponseEntity<Marka> getMarka(@PathVariable Long id) {
        log.debug("REST request to get Marka : {}", id);
        Optional<Marka> marka = markaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marka);
    }

    /**
     * {@code DELETE  /markas/:id} : delete the "id" marka.
     *
     * @param id the id of the marka to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/markas/{id}")
    public ResponseEntity<Void> deleteMarka(@PathVariable Long id) {
        log.debug("REST request to delete Marka : {}", id);
        markaRepository.deleteById(id);
        markaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/markas?query=:query} : search for the marka corresponding
     * to the query.
     *
     * @param query the query of the marka search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/markas")
    public ResponseEntity<List<Marka>> searchMarkas(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Markas for query {}", query);
        Page<Marka> page = markaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
