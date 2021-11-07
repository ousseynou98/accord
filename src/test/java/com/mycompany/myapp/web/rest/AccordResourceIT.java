package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Accord;
import com.mycompany.myapp.domain.enumeration.ValidationDC;
import com.mycompany.myapp.domain.enumeration.ValidationR;
import com.mycompany.myapp.repository.AccordRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccordResourceIT {

    private static final String DEFAULT_PARTENAIRE = "AAAAAAAAAA";
    private static final String UPDATED_PARTENAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAINE = "AAAAAAAAAA";
    private static final String UPDATED_DOMAINE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DURE = "AAAAAAAAAA";
    private static final String UPDATED_DURE = "BBBBBBBBBB";

    private static final String DEFAULT_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_ZONE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NATURE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE = "BBBBBBBBBB";

    private static final ValidationDC DEFAULT_VALIDATION_DIRCOOP = ValidationDC.VALIDE;
    private static final ValidationDC UPDATED_VALIDATION_DIRCOOP = ValidationDC.INVALIDE;

    private static final ValidationR DEFAULT_VALIDATION_RECTEUR = ValidationR.VALIDE;
    private static final ValidationR UPDATED_VALIDATION_RECTEUR = ValidationR.INVALIDE;

    private static final String ENTITY_API_URL = "/api/accords";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccordRepository accordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccordMockMvc;

    private Accord accord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accord createEntity(EntityManager em) {
        Accord accord = new Accord()
            .partenaire(DEFAULT_PARTENAIRE)
            .domaine(DEFAULT_DOMAINE)
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE)
            .dure(DEFAULT_DURE)
            .zone(DEFAULT_ZONE)
            .type(DEFAULT_TYPE)
            .nature(DEFAULT_NATURE)
            .validationDircoop(DEFAULT_VALIDATION_DIRCOOP)
            .validationRecteur(DEFAULT_VALIDATION_RECTEUR);
        return accord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accord createUpdatedEntity(EntityManager em) {
        Accord accord = new Accord()
            .partenaire(UPDATED_PARTENAIRE)
            .domaine(UPDATED_DOMAINE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .dure(UPDATED_DURE)
            .zone(UPDATED_ZONE)
            .type(UPDATED_TYPE)
            .nature(UPDATED_NATURE)
            .validationDircoop(UPDATED_VALIDATION_DIRCOOP)
            .validationRecteur(UPDATED_VALIDATION_RECTEUR);
        return accord;
    }

    @BeforeEach
    public void initTest() {
        accord = createEntity(em);
    }

    @Test
    @Transactional
    void createAccord() throws Exception {
        int databaseSizeBeforeCreate = accordRepository.findAll().size();
        // Create the Accord
        restAccordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accord)))
            .andExpect(status().isCreated());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeCreate + 1);
        Accord testAccord = accordList.get(accordList.size() - 1);
        assertThat(testAccord.getPartenaire()).isEqualTo(DEFAULT_PARTENAIRE);
        assertThat(testAccord.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testAccord.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccord.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAccord.getDure()).isEqualTo(DEFAULT_DURE);
        assertThat(testAccord.getZone()).isEqualTo(DEFAULT_ZONE);
        assertThat(testAccord.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAccord.getNature()).isEqualTo(DEFAULT_NATURE);
        assertThat(testAccord.getValidationDircoop()).isEqualTo(DEFAULT_VALIDATION_DIRCOOP);
        assertThat(testAccord.getValidationRecteur()).isEqualTo(DEFAULT_VALIDATION_RECTEUR);
    }

    @Test
    @Transactional
    void createAccordWithExistingId() throws Exception {
        // Create the Accord with an existing ID
        accord.setId(1L);

        int databaseSizeBeforeCreate = accordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accord)))
            .andExpect(status().isBadRequest());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccords() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        // Get all the accordList
        restAccordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accord.getId().intValue())))
            .andExpect(jsonPath("$.[*].partenaire").value(hasItem(DEFAULT_PARTENAIRE)))
            .andExpect(jsonPath("$.[*].domaine").value(hasItem(DEFAULT_DOMAINE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].dure").value(hasItem(DEFAULT_DURE)))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_ZONE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE)))
            .andExpect(jsonPath("$.[*].validationDircoop").value(hasItem(DEFAULT_VALIDATION_DIRCOOP.toString())))
            .andExpect(jsonPath("$.[*].validationRecteur").value(hasItem(DEFAULT_VALIDATION_RECTEUR.toString())));
    }

    @Test
    @Transactional
    void getAccord() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        // Get the accord
        restAccordMockMvc
            .perform(get(ENTITY_API_URL_ID, accord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accord.getId().intValue()))
            .andExpect(jsonPath("$.partenaire").value(DEFAULT_PARTENAIRE))
            .andExpect(jsonPath("$.domaine").value(DEFAULT_DOMAINE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.dure").value(DEFAULT_DURE))
            .andExpect(jsonPath("$.zone").value(DEFAULT_ZONE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.nature").value(DEFAULT_NATURE))
            .andExpect(jsonPath("$.validationDircoop").value(DEFAULT_VALIDATION_DIRCOOP.toString()))
            .andExpect(jsonPath("$.validationRecteur").value(DEFAULT_VALIDATION_RECTEUR.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccord() throws Exception {
        // Get the accord
        restAccordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccord() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        int databaseSizeBeforeUpdate = accordRepository.findAll().size();

        // Update the accord
        Accord updatedAccord = accordRepository.findById(accord.getId()).get();
        // Disconnect from session so that the updates on updatedAccord are not directly saved in db
        em.detach(updatedAccord);
        updatedAccord
            .partenaire(UPDATED_PARTENAIRE)
            .domaine(UPDATED_DOMAINE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .dure(UPDATED_DURE)
            .zone(UPDATED_ZONE)
            .type(UPDATED_TYPE)
            .nature(UPDATED_NATURE)
            .validationDircoop(UPDATED_VALIDATION_DIRCOOP)
            .validationRecteur(UPDATED_VALIDATION_RECTEUR);

        restAccordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccord))
            )
            .andExpect(status().isOk());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
        Accord testAccord = accordList.get(accordList.size() - 1);
        assertThat(testAccord.getPartenaire()).isEqualTo(UPDATED_PARTENAIRE);
        assertThat(testAccord.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testAccord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAccord.getDure()).isEqualTo(UPDATED_DURE);
        assertThat(testAccord.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testAccord.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccord.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testAccord.getValidationDircoop()).isEqualTo(UPDATED_VALIDATION_DIRCOOP);
        assertThat(testAccord.getValidationRecteur()).isEqualTo(UPDATED_VALIDATION_RECTEUR);
    }

    @Test
    @Transactional
    void putNonExistingAccord() throws Exception {
        int databaseSizeBeforeUpdate = accordRepository.findAll().size();
        accord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccord() throws Exception {
        int databaseSizeBeforeUpdate = accordRepository.findAll().size();
        accord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccord() throws Exception {
        int databaseSizeBeforeUpdate = accordRepository.findAll().size();
        accord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccordWithPatch() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        int databaseSizeBeforeUpdate = accordRepository.findAll().size();

        // Update the accord using partial update
        Accord partialUpdatedAccord = new Accord();
        partialUpdatedAccord.setId(accord.getId());

        partialUpdatedAccord
            .partenaire(UPDATED_PARTENAIRE)
            .domaine(UPDATED_DOMAINE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .dure(UPDATED_DURE)
            .zone(UPDATED_ZONE)
            .type(UPDATED_TYPE)
            .validationDircoop(UPDATED_VALIDATION_DIRCOOP);

        restAccordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccord))
            )
            .andExpect(status().isOk());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
        Accord testAccord = accordList.get(accordList.size() - 1);
        assertThat(testAccord.getPartenaire()).isEqualTo(UPDATED_PARTENAIRE);
        assertThat(testAccord.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testAccord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAccord.getDure()).isEqualTo(UPDATED_DURE);
        assertThat(testAccord.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testAccord.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccord.getNature()).isEqualTo(DEFAULT_NATURE);
        assertThat(testAccord.getValidationDircoop()).isEqualTo(UPDATED_VALIDATION_DIRCOOP);
        assertThat(testAccord.getValidationRecteur()).isEqualTo(DEFAULT_VALIDATION_RECTEUR);
    }

    @Test
    @Transactional
    void fullUpdateAccordWithPatch() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        int databaseSizeBeforeUpdate = accordRepository.findAll().size();

        // Update the accord using partial update
        Accord partialUpdatedAccord = new Accord();
        partialUpdatedAccord.setId(accord.getId());

        partialUpdatedAccord
            .partenaire(UPDATED_PARTENAIRE)
            .domaine(UPDATED_DOMAINE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .dure(UPDATED_DURE)
            .zone(UPDATED_ZONE)
            .type(UPDATED_TYPE)
            .nature(UPDATED_NATURE)
            .validationDircoop(UPDATED_VALIDATION_DIRCOOP)
            .validationRecteur(UPDATED_VALIDATION_RECTEUR);

        restAccordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccord))
            )
            .andExpect(status().isOk());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
        Accord testAccord = accordList.get(accordList.size() - 1);
        assertThat(testAccord.getPartenaire()).isEqualTo(UPDATED_PARTENAIRE);
        assertThat(testAccord.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testAccord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAccord.getDure()).isEqualTo(UPDATED_DURE);
        assertThat(testAccord.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testAccord.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccord.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testAccord.getValidationDircoop()).isEqualTo(UPDATED_VALIDATION_DIRCOOP);
        assertThat(testAccord.getValidationRecteur()).isEqualTo(UPDATED_VALIDATION_RECTEUR);
    }

    @Test
    @Transactional
    void patchNonExistingAccord() throws Exception {
        int databaseSizeBeforeUpdate = accordRepository.findAll().size();
        accord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccord() throws Exception {
        int databaseSizeBeforeUpdate = accordRepository.findAll().size();
        accord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accord))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccord() throws Exception {
        int databaseSizeBeforeUpdate = accordRepository.findAll().size();
        accord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accord in the database
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccord() throws Exception {
        // Initialize the database
        accordRepository.saveAndFlush(accord);

        int databaseSizeBeforeDelete = accordRepository.findAll().size();

        // Delete the accord
        restAccordMockMvc
            .perform(delete(ENTITY_API_URL_ID, accord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Accord> accordList = accordRepository.findAll();
        assertThat(accordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
