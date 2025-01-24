package com.turkcell.mini_e_commere_hw2.service.domain;

import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.rules.CartBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.ProductBusinessRules;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    private final CartItemService cartItemService;
    private final ProductService productService;

    private final ProductBusinessRules productBusinessRules;
    private final CartBusinessRules cartBusinessRules;

    @Override
    public Cart getById(Integer id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    @Override
    public void addCartItemToCart(UUID userId, Integer productId, Integer quantity) {
        int cartId = getCartIdByUserId(userId);

        cartBusinessRules.cartIdMustExist(cartId);
        productBusinessRules.productIdMustExist(productId);
        productBusinessRules.productMustBeInStock(productId, quantity);
        productBusinessRules.productMustBeInStockInCart(cartId, productId, quantity);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        // Check if product already exists in cart
        CartItem existingItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        Product product = productService.getById(productId);
        BigDecimal itemPrice = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

        if (existingItem != null) {
            // Update existing cart item
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setPrice(existingItem.getPrice().add(itemPrice));
        } else {
            // Create new cart item
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(quantity);
            newCartItem.setPrice(itemPrice);
            cart.getCartItems().add(newCartItem);
        }

        // Update cart total
        cart.setTotalPrice((cart).getTotalPrice()
                .add(itemPrice));
        
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItemFromCart(UUID userId, Integer cartItemId, Integer quantity) {
        int cartId = getCartIdByUserId(userId);

        cartBusinessRules.cartIdMustExist(cartId);
        cartBusinessRules.cartItemMustExist(cartItemId);
        
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem cartItem = cartItemService.getById(cartItemId);
        
        productBusinessRules.productIdMustExist(cartItem.getProduct().getId());

        if (cartItem.getQuantity() <= quantity) {
            // Remove the entire cart item
            cart.getCartItems().remove(cartItem);
            cartItemService.delete(cartItem.getId());
        } else {
            // Reduce the quantity
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            BigDecimal priceReduction = cartItem.getProduct().getUnitPrice()
                    .multiply(BigDecimal.valueOf(quantity));
            cartItem.setPrice(cartItem.getPrice().subtract(priceReduction));
        }
        
        // Update cart total
        cart.setTotalPrice(cart.getTotalPrice()
                .subtract(cartItem.getProduct().getUnitPrice()
                        .multiply(BigDecimal.valueOf(quantity))));
        
        cartRepository.save(cart);
    }

    @Override
    public void resetCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);

        cartRepository.save(cart);
    }

    @Override
    public boolean existsById(Integer id) {
        return cartRepository.existsById(id);
    }

    @Override
    public int getCartIdByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"))
                .getId();
    }

    @Override
    public Object getMyCart(UUID activeUserId) {
        return cartRepository.findByUserId(activeUserId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}
