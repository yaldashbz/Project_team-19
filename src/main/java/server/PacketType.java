package server;

public enum PacketType {
    LOGIN, REGISTER, ADD_DISCOUNT_REQUEST, EDIT_DISCOUNT_REQUEST, REMOVE_DISCOUNT_REQUEST,
    ADD_PRODUCT_FROM_STOCK, ADD_NEW_PRODUCT, ACCEPT_REQUEST, DECLINE_REQUEST, INCREASE_COUNT_CART, DECREASE_COUNT_CART, ADD_CATEGORY, EDIT_CATEGORY,
    REMOVE_CATEGORY, GET_PERSON, IS_FIRST_MANAGER_REGISTERED, LOG_OUT, GET_PERSON_TYPE, GET_BANK_TOKEN, GET_BANK_BALANCE,
    GET_TRANSACTION, INCREASE_BANK_BALANCE, INCREASE_WALLET_BALANCE, DECREASE_WALLET_BALANCE,
    GET_ALL_PRODUCTS, GET_ALL_PRODUCTS_IN_DISCOUNT,
    EDIT_PRODUCT_REQUEST, GET_CATEGORY_BY_NAME, GET_PARENT_CATEGORIES, GET_ALL_REQUESTS, GET_PRODUCT_BY_ID, GET_SELLER_PRODUCTS,
    ADD_AUCTION_REQUEST, GET_AVAILABLE_AUCTION_PRODUCTS,
    GET_ALL_AUCTIONS, GET_ROOT_CATEGORIES,
    GET_SIMILAR_PRODUCTS, GET_SELLERS_OF_PRODUCTS, GET_VERIFIED_PRODUCTS, INCREASE_SEEN, INCREASE_SCORE,
    GET_ALL_DISCOUNTS_OF_SELLER, REMOVE_PRODUCT_FOR_SELLER, DELETE_PRODUCT_MANAGER,
    DELETE_PRODUCT_REQUEST, ADD_COMMENT, IS_PRODUCT_AVAILABLE, GET_TYPE_BY_TOKEN, IS_PRODUCT_BOUGHT,
    GET_AVERAGE_PRICE, GET_PERSON_BY_TOKEN, GET_BANK_ID, GET_WALLET_BALANCE, GET_REQUESTS_OF_TYPE,
    GET_PERSON_INFO_BY_TOKEN, WALLET_PURCHASE, BANK_PURCHASE,
    GET_CATEGORY_PRODUCTS, GET_IN_DISCOUNT_CATEGORY_PRODUCTS, GET_NODE_CATEGORIES,
    ADD_TO_CART,GET_CART,GET_COUNT_IN_CART,SET_COUNT_IN_CART
}
