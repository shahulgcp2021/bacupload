package com.bacuti.web.rest;

import com.bacuti.common.errors.BusinessException;
import com.bacuti.communication.InterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/${app.api.version}/interservice")
@Transactional
public class InterServiceCommunication {

    private final Logger log = LoggerFactory.getLogger(InterServiceCommunication.class);

    private static final String ENTITY_NAME = "interservice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterService interService;

    public InterServiceCommunication(InterService interService) {
        this.interService = interService;
    }

    @GetMapping("")
    public ResponseEntity<String> requestToCreateDB(@RequestParam(defaultValue = "0") String domain) throws URISyntaxException {
        log.debug("REST request to create DB");

        if (StringUtils.isEmpty(domain)) {
            throw new BusinessException("Empty company name provided", HttpStatus.BAD_REQUEST.value());
        }

        String success = interService.createDBForCompany(domain);

        return ResponseEntity.created(new URI("/api/v1/interservice/" + domain))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, domain))
            .body(success);
    }


}

