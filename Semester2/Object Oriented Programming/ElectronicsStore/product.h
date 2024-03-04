#pragma once

#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <stdio.h>
#include <crtdbg.h>

typedef struct {
	int id;
	char* type;
	char* manufacturer;
	char* model;
	double price;
	int quantity;
} Product;

/*
* Create a new product
* @param id: the unique identifier of a product
* @param type: type of product (laptop, TV, refrigerator, etc.)
* @param manufacturer: the manufacturer of the product
* @param model: the model of the product
* @param price: the price of the product
* @param quantity: how many
* @returns the created product
*/
Product createProduct(int id, char* type, char* manufacturer, char* model, double price, int quantity);

/*
* Creates a copy of a product
* @param product: the product that need a copy
* @returns the copy of the product
*/
Product copyProduct(Product product);

/*
* Verifies if two real numbers are equal
* @param a: double
* @param b: double
* @returns the value 1 if they are equal or 0 otherwise
*/
int eq_doubles(double a, double b);

/*
* Verifies if two products are equal
* @param product: Product
* @param product: Product
* @returns the value 1 if they are equal or 0 otherwise
*/
int eq(Product product, Product other);

/*
* Deallocate the memory occupied by a product
* @param *product: the address of a product
*/
void destroyProduct(Product* produs);