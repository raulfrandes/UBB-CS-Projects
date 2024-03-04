#pragma once

#include "product.h"
#include "MyList.h"

/*
* Adds a product in the list or it increases the quantity if it is already in the list
* @param *products: the address of the list
* @param product: the product that will be added
* @param position: the position where the product is in the list
*/
void addProductRepo(MyList* products, Product product, int position);

/*
* Determins the position of the product in the list
* @param *products: the address of the list
* @param id: the id of the product
* @returns the position of the searched product
*/
int searchProductRepo(MyList* products, int id);

/*
* Deletes the product from the list
* @param *products: the address of the list
* @param id: the id of the product
* @returns the value 0 if the product was deleted successfully or value 1 if it exists already in the list
*/
int deleteProductRepo(MyList* products, int id);

/*
* Update the product at the given position
* @param *products: the address of the list
* @param new_product: the new product
* @param position: the position of the product
*/
void updateProductRepo(MyList* products, Product new_product, int position);
