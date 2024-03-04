#pragma once

#include "product.h"

/*
* validate product
* @param product: the product that it validates
* @returns value 0 if the product is valid;
*		   value 1 if the id is <= 0;
*		   value 2 if the type id is not: laptop, TV, refrigerator sau other;
*		   value 3 if the manufacturer is an empty string;
*		   value 4 if the model is an empty string;
*		   value 5 if the price is <= 0;
*		   value 6 if the quantity is <= 0
*/
int validate(Product product);