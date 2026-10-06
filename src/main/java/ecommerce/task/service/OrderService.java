package ecommerce.task.service;

import ecommerce.task.model.Cart;
import ecommerce.task.model.CartItem;
import ecommerce.task.model.Order;
import ecommerce.task.model.OrderItem;
import ecommerce.task.model.Product;
import ecommerce.task.repository.CartRepository;
import ecommerce.task.repository.OrderRepository;
import ecommerce.task.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Order placeOrder(String userEmail) {

        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        double totalAmount = 0;

        for (CartItem cartItem : cart.getItems()) {

            Product product = productRepository
                    .findById(cartItem.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found: "
                                            + cartItem.getProductId()
                            ));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    cartItem.getQuantity()
            );

            orderItems.add(orderItem);

            totalAmount +=
                    product.getPrice()
                            * cartItem.getQuantity();

            product.setStock(
                    product.getStock()
                            - cartItem.getQuantity()
            );

            productRepository.save(product);
        }

        Order order = new Order(
                userEmail,
                orderItems,
                totalAmount,
                "PLACED",
                LocalDateTime.now()
        );

        Order savedOrder = orderRepository.save(order);

        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getMyOrders(String userEmail) {

        return orderRepository.findByUserEmail(userEmail);
    }

    public Order getOrderById(
            String orderId,
            String userEmail) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUserEmail().equals(userEmail)) {
            throw new RuntimeException(
                    "You are not allowed to view this order"
            );
        }

        return order;
    }

    public Order cancelOrder(
            String orderId,
            String userEmail) {

        Order order = getOrderById(orderId, userEmail);

        if (!order.getStatus().equals("PLACED")) {
            throw new RuntimeException(
                    "Only placed orders can be cancelled"
            );
        }

        for (OrderItem item : order.getItems()) {

            Product product = productRepository
                    .findById(item.getProductId())
                    .orElse(null);

            if (product != null) {

                product.setStock(
                        product.getStock()
                                + item.getQuantity()
                );

                productRepository.save(product);
            }
        }

        order.setStatus("CANCELLED");

        return orderRepository.save(order);
    }
}