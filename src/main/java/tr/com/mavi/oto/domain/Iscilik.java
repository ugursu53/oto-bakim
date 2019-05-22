package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Iscilik.
 */
@Entity
@Table(name = "iscilik")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "iscilik")
public class Iscilik implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "fiyat")
    private Double fiyat;

    @Column(name = "iskonto")
    private Double iskonto;

    @ManyToOne
    @JsonIgnoreProperties("isciliks")
    private IsEmri isEmri;

    @ManyToOne
    @JsonIgnoreProperties("isciliks")
    private IscilikTipi tipi;

    @ManyToOne
    @JsonIgnoreProperties("isciliks")
    private Personel personel;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAciklama() {
        return aciklama;
    }

    public Iscilik aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Double getFiyat() {
        return fiyat;
    }

    public Iscilik fiyat(Double fiyat) {
        this.fiyat = fiyat;
        return this;
    }

    public void setFiyat(Double fiyat) {
        this.fiyat = fiyat;
    }

    public Double getIskonto() {
        return iskonto;
    }

    public Iscilik iskonto(Double iskonto) {
        this.iskonto = iskonto;
        return this;
    }

    public void setIskonto(Double iskonto) {
        this.iskonto = iskonto;
    }

    public IsEmri getIsEmri() {
        return isEmri;
    }

    public Iscilik isEmri(IsEmri isEmri) {
        this.isEmri = isEmri;
        return this;
    }

    public void setIsEmri(IsEmri isEmri) {
        this.isEmri = isEmri;
    }

    public IscilikTipi getTipi() {
        return tipi;
    }

    public Iscilik tipi(IscilikTipi iscilikTipi) {
        this.tipi = iscilikTipi;
        return this;
    }

    public void setTipi(IscilikTipi iscilikTipi) {
        this.tipi = iscilikTipi;
    }

    public Personel getPersonel() {
        return personel;
    }

    public Iscilik personel(Personel personel) {
        this.personel = personel;
        return this;
    }

    public void setPersonel(Personel personel) {
        this.personel = personel;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Iscilik)) {
            return false;
        }
        return id != null && id.equals(((Iscilik) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Iscilik{" +
            "id=" + getId() +
            ", aciklama='" + getAciklama() + "'" +
            ", fiyat=" + getFiyat() +
            ", iskonto=" + getIskonto() +
            "}";
    }
}
