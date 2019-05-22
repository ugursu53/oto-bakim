package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.Hesap;
import tr.com.mavi.oto.repository.HesapRepository;
import tr.com.mavi.oto.repository.search.HesapSearchRepository;
import tr.com.mavi.oto.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.Hesap}.
 */
@RestController
@RequestMapping("/api")
public class HesapResource {

    private final Logger log = LoggerFactory.getLogger(HesapResource.class);

    private static final String ENTITY_NAME = "hesap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HesapRepository hesapRepository;

    private final HesapSearchRepository hesapSearchRepository;

    public HesapResource(HesapRepository hesapRepository, HesapSearchRepository hesapSearchRepository) {
        this.hesapRepository = hesapRepository;
        this.hesapSearchRepository = hesapSearchRepository;
    }

    /**
     * {@code POST  /hesaps} : Create a new hesap.
     *
     * @param hesap the hesap to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hesap, or with status {@code 400 (Bad Request)} if the hesap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hesaps")
    public ResponseEntity<Hesap> createHesap(@RequestBody Hesap hesap) throws URISyntaxException {
        log.debug("REST request to save Hesap : {}", hesap);
        if (hesap.getId() != null) {
            throw new BadRequestAlertException("A new hesap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hesap result = hesapRepository.save(hesap);
        hesapSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hesaps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hesaps} : Updates an existing hesap.
     *
     * @param hesap the hesap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hesap,
     * or with status {@code 400 (Bad Request)} if the hesap is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hesap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hesaps")
    public ResponseEntity<Hesap> updateHesap(@RequestBody Hesap hesap) throws URISyntaxException {
        log.debug("REST request to update Hesap : {}", hesap);
        if (hesap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Hesap result = hesapRepository.save(hesap);
        hesapSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hesap.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hesaps} : get all the hesaps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hesaps in body.
     */
    @GetMapping("/hesaps")
    public List<Hesap> getAllHesaps() {
        log.debug("REST request to get all Hesaps");
        return hesapRepository.findAll();
    }

    /**
     * {@code GET  /hesaps/:id} : get the "id" hesap.
     *
     * @param id the id of the hesap to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hesap, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hesaps/{id}")
    public ResponseEntity<Hesap> getHesap(@PathVariable Long id) {
        log.debug("REST request to get Hesap : {}", id);
        Optional<Hesap> hesap = hesapRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hesap);
    }

    /**
     * {@code DELETE  /hesaps/:id} : delete the "id" hesap.
     *
     * @param id the id of the hesap to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hesaps/{id}")
    public ResponseEntity<Void> deleteHesap(@PathVariable Long id) {
        log.debug("REST request to delete Hesap : {}", id);
        hesapRepository.deleteById(id);
        hesapSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/hesaps?query=:query} : search for the hesap corresponding
     * to the query.
     *
     * @param query the query of the hesap search.
     * @return the result of the search.
     */
    @GetMapping("/_search/hesaps")
    public List<Hesap> searchHesaps(@RequestParam String query) {
        log.debug("REST request to search Hesaps for query {}", query);
        return StreamSupport
            .stream(hesapSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
