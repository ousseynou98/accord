package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.ValidationDC;
import com.mycompany.myapp.domain.enumeration.ValidationR;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Accord.
 */
@Entity
@Table(name = "accord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Accord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "partenaire")
    private String partenaire;

    @Column(name = "domaine")
    private String domaine;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Instant date;

    @Column(name = "dure")
    private String dure;

    @Column(name = "zone")
    private String zone;

    @Column(name = "type")
    private String type;

    @Column(name = "nature")
    private String nature;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_dircoop")
    private ValidationDC validationDircoop;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_recteur")
    private ValidationR validationRecteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Accord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartenaire() {
        return this.partenaire;
    }

    public Accord partenaire(String partenaire) {
        this.setPartenaire(partenaire);
        return this;
    }

    public void setPartenaire(String partenaire) {
        this.partenaire = partenaire;
    }

    public String getDomaine() {
        return this.domaine;
    }

    public Accord domaine(String domaine) {
        this.setDomaine(domaine);
        return this;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public String getDescription() {
        return this.description;
    }

    public Accord description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate() {
        return this.date;
    }

    public Accord date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDure() {
        return this.dure;
    }

    public Accord dure(String dure) {
        this.setDure(dure);
        return this;
    }

    public void setDure(String dure) {
        this.dure = dure;
    }

    public String getZone() {
        return this.zone;
    }

    public Accord zone(String zone) {
        this.setZone(zone);
        return this;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getType() {
        return this.type;
    }

    public Accord type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNature() {
        return this.nature;
    }

    public Accord nature(String nature) {
        this.setNature(nature);
        return this;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public ValidationDC getValidationDircoop() {
        return this.validationDircoop;
    }

    public Accord validationDircoop(ValidationDC validationDircoop) {
        this.setValidationDircoop(validationDircoop);
        return this;
    }

    public void setValidationDircoop(ValidationDC validationDircoop) {
        this.validationDircoop = validationDircoop;
    }

    public ValidationR getValidationRecteur() {
        return this.validationRecteur;
    }

    public Accord validationRecteur(ValidationR validationRecteur) {
        this.setValidationRecteur(validationRecteur);
        return this;
    }

    public void setValidationRecteur(ValidationR validationRecteur) {
        this.validationRecteur = validationRecteur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accord)) {
            return false;
        }
        return id != null && id.equals(((Accord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Accord{" +
            "id=" + getId() +
            ", partenaire='" + getPartenaire() + "'" +
            ", domaine='" + getDomaine() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", dure='" + getDure() + "'" +
            ", zone='" + getZone() + "'" +
            ", type='" + getType() + "'" +
            ", nature='" + getNature() + "'" +
            ", validationDircoop='" + getValidationDircoop() + "'" +
            ", validationRecteur='" + getValidationRecteur() + "'" +
            "}";
    }
}
