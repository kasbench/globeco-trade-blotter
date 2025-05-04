package org.kasbench.blotter;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "security", schema = "public")
public class Security {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String ticker;

    @Column(length = 200)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_type_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SecurityType securityType;

    @Version
    @Column(nullable = false)
    private Integer version;

    public Security() {}

    public Security(Integer id, String ticker, String description, SecurityType securityType, Integer version) {
        this.id = id;
        this.ticker = ticker;
        this.description = description;
        this.securityType = securityType;
        this.version = version;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public SecurityType getSecurityType() { return securityType; }
    public void setSecurityType(SecurityType securityType) { this.securityType = securityType; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        return Objects.equals(id, security.id) &&
               Objects.equals(ticker, security.ticker) &&
               Objects.equals(description, security.description) &&
               Objects.equals(version, security.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker, description, version);
    }

    @Override
    public String toString() {
        return "Security{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", description='" + description + '\'' +
                ", securityType=" + (securityType != null ? securityType.getId() : null) +
                ", version=" + version +
                '}';
    }
} 