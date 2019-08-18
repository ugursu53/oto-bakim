package tr.com.mavi.oto.web.rest;

import org.elasticsearch.index.query.Operator;
import tr.com.mavi.oto.domain.Cari;
import tr.com.mavi.oto.repository.CariRepository;
import tr.com.mavi.oto.repository.search.CariSearchRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link tr.com.mavi.oto.domain.Cari}.
 */
@RestController
@RequestMapping("/api")
public class CariResource {

    private final Logger log = LoggerFactory.getLogger(CariResource.class);

    private static final String ENTITY_NAME = "cari";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CariRepository cariRepository;

    private final CariSearchRepository cariSearchRepository;

    public CariResource(CariRepository cariRepository, CariSearchRepository cariSearchRepository) {
        this.cariRepository = cariRepository;
        this.cariSearchRepository = cariSearchRepository;
    }

    /**
     * {@code POST  /caris} : Create a new cari.
     *
     * @param cari the cari to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cari, or with status {@code 400 (Bad Request)} if the cari has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caris")
    public ResponseEntity<Cari> createCari(@Valid @RequestBody Cari cari) throws URISyntaxException {
        log.debug("REST request to save Cari : {}", cari);
        if (cari.getId() != null) {
            throw new BadRequestAlertException("A new cari cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cari result = cariRepository.save(cari);
        cariSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/caris/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caris} : Updates an existing cari.
     *
     * @param cari the cari to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cari,
     * or with status {@code 400 (Bad Request)} if the cari is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cari couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caris")
    public ResponseEntity<Cari> updateCari(@Valid @RequestBody Cari cari) throws URISyntaxException {
        log.debug("REST request to update Cari : {}", cari);
        if (cari.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cari result = cariRepository.save(cari);
        cariSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cari.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /caris} : get all the caris.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caris in body.
     */
    @GetMapping("/caris")
    public ResponseEntity<List<Cari>> getAllCaris(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Caris");
        Page<Cari> page = cariRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /caris/:id} : get the "id" cari.
     *
     * @param id the id of the cari to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cari, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caris/{id}")
    public ResponseEntity<Cari> getCari(@PathVariable Long id) {
        log.debug("REST request to get Cari : {}", id);
        Optional<Cari> cari = cariRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cari);
    }

    /**
     * {@code DELETE  /caris/:id} : delete the "id" cari.
     *
     * @param id the id of the cari to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caris/{id}")
    public ResponseEntity<Void> deleteCari(@PathVariable Long id) {
        log.debug("REST request to delete Cari : {}", id);
        cariRepository.deleteById(id);
        cariSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/caris?query=:query} : search for the cari corresponding
     * to the query.
     *
     * @param query the query of the cari search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/caris")
    public ResponseEntity<List<Cari>> searchCaris(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Caris for query {}", query);
        Page<Cari> page = cariSearchRepository.search(queryStringQuery(SearchUtil.normalizedQuery(query)).defaultOperator(Operator.AND), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
