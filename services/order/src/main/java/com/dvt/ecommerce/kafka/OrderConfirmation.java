package com.dvt.ecommerce.kafka;

import com.dvt.ecommerce.customer.CustomerResponse;
import com.dvt.ecommerce.order.PaymentMethod;
import com.dvt.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
