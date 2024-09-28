package com.dvt.ecommerce.order;

import com.dvt.ecommerce.customer.CustomerClient;
import com.dvt.ecommerce.exception.BusinessException;
import com.dvt.ecommerce.kafka.OrderConfirmation;
import com.dvt.ecommerce.kafka.OrderProducer;
import com.dvt.ecommerce.orderline.OrderLineRequest;
import com.dvt.ecommerce.orderline.OrderLineService;
import com.dvt.ecommerce.payment.PaymentClient;
import com.dvt.ecommerce.payment.PaymentRequest;
import com.dvt.ecommerce.product.ProductClient;
import com.dvt.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        // check the customer - openFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        // purchase products
        var purchasedProducts = productClient.purchaseProducts(request.products());

        // persist order
        var order = this.repository.save(mapper.toOrder(request));

        // persist orderLine
        for (PurchaseRequest purchaseRequest : request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // start payment_process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        // send order confirmation (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAllOrders(){
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }
    public OrderResponse findById(Integer id){
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}
