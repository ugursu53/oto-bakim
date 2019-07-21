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
 * A Hesap.
 */
@Setter
@Getter
@Entity
@Table(name = "hesap")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "hesap")
public class Hesap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "banka")
    private String banka;

    @Column(name = "sube")
    private String sube;

    @Column(name = "hesap_no")
    private String hesapNo;

    @Column(name = "iban")
    private String iban;

    public Hesap banka(String banka) {
        this.banka = banka;
        return this;
    }

    public Hesap sube(String sube) {
        this.sube = sube;
        return this;
    }

    public Hesap hesapNo(String hesapNo) {
        this.hesapNo = hesapNo;
        return this;
    }

    public Hesap iban(String iban) {
        this.iban = iban;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hesap)) {
            return false;
        }
        return id != null && id.equals(((Hesap) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Hesap{" +
            "id=" + getId() +
            ", banka='" + getBanka() + "'" +
            ", sube='" + getSube() + "'" +
            ", hesapNo='" + getHesapNo() + "'" +
            ", iban='" + getIban() + "'" +
            "}";
    }
}
