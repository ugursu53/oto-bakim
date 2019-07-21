package tr.com.mavi.oto.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import tr.com.mavi.oto.domain.enumeration.YakitTuru;

import tr.com.mavi.oto.domain.enumeration.VitesTuru;

import tr.com.mavi.oto.domain.enumeration.KullanimSekli;

import tr.com.mavi.oto.domain.enumeration.AracTipi;

/**
 * A Arac.
 */
@Entity
@Table(name = "arac")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "arac")
public class Arac implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "plaka_no", nullable = false)
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

    @OneToMany(mappedBy = "arac")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AracCarisi> caris = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("aracs")
    private Model model;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlakaNo() {
        return plakaNo;
    }

    public Arac plakaNo(String plakaNo) {
        this.plakaNo = plakaNo;
        return this;
    }

    public void setPlakaNo(String plakaNo) {
        this.plakaNo = plakaNo;
    }

    public Integer getModelYili() {
        return modelYili;
    }

    public Arac modelYili(Integer modelYili) {
        this.modelYili = modelYili;
        return this;
    }

    public void setModelYili(Integer modelYili) {
        this.modelYili = modelYili;
    }

    public String getRengi() {
        return rengi;
    }

    public Arac rengi(String rengi) {
        this.rengi = rengi;
        return this;
    }

    public void setRengi(String rengi) {
        this.rengi = rengi;
    }

    public YakitTuru getYakitTuru() {
        return yakitTuru;
    }

    public Arac yakitTuru(YakitTuru yakitTuru) {
        this.yakitTuru = yakitTuru;
        return this;
    }

    public void setYakitTuru(YakitTuru yakitTuru) {
        this.yakitTuru = yakitTuru;
    }

    public VitesTuru getVitesTuru() {
        return vitesTuru;
    }

    public Arac vitesTuru(VitesTuru vitesTuru) {
        this.vitesTuru = vitesTuru;
        return this;
    }

    public void setVitesTuru(VitesTuru vitesTuru) {
        this.vitesTuru = vitesTuru;
    }

    public String getMotorNo() {
        return motorNo;
    }

    public Arac motorNo(String motorNo) {
        this.motorNo = motorNo;
        return this;
    }

    public void setMotorNo(String motorNo) {
        this.motorNo = motorNo;
    }

    public String getSasiNo() {
        return sasiNo;
    }

    public Arac sasiNo(String sasiNo) {
        this.sasiNo = sasiNo;
        return this;
    }

    public void setSasiNo(String sasiNo) {
        this.sasiNo = sasiNo;
    }

    public KullanimSekli getKullanimSekli() {
        return kullanimSekli;
    }

    public Arac kullanimSekli(KullanimSekli kullanimSekli) {
        this.kullanimSekli = kullanimSekli;
        return this;
    }

    public void setKullanimSekli(KullanimSekli kullanimSekli) {
        this.kullanimSekli = kullanimSekli;
    }

    public AracTipi getAracTipi() {
        return aracTipi;
    }

    public Arac aracTipi(AracTipi aracTipi) {
        this.aracTipi = aracTipi;
        return this;
    }

    public void setAracTipi(AracTipi aracTipi) {
        this.aracTipi = aracTipi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public Arac aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Set<AracCarisi> getCaris() {
        return caris;
    }

    public Arac caris(Set<AracCarisi> aracCarisis) {
        this.caris = aracCarisis;
        return this;
    }

    public Arac addCari(AracCarisi aracCarisi) {
        this.caris.add(aracCarisi);
        aracCarisi.setArac(this);
        return this;
    }

    public Arac removeCari(AracCarisi aracCarisi) {
        this.caris.remove(aracCarisi);
        aracCarisi.setArac(null);
        return this;
    }

    public void setCaris(Set<AracCarisi> aracCarisis) {
        this.caris = aracCarisis;
    }

    public Model getModel() {
        return model;
    }

    public Arac model(Model model) {
        this.model = model;
        return this;
    }

    public void setModel(Model model) {
        this.model = model;
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
