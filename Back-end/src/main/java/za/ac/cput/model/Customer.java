package za.ac.cput.model;
/*Customer.java
 * Entity for the Customer
 * Thabiso Matsaba
 * 18 April 2023
 */
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.Objects;
import static jakarta.persistence.GenerationType.AUTO;

@Entity
@AllArgsConstructor
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO, generator="system-uuid")
    private Long customerId;
    @Embedded
    private Name name;

    private Customer(Builder builder) {
        this.customerId = builder.customerId;
        this.name = builder.name;
    }

    protected Customer() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Name getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(getCustomerId(), customer.getCustomerId())
                && Objects.equals(getName(), customer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getName());
    }

    public static class Builder {

        private Long customerId;
        private Name name;

        public Builder setCustomerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder setName(Name name) {
            this.name = name;
            return this;
        }

        public Builder copy(Customer Customer) {
            this.customerId = Customer.customerId;
            this.name = Customer.name;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}

