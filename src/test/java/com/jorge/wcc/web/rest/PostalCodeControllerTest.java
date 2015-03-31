package com.jorge.wcc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.jorge.wcc.WccPostalCodeApp;
import com.jorge.wcc.domain.PostalCode;
import com.jorge.wcc.repository.PostalCodeRepository;

/**
 * Test class for the PostalCodeVontroller REST controller.
 *
 * @see PostalCodeController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WccPostalCodeApp.class)
@WebAppConfiguration
@IntegrationTest
public class PostalCodeControllerTest {
   private static final String DEFAULT_POSTAL_CODE = "SAMPLE_TEXT";
   private static final String UPDATED_POSTAL_CODE = "UPDATED_TEXT";

   private static final BigDecimal DEFAULT_LATITUDE = BigDecimal.ZERO;
   private static final BigDecimal UPDATED_LATITUDE = BigDecimal.ONE;

   private static final BigDecimal DEFAULT_LONGITUDE = BigDecimal.ZERO;
   private static final BigDecimal UPDATED_LONGITUDE = BigDecimal.ONE;

   @Autowired
   private PostalCodeRepository postalCodeRepository;

   private MockMvc restPostalCodeMockMvc;

   private PostalCode postalCode;

   @PostConstruct
   public void setup() {
      MockitoAnnotations.initMocks(this);
      PostalCodeController postalCodeResource = new PostalCodeController();
      ReflectionTestUtils.setField(postalCodeResource, "postalCodeRepository", postalCodeRepository);
      this.restPostalCodeMockMvc = MockMvcBuilders.standaloneSetup(postalCodeResource).build();
   }

   @Before
   public void initTest() {
      postalCodeRepository.deleteAll();
      postalCode = new PostalCode();
      postalCode.setPostalCode(DEFAULT_POSTAL_CODE);
      postalCode.setLatitude(DEFAULT_LATITUDE);
      postalCode.setLongitude(DEFAULT_LONGITUDE);
   }

   @Test
   @Transactional
   public void createPostalCode() throws Exception {
      // Validate the database is empty
      assertThat(postalCodeRepository.findAll()).hasSize(0);

      // Create the PostalCode
      restPostalCodeMockMvc.perform(post("/wcc/rest/postalCodes").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(postalCode))).andExpect(status().isCreated());

      // Validate the PostalCode in the database
      List<PostalCode> postalCodes = postalCodeRepository.findAll();
      assertThat(postalCodes).hasSize(1);
      PostalCode testPostalCode = postalCodes.iterator().next();
      assertThat(testPostalCode.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
      assertThat(testPostalCode.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
      assertThat(testPostalCode.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
   }

   @Test
   @Transactional
   public void getAllPostalCodes() throws Exception {
      // Initialize the database
      postalCodeRepository.saveAndFlush(postalCode);

      // Get all the postalCodes
      restPostalCodeMockMvc.perform(get("/wcc/rest/postalCodes")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(postalCode.getId().intValue())).andExpect(jsonPath("$.[0].postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.[0].latitude").value(DEFAULT_LATITUDE.intValue())).andExpect(jsonPath("$.[0].longitude").value(DEFAULT_LONGITUDE.intValue()));
   }

   @Test
   @Transactional
   public void getPostalCode() throws Exception {
      // Initialize the database
      postalCodeRepository.saveAndFlush(postalCode);

      // Get the postalCode
      restPostalCodeMockMvc.perform(get("/wcc/rest/postalCodes/{id}", postalCode.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(postalCode.getId().intValue())).andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue())).andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()));
   }

   @Test
   @Transactional
   public void getNonExistingPostalCode() throws Exception {
      // Get the postalCode
      restPostalCodeMockMvc.perform(get("/wcc/rest/postalCodes/{id}", 1L)).andExpect(status().isNotFound());
   }

   @Test
   @Transactional
   public void updatePostalCode() throws Exception {
      // Initialize the database
      postalCodeRepository.saveAndFlush(postalCode);

      // Update the postalCode
      postalCode.setPostalCode(UPDATED_POSTAL_CODE);
      postalCode.setLatitude(UPDATED_LATITUDE);
      postalCode.setLongitude(UPDATED_LONGITUDE);
      restPostalCodeMockMvc.perform(put("/wcc/rest/postalCodes").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(postalCode))).andExpect(status().isOk());

      // Validate the PostalCode in the database
      List<PostalCode> postalCodes = postalCodeRepository.findAll();
      assertThat(postalCodes).hasSize(1);
      PostalCode testPostalCode = postalCodes.iterator().next();
      assertThat(testPostalCode.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
      assertThat(testPostalCode.getLatitude()).isEqualTo(UPDATED_LATITUDE);
      assertThat(testPostalCode.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
   }

   @Test
   @Transactional
   public void deletePostalCode() throws Exception {
      // Initialize the database
      postalCodeRepository.saveAndFlush(postalCode);

      // Get the postalCode
      restPostalCodeMockMvc.perform(delete("/wcc/rest/postalCodes/{id}", postalCode.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

      // Validate the database is empty
      List<PostalCode> postalCodes = postalCodeRepository.findAll();
      assertThat(postalCodes).hasSize(0);
   }
}
