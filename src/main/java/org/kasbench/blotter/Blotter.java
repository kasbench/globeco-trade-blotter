package org.kasbench.blotter;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "blotter", schema = "public")
public class Blotter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 60, nullable = false)
    private String name;

    @Column(name = "auto_populate", nullable = false)
    private Boolean autoPopulate = false;

    @Column(name = "security_type_id")
    private Integer securityTypeId;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Blotter() {}

    public Blotter(Integer id, String name, Boolean autoPopulate, Integer securityTypeId, Integer version) {
        this.id = id;
        this.name = name;
        this.autoPopulate = autoPopulate;
        this.securityTypeId = securityTypeId;
        this.version = version;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getAutoPopulate() { return autoPopulate; }
    public void setAutoPopulate(Boolean autoPopulate) { this.autoPopulate = autoPopulate; }

    public Integer getSecurityTypeId() { return securityTypeId; }
    public void setSecurityTypeId(Integer securityTypeId) { this.securityTypeId = securityTypeId; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blotter blotter = (Blotter) o;
        return Objects.equals(id, blotter.id) &&
               Objects.equals(name, blotter.name) &&
               Objects.equals(autoPopulate, blotter.autoPopulate) &&
               Objects.equals(securityTypeId, blotter.securityTypeId) &&
               Objects.equals(version, blotter.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, autoPopulate, securityTypeId, version);
    }

    @Override
    public String toString() {
        return "Blotter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", autoPopulate=" + autoPopulate +
                ", securityTypeId=" + securityTypeId +
                ", version=" + version +
                '}';
    }
} 