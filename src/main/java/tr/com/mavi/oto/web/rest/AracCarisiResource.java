package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.AracCarisi;
import tr.com.mavi.oto.repository.AracCarisiRepository;
import tr.com.mavi.oto.repository.search.AracCarisiSearchRepository;
import tr.com.mavi.oto.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.AracCarisi}.
 */
@RestController
@RequestMapping("/api")
public class AracCarisiResource {

    private final Logger log = LoggerFactory.getLogger(AracCarisiResource.class);

    private static final String ENTITY_NAME = "aracCarisi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AracCarisiRepository aracCarisiRepository;

    private final AracCarisiSearchRepository aracCarisiSearchRepository;

    public AracCarisiResource(AracCarisiRepository aracCarisiRepository, AracCarisiSearchRepository aracCarisiSearchRepository) {
        this.aracCarisiRepository = aracCarisiRepository;
        this.aracCarisiSearchRepository = aracCarisiSearchRepository;
    }

    /**
     * {@code POST  /arac-carisis} : Create a new aracCarisi.
     *
     * @param aracCarisi the aracCarisi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aracCarisi, or with status {@code 400 (Bad Request)} if the aracCarisi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arac-carisis")
    public ResponseEntity<AracCarisi> createAracCarisi(@Valid @RequestBody AracCarisi aracCarisi) throws URISyntaxException {
        log.debug("REST request to save AracCarisi : {}", aracCarisi);
        if (aracCarisi.getId() != null) {
            throw new BadRequestAlertException("A new aracCarisi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AracCarisi result = aracCarisiRepository.save(aracCarisi);
        aracCarisiSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/arac-carisis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arac-carisis} : Updates an existing aracCarisi.
     *
     * @param aracCarisi the aracCarisi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aracCarisi,
     * or with status {@code 400 (Bad Request)} if the aracCarisi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aracCarisi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arac-carisis")
    public ResponseEntity<AracCarisi> updateAracCarisi(@Valid @RequestBody AracCarisi aracCarisi) throws URISyntaxException {
        log.debug("REST request to update AracCarisi : {}", aracCarisi);
        if (aracCarisi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AracCarisi result = aracCarisiRepository.save(aracCarisi);
        aracCarisiSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aracCarisi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arac-carisis} : get all the aracCarisis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aracCarisis in body.
     */
    @GetMapping("/arac-carisis")
    public List<AracCarisi> getAllAracCarisis() {
        log.debug("REST request to get all AracCarisis");
        return aracCarisiRepository.findAll();
    }

    /**
     * {@code GET  /arac-carisis/:id} : get the "id" aracCarisi.
     *
     * @param id the id of the aracCarisi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aracCarisi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arac-carisis/{id}")
    public ResponseEntity<AracCarisi> getAracCarisi(@PathVariable Long id) {
        log.debug("REST request to get AracCarisi : {}", id);
        Optional<AracCarisi> aracCarisi = aracCarisiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aracCarisi);
    }

    /**
     * {@code DELETE  /arac-carisis/:id} : delete the "id" aracCarisi.
     *
     * @param id the id of the aracCarisi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arac-carisis/{id}")
    public ResponseEntity<Void> deleteAracCarisi(@PathVariable Long id) {
        log.debug("REST request to delete AracCarisi : {}", id);
        aracCarisiRepository.deleteById(id);
        aracCarisiSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/arac-carisis?query=:query} : search for the aracCarisi corresponding
     * to the query.
     *
     * @param query the query of the aracCarisi search.
     * @return the result of the search.
     */
    @GetMapping("/_search/arac-carisis")
    public List<AracCarisi> searchAracCarisis(@RequestParam String query) {
        log.debug("REST request to search AracCarisis for query {}", query);
        return StreamSupport
            .stream(aracCarisiSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
