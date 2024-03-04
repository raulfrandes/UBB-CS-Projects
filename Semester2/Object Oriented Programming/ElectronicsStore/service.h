#pragma once

#include "repo.h"
#include "validation.h"

typedef int(*CompareFunction)(Product product1, Product product2);

/*
* Add product
* @param *products: the address of the list
* @param id: the id of the product
* @param type: the type of the product
* @param manufacturer: the manufacturer of the product
* @param model: the model of the product
* @param price: the price of the product
* @param quantity: the quantity of the product
* @returns the value 0 if it was added successfully or value 1 otherwise
*/
int addProductService(MyList* products, int id, char* type, char* manufacturer, char* model, double price, int quantity);

/*
* Delete product
* @param *products: the address of the list
* @param id: the id of the product
* @returns the value 0 if it was deleted successfully or value 1 otherwise
*/
int deleteProductService(MyList* products, int id);

/*
* Update product's price
* @param *products: the address of the list
* @param id: the id of the product
* @param new_price: the new price
* @returns the value 0 if it was updated successfully or value 1 otherwise
*/
int updateProductPriceService(MyList* products, int id, double new_price);

/*
* Update product's quantity
* @param *products: the address of the list
* @param id: the id of the product
* @param new_quantity: the new quantity
* @returns the value 0 if it was updated successfully or value 1 otherwise
*/
int updateProductQuantityService(MyList* products, int id, int new_quantity);

/*
* Filter products by manufacturer, price or quantity
* @param *products: the address of the list
* @param type: type of filtration ("a" - by manufacturer, "b" - by price, "c" - by quantity)
* @param criteria: the manufacturer/price/quantity after which the filtering will be done
* @returns the filtered list
*/
MyList filterProductService(MyList* products, char* type, char* criteria);

/*
* Compare the price of two products
* @param product1: a product
* @param product2: a product
* @returns the value 1 if the price of the first product is greater than the price of the second product or value 0 otherwise
*/
int cmpPrice(Product product1, Product product2);

/*
* Compare the quantity of two products
* @param product1: a product
* @param product2: a product
* @returns the value 1 if the quantity of the first product is greater than the quantity of the second product or value 0 otherwise
*/
int cmpQuantity(Product product1, Product product2);

/*
* Sort list
* @param *products: the address of the list
* @param cmp: the sorting criteria
* @param mode: the order in which they are sorted (ascending sau descending)
*/
void sort(MyList* products, CompareFunction cmp, char* mode);

/*
* Sort the list by price
* @param *products: the address of the list
* @param mode: the order in which they are sorted (ascending sau descending)
* @returns the sorted list
*/
MyList sortProductsByPriceService(MyList* products, char* mode);

/*
* Sort the list by quantity
* @param *products: the address of the list
* @param mode: the order in which they are sorted (ascending sau descending)
* @returns the sorted list
*/
MyList sortProductsByQuantityService(MyList* products, char* mode);
