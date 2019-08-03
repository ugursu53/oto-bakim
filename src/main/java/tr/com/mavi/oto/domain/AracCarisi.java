package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
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
@Setter
@Getter
@Entity
@Table(name = "arac_carisi")
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
    @JsonIgnoreProperties("caris")
    private Arac arac;

    @ManyToOne
    private Cari cari;

    public Boolean isAktif() {
        return aktif;
    }

    public AracCarisi aktif(Boolean aktif) {
        this.aktif = aktif;
        return this;
    }

    public AracCarisi arac(Arac arac) {
        this.arac = arac;
        return this;
    }

    public AracCarisi cari(Cari cari) {
        this.cari = cari;
        return this;
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
