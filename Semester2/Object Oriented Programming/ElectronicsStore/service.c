#include "service.h"


/*
* Add product
*/
int addProductService(MyList* products, int id, char* type, char* manufacturer, char* model, double price, int quantity) {
	Product product = createProduct(id, type, manufacturer, model, price, quantity);
	int error_code = validate(product);
	if (error_code != 0) {
		destroyProduct(&product);
		return error_code;
	}
	int position = searchProductRepo(products, id);
	addProductRepo(products, product, position);
	return 0;
}


/*
* Delete product
*/
int deleteProductService(MyList* products, int id) {
	return deleteProductRepo(products, id);
}


/*
* Update product's price
*/
int updateProductPriceService(MyList* products, int id, double new_price) {
	int position = searchProductRepo(products, id);
	if (position != -1) {
		Product new_product = createProduct(products->elements[position].id, products->elements[position].type, products->elements[position].manufacturer, products->elements[position].model, new_price, products->elements[position].quantity);
		int error_code = validate(new_product);
		if (error_code != 0) {
			destroyProduct(&new_product);
			return 2;
		}
		updateProductRepo(products, new_product, position);
		return 0;
	}
	return 1;
}


/*
* Update product's quantity
*/
int updateProductQuantityService(MyList* products, int id, int new_quantity) {
	int position = searchProductRepo(products, id);
	if (position != -1) {
		Product new_product = createProduct(products->elements[position].id, products->elements[position].type, products->elements[position].manufacturer, products->elements[position].model, products->elements[position].price, new_quantity);
		int error_code = validate(new_product);
		if (error_code != 0) {
			destroyProduct(&new_product);
			return 2;
		}
		updateProductRepo(products, new_product, position);
		return 0;
	}
	return 1;
}


/*
* Filter products by manufacturer, price or quantity
*/
MyList filterProductService(MyList* products, char* type, char* criteria) {
	if (criteria == NULL || strlen(criteria) == 0) {
		return copyList(products);
	}
	MyList filtered_list = createList();
	for (int i = 0; i < products->len; i++) {
		Product produs = getElement(products, i);
		if (strcmp(type, "a") == 0) { // filter by manufacturer
			if (strcmp(produs.manufacturer, criteria) == 0) {
				addProductRepo(&filtered_list, copyProduct(produs), -1);
			}
		}
		if (strcmp(type, "b") == 0) { // filter by price
			if (eq_doubles(produs.price, strtod(criteria, NULL))) {
				addProductRepo(&filtered_list, copyProduct(produs), -1);
			}
		}
		if (strcmp(type, "c") == 0) { // filter by quantity
			if (produs.quantity == atoi(criteria)) {
				addProductRepo(&filtered_list, copyProduct(produs), -1);
			}
		}
	}
	return filtered_list;
}


/*
* Compare the price of two products
*/
int cmpPrice(Product product1, Product product2) {
	if (product1.price > product2.price) {
		return 1;
	}
	return 0;
}


/*
* Compare the quantity of two products
*/
int cmpQuantity(Product product1, Product product2) {
	if (product1.quantity > product2.quantity) {
		return 1;
	}
	return 0;
}


/*
* Sort list
*/
void sort(MyList* products, CompareFunction cmp, char* mode) {
	for (int i = 0; i < lenList(products) - 1; i++) {
		for (int j = i + 1; j < lenList(products); j++) {
			Product product1 = getElement(products, i);
			Product product2 = getElement(products, j);
			if (strcmp(mode, "ascending") == 0) { // ascending order
				if (cmp(product1, product2) == 1) {
					setElement(products, i, product2);
					setElement(products, j, product1);
				}
			}
			else if (strcmp(mode, "descending") == 0) { // descending order
				if (cmp(product1, product2) == 0) {
					setElement(products, i, product2);
					setElement(products, j, product1);
				}
			}
		}
	}
}


/*
* Sort the list by price
*/
MyList sortProductsByPriceService(MyList* products, char* mode) {
	MyList sorted_list = copyList(products);
	sort(&sorted_list, cmpPrice, mode);
	return sorted_list;


	/*
	* Sort the list by quantity
	*/
}MyList sortProductsByQuantityService(MyList* products, char* mode) {
	MyList sorted_list = copyList(products);
	sort(&sorted_list, cmpQuantity, mode);
	return sorted_list;
}