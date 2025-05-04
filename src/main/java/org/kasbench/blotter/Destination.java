package org.kasbench.blotter;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "destination", schema = "public")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String abbreviation;

    @Column(length = 100, nullable = false)
    private String description;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Destination() {}

    public Destination(Integer id, String abbreviation, String description, Integer version) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.description = description;
        this.version = version;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAbbreviation() { return abbreviation; }
    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination that = (Destination) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(abbreviation, that.abbreviation) &&
               Objects.equals(description, that.description) &&
               Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, abbreviation, description, version);
    }

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", abbreviation='" + abbreviation + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                '}';
    }
} 