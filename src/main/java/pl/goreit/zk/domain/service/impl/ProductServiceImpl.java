package pl.goreit.zk.domain.service.impl;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.goreit.api.generated.ProductView;
import pl.goreit.api.generated.ProductViewDetails;
import pl.goreit.api.generated.product_api.CreateProductRequest;
import pl.goreit.zk.domain.DomainException;
import pl.goreit.zk.domain.ExceptionCode;
import pl.goreit.zk.domain.model.Comment;
import pl.goreit.zk.domain.model.Product;
import pl.goreit.zk.domain.service.ProductService;
import pl.goreit.zk.infrastructure.mongo.ProductRepo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ConversionService sellConversionService;

    @Autowired
    private ProductRepo productRepo;


    @Override
    public List<ProductViewDetails> findAll() throws DomainException {
        List<Product> products = productRepo.findAll();
        return products.stream()
                .map(product -> sellConversionService.convert(product, ProductViewDetails.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductViewDetails add(CreateProductRequest createProductRequest) {

        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        String sellerId = authentication.getName();

        //@FIXME allow add photo album to product
        Product product = new Product(UUID.randomUUID().toString(),
                sellerId,
                createProductRequest.getTitle(),
                createProductRequest.getText(),
                createProductRequest.getPrice());
        productRepo.save(product);

        ProductView productView = sellConversionService.convert(product, ProductView.class);
        return sellConversionService.convert(product, ProductViewDetails.class);
    }

    @Override
    public ProductViewDetails addComment(String userId, String productTitle, String text) throws DomainException {
        Product product = productRepo.findByTitle(productTitle).orElseThrow(() -> new DomainException(ExceptionCode.PRODUCT_NOT_EXIST));

        Integer sequenceNo = 0;
        if (product.getComments().size() > 0) {
            sequenceNo = product.getComments()
                    .get(product.getComments().size() - 1)
                    .getSequenceNo();
        }

        product.addComment(new Comment(sequenceNo + 1, userId, text));
        Product saved = productRepo.save(product);
        return sellConversionService.convert(saved, ProductViewDetails.class);
    }

}
