package tr.com.mavi.oto.web.rest;

import org.elasticsearch.index.query.Operator;
import tr.com.mavi.oto.domain.IscilikTipi;
import tr.com.mavi.oto.repository.IscilikTipiRepository;
import tr.com.mavi.oto.repository.search.IscilikTipiSearchRepository;
import tr.com.mavi.oto.service.util.SearchUtil;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.IscilikTipi}.
 */
@RestController
@RequestMapping("/api")
public class IscilikTipiResource {

    private final Logger log = LoggerFactory.getLogger(IscilikTipiResource.class);

    private static final String ENTITY_NAME = "iscilikTipi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IscilikTipiRepository iscilikTipiRepository;

    private final IscilikTipiSearchRepository iscilikTipiSearchRepository;

    public IscilikTipiResource(IscilikTipiRepository iscilikTipiRepository, IscilikTipiSearchRepository iscilikTipiSearchRepository) {
        this.iscilikTipiRepository = iscilikTipiRepository;
        this.iscilikTipiSearchRepository = iscilikTipiSearchRepository;
    }

    /**
     * {@code POST  /iscilik-tipis} : Create a new iscilikTipi.
     *
     * @param iscilikTipi the iscilikTipi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iscilikTipi, or with status {@code 400 (Bad Request)} if the iscilikTipi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/iscilik-tipis")
    public ResponseEntity<IscilikTipi> createIscilikTipi(@RequestBody IscilikTipi iscilikTipi) throws URISyntaxException {
        log.debug("REST request to save IscilikTipi : {}", iscilikTipi);
        if (iscilikTipi.getId() != null) {
            throw new BadRequestAlertException("A new iscilikTipi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IscilikTipi result = iscilikTipiRepository.save(iscilikTipi);
        iscilikTipiSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/iscilik-tipis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /iscilik-tipis} : Updates an existing iscilikTipi.
     *
     * @param iscilikTipi the iscilikTipi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iscilikTipi,
     * or with status {@code 400 (Bad Request)} if the iscilikTipi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iscilikTipi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/iscilik-tipis")
    public ResponseEntity<IscilikTipi> updateIscilikTipi(@RequestBody IscilikTipi iscilikTipi) throws URISyntaxException {
        log.debug("REST request to update IscilikTipi : {}", iscilikTipi);
        if (iscilikTipi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IscilikTipi result = iscilikTipiRepository.save(iscilikTipi);
        iscilikTipiSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iscilikTipi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /iscilik-tipis} : get all the iscilikTipis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iscilikTipis in body.
     */
    @GetMapping("/iscilik-tipis")
    public ResponseEntity<List<IscilikTipi>> getAllIscilikTipis(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of IscilikTipis");
        Page<IscilikTipi> page = iscilikTipiRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /iscilik-tipis/:id} : get the "id" iscilikTipi.
     *
     * @param id the id of the iscilikTipi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iscilikTipi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/iscilik-tipis/{id}")
    public ResponseEntity<IscilikTipi> getIscilikTipi(@PathVariable Long id) {
        log.debug("REST request to get IscilikTipi : {}", id);
        Optional<IscilikTipi> iscilikTipi = iscilikTipiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iscilikTipi);
    }

    /**
     * {@code DELETE  /iscilik-tipis/:id} : delete the "id" iscilikTipi.
     *
     * @param id the id of the iscilikTipi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/iscilik-tipis/{id}")
    public ResponseEntity<Void> deleteIscilikTipi(@PathVariable Long id) {
        log.debug("REST request to delete IscilikTipi : {}", id);
        iscilikTipiRepository.deleteById(id);
        iscilikTipiSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/iscilik-tipis?query=:query} : search for the iscilikTipi corresponding
     * to the query.
     *
     * @param query the query of the iscilikTipi search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/iscilik-tipis")
    public ResponseEntity<List<IscilikTipi>> searchIscilikTipis(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of IscilikTipis for query {}", query);
        Page<IscilikTipi> page = iscilikTipiSearchRepository.search(queryStringQuery(SearchUtil.normalizedQuery(query)).defaultOperator(Operator.AND), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
