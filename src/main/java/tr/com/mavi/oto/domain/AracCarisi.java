package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AracCarisi.
 */
@Entity
@Table(name = "arac_carisi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "araccarisi")
public class AracCarisi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "aktif", nullable = false)
    private Boolean aktif;

    @ManyToOne
    @JsonIgnoreProperties("aracCarisis")
    private Arac arac;

    @ManyToOne
    @JsonIgnoreProperties("aracCarisis")
    private Cari cari;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAktif() {
        return aktif;
    }

    public AracCarisi aktif(Boolean aktif) {
        this.aktif = aktif;
        return this;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public Arac getArac() {
        return arac;
    }

    public AracCarisi arac(Arac arac) {
        this.arac = arac;
        return this;
    }

    public void setArac(Arac arac) {
        this.arac = arac;
    }

    public Cari getCari() {
        return cari;
    }

    public AracCarisi cari(Cari cari) {
        this.cari = cari;
        return this;
    }

    public void setCari(Cari cari) {
        this.cari = cari;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AracCarisi)) {
            return false;
        }
        return id != null && id.equals(((AracCarisi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AracCarisi{" +
            "id=" + getId() +
            ", aktif='" + isAktif() + "'" +
            "}";
    }
}
