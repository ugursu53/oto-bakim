package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.IsEmri;
import tr.com.mavi.oto.repository.IsEmriRepository;
import tr.com.mavi.oto.repository.search.IsEmriSearchRepository;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.IsEmri}.
 */
@RestController
@RequestMapping("/api")
public class IsEmriResource {

    private final Logger log = LoggerFactory.getLogger(IsEmriResource.class);

    private static final String ENTITY_NAME = "isEmri";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsEmriRepository isEmriRepository;

    private final IsEmriSearchRepository isEmriSearchRepository;

    public IsEmriResource(IsEmriRepository isEmriRepository, IsEmriSearchRepository isEmriSearchRepository) {
        this.isEmriRepository = isEmriRepository;
        this.isEmriSearchRepository = isEmriSearchRepository;
    }

    /**
     * {@code POST  /is-emris} : Create a new isEmri.
     *
     * @param isEmri the isEmri to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isEmri, or with status {@code 400 (Bad Request)} if the isEmri has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/is-emris")
    public ResponseEntity<IsEmri> createIsEmri(@Valid @RequestBody IsEmri isEmri) throws URISyntaxException {
        log.debug("REST request to save IsEmri : {}", isEmri);
        if (isEmri.getId() != null) {
            throw new BadRequestAlertException("A new isEmri cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsEmri result = isEmriRepository.save(isEmri);
        isEmriSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/is-emris/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /is-emris} : Updates an existing isEmri.
     *
     * @param isEmri the isEmri to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isEmri,
     * or with status {@code 400 (Bad Request)} if the isEmri is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isEmri couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/is-emris")
    public ResponseEntity<IsEmri> updateIsEmri(@Valid @RequestBody IsEmri isEmri) throws URISyntaxException {
        log.debug("REST request to update IsEmri : {}", isEmri);
        if (isEmri.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IsEmri result = isEmriRepository.save(isEmri);
        isEmriSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isEmri.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /is-emris} : get all the isEmris.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isEmris in body.
     */
    @GetMapping("/is-emris")
    public ResponseEntity<List<IsEmri>> getAllIsEmris(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of IsEmris");
        Page<IsEmri> page = isEmriRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /is-emris/:id} : get the "id" isEmri.
     *
     * @param id the id of the isEmri to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isEmri, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/is-emris/{id}")
    public ResponseEntity<IsEmri> getIsEmri(@PathVariable Long id) {
        log.debug("REST request to get IsEmri : {}", id);
        Optional<IsEmri> isEmri = isEmriRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(isEmri);
    }

    /**
     * {@code DELETE  /is-emris/:id} : delete the "id" isEmri.
     *
     * @param id the id of the isEmri to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/is-emris/{id}")
    public ResponseEntity<Void> deleteIsEmri(@PathVariable Long id) {
        log.debug("REST request to delete IsEmri : {}", id);
        isEmriRepository.deleteById(id);
        isEmriSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/is-emris?query=:query} : search for the isEmri corresponding
     * to the query.
     *
     * @param query the query of the isEmri search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/is-emris")
    public ResponseEntity<List<IsEmri>> searchIsEmris(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of IsEmris for query {}", query);
        Page<IsEmri> page = isEmriSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
