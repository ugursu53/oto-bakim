package tr.com.mavi.oto.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Parca.
 */
@Setter
@Getter
@Entity
@Table(name = "parca")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "parca")
public class Parca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "fiyati")
    private Double fiyati;

    @Column(name = "iskonto")
    private Double iskonto;

    @ManyToOne
    private ParcaTipi tipi;

    public Parca fiyati(Double fiyati) {
        this.fiyati = fiyati;
        return this;
    }

    public Parca tipi(ParcaTipi parcaTipi) {
        this.tipi = parcaTipi;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parca)) {
            return false;
        }
        return id != null && id.equals(((Parca) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Parca{" +
            "id=" + getId() +
            ", fiyati=" + getFiyati() +
            "}";
    }
}
