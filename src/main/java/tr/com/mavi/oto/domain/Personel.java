package tr.com.mavi.oto.domain;


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
 * A Personel.
 */
@Setter
@Getter
@Entity
@Table(name = "personel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "personel")
public class Personel extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "ad", nullable = false)
    private String ad;

    @NotNull
    @Column(name = "soyad", nullable = false)
    private String soyad;

    @NotNull
    @Column(name = "gorevi", nullable = false)
    private String gorevi;

    public Personel ad(String ad) {
        this.ad = ad;
        return this;
    }

    public Personel soyad(String soyad) {
        this.soyad = soyad;
        return this;
    }

    public Personel gorevi(String gorevi) {
        this.gorevi = gorevi;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personel)) {
            return false;
        }
        return id != null && id.equals(((Personel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Personel{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            ", soyad='" + getSoyad() + "'" +
            ", gorevi='" + getGorevi() + "'" +
            "}";
    }
}
