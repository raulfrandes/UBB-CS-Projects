#include "product.h"


/*
* Creates a new product
*/
Product createProduct(int id, char* type, char* manufacturer, char* model, double price, int quantity) {
	Product product;
	product.id = id;

	// allocate memory for type
	int numCh = (int)strlen(type) + 1; // the length of type
	product.type = (char*)malloc(sizeof(char*) * numCh);
	if (product.type) {
		strcpy_s(product.type, numCh, type);
	}

	// allocate memory for manufacturer
	numCh = (int)strlen(manufacturer) + 1; // the length of manufacturer
	product.manufacturer = (char*)malloc(sizeof(char*) * numCh);
	if (product.manufacturer) {
		strcpy_s(product.manufacturer, numCh, manufacturer);
	}

	// allocate memory for model
	numCh = (int)strlen(model) + 1; // the length of model
	product.model = (char*)malloc(sizeof(char*) * numCh);
	if (product.model) {
		strcpy_s(product.model, numCh, model);
	}
	product.price = price;
	product.quantity = quantity;
	return product;
}


/*
* Creates a copy of a product
*/
Product copyProduct(Product product) {
	return createProduct(product.id, product.type, product.manufacturer, product.model, product.price, product.quantity);
}


/*
* Verifies if two real numbers are equal
*/
int eq_doubles(double a, double b) {
	double epsilon = 0.00001;
	if (fabs(a - b) < epsilon)
		return 1;
	return 0;
}


/*
* Verifies if two products are equal
*/
int eq(Product product, Product other) {
	if (product.id == other.id) {
		if (strcmp(product.type, other.type) == 0) {
			if (strcmp(product.manufacturer, other.manufacturer) == 0) {
				if (strcmp(product.model, other.model) == 0) {
					if (product.price == other.price) {
						if (product.quantity == other.quantity) {
							return 1;
						}
					}
				}
			}
		}
	}
	return 0;
}


/*
* Deallocate the memory occupied by a product
*/
void destroyProduct(Product* product) {
	// deallocate memory for type, manufacturer and model
	free(product->type);
	free(product->manufacturer);
	free(product->model);

	// give invalid values for marking the destruction of the product
	product->id = -1;
	product->type = NULL;
	product->manufacturer = NULL;
	product->model = NULL;
	product->price = -1;
	product->quantity = -1;
}