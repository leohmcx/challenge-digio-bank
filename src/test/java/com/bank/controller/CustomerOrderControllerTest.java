package com.bank.controller;

import com.bank.service.CustomerOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerOrderController.class)
class CustomerOrderControllerTest {

    private static final Integer YEAR = 2020;
    private static final String FIND_ALL = "/v1/compras";
    private static final String LOYAL_CUSTOMERS = "/v1/clientes_fieis";
    private static final String GREATEST_PURCHASE = "/v1/maior_compra/{year}";
    private static final String RECOMMENDATION = "/v1/recomendacao/cliente/tipo";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerOrderService service;

    @Test
    void shouldReturnFindAllOk() throws Exception {
        mockMvc.perform(get(FIND_ALL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(print());

        verify(service, atMostOnce()).findAll();
    }

    @Test
    void shouldReturnFindGreatestByYearOk() throws Exception {
        mockMvc.perform(get(GREATEST_PURCHASE, YEAR).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(print());

        verify(service, atMostOnce()).findGreatestBy(anyInt());
    }

    @Test
    void shouldFindLoyalCustomersOk() throws Exception {
        mockMvc.perform(get(LOYAL_CUSTOMERS).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(print());

        verify(service, atMostOnce()).findLoyalCustomers();
    }

    @Test
    void shouldReturnOk() throws Exception {
        mockMvc.perform(get(RECOMMENDATION).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(print());

        verify(service, atMostOnce()).findCustomerRecommendations();
    }
}