package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.Iscilik;
import tr.com.mavi.oto.repository.IscilikRepository;
import tr.com.mavi.oto.repository.search.IscilikSearchRepository;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.Iscilik}.
 */
@RestController
@RequestMapping("/api")
public class IscilikResource {

    private final Logger log = LoggerFactory.getLogger(IscilikResource.class);

    private static final String ENTITY_NAME = "iscilik";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IscilikRepository iscilikRepository;

    private final IscilikSearchRepository iscilikSearchRepository;

    public IscilikResource(IscilikRepository iscilikRepository, IscilikSearchRepository iscilikSearchRepository) {
        this.iscilikRepository = iscilikRepository;
        this.iscilikSearchRepository = iscilikSearchRepository;
    }

    /**
     * {@code POST  /isciliks} : Create a new iscilik.
     *
     * @param iscilik the iscilik to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iscilik, or with status {@code 400 (Bad Request)} if the iscilik has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/isciliks")
    public ResponseEntity<Iscilik> createIscilik(@RequestBody Iscilik iscilik) throws URISyntaxException {
        log.debug("REST request to save Iscilik : {}", iscilik);
        if (iscilik.getId() != null) {
            throw new BadRequestAlertException("A new iscilik cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Iscilik result = iscilikRepository.save(iscilik);
        iscilikSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/isciliks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /isciliks} : Updates an existing iscilik.
     *
     * @param iscilik the iscilik to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iscilik,
     * or with status {@code 400 (Bad Request)} if the iscilik is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iscilik couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/isciliks")
    public ResponseEntity<Iscilik> updateIscilik(@RequestBody Iscilik iscilik) throws URISyntaxException {
        log.debug("REST request to update Iscilik : {}", iscilik);
        if (iscilik.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Iscilik result = iscilikRepository.save(iscilik);
        iscilikSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iscilik.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /isciliks} : get all the isciliks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isciliks in body.
     */
    @GetMapping("/isciliks")
    public ResponseEntity<List<Iscilik>> getAllIsciliks(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Isciliks");
        Page<Iscilik> page = iscilikRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /isciliks/:id} : get the "id" iscilik.
     *
     * @param id the id of the iscilik to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iscilik, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/isciliks/{id}")
    public ResponseEntity<Iscilik> getIscilik(@PathVariable Long id) {
        log.debug("REST request to get Iscilik : {}", id);
        Optional<Iscilik> iscilik = iscilikRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iscilik);
    }

    /**
     * {@code DELETE  /isciliks/:id} : delete the "id" iscilik.
     *
     * @param id the id of the iscilik to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/isciliks/{id}")
    public ResponseEntity<Void> deleteIscilik(@PathVariable Long id) {
        log.debug("REST request to delete Iscilik : {}", id);
        iscilikRepository.deleteById(id);
        iscilikSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/isciliks?query=:query} : search for the iscilik corresponding
     * to the query.
     *
     * @param query the query of the iscilik search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/isciliks")
    public ResponseEntity<List<Iscilik>> searchIsciliks(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Isciliks for query {}", query);
        Page<Iscilik> page = iscilikSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
