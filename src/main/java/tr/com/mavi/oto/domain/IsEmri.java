package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import tr.com.mavi.oto.domain.enumeration.IsEmriTipi;

/**
 * Task entity.
 * @author The JHipster team.
 */
@ApiModel(description = "Task entity. @author The JHipster team.")
@Entity
@Table(name = "is_emri")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "isemri")
public class IsEmri implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "gelis_zamani")
    private Instant gelisZamani;

    @NotNull
    @Column(name = "aciklama", nullable = false)
    private String aciklama;

    @NotNull
    @Column(name = "kabul_tarihi", nullable = false)
    private Instant kabulTarihi;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipi", nullable = false)
    private IsEmriTipi tipi;

    @OneToMany(mappedBy = "isEmri")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Iscilik> isciliklers = new HashSet<>();

    @OneToMany(mappedBy = "isEmri")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Parca> parcalars = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("isEmris")
    private Arac arac;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getGelisZamani() {
        return gelisZamani;
    }

    public IsEmri gelisZamani(Instant gelisZamani) {
        this.gelisZamani = gelisZamani;
        return this;
    }

    public void setGelisZamani(Instant gelisZamani) {
        this.gelisZamani = gelisZamani;
    }

    public String getAciklama() {
        return aciklama;
    }

    public IsEmri aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Instant getKabulTarihi() {
        return kabulTarihi;
    }

    public IsEmri kabulTarihi(Instant kabulTarihi) {
        this.kabulTarihi = kabulTarihi;
        return this;
    }

    public void setKabulTarihi(Instant kabulTarihi) {
        this.kabulTarihi = kabulTarihi;
    }

    public IsEmriTipi getTipi() {
        return tipi;
    }

    public IsEmri tipi(IsEmriTipi tipi) {
        this.tipi = tipi;
        return this;
    }

    public void setTipi(IsEmriTipi tipi) {
        this.tipi = tipi;
    }

    public Set<Iscilik> getIsciliklers() {
        return isciliklers;
    }

    public IsEmri isciliklers(Set<Iscilik> isciliks) {
        this.isciliklers = isciliks;
        return this;
    }

    public IsEmri addIscilikler(Iscilik iscilik) {
        this.isciliklers.add(iscilik);
        iscilik.setIsEmri(this);
        return this;
    }

    public IsEmri removeIscilikler(Iscilik iscilik) {
        this.isciliklers.remove(iscilik);
        iscilik.setIsEmri(null);
        return this;
    }

    public void setIsciliklers(Set<Iscilik> isciliks) {
        this.isciliklers = isciliks;
    }

    public Set<Parca> getParcalars() {
        return parcalars;
    }

    public IsEmri parcalars(Set<Parca> parcas) {
        this.parcalars = parcas;
        return this;
    }

    public IsEmri addParcalar(Parca parca) {
        this.parcalars.add(parca);
        parca.setIsEmri(this);
        return this;
    }

    public IsEmri removeParcalar(Parca parca) {
        this.parcalars.remove(parca);
        parca.setIsEmri(null);
        return this;
    }

    public void setParcalars(Set<Parca> parcas) {
        this.parcalars = parcas;
    }

    public Arac getArac() {
        return arac;
    }

    public IsEmri arac(Arac arac) {
        this.arac = arac;
        return this;
    }

    public void setArac(Arac arac) {
        this.arac = arac;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsEmri)) {
            return false;
        }
        return id != null && id.equals(((IsEmri) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IsEmri{" +
            "id=" + getId() +
            ", gelisZamani='" + getGelisZamani() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", kabulTarihi='" + getKabulTarihi() + "'" +
            ", tipi='" + getTipi() + "'" +
            "}";
    }
}
