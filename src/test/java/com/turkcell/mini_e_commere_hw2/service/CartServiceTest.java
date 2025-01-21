package com.turkcell.mini_e_commere_hw2.service;

import com.turkcell.mini_e_commere_hw2.dto.cart.CartItemDto;
import com.turkcell.mini_e_commere_hw2.dto.cart.CartListingDto;
import com.turkcell.mini_e_commere_hw2.entity.Cart;
import com.turkcell.mini_e_commere_hw2.entity.CartItem;
import com.turkcell.mini_e_commere_hw2.entity.Product;
import com.turkcell.mini_e_commere_hw2.entity.User;
import com.turkcell.mini_e_commere_hw2.repository.CartItemRepository;
import com.turkcell.mini_e_commere_hw2.repository.CartRepository;
import com.turkcell.mini_e_commere_hw2.repository.ProductRepository;
import com.turkcell.mini_e_commere_hw2.rules.CartBusinessRules;
import com.turkcell.mini_e_commere_hw2.rules.ProductBusinessRules;
import com.turkcell.mini_e_commere_hw2.util.exception.type.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductBusinessRules productBusinessRules;
    @Mock
    private CartBusinessRules cartBusinessRules;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(cartRepository, cartItemRepository, productRepository,
                productBusinessRules, cartBusinessRules);
    }

    @Test
    void getById_WithValidId_ShouldReturnCart() {
        // Arrange
        Integer cartId = 1;
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setTotalPrice(BigDecimal.valueOf(99.99));
        
        User user = new User();
        user.setId(java.util.UUID.randomUUID());
        cart.setUser(user);

        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setUnitPrice(BigDecimal.valueOf(99.99));

        CartItem cartItem = new CartItem();
        cartItem.setId(1);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setPrice(BigDecimal.valueOf(99.99));

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        // Act
        CartListingDto result = cartService.getById(cartId);

        // Assert
        assertNotNull(result);
        assertEquals(cartId, result.getId());
        assertEquals(user.getId(), result.getUserId());
        assertEquals(cart.getTotalPrice(), result.getTotalPrice());
        assertEquals(1, result.getCartItems().size());
        
        CartItemDto resultItem = result.getCartItems().get(0);
        assertEquals(cartItem.getId(), resultItem.getId());
        assertEquals(cartItem.getProduct().getId(), resultItem.getProductId());
        assertEquals(cartItem.getQuantity(), resultItem.getQuantity());
        assertEquals(cartItem.getPrice(), resultItem.getPrice());
    }

    @Test
    void getById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Integer cartId = 999;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cartService.getById(cartId));
    }

    @Test
    void addCartItemToCart_WithValidData_ShouldSucceed() {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;
        Integer quantity = 2;

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCartItems(new ArrayList<>());
        cart.setTotalPrice(BigDecimal.ZERO);

        Product product = new Product();
        product.setId(productId);
        product.setUnitPrice(BigDecimal.valueOf(99.99));
        product.setStock(10);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        cartService.addCartItemToCart(cartId, productId, quantity);

        // Assert
        verify(cartBusinessRules).cartIdMustExist(cartId);
        verify(productBusinessRules).productIdMustExist(productId);
        verify(productBusinessRules).productMustBeInStock(productId, quantity);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addCartItemToCart_WithInvalidCartId_ShouldThrowException() {
        // Arrange
        Integer cartId = 999;
        Integer productId = 1;
        Integer quantity = 1;

        doThrow(new BusinessException("Cart not found"))
            .when(cartBusinessRules).cartIdMustExist(cartId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> cartService.addCartItemToCart(cartId, productId, quantity));
        verify(cartRepository, never()).save(any());
    }

    @Test
    void addCartItemToCart_WithInvalidProductId_ShouldThrowException() {
        // Arrange
        Integer cartId = 1;
        Integer productId = 999;
        Integer quantity = 1;

        doThrow(new BusinessException("Product not found"))
            .when(productBusinessRules).productIdMustExist(productId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> cartService.addCartItemToCart(cartId, productId, quantity));
        verify(cartRepository, never()).save(any());
    }

    @Test
    void addCartItemToCart_WithInsufficientStock_ShouldThrowException() {
        // Arrange
        Integer cartId = 1;
        Integer productId = 1;
        Integer quantity = 100; // More than available stock

        doThrow(new BusinessException("Product is not in stock"))
            .when(productBusinessRules).productMustBeInStock(productId, quantity);

        // Act & Assert
        assertThrows(BusinessException.class, () -> cartService.addCartItemToCart(cartId, productId, quantity));
        verify(cartRepository, never()).save(any());
    }

    @Test
    void removeCartItemFromCart_WithValidData_ShouldSucceed() {
        // Arrange
        Integer cartId = 1;
        Integer cartItemId = 1;
        Integer quantity = 2;
        
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setTotalPrice(BigDecimal.valueOf(199.98)); // 2 items at 99.99 each
        
        Product product = new Product();
        product.setId(1);
        product.setStock(10);
        product.setUnitPrice(BigDecimal.valueOf(99.99));
        
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(BigDecimal.valueOf(199));
        cartItem.setCart(cart);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        // Act
        cartService.removeCartItemFromCart(cartId, cartItemId, quantity);

        // Assert
        verify(cartBusinessRules).cartIdMustExist(cartId);
        verify(cartBusinessRules).cartItemMustExist(cartItemId);
        verify(cartItemRepository).delete(cartItem);
        verify(cartRepository).save(cart);
    }

    @Test
    void removeCartItemFromCart_WithInvalidCartId_ShouldThrowException() {
        // Arrange
        Integer cartId = 999;
        Integer cartItemId = 1;
        Integer quantity = 1;

        doThrow(new BusinessException("Cart not found"))
            .when(cartBusinessRules).cartIdMustExist(cartId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> cartService.removeCartItemFromCart(cartId, cartItemId, quantity));
        verify(cartItemRepository, never()).delete(any());
    }

    @Test
    void removeCartItemFromCart_WithInvalidCartItemId_ShouldThrowException() {
        // Arrange
        Integer cartId = 1;
        Integer cartItemId = 999;
        Integer quantity = 1;

        doThrow(new BusinessException("Cart item not found"))
            .when(cartBusinessRules).cartItemMustExist(cartItemId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> cartService.removeCartItemFromCart(cartId, cartItemId, quantity));
        verify(cartItemRepository, never()).delete(any());
    }

    @Test
    void getAll_ShouldReturnAllCarts() {
        // Arrange
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        
        User user2 = new User();
        user2.setId(UUID.randomUUID());

        Cart cart1 = new Cart();
        cart1.setId(1);
        cart1.setUser(user1);
        cart1.setCartItems(new ArrayList<>());
        cart1.setTotalPrice(BigDecimal.ZERO);

        Cart cart2 = new Cart();
        cart2.setId(2);
        cart2.setUser(user2);
        cart2.setCartItems(new ArrayList<>());
        cart2.setTotalPrice(BigDecimal.ZERO);

        when(cartRepository.findAll()).thenReturn(Arrays.asList(cart1, cart2));

        // Act
        List<CartListingDto> result = cartService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(cart1.getId(), result.get(0).getId());
        assertEquals(cart2.getId(), result.get(1).getId());
    }
} 