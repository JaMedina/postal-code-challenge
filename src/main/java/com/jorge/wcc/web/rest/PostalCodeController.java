package com.jorge.wcc.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jorge.wcc.domain.PostalCode;
import com.jorge.wcc.repository.PostalCodeRepository;
import com.jorge.wcc.web.rest.util.PaginationUtil;

@RestController
@RequestMapping(value = "/wcc/rest", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class PostalCodeController {
   private final Logger log = LoggerFactory.getLogger(PostalCodeController.class);

   @Autowired
   private PostalCodeRepository postalCodeRepository;

   @RequestMapping(value = "/postalCodes", method = RequestMethod.POST)
   public ResponseEntity<Void> create(@Valid @RequestBody PostalCode postalCode) throws URISyntaxException {
     postalCode.setPostalCode(postalCode.getPostalCode().toUpperCase());
      log.debug("REST request to save PostalCode : {}", postalCode);
      if (postalCode.getId() != null) {
         return ResponseEntity.badRequest().header("Failure", "A new postalCode cannot already have an ID").build();
      }
      postalCodeRepository.save(postalCode);
      return ResponseEntity.created(new URI("/api/postalCodes/" + postalCode.getId())).build();
   }

   /**
    * PUT /postalCodes -> Updates an existing postalCode.
    */
   @RequestMapping(value = "/postalCodes", method = RequestMethod.PUT)
   public ResponseEntity<Void> update(@Valid @RequestBody PostalCode postalCode) throws URISyntaxException {
     postalCode.setPostalCode(postalCode.getPostalCode().toUpperCase());
      log.debug("REST request to update PostalCode : {}", postalCode);
      if (postalCode.getId() == null) {
         return create(postalCode);
      }
      postalCodeRepository.save(postalCode);
      return ResponseEntity.ok().build();
   }

   /**
    * GET /postalCodes -> get all the postalCodes.
    */
   @RequestMapping(value = "/postalCodes", method = RequestMethod.GET)
   public ResponseEntity<List<PostalCode>> getAll(@RequestParam(value = "page", required = false) Integer offset, @RequestParam(value = "per_page", required = false) Integer limit)
         throws URISyntaxException {
      Page<PostalCode> page = postalCodeRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postalCodes", offset, limit);
      return new ResponseEntity<List<PostalCode>>(page.getContent(), headers, HttpStatus.OK);
   }

   /**
    * GET /postalCodes/:id -> get the "id" postalCode.
    */
   @RequestMapping(value = "/postalCodes/{id}", method = RequestMethod.GET)
   public ResponseEntity<PostalCode> get(@PathVariable Long id, HttpServletResponse response) {
      log.debug("REST request to get PostalCode : {}", id);
      PostalCode postalCode = postalCodeRepository.findOne(id);
      if (postalCode == null) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(postalCode, HttpStatus.OK);
   }

   /**
    * DELETE /postalCodes/:id -> delete the "id" postalCode.
    */
   @RequestMapping(value = "/postalCodes/{id}", method = RequestMethod.DELETE)
   public void delete(@PathVariable Long id) {
      log.debug("REST request to delete PostalCode : {}", id);
      postalCodeRepository.delete(id);
   }

}
