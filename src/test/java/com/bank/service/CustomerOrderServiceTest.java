package com.bank.service;

import com.bank.client.CustomerOrderClient;
import com.bank.client.ProductDetailsClient;
import com.bank.client.dto.response.CustomerOrder;
import com.bank.client.dto.response.ProductDetails;
import com.bank.exception.ClientErrorException;
import com.bank.service.support.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.bank.util.MockObject.getListFromFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CustomerOrderServiceTest {

    private static final Integer YEAR = 2019;
    private static final Integer YEAR_NOT_FOUND = 2021;

    @InjectMocks
    private CustomerOrderService service;

    @Mock
    private MessageService messageService;
    @Mock
    private CustomerOrderClient orderClient;
    @Mock
    private ProductDetailsClient productClient;

    private List<CustomerOrder> customerOrder;
    private List<ProductDetails> productDetails;

    @BeforeEach
    void initialize() throws IOException {
        customerOrder = getListFromFile("json/CustomerOrder.json", CustomerOrder.class);
        productDetails = getListFromFile("json/ProductDetails.json", ProductDetails.class);
    }

    @Nested
    class WhenMethodFindAllCustomerOrders {
        @Test @DisplayName("Method: findAll - Should return NotFoundException when does not found any customer orders")
        void shouldReturnExceptionWhenCustomerOrdersNotFound() {
            when(orderClient.getCustomerOrders()).thenReturn(null);

            assertThat(catchThrowable(() -> service.findAll())).isNotNull()
                    .isExactlyInstanceOf(ClientErrorException.class);
        }

        @Test @DisplayName("Method: findAll - Should return an OrderCustomerTotal")
        void shouldReturnSummaryListPurchaseOrders() {
            when(orderClient.getCustomerOrders()).thenReturn(customerOrder);
            when(productClient.getProducts()).thenReturn(productDetails);

            assertThat(service.findAll()).isNotEmpty().hasSize(2);
        }
    }

    @Nested
    class WhenMethodFindGreatestCustomerOrdersByYear {
        @Test @DisplayName("Method: findGreatestBy - Should return NoContentException when not found customers orders")
        void shouldReturnNoContentExceptionWhenDoesNotFindCustomerOrProducts() {
            when(orderClient.getCustomerOrders()).thenReturn(null);

            assertThat(catchThrowable(() -> service.findGreatestBy(YEAR_NOT_FOUND))).isNotNull()
                    .isExactlyInstanceOf(ClientErrorException.class);
        }

        @Test @DisplayName("Method: findGreatestBy - Should return NoContentException when not found orders by year")
        void shouldReturnNoContentExceptionWhenDoesNotFindPurchaseOrderByYear() {
            when(orderClient.getCustomerOrders()).thenReturn(customerOrder);
            when(productClient.getProducts()).thenReturn(productDetails);

            assertThat(catchThrowable(() -> service.findGreatestBy(YEAR_NOT_FOUND))).isNotNull()
                    .isExactlyInstanceOf(ClientErrorException.class);
        }

        @Test @DisplayName("Method: findGreatestBy - Should return the greatest by year")
        void shouldReturnSummaryOrdersForTheGreatestByYear() {
            when(orderClient.getCustomerOrders()).thenReturn(customerOrder);
            when(productClient.getProducts()).thenReturn(productDetails);

            var summary = service.findGreatestBy(YEAR);

            assertThat(summary).isNotNull();
            assertThat(summary.getTotalValue()).isNotNull().isEqualTo(new BigDecimal("632.50"));
        }
    }

    @Nested
    class WhenMethodFindLoyalCustomers {
        @Test @DisplayName("Method: findLoyalCustomers - Should return NoContentException when not found customers orders")
        void shouldReturnNoContentExceptionWhenNotFoundLoyalCustomers() {
            when(orderClient.getCustomerOrders()).thenReturn(null);

            assertThat(catchThrowable(() -> service.findLoyalCustomers())).isNotNull()
                    .isExactlyInstanceOf(ClientErrorException.class);
        }

        @Test @DisplayName("Method: findLoyalCustomers - Should return the loyal customers")
        void shouldReturnListOfLoyalCustomers() {
            when(orderClient.getCustomerOrders()).thenReturn(customerOrder);
            when(productClient.getProducts()).thenReturn(productDetails);

            assertThat(service.findLoyalCustomers()).isNotEmpty().hasSize(2);
        }
    }

    @Nested
    class WhenMethodFindCustomerRecommendations {
        @Test @DisplayName("Method: findCustomerRecommendations - Should return NoContentException when not found customers orders")
        void shouldReturnNoContentExceptionWhenRecommendationsNotFound() {
            when(orderClient.getCustomerOrders()).thenReturn(null);

            assertThat(catchThrowable(() -> service.findCustomerRecommendations())).isNotNull()
                    .isExactlyInstanceOf(ClientErrorException.class);
        }

        @Test @DisplayName("Method: findCustomerRecommendations - Should return recommendations")
        void shouldReturnRecommendations() {
            when(orderClient.getCustomerOrders()).thenReturn(customerOrder);
            when(productClient.getProducts()).thenReturn(productDetails);

            assertThat(service.findCustomerRecommendations()).isNotEmpty().hasSize(2);
        }
    }
}
