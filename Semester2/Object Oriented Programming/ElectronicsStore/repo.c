#include "repo.h"


/*
* Adds a product in the list or it increases the quantity if it is already in the list
*/
void addProductRepo(MyList* products, Product product, int position) {
	// if the elements is not in the list it is added
	if (position == -1) {
		addElement(products, product);
	}
	else { // if it is in the list it increases the quantity
		products->elements[position].quantity += product.quantity;
		destroyProduct(&product);
	}
}


/*
* Determins the position of the product in the list
*/
int searchProductRepo(MyList* products, int id) {
	for (int i = 0; i < products->len; i++) {
		if (products->elements[i].id == id) {
			return i;
		}
	}

	// if the product does not exist it returns -1
	return -1;
}


/*
* Deletes the product from the list
*/
int deleteProductRepo(MyList* products, int id) {
	int position = searchProductRepo(products, id);
	if (position != -1) {
		// deallocate the memory occupied by product
		destroyProduct(&products->elements[position]);

		// move the elements to the left to occupy the empty positions 
		for (int i = position; i < products->len; i++) {
			products->elements[i] = products->elements[i + 1];
		}
		products->len--;
		return 0;
	}
	return 1;
}


/*
* Update the product at the given position
*/
void updateProductRepo(MyList* products, Product new_product, int position) {
	destroyProduct(&products->elements[position]);
	setElement(products, position, new_product);
}