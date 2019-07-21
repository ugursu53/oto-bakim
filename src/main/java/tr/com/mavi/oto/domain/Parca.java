package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Parca.
 */
@Setter
@Getter
@Entity
@Table(name = "parca")
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

    public Parca fiyati(Double fiyati) {
        this.fiyati = fiyati;
        return this;
    }

    public Parca isEmri(IsEmri isEmri) {
        this.isEmri = isEmri;
        return this;
    }

    public Parca tipi(ParcaTipi parcaTipi) {
        this.tipi = parcaTipi;
        return this;
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
