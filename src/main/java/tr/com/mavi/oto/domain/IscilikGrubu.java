package tr.com.mavi.oto.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A IscilikGrubu.
 */
@Setter
@Getter
@Entity
@Table(name = "iscilik_grubu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "iscilikgrubu")
public class IscilikGrubu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "ad")
    private String ad;

    public IscilikGrubu ad(String ad) {
        this.ad = ad;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IscilikGrubu)) {
            return false;
        }
        return id != null && id.equals(((IscilikGrubu) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IscilikGrubu{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            "}";
    }
}
