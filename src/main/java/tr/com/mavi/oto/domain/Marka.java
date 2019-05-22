package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Marka.
 */
@Entity
@Table(name = "marka")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "marka")
public class Marka implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "ad")
    private String ad;

    @OneToMany(mappedBy = "marka")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Model> modellers = new HashSet<>();

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

    public Marka ad(String ad) {
        this.ad = ad;
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public Set<Model> getModellers() {
        return modellers;
    }

    public Marka modellers(Set<Model> models) {
        this.modellers = models;
        return this;
    }

    public Marka addModeller(Model model) {
        this.modellers.add(model);
        model.setMarka(this);
        return this;
    }

    public Marka removeModeller(Model model) {
        this.modellers.remove(model);
        model.setMarka(null);
        return this;
    }

    public void setModellers(Set<Model> models) {
        this.modellers = models;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marka)) {
            return false;
        }
        return id != null && id.equals(((Marka) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Marka{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            "}";
    }
}
