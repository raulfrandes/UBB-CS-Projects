#include "validation.h"


/*
* validate product
*/
int validate(Product product) {
	if (product.id <= 0) {
		return 1;
	}
	if (strcmp(product.type, "laptop") != 0 && strcmp(product.type, "refrigerator") != 0 && strcmp(product.type, "TV") != 0 && strcmp(product.type, "other") != 0) {
		return 2;
	}
	if (strlen(product.manufacturer) == 0) {
		return 3;
	}
	if (strlen(product.model) == 0) {
		return 4;
	}
	if (product.price <= 0) {
		return 5;
	}
	if (product.quantity <= 0) {
		return 6;
	}
	return 0;
}
