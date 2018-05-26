package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.entity.Payment;
import com.enrsolidr.energyanalysis.model.Address;
import com.enrsolidr.energyanalysis.model.User;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReceiptServiceTest {

    @Mock
    PaymentService paymentService;

    @Mock
    EmailService emailService;

    @InjectMocks
    ReceiptService receiptService;

    static File tempDirectory;

    @BeforeAll
    public static void beforeAll() throws IOException {
        tempDirectory = Files.createTempDir();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        tempDirectory.delete();
    }

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void canGenerateReceipts() throws Exception {
        receiptService.setDestinationFolder(tempDirectory.getPath());
        receiptService.setEmailFrom("toto@toto.com");
        List<Payment> payments = new ArrayList<Payment>();
        payments.add(generatePayment(22));
        payments.add(generatePayment(23));
        payments.add(generatePayment(24));
        when(paymentService.getPaymentsByDate(any(Date.class), any(Date.class))).thenReturn(payments);
        Assert.assertEquals("Bad number of receipts", 3, receiptService.sendReceipts("2018"));
        Mockito.verify(emailService, times(3)).sendMessageWithAttachment(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    private Payment generatePayment(int amount) {
        Payment payment = new Payment();
        User user = new User();
        user.setEmail("toto@toto.com");
        user.setLast_name("lastname");
        user.setNames("names");
        user.setPhone("0628337914");
        Address address = new Address();
        address.setCity("LILLE");
        address.setCountry("France");
        address.setPostalCode("59000");
        address.setStreet("Bastion Saint André");
        user.setAddress(address);
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setPaymentDate(new Date());
        return payment;
    }

}