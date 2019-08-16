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
 * A ParcaTipi.
 */
@Setter
@Getter
@Entity
@Table(name = "parca_tipi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "parcatipi")
public class ParcaTipi extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "ad", nullable = false)
    private String ad;

    @Column(name = "kodu")
    private String kodu;

    @Column(name = "varsayilan_fiyati")
    private Double varsayilanFiyati;

    @Column(name = "aciklama")
    private String aciklama;

    public ParcaTipi ad(String ad) {
        this.ad = ad;
        return this;
    }

    public ParcaTipi kodu(String kodu) {
        this.kodu = kodu;
        return this;
    }

    public ParcaTipi varsayilanFiyati(Double varsayilanFiyati) {
        this.varsayilanFiyati = varsayilanFiyati;
        return this;
    }

    public ParcaTipi aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParcaTipi)) {
            return false;
        }
        return id != null && id.equals(((ParcaTipi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ParcaTipi{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            ", kodu='" + getKodu() + "'" +
            ", varsayilanFiyati=" + getVarsayilanFiyati() +
            ", aciklama='" + getAciklama() + "'" +
            "}";
    }
}
