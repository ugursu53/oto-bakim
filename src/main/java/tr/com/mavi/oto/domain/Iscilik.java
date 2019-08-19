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
 * A Iscilik.
 */
@Setter
@Getter
@Entity
@Table(name = "iscilik")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "iscilik")
public class Iscilik implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "fiyat")
    private Double fiyat;

    @Column(name = "iskonto")
    private Double iskonto;

    @ManyToOne
    @JsonIgnoreProperties("isciliks")
    private IscilikTipi tipi;

    @ManyToOne
    private Personel personel;

    public Iscilik aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public Iscilik fiyat(Double fiyat) {
        this.fiyat = fiyat;
        return this;
    }

    public Iscilik iskonto(Double iskonto) {
        this.iskonto = iskonto;
        return this;
    }

    public Iscilik tipi(IscilikTipi iscilikTipi) {
        this.tipi = iscilikTipi;
        return this;
    }

    public Iscilik personel(Personel personel) {
        this.personel = personel;
        return this;
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
