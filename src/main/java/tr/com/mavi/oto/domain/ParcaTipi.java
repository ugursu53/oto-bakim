package tr.com.mavi.oto.domain;


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
@Entity
@Table(name = "parca_tipi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "parcatipi")
public class ParcaTipi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "ad", nullable = false)
    private String ad;

    @Column(name = "kodu")
    private String kodu;

    @Column(name = "varsayilan_fiyati")
    private Double varsayilanFiyati;

    @Column(name = "aciklama")
    private String aciklama;

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

    public ParcaTipi ad(String ad) {
        this.ad = ad;
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getKodu() {
        return kodu;
    }

    public ParcaTipi kodu(String kodu) {
        this.kodu = kodu;
        return this;
    }

    public void setKodu(String kodu) {
        this.kodu = kodu;
    }

    public Double getVarsayilanFiyati() {
        return varsayilanFiyati;
    }

    public ParcaTipi varsayilanFiyati(Double varsayilanFiyati) {
        this.varsayilanFiyati = varsayilanFiyati;
        return this;
    }

    public void setVarsayilanFiyati(Double varsayilanFiyati) {
        this.varsayilanFiyati = varsayilanFiyati;
    }

    public String getAciklama() {
        return aciklama;
    }

    public ParcaTipi aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
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
