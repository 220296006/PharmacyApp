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
    @GeneratedValue(strategy = AUTO)
    private Long customerId;
    @Embedded
    private Name name;

    private Customer(CustomerBuilder customerBuilder) {
        this.customerId = customerBuilder.customerId;
        this.name = customerBuilder.name;
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
                "customerId=" + customerId +
                ", name=" + name +
                 '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(getCustomerId(), customer.getCustomerId()) && Objects.equals(getName(), customer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getName());
    }

    public static class CustomerBuilder {
        private Long customerId;
        private Name name;

        public CustomerBuilder setCustomerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public CustomerBuilder setName(Name name) {
            this.name = name;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}

