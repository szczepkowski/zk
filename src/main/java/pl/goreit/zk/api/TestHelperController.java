package pl.goreit.zk.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import pl.goreit.api.generated.CreateOrderRequest;
import pl.goreit.api.generated.OrderLineRequest;
import pl.goreit.api.generated.OrderResponse;
import pl.goreit.zk.domain.DomainException;
import pl.goreit.zk.domain.model.Product;
import pl.goreit.zk.domain.service.OrderService;
import pl.goreit.zk.infrastructure.mongo.ProductRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
@Profile("!prod")
public class TestHelperController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderService orderService;


    @PostMapping("addOrder")
    @ApiOperation(value = "add order")
    public OrderResponse addOrder(@RequestParam("userId") String userId,
                                  @RequestParam("orderProductNumber") Integer orderProductNumber,
                                  @RequestParam("amount") Integer amount) throws DomainException {

        Product korepetycje0 = productRepo.findByTitle("korepetycje").get();
        List<OrderLineRequest> orderLineRequests = new ArrayList<>();

        for (int i = 0; i < orderProductNumber; i++) {
            OrderLineRequest orderProductView = new OrderLineRequest(korepetycje0.getTitle(), amount);
            orderLineRequests.add(orderProductView);
        }

        return orderService.schedule(new CreateOrderRequest(userId, orderLineRequests));
    }


    @GetMapping("/addProduct/")
    @ApiOperation(value = "add 100 products")
    public void addProducts(@RequestParam("amount") Integer amount) {

        for (int count = 0; count < amount; count++) {
            Product product = new Product(UUID.randomUUID().toString(), "GoreWorkshop", "korepetycje", "Pomoc w programowaniu", BigDecimal.valueOf(150));
            productRepo.save(product);
        }


    }


}
