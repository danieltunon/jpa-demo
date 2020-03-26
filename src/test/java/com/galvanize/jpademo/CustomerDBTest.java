package com.galvanize.jpademo;

import java.util.Optional;

import javax.transaction.Transactional;

import com.galvanize.jpademo.entities.Account;
import com.galvanize.jpademo.entities.CreditCard;
import com.galvanize.jpademo.entities.CreditCardType;
import com.galvanize.jpademo.entities.Customer;
import com.galvanize.jpademo.repositories.CustomerRepository;

import org.assertj.core.api.Assertions;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * CustomerDBTest
 */
@ActiveProfiles(profiles = "test")
@DataJpaTest
@Transactional
public class CustomerDBTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void canSaveACustomer() {
        Customer customer = new Customer("Jane", "Doe", "jane.doe@email.com");
        customer = customerRepository.save(customer);

        Optional<Customer> persistedResult = customerRepository.findById(customer.getId());
        Assertions.assertThat(persistedResult.isPresent()).isTrue();
        Assertions.assertThat(persistedResult.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    public void canLinkCustomerWithAccountAndCreditCard() {
        Account account = new Account("12345");
        Customer customer = new Customer("Kari", "Mann", "km@email.com", account);
        CreditCard creditCard = new CreditCard("123456789", CreditCardType.AMEX);

        customer.getAccount().getCreditCards().add(creditCard);
        customer = customerRepository.save(customer);

        Customer persistedResult = customerRepository.findByEmailContaining("km@email.com")
            .orElseThrow(() -> new AssertionFailure("should have found customer by email"));

        Assertions.assertThat(persistedResult.getAccount().getAccountNumber()).isEqualTo("12345");
        Assertions.assertThat(persistedResult.getAccount().getCreditCards()).isNotEmpty();
        Assertions.assertThat(persistedResult.getAccount().getCreditCards()).contains(creditCard);

        
    }
    
}