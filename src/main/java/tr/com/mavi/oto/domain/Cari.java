package tr.com.mavi.oto.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

import tr.com.mavi.oto.domain.enumeration.CariTipi;

import tr.com.mavi.oto.domain.enumeration.KisiTipi;

import tr.com.mavi.oto.domain.enumeration.IsEmriTipi;

/**
 * A Cari.
 */
@Entity
@Table(name = "cari")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cari")
public class Cari implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipi", nullable = false)
    private CariTipi tipi;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "kisi_tipi", nullable = false)
    private KisiTipi kisiTipi;

    @NotNull
    @Column(name = "aktif", nullable = false)
    private Boolean aktif;

    @NotNull
    @Column(name = "ad", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Text)
    private String ad;

    @Column(name = "adres")
    private String adres;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "tc_no")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Text)
    private String tcNo;

    @Column(name = "vergi_no")
    private String vergiNo;

    @Column(name = "yetkili")
    private String yetkili;

    @Column(name = "fax")
    private String fax;

    @Column(name = "eposta")
    private String eposta;

    @Column(name = "web_adresi")
    private String webAdresi;

    @Column(name = "iskonto")
    private Double iskonto;

    @Column(name = "efatura_kullanimi")
    private Boolean efaturaKullanimi;

    @Column(name = "aciklama")
    private String aciklama;

    @Enumerated(EnumType.STRING)
    @Column(name = "varsayilan_is_emri_tipi")
    private IsEmriTipi varsayilanIsEmriTipi;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Hesap hesap;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CariTipi getTipi() {
        return tipi;
    }

    public Cari tipi(CariTipi tipi) {
        this.tipi = tipi;
        return this;
    }

    public void setTipi(CariTipi tipi) {
        this.tipi = tipi;
    }

    public KisiTipi getKisiTipi() {
        return kisiTipi;
    }

    public Cari kisiTipi(KisiTipi kisiTipi) {
        this.kisiTipi = kisiTipi;
        return this;
    }

    public void setKisiTipi(KisiTipi kisiTipi) {
        this.kisiTipi = kisiTipi;
    }

    public Boolean isAktif() {
        return aktif;
    }

    public Cari aktif(Boolean aktif) {
        this.aktif = aktif;
        return this;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public String getAd() {
        return ad;
    }

    public Cari ad(String ad) {
        this.ad = ad;
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAdres() {
        return adres;
    }

    public Cari adres(String adres) {
        this.adres = adres;
        return this;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getTelefon() {
        return telefon;
    }

    public Cari telefon(String telefon) {
        this.telefon = telefon;
        return this;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getTcNo() {
        return tcNo;
    }

    public Cari tcNo(String tcNo) {
        this.tcNo = tcNo;
        return this;
    }

    public void setTcNo(String tcNo) {
        this.tcNo = tcNo;
    }

    public String getVergiNo() {
        return vergiNo;
    }

    public Cari vergiNo(String vergiNo) {
        this.vergiNo = vergiNo;
        return this;
    }

    public void setVergiNo(String vergiNo) {
        this.vergiNo = vergiNo;
    }

    public String getYetkili() {
        return yetkili;
    }

    public Cari yetkili(String yetkili) {
        this.yetkili = yetkili;
        return this;
    }

    public void setYetkili(String yetkili) {
        this.yetkili = yetkili;
    }

    public String getFax() {
        return fax;
    }

    public Cari fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEposta() {
        return eposta;
    }

    public Cari eposta(String eposta) {
        this.eposta = eposta;
        return this;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getWebAdresi() {
        return webAdresi;
    }

    public Cari webAdresi(String webAdresi) {
        this.webAdresi = webAdresi;
        return this;
    }

    public void setWebAdresi(String webAdresi) {
        this.webAdresi = webAdresi;
    }

    public Double getIskonto() {
        return iskonto;
    }

    public Cari iskonto(Double iskonto) {
        this.iskonto = iskonto;
        return this;
    }

    public void setIskonto(Double iskonto) {
        this.iskonto = iskonto;
    }

    public Boolean isEfaturaKullanimi() {
        return efaturaKullanimi;
    }

    public Cari efaturaKullanimi(Boolean efaturaKullanimi) {
        this.efaturaKullanimi = efaturaKullanimi;
        return this;
    }

    public void setEfaturaKullanimi(Boolean efaturaKullanimi) {
        this.efaturaKullanimi = efaturaKullanimi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public Cari aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public IsEmriTipi getVarsayilanIsEmriTipi() {
        return varsayilanIsEmriTipi;
    }

    public Cari varsayilanIsEmriTipi(IsEmriTipi varsayilanIsEmriTipi) {
        this.varsayilanIsEmriTipi = varsayilanIsEmriTipi;
        return this;
    }

    public void setVarsayilanIsEmriTipi(IsEmriTipi varsayilanIsEmriTipi) {
        this.varsayilanIsEmriTipi = varsayilanIsEmriTipi;
    }

    public Hesap getHesap() {
        return hesap;
    }

    public Cari hesap(Hesap hesap) {
        this.hesap = hesap;
        return this;
    }

    public void setHesap(Hesap hesap) {
        this.hesap = hesap;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cari)) {
            return false;
        }
        return id != null && id.equals(((Cari) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cari{" +
            "id=" + getId() +
            ", tipi='" + getTipi() + "'" +
            ", kisiTipi='" + getKisiTipi() + "'" +
            ", aktif='" + isAktif() + "'" +
            ", ad='" + getAd() + "'" +
            ", adres='" + getAdres() + "'" +
            ", telefon='" + getTelefon() + "'" +
            ", tcNo='" + getTcNo() + "'" +
            ", vergiNo='" + getVergiNo() + "'" +
            ", yetkili='" + getYetkili() + "'" +
            ", fax='" + getFax() + "'" +
            ", eposta='" + getEposta() + "'" +
            ", webAdresi='" + getWebAdresi() + "'" +
            ", iskonto=" + getIskonto() +
            ", efaturaKullanimi='" + isEfaturaKullanimi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", varsayilanIsEmriTipi='" + getVarsayilanIsEmriTipi() + "'" +
            "}";
    }
}
