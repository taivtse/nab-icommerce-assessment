package au.com.nab.icommerce.api.gateway.controller;

import au.com.nab.icommerce.api.gateway.aspect.CustomerActivity;
import au.com.nab.icommerce.api.gateway.client.CartServiceClient;
import au.com.nab.icommerce.api.gateway.client.ProductServiceClient;
import au.com.nab.icommerce.api.gateway.common.ApiMessage;
import au.com.nab.icommerce.api.gateway.dto.request.AddCartItemsRequest;
import au.com.nab.icommerce.api.gateway.dto.request.CartItemRequest;
import au.com.nab.icommerce.api.gateway.dto.request.RemoveCartItemsRequest;
import au.com.nab.icommerce.api.gateway.mapper.request.AddCartItemsRequestMapper;
import au.com.nab.icommerce.api.gateway.mapper.request.RemoveCartItemsRequestMapper;
import au.com.nab.icommerce.api.gateway.mapper.response.CartResponseMapper;
import au.com.nab.icommerce.api.gateway.security.SecurityContextHelper;
import au.com.nab.icommerce.cart.protobuf.PAddCartItemsRequest;
import au.com.nab.icommerce.cart.protobuf.PCart;
import au.com.nab.icommerce.cart.protobuf.PRemoveCartItemsRequest;
import au.com.nab.icommerce.common.error.ErrorCode;
import au.com.nab.icommerce.common.error.ErrorCodeHelper;
import au.com.nab.icommerce.customer.protobuf.PCustomer;
import au.com.nab.icommerce.product.protobuf.PProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private ProductServiceClient productServiceClient;

    private final AddCartItemsRequestMapper addToCartRequestMapper = AddCartItemsRequestMapper.INSTANCE;
    private final CartResponseMapper cartResponseMapper = CartResponseMapper.INSTANCE;
    private final RemoveCartItemsRequestMapper removeCartItemsRequestMapper = RemoveCartItemsRequestMapper.INSTANCE;

    @PostMapping("/items/customer")
    @CustomerActivity("ADD_CART_ITEMS")
    public ApiMessage addCartItems(@RequestBody @Valid AddCartItemsRequest addCartItemsRequest) {
        try {
            PCustomer customer = SecurityContextHelper.getLoggedInCustomer();
            if (customer.getId() != addCartItemsRequest.getCustomerId()) {
                return ApiMessage.CUSTOMER_VIOLATION;
            }

            List<CartItemRequest> cartItemRequests = addCartItemsRequest.getItems();
            if (CollectionUtils.isEmpty(cartItemRequests)) {
                return ApiMessage.CART_ITEMS_EMPTY;
            }

            // Check product is existed
            for (CartItemRequest cartItemRequest : cartItemRequests) {
                PProduct product = productServiceClient.getProductsById(cartItemRequest.getProductId());
                if (product == null) {
                    return ApiMessage.CART_ITEMS_INVALID;
                }
            }

            // Call cart service
            PAddCartItemsRequest pAddCartItemsRequest = addToCartRequestMapper.toProtobuf(addCartItemsRequest);
            int response = cartServiceClient.addCartItems(pAddCartItemsRequest);
            if (ErrorCodeHelper.isFail(response)) {
                return ApiMessage.CART_ADD_ITEMS_FAILED;
            }

            return ApiMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.UNKNOWN_EXCEPTION;
        }
    }

    @DeleteMapping("/items/customer")
    @CustomerActivity("REMOVE_CART_ITEMS")
    public ApiMessage removeCartItems(@RequestBody @Valid RemoveCartItemsRequest removeItemsInCartRequest) {
        try {
            PCustomer customer = SecurityContextHelper.getLoggedInCustomer();
            if (customer.getId() != removeItemsInCartRequest.getCustomerId()) {
                return ApiMessage.CUSTOMER_VIOLATION;
            }

            // Call cart service
            PRemoveCartItemsRequest pRemoveCartItemsRequest =
                    removeCartItemsRequestMapper.toProtobuf(removeItemsInCartRequest);
            int response = cartServiceClient.removeCartItems(pRemoveCartItemsRequest);
            if (ErrorCodeHelper.isFail(response)) {
                if (response == ErrorCode.CART_EMPTY) {
                    return ApiMessage.CART_EMPTY;
                }
                return ApiMessage.DELETE_FAILED;
            }

            return ApiMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.UNKNOWN_EXCEPTION;
        }
    }

    @GetMapping("/customer/{customerId}")
    @CustomerActivity("GET_CUSTOMER_CART")
    public ApiMessage getCustomerCart(@PathVariable Integer customerId) {
        try {
            PCustomer customer = SecurityContextHelper.getLoggedInCustomer();
            if (customer.getId() != customerId) {
                return ApiMessage.CUSTOMER_VIOLATION;
            }

            PCart cart = cartServiceClient.getCustomerCart(customerId);
            if (cart == null) {
                return ApiMessage.CART_EMPTY;
            }

            return ApiMessage.success(cartResponseMapper.toDomain(cart));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.UNKNOWN_EXCEPTION;
        }
    }

    @DeleteMapping("/customer/{customerId}")
    @CustomerActivity("CLEAR_CUSTOMER_CART")
    public ApiMessage clearCustomerCart(@PathVariable Integer customerId) {
        try {
            PCustomer customer = SecurityContextHelper.getLoggedInCustomer();
            if (customer.getId() != customerId) {
                return ApiMessage.CUSTOMER_VIOLATION;
            }

            int response = cartServiceClient.clearCustomerCart(customerId);
            if (ErrorCodeHelper.isFail(response)) {
                return ApiMessage.CART_CLEAR_FAILED;
            }

            return ApiMessage.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiMessage.UNKNOWN_EXCEPTION;
        }
    }

}
