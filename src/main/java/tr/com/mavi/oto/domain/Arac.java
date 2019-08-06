package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Objects;

import tr.com.mavi.oto.domain.enumeration.YakitTuru;

import tr.com.mavi.oto.domain.enumeration.VitesTuru;

import tr.com.mavi.oto.domain.enumeration.KullanimSekli;

import tr.com.mavi.oto.domain.enumeration.AracTipi;

/**
 * A Arac.
 */
@Setter
@Getter
@Entity
@Table(name = "arac")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "arac")
public class Arac implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "plaka_no", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String plakaNo;

    @NotNull
    @Column(name = "model_yili", nullable = false)
    private Integer modelYili;

    @Column(name = "rengi")
    private String rengi;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "yakit_turu", nullable = false)
    private YakitTuru yakitTuru;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vites_turu", nullable = false)
    private VitesTuru vitesTuru;

    @Column(name = "motor_no")
    private String motorNo;

    @Column(name = "sasi_no")
    private String sasiNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "kullanim_sekli")
    private KullanimSekli kullanimSekli;

    @Enumerated(EnumType.STRING)
    @Column(name = "arac_tipi")
    private AracTipi aracTipi;

    @Column(name = "aciklama")
    private String aciklama;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Cari aktifCari;

    @ManyToOne
    @JsonIgnoreProperties("aracs")
    private Model model;

    public void setAktifCari(Optional<AracCarisi> aktifAracCarisi) {
        aktifCari = aktifAracCarisi.map(AracCarisi::getCari).orElse(null);
    }

    public Arac plakaNo(String plakaNo) {
        this.plakaNo = plakaNo;
        return this;
    }

    public Arac modelYili(Integer modelYili) {
        this.modelYili = modelYili;
        return this;
    }

    public Arac rengi(String rengi) {
        this.rengi = rengi;
        return this;
    }

    public Arac yakitTuru(YakitTuru yakitTuru) {
        this.yakitTuru = yakitTuru;
        return this;
    }

    public Arac vitesTuru(VitesTuru vitesTuru) {
        this.vitesTuru = vitesTuru;
        return this;
    }

    public Arac motorNo(String motorNo) {
        this.motorNo = motorNo;
        return this;
    }

    public Arac sasiNo(String sasiNo) {
        this.sasiNo = sasiNo;
        return this;
    }

    public Arac kullanimSekli(KullanimSekli kullanimSekli) {
        this.kullanimSekli = kullanimSekli;
        return this;
    }

    public Arac aracTipi(AracTipi aracTipi) {
        this.aracTipi = aracTipi;
        return this;
    }

    public Arac aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public Arac model(Model model) {
        this.model = model;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Arac)) {
            return false;
        }
        return id != null && id.equals(((Arac) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Arac{" +
            "id=" + getId() +
            ", plakaNo='" + getPlakaNo() + "'" +
            ", modelYili=" + getModelYili() +
            ", rengi='" + getRengi() + "'" +
            ", yakitTuru='" + getYakitTuru() + "'" +
            ", vitesTuru='" + getVitesTuru() + "'" +
            ", motorNo='" + getMotorNo() + "'" +
            ", sasiNo='" + getSasiNo() + "'" +
            ", kullanimSekli='" + getKullanimSekli() + "'" +
            ", aracTipi='" + getAracTipi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            "}";
    }
}
