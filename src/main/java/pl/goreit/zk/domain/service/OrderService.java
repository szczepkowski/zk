package pl.goreit.zk.domain.service;

import org.bson.types.ObjectId;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.goreit.api.generated.CreateOrderRequest;
import pl.goreit.api.generated.OrderLineRequest;
import pl.goreit.api.generated.OrderResponse;
import pl.goreit.api.generated.ReceiveOrderRequest;
import pl.goreit.zk.domain.DomainException;
import pl.goreit.zk.domain.ExceptionCode;
import pl.goreit.zk.domain.model.*;
import pl.goreit.zk.infrastructure.mongo.OrderRepo;
import pl.goreit.zk.infrastructure.mongo.ProductRepo;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ConversionService sellConversionService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductRepo productRepo;

    public OrderResponse findById(String id) throws DomainException {
        Order order = orderRepo.findById(id).orElseThrow(() -> new DomainException(ExceptionCode.ORDER_NOT_FOUND));
        return sellConversionService.convert(order, OrderResponse.class);
    }

    public List<OrderResponse> findByUserId() {

        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        List<Order> ordersByUser = orderRepo.findByUserId(authentication.getName());
        return ordersByUser.stream()
                .map(order -> sellConversionService.convert(order, OrderResponse.class))
                .collect(Collectors.toList());
    }

    public OrderResponse schedule(CreateOrderRequest createOrderRequest) throws DomainException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        List<OrderLineRequest> orderLineRequests = createOrderRequest.getOrderlines();

        if (orderLineRequests == null || orderLineRequests.isEmpty()) {
            throw new DomainException(ExceptionCode.ORDER_NOT_HAVE_ORDERLINES);
        }

        ObjectId orderId = ObjectId.get();

        List<OrderLine> orderlines = orderLineRequests.stream()
                .map(orderLineView -> {

                    //@FIXME get all upper
                    Product product = productRepo.findByTitle(orderLineView.getProductTitle()).get();
                    return new OrderLine(orderId.toString(), product.getWorkshopId(), product.getTitle(), orderLineView.getAmount(), product.getPrice());
                })
                .collect(Collectors.toList());


        Order order = new Order(createOrderRequest.getSellerId(), userId, orderlines);
        orderRepo.save(order);

        return sellConversionService.convert(order, OrderResponse.class);
    }

    public OrderResponse receive(ReceiveOrderRequest receiveOrderRequest) throws DomainException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        String orderId = receiveOrderRequest.getOrderId();
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new DomainException(ExceptionCode.ORDER_NOT_FOUND));


        Account user = accountService.findByUserId(userId);
        Person person = user.getPerson();

        orderRepo.save(order);

        return sellConversionService.convert(order, OrderResponse.class);
    }


}
