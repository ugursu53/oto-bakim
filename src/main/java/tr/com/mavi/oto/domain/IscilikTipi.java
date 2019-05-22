package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A IscilikTipi.
 */
@Entity
@Table(name = "iscilik_tipi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "isciliktipi")
public class IscilikTipi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "ad")
    private String ad;

    @Column(name = "varsayilan_fiyat")
    private Double varsayilanFiyat;

    @ManyToOne
    @JsonIgnoreProperties("iscilikTipis")
    private IscilikGrubu grubu;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public IscilikTipi ad(String ad) {
        this.ad = ad;
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public Double getVarsayilanFiyat() {
        return varsayilanFiyat;
    }

    public IscilikTipi varsayilanFiyat(Double varsayilanFiyat) {
        this.varsayilanFiyat = varsayilanFiyat;
        return this;
    }

    public void setVarsayilanFiyat(Double varsayilanFiyat) {
        this.varsayilanFiyat = varsayilanFiyat;
    }

    public IscilikGrubu getGrubu() {
        return grubu;
    }

    public IscilikTipi grubu(IscilikGrubu iscilikGrubu) {
        this.grubu = iscilikGrubu;
        return this;
    }

    public void setGrubu(IscilikGrubu iscilikGrubu) {
        this.grubu = iscilikGrubu;
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
