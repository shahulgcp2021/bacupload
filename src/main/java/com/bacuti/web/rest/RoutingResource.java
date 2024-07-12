package com.bacuti.web.rest;

import com.bacuti.domain.Routing;
import com.bacuti.repository.RoutingRepository;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bacuti.domain.Routing}.
 */
@RestController
@RequestMapping("/api/routings")
@Transactional
public class RoutingResource {

    private final Logger log = LoggerFactory.getLogger(RoutingResource.class);

    private static final String ENTITY_NAME = "uploadServiceRouting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoutingRepository routingRepository;

    public RoutingResource(RoutingRepository routingRepository) {
        this.routingRepository = routingRepository;
    }

    /**
     * {@code POST  /routings} : Create a new routing.
     *
     * @param routing the routing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new routing, or with status {@code 400 (Bad Request)} if the routing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Routing> createRouting(@RequestBody Routing routing) throws URISyntaxException {
        log.debug("REST request to save Routing : {}", routing);
        if (routing.getId() != null) {
            throw new BadRequestAlertException("A new routing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        routing = routingRepository.save(routing);
        return ResponseEntity.created(new URI("/api/routings/" + routing.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, routing.getId().toString()))
            .body(routing);
    }

    /**
     * {@code PUT  /routings/:id} : Updates an existing routing.
     *
     * @param id the id of the routing to save.
     * @param routing the routing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routing,
     * or with status {@code 400 (Bad Request)} if the routing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the routing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Routing> updateRouting(@PathVariable(value = "id", required = false) final Long id, @RequestBody Routing routing)
        throws URISyntaxException {
        log.debug("REST request to update Routing : {}, {}", id, routing);
        if (routing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        routing = routingRepository.save(routing);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, routing.getId().toString()))
            .body(routing);
    }

    /**
     * {@code PATCH  /routings/:id} : Partial updates given fields of an existing routing, field will ignore if it is null
     *
     * @param id the id of the routing to save.
     * @param routing the routing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated routing,
     * or with status {@code 400 (Bad Request)} if the routing is not valid,
     * or with status {@code 404 (Not Found)} if the routing is not found,
     * or with status {@code 500 (Internal Server Error)} if the routing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Routing> partialUpdateRouting(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Routing routing
    ) throws URISyntaxException {
        log.debug("REST request to partial update Routing partially : {}, {}", id, routing);
        if (routing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, routing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!routingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Routing> result = routingRepository
            .findById(routing.getId())
            .map(existingRouting -> {
                if (routing.getStep() != null) {
                    existingRouting.setStep(routing.getStep());
                }
                if (routing.getDuration() != null) {
                    existingRouting.setDuration(routing.getDuration());
                }

                return existingRouting;
            })
            .map(routingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, routing.getId().toString())
        );
    }

    /**
     * {@code GET  /routings} : get all the routings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of routings in body.
     */
    @GetMapping("")
    public List<Routing> getAllRoutings() {
        log.debug("REST request to get all Routings");
        return routingRepository.findAll();
    }

    /**
     * {@code GET  /routings/:id} : get the "id" routing.
     *
     * @param id the id of the routing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the routing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Routing> getRouting(@PathVariable("id") Long id) {
        log.debug("REST request to get Routing : {}", id);
        Optional<Routing> routing = routingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(routing);
    }

    /**
     * {@code DELETE  /routings/:id} : delete the "id" routing.
     *
     * @param id the id of the routing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRouting(@PathVariable("id") Long id) {
        log.debug("REST request to delete Routing : {}", id);
        routingRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
