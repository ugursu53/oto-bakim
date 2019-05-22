package tr.com.mavi.oto.web.rest;

import tr.com.mavi.oto.domain.IscilikGrubu;
import tr.com.mavi.oto.repository.IscilikGrubuRepository;
import tr.com.mavi.oto.repository.search.IscilikGrubuSearchRepository;
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
 * REST controller for managing {@link tr.com.mavi.oto.domain.IscilikGrubu}.
 */
@RestController
@RequestMapping("/api")
public class IscilikGrubuResource {

    private final Logger log = LoggerFactory.getLogger(IscilikGrubuResource.class);

    private static final String ENTITY_NAME = "iscilikGrubu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IscilikGrubuRepository iscilikGrubuRepository;

    private final IscilikGrubuSearchRepository iscilikGrubuSearchRepository;

    public IscilikGrubuResource(IscilikGrubuRepository iscilikGrubuRepository, IscilikGrubuSearchRepository iscilikGrubuSearchRepository) {
        this.iscilikGrubuRepository = iscilikGrubuRepository;
        this.iscilikGrubuSearchRepository = iscilikGrubuSearchRepository;
    }

    /**
     * {@code POST  /iscilik-grubus} : Create a new iscilikGrubu.
     *
     * @param iscilikGrubu the iscilikGrubu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iscilikGrubu, or with status {@code 400 (Bad Request)} if the iscilikGrubu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/iscilik-grubus")
    public ResponseEntity<IscilikGrubu> createIscilikGrubu(@RequestBody IscilikGrubu iscilikGrubu) throws URISyntaxException {
        log.debug("REST request to save IscilikGrubu : {}", iscilikGrubu);
        if (iscilikGrubu.getId() != null) {
            throw new BadRequestAlertException("A new iscilikGrubu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IscilikGrubu result = iscilikGrubuRepository.save(iscilikGrubu);
        iscilikGrubuSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/iscilik-grubus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /iscilik-grubus} : Updates an existing iscilikGrubu.
     *
     * @param iscilikGrubu the iscilikGrubu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iscilikGrubu,
     * or with status {@code 400 (Bad Request)} if the iscilikGrubu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iscilikGrubu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/iscilik-grubus")
    public ResponseEntity<IscilikGrubu> updateIscilikGrubu(@RequestBody IscilikGrubu iscilikGrubu) throws URISyntaxException {
        log.debug("REST request to update IscilikGrubu : {}", iscilikGrubu);
        if (iscilikGrubu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IscilikGrubu result = iscilikGrubuRepository.save(iscilikGrubu);
        iscilikGrubuSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iscilikGrubu.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /iscilik-grubus} : get all the iscilikGrubus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iscilikGrubus in body.
     */
    @GetMapping("/iscilik-grubus")
    public ResponseEntity<List<IscilikGrubu>> getAllIscilikGrubus(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of IscilikGrubus");
        Page<IscilikGrubu> page = iscilikGrubuRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /iscilik-grubus/:id} : get the "id" iscilikGrubu.
     *
     * @param id the id of the iscilikGrubu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iscilikGrubu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/iscilik-grubus/{id}")
    public ResponseEntity<IscilikGrubu> getIscilikGrubu(@PathVariable Long id) {
        log.debug("REST request to get IscilikGrubu : {}", id);
        Optional<IscilikGrubu> iscilikGrubu = iscilikGrubuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(iscilikGrubu);
    }

    /**
     * {@code DELETE  /iscilik-grubus/:id} : delete the "id" iscilikGrubu.
     *
     * @param id the id of the iscilikGrubu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/iscilik-grubus/{id}")
    public ResponseEntity<Void> deleteIscilikGrubu(@PathVariable Long id) {
        log.debug("REST request to delete IscilikGrubu : {}", id);
        iscilikGrubuRepository.deleteById(id);
        iscilikGrubuSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/iscilik-grubus?query=:query} : search for the iscilikGrubu corresponding
     * to the query.
     *
     * @param query the query of the iscilikGrubu search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/iscilik-grubus")
    public ResponseEntity<List<IscilikGrubu>> searchIscilikGrubus(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of IscilikGrubus for query {}", query);
        Page<IscilikGrubu> page = iscilikGrubuSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
