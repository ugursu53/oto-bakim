package tr.com.mavi.oto.web.rest;

import org.elasticsearch.index.query.Operator;
import tr.com.mavi.oto.domain.Arac;
import tr.com.mavi.oto.repository.AracCarisiRepository;
import tr.com.mavi.oto.repository.AracRepository;
import tr.com.mavi.oto.repository.search.AracSearchRepository;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.Arac}.
 */
@RestController
@RequestMapping("/api")
public class AracResource {

    private final Logger log = LoggerFactory.getLogger(AracResource.class);

    private static final String ENTITY_NAME = "arac";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AracRepository aracRepository;
    private final AracSearchRepository aracSearchRepository;
    private final AracCarisiRepository aracCarisiRepository;

    public AracResource(AracRepository aracRepository, AracSearchRepository aracSearchRepository, AracCarisiRepository aracCarisiRepository) {
        this.aracRepository = aracRepository;
        this.aracSearchRepository = aracSearchRepository;
        this.aracCarisiRepository = aracCarisiRepository;
    }

    /**
     * {@code POST  /aracs} : Create a new arac.
     *
     * @param arac the arac to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arac, or with status {@code 400 (Bad Request)} if the arac has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aracs")
    public ResponseEntity<Arac> createArac(@Valid @RequestBody Arac arac) throws URISyntaxException {
        log.debug("REST request to save Arac : {}", arac);
        if (arac.getId() != null) {
            throw new BadRequestAlertException("A new arac cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Arac result = aracRepository.save(arac);
        aracSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/aracs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aracs} : Updates an existing arac.
     *
     * @param arac the arac to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arac,
     * or with status {@code 400 (Bad Request)} if the arac is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arac couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aracs")
    public ResponseEntity<Arac> updateArac(@Valid @RequestBody Arac arac) throws URISyntaxException {
        log.debug("REST request to update Arac : {}", arac);
        if (arac.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Arac result = aracRepository.save(arac);
        aracSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arac.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /aracs} : get all the aracs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aracs in body.
     */
    @GetMapping("/aracs")
    public ResponseEntity<List<Arac>> getAllAracs(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Aracs");
        Page<Arac> page = aracRepository.findAll(pageable);
        page.getContent().forEach(arac -> arac.setAktifCari(aracCarisiRepository.findFirstByAracIdAndAktifTrue(arac.getId())));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aracs/:id} : get the "id" arac.
     *
     * @param id the id of the arac to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arac, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aracs/{id}")
    public ResponseEntity<Arac> getArac(@PathVariable Long id) {
        log.debug("REST request to get Arac : {}", id);
        Optional<Arac> arac = aracRepository.findById(id);
        arac.ifPresent(a -> a.setAktifCari(aracCarisiRepository.findFirstByAracIdAndAktifTrue(a.getId())));
        return ResponseUtil.wrapOrNotFound(arac);
    }

    /**
     * {@code DELETE  /aracs/:id} : delete the "id" arac.
     *
     * @param id the id of the arac to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aracs/{id}")
    public ResponseEntity<Void> deleteArac(@PathVariable Long id) {
        log.debug("REST request to delete Arac : {}", id);
        aracRepository.deleteById(id);
        aracSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/aracs?query=:query} : search for the arac corresponding
     * to the query.
     *
     * @param query the query of the arac search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/aracs")
    public ResponseEntity<List<Arac>> searchAracs(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Aracs for query {}", query);
        aracSearchRepository.refresh();
        Page<Arac> page = aracSearchRepository.search(queryStringQuery(SearchUtil.normalizedQuery(query)).defaultOperator(Operator.AND), pageable);
        page.getContent().forEach(arac -> arac.setAktifCari(aracCarisiRepository.findFirstByAracIdAndAktifTrue(arac.getId())));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
