package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.cart.CartItemDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.UpdateCartDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.repository.CartItemRepository;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.rules.CartBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.ProductBusinessRules;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductBusinessRules productBusinessRules;
    private final CartBusinessRules cartBusinessRules;

    public CartServiceImpl(CartRepository cartRepository, 
                         CartItemRepository cartItemRepository, 
                         ProductService productService,
                         ProductRepository productRepository,
                         ProductBusinessRules productBusinessRules, 
                         CartBusinessRules cartBusinessRules) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.productBusinessRules = productBusinessRules;
        this.cartBusinessRules = cartBusinessRules;
    }

    @Override
    public CartListingDto getById(Integer id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItemDto> cartItemDtos = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            cartItemDtos.add(new CartItemDto(cartItem.getId(), cartItem.getProduct().getId(), cartItem.getQuantity(), cartItem.getPrice()));
        }

        return new CartListingDto(cart.getId(), cart.getUser().getId(), cartItemDtos, cart.getTotalPrice());
    }

    @Override
    public List<CartListingDto> getAll() {
        List<Cart> carts = cartRepository.findAll();
        List<CartListingDto> cartListingDtos = new ArrayList<>();

        for (Cart cart : carts) {
            List<CartItem> cartItems = cart.getCartItems();
            List<CartItemDto> cartItemDtos = new ArrayList<>();

            for (CartItem cartItem : cartItems) {
                cartItemDtos.add(new CartItemDto(cartItem.getId(), cartItem.getProduct().getId(), cartItem.getQuantity(), cartItem.getPrice()));
            }

            cartListingDtos.add(new CartListingDto(cart.getId(), cart.getUser().getId(), cartItemDtos, cart.getTotalPrice()));
        }

        return cartListingDtos;
    }

    @Override
    public void addCartItemToCart(Integer cartId, Integer productId, Integer quantity) {
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

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        int itemPrice = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)).intValue();

        if (existingItem != null) {
            // Update existing cart item
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setPrice(existingItem.getPrice() + itemPrice);
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
        cart.setTotalPrice(cart.getTotalPrice()
                .add(product.getUnitPrice()
                        .multiply(BigDecimal.valueOf(quantity))));
        
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItemFromCart(Integer cartId, Integer cartItemId, Integer quantity) {
        cartBusinessRules.cartIdMustExist(cartId);
        cartBusinessRules.cartItemMustExist(cartItemId);
        
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        productBusinessRules.productIdMustExist(cartItem.getProduct().getId());

        if (cartItem.getQuantity() <= quantity) {
            // Remove the entire cart item
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            // Reduce the quantity
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            int priceReduction = cartItem.getProduct().getUnitPrice()
                    .multiply(BigDecimal.valueOf(quantity))
                    .intValue();
            cartItem.setPrice(cartItem.getPrice() - priceReduction);
        }
        
        // Update cart total
        cart.setTotalPrice(cart.getTotalPrice()
                .subtract(cartItem.getProduct().getUnitPrice()
                        .multiply(BigDecimal.valueOf(quantity))));
        
        cartRepository.save(cart);
    }

    @Override
    public void saveCard(Cart cart) { //Createorder işlemi sonrası kartın boş halini kaydetmek için
        cartRepository.save(cart);
    }
}
