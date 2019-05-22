package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.ParcaTipi;
import tr.com.mavi.oto.repository.ParcaTipiRepository;
import tr.com.mavi.oto.repository.search.ParcaTipiSearchRepository;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.ParcaTipi}.
 */
@RestController
@RequestMapping("/api")
public class ParcaTipiResource {

    private final Logger log = LoggerFactory.getLogger(ParcaTipiResource.class);

    private static final String ENTITY_NAME = "parcaTipi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParcaTipiRepository parcaTipiRepository;

    private final ParcaTipiSearchRepository parcaTipiSearchRepository;

    public ParcaTipiResource(ParcaTipiRepository parcaTipiRepository, ParcaTipiSearchRepository parcaTipiSearchRepository) {
        this.parcaTipiRepository = parcaTipiRepository;
        this.parcaTipiSearchRepository = parcaTipiSearchRepository;
    }

    /**
     * {@code POST  /parca-tipis} : Create a new parcaTipi.
     *
     * @param parcaTipi the parcaTipi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parcaTipi, or with status {@code 400 (Bad Request)} if the parcaTipi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parca-tipis")
    public ResponseEntity<ParcaTipi> createParcaTipi(@Valid @RequestBody ParcaTipi parcaTipi) throws URISyntaxException {
        log.debug("REST request to save ParcaTipi : {}", parcaTipi);
        if (parcaTipi.getId() != null) {
            throw new BadRequestAlertException("A new parcaTipi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParcaTipi result = parcaTipiRepository.save(parcaTipi);
        parcaTipiSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/parca-tipis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parca-tipis} : Updates an existing parcaTipi.
     *
     * @param parcaTipi the parcaTipi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcaTipi,
     * or with status {@code 400 (Bad Request)} if the parcaTipi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parcaTipi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parca-tipis")
    public ResponseEntity<ParcaTipi> updateParcaTipi(@Valid @RequestBody ParcaTipi parcaTipi) throws URISyntaxException {
        log.debug("REST request to update ParcaTipi : {}", parcaTipi);
        if (parcaTipi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParcaTipi result = parcaTipiRepository.save(parcaTipi);
        parcaTipiSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parcaTipi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /parca-tipis} : get all the parcaTipis.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parcaTipis in body.
     */
    @GetMapping("/parca-tipis")
    public ResponseEntity<List<ParcaTipi>> getAllParcaTipis(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ParcaTipis");
        Page<ParcaTipi> page = parcaTipiRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parca-tipis/:id} : get the "id" parcaTipi.
     *
     * @param id the id of the parcaTipi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parcaTipi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parca-tipis/{id}")
    public ResponseEntity<ParcaTipi> getParcaTipi(@PathVariable Long id) {
        log.debug("REST request to get ParcaTipi : {}", id);
        Optional<ParcaTipi> parcaTipi = parcaTipiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(parcaTipi);
    }

    /**
     * {@code DELETE  /parca-tipis/:id} : delete the "id" parcaTipi.
     *
     * @param id the id of the parcaTipi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parca-tipis/{id}")
    public ResponseEntity<Void> deleteParcaTipi(@PathVariable Long id) {
        log.debug("REST request to delete ParcaTipi : {}", id);
        parcaTipiRepository.deleteById(id);
        parcaTipiSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/parca-tipis?query=:query} : search for the parcaTipi corresponding
     * to the query.
     *
     * @param query the query of the parcaTipi search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/parca-tipis")
    public ResponseEntity<List<ParcaTipi>> searchParcaTipis(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of ParcaTipis for query {}", query);
        Page<ParcaTipi> page = parcaTipiSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
