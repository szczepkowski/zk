package pl.goreit.zk.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.goreit.api.generated.ProductViewDetails;
import pl.goreit.api.generated.product_api.CreateProductRequest;
import pl.goreit.zk.domain.DomainException;
import pl.goreit.zk.domain.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;




    @PostMapping("/add")
    @ApiOperation(value = "dodaje 1 produkt", notes = "dodaje produkt")
    public ProductViewDetails addProduct(@RequestBody CreateProductRequest request) {
        return productService.add(request);
    }

    @PostMapping("/comment")
    @ApiOperation(value = "Dodaje komentarz do produktu", notes = "dodaje komentarz")
    public ProductViewDetails addComment(@RequestParam("userId") String userId,
                                         @RequestParam("title") String productTitle,
                                         @RequestParam("text") String text) throws DomainException {
        return productService.addComment(userId, productTitle, text);
    }

}
