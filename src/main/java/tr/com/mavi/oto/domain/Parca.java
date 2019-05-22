package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Parca.
 */
@Entity
@Table(name = "parca")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "parca")
public class Parca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "fiyati")
    private Double fiyati;

    @ManyToOne
    @JsonIgnoreProperties("parcas")
    private IsEmri isEmri;

    @ManyToOne
    @JsonIgnoreProperties("parcas")
    private ParcaTipi tipi;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFiyati() {
        return fiyati;
    }

    public Parca fiyati(Double fiyati) {
        this.fiyati = fiyati;
        return this;
    }

    public void setFiyati(Double fiyati) {
        this.fiyati = fiyati;
    }

    public IsEmri getIsEmri() {
        return isEmri;
    }

    public Parca isEmri(IsEmri isEmri) {
        this.isEmri = isEmri;
        return this;
    }

    public void setIsEmri(IsEmri isEmri) {
        this.isEmri = isEmri;
    }

    public ParcaTipi getTipi() {
        return tipi;
    }

    public Parca tipi(ParcaTipi parcaTipi) {
        this.tipi = parcaTipi;
        return this;
    }

    public void setTipi(ParcaTipi parcaTipi) {
        this.tipi = parcaTipi;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parca)) {
            return false;
        }
        return id != null && id.equals(((Parca) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Parca{" +
            "id=" + getId() +
            ", fiyati=" + getFiyati() +
            "}";
    }
}
