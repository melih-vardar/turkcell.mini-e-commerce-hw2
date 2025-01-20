package com.turkcell.mini_e_commere_hw2.rules;

import com.turkcell.mini_e_commere_hw2.dto.product.ProductListingDto;
import com.turkcell.mini_e_commere_hw2.dto.product.UpdateProductDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.entity.Order;
import com.turkcell.mini_e_commere_hw2.entity.OrderStatus;
import com.turkcell.mini_e_commere_hw2.repository.OrderRepository;
import com.turkcell.mini_e_commere_hw2.service.ProductService;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class OrderBusinessRules {


    private final OrderRepository orderRepository;

    public OrderBusinessRules( OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void checkTheProductStockAfterUpdateProductStockForOrder(Cart cart, ProductService productService){

        for (CartItem cartItem : cart.getCartItems()){
            ProductListingDto productListingDto = productService.findById(cartItem.getProduct().getId());

            if(productListingDto.getStock() < cartItem.getQuantity()){
                throw new BusinessException("Insufficient stock for product: " + productListingDto.getName() +
                        ". Available stock: " + productListingDto.getStock() + ", Required: " + cartItem.getQuantity());
            }

            productListingDto.setStock(productListingDto.getStock()-cartItem.getQuantity());
            UpdateProductDto updateProductDto = convertToUpdateProductDto(productListingDto);
            productService.update(updateProductDto);

        }
    }

    public void handleOrderStatusTransition(OrderStatus currentStatus, Order order){

        if (currentStatus == OrderStatus.HAZIRLANIYOR) {
            order.setStatus(OrderStatus.KARGODA);
        } else if (currentStatus == OrderStatus.KARGODA) {
            order.setStatus(OrderStatus.TESLIM_EDILDI);
        } else if (currentStatus == OrderStatus.TESLIM_EDILDI) {
            throw new BusinessException("Order already delivered. No further status updates allowed.");
        } else {
            throw new BusinessException("Invalid order status transition.");
        }
    }

    //map sınıfı olursa yaz
    private UpdateProductDto convertToUpdateProductDto(ProductListingDto productListingDto){

        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setId(productListingDto.getId());
        updateProductDto.setName(productListingDto.getName());
        updateProductDto.setSubCategoryId(productListingDto.getSubCategoryId());
        updateProductDto.setStock(productListingDto.getStock());
        updateProductDto.setDescription(productListingDto.getDescription());
        updateProductDto.setImage(productListingDto.getImage());
        updateProductDto.setUnitPrice(productListingDto.getUnitPrice());
        return updateProductDto;
    }
}
