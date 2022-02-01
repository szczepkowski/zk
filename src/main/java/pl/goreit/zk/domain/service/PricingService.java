package pl.goreit.zk.domain.service;


import pl.goreit.api.generated.OrderResponse;

public interface PricingService {

    boolean commissionSettlement(OrderResponse orderResponse);

    void coinsSettlement(OrderResponse orderResponse);
}
