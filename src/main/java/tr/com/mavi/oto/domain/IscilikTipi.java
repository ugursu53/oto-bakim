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
 * A IscilikTipi.
 */
@Setter
@Getter
@Entity
@Table(name = "iscilik_tipi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "isciliktipi")
public class IscilikTipi extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ad")
    private String ad;

    @Column(name = "varsayilan_fiyat")
    private Double varsayilanFiyat;

    @ManyToOne
    @JsonIgnoreProperties("iscilikTipis")
    private IscilikGrubu grubu;

    public IscilikTipi ad(String ad) {
        this.ad = ad;
        return this;
    }

    public IscilikTipi varsayilanFiyat(Double varsayilanFiyat) {
        this.varsayilanFiyat = varsayilanFiyat;
        return this;
    }

    public IscilikTipi grubu(IscilikGrubu iscilikGrubu) {
        this.grubu = iscilikGrubu;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IscilikTipi)) {
            return false;
        }
        return id != null && id.equals(((IscilikTipi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IscilikTipi{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            ", varsayilanFiyat=" + getVarsayilanFiyat() +
            "}";
    }
}
