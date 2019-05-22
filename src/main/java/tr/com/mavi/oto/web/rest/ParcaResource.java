package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.Parca;
import tr.com.mavi.oto.repository.ParcaRepository;
import tr.com.mavi.oto.repository.search.ParcaSearchRepository;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.Parca}.
 */
@RestController
@RequestMapping("/api")
public class ParcaResource {

    private final Logger log = LoggerFactory.getLogger(ParcaResource.class);

    private static final String ENTITY_NAME = "parca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParcaRepository parcaRepository;

    private final ParcaSearchRepository parcaSearchRepository;

    public ParcaResource(ParcaRepository parcaRepository, ParcaSearchRepository parcaSearchRepository) {
        this.parcaRepository = parcaRepository;
        this.parcaSearchRepository = parcaSearchRepository;
    }

    /**
     * {@code POST  /parcas} : Create a new parca.
     *
     * @param parca the parca to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parca, or with status {@code 400 (Bad Request)} if the parca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parcas")
    public ResponseEntity<Parca> createParca(@RequestBody Parca parca) throws URISyntaxException {
        log.debug("REST request to save Parca : {}", parca);
        if (parca.getId() != null) {
            throw new BadRequestAlertException("A new parca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parca result = parcaRepository.save(parca);
        parcaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/parcas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parcas} : Updates an existing parca.
     *
     * @param parca the parca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parca,
     * or with status {@code 400 (Bad Request)} if the parca is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parcas")
    public ResponseEntity<Parca> updateParca(@RequestBody Parca parca) throws URISyntaxException {
        log.debug("REST request to update Parca : {}", parca);
        if (parca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Parca result = parcaRepository.save(parca);
        parcaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parca.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /parcas} : get all the parcas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parcas in body.
     */
    @GetMapping("/parcas")
    public ResponseEntity<List<Parca>> getAllParcas(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Parcas");
        Page<Parca> page = parcaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parcas/:id} : get the "id" parca.
     *
     * @param id the id of the parca to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parca, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parcas/{id}")
    public ResponseEntity<Parca> getParca(@PathVariable Long id) {
        log.debug("REST request to get Parca : {}", id);
        Optional<Parca> parca = parcaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(parca);
    }

    /**
     * {@code DELETE  /parcas/:id} : delete the "id" parca.
     *
     * @param id the id of the parca to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parcas/{id}")
    public ResponseEntity<Void> deleteParca(@PathVariable Long id) {
        log.debug("REST request to delete Parca : {}", id);
        parcaRepository.deleteById(id);
        parcaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/parcas?query=:query} : search for the parca corresponding
     * to the query.
     *
     * @param query the query of the parca search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/parcas")
    public ResponseEntity<List<Parca>> searchParcas(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Parcas for query {}", query);
        Page<Parca> page = parcaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
