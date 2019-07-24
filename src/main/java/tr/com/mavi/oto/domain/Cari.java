package tr.com.mavi.oto.domain;


import lombok.Getter;
import lombok.Setter;
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
@Setter
@Getter
@Entity
@Table(name = "cari")
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

    public Cari tipi(CariTipi tipi) {
        this.tipi = tipi;
        return this;
    }

    public Cari kisiTipi(KisiTipi kisiTipi) {
        this.kisiTipi = kisiTipi;
        return this;
    }

    public Boolean isAktif() {
        return aktif;
    }

    public Cari aktif(Boolean aktif) {
        this.aktif = aktif;
        return this;
    }

    public Cari ad(String ad) {
        this.ad = ad;
        return this;
    }

    public Cari adres(String adres) {
        this.adres = adres;
        return this;
    }

    public Cari telefon(String telefon) {
        this.telefon = telefon;
        return this;
    }

    public Cari tcNo(String tcNo) {
        this.tcNo = tcNo;
        return this;
    }

    public Cari vergiNo(String vergiNo) {
        this.vergiNo = vergiNo;
        return this;
    }

    public Cari yetkili(String yetkili) {
        this.yetkili = yetkili;
        return this;
    }

    public Cari fax(String fax) {
        this.fax = fax;
        return this;
    }

    public Cari eposta(String eposta) {
        this.eposta = eposta;
        return this;
    }

    public Cari webAdresi(String webAdresi) {
        this.webAdresi = webAdresi;
        return this;
    }

    public Cari iskonto(Double iskonto) {
        this.iskonto = iskonto;
        return this;
    }

    public Boolean isEfaturaKullanimi() {
        return efaturaKullanimi;
    }

    public Cari efaturaKullanimi(Boolean efaturaKullanimi) {
        this.efaturaKullanimi = efaturaKullanimi;
        return this;
    }

    public Cari aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public Cari varsayilanIsEmriTipi(IsEmriTipi varsayilanIsEmriTipi) {
        this.varsayilanIsEmriTipi = varsayilanIsEmriTipi;
        return this;
    }

    public Cari hesap(Hesap hesap) {
        this.hesap = hesap;
        return this;
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
