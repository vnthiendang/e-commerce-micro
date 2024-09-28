package com.dvt.ecommerce.payment;

import com.dvt.ecommerce.customer.CustomerResponse;
import com.dvt.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
