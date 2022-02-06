import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestRunner {
    Customer customer;

    @Test (priority = 1)
    public void doLogin() throws ConfigurationException, IOException {
        customer = new Customer();
        customer.callingCustomerLoginAPI();
    }

    @Test (priority = 2)
    public void getCustomerList() throws IOException {
        customer = new Customer();
        customer.callingCustomerListAPI();

    }

    @Test(priority = 3)
    public void searchCustomer() throws IOException {
        customer = new Customer();
        customer.searchCustomer();

    }

    @Test(priority = 4)
    public void createCustomer() throws IOException {
        customer = new Customer();
        customer.creatCustomer();
    }

    @Test(priority = 5)
    public void createCustomerExisting() throws IOException {
        customer = new Customer();
        customer.createExistingCustomer();
    }

    @Test(priority = 6)
    public void customerUpdate() throws IOException {
        customer = new Customer();
        customer.updateCustomer();
    }

    @Test(priority = 7)
    public void CustomerDelete() throws IOException {
        customer = new Customer();
        customer.deleteCustomer();
    }
}
