#include "tests.h"

void runTests() {
	testProduct();
	testValidation();
	testMyList();
	testRepo();
	testService();
}

void testProduct() {
	Product product;
	int id = 1;
	char type[] = "laptop";
	char manufacturer[] = "prod1";
	char model[] = "model1";
	double price = 1234.5;
	int quantity = 1;

	// test create product
	product = createProduct(id, type, manufacturer, model, price, quantity);
	assert(product.id == id);
	assert(strcmp(product.type, type) == 0);
	assert(strcmp(product.manufacturer, manufacturer) == 0);
	assert(strcmp(product.model, model) == 0);
	assert(eq_doubles(product.price, price) == 1);
	assert(product.quantity == quantity);

	// test copy
	Product copy = copyProduct(product);
	assert(eq(product, copy) == 1);

	// test eq_doubles
	assert(eq_doubles(12.34, 12.33) == 0);

	// test eq
	int id_2 = 2;
	char type_2[] = "TV";
	char manufacturer_2[] = "prod2";
	char model_2[] = "model2";
	double price_2 = 123.5;
	int quantity_2 = 2;
	Product product_2 = createProduct(id_2, type_2, manufacturer_2, model_2, price_2, quantity_2);
	assert(eq(product, product_2) == 0);

	// test destroy
	destroyProduct(&product);
	assert(product.id == -1);
	assert(product.manufacturer == NULL);
	assert(product.model == NULL);
	assert(product.type == NULL);
	assert(product.price == -1);
	assert(product.quantity == -1);

	destroyProduct(&copy);
	destroyProduct(&product_2);
}

void testValidation() {
	int id = 1;
	char type[] = "laptop";
	char manufacturer[] = "prod1";
	char model[] = "model1";
	double price = 1234.5;
	int quantity = 1;

	int invalid_id = 0;
	Product invalid_id_product = createProduct(invalid_id, type, manufacturer, model, price, quantity);
	assert(validate(invalid_id_product) == 1);

	char invalid_type[] = "water";
	Product invalid_type_product = createProduct(id, invalid_type, manufacturer, model, price, quantity);
	assert(validate(invalid_type_product) == 2);

	char invalid_manufacturer[] = "";
	Product invalid_manufacturer_product = createProduct(id, type, invalid_manufacturer, model, price, quantity);
	assert(validate(invalid_manufacturer_product) == 3);

	char invalid_model[] = "";
	Product invalid_model_product = createProduct(id, type, manufacturer, invalid_model, price, quantity);
	assert(validate(invalid_model_product) == 4);

	double invalid_price = -1;
	Product invalid_price_product = createProduct(id, type, manufacturer, model, invalid_price, quantity);
	assert(validate(invalid_price_product) == 5);

	int invalid_quantity = -12;
	Product invalid_quantity_product = createProduct(id, type, manufacturer, model, price, invalid_quantity);
	assert(validate(invalid_quantity_product) == 6);

	Product product = createProduct(id, type, manufacturer, model, price, quantity);
	assert(validate(product) == 0);

	destroyProduct(&invalid_id_product);
	destroyProduct(&invalid_type_product);
	destroyProduct(&invalid_manufacturer_product);
	destroyProduct(&invalid_model_product);
	destroyProduct(&invalid_price_product);
	destroyProduct(&invalid_quantity_product);
	destroyProduct(&product);
}

void testMyList() {
	// test create list
	MyList list = createList();
	assert(list.cp == 2);
	assert(list.len == 0);
	assert(list.elements != NULL);

	// test destroy list
	MyList list_2 = createList();
	int id_5 = 1;
	char type_5[] = "laptop";
	char manufacturer_5[] = "prod5";
	char model_5[] = "model5";
	double price_5 = 0.5;
	int quantity_5 = 5;
	Product product_5 = createProduct(id_5, type_5, manufacturer_5, model_5, price_5, quantity_5);
	addElement(&list_2, product_5);
	destroyList(&list_2);
	assert(list.len == 0);

	// test add
	int id = 1;
	char type[] = "laptop";
	char manufacturer[] = "prod1";
	char model[] = "model1";
	double price = 1234.5;
	int quantity = 1;
	Product product = createProduct(id, type, manufacturer, model, price, quantity);
	addElement(&list, product);
	int id_2 = 2;
	char type_2[] = "TV";
	char manufacturer_2[] = "prod2";
	char model_2[] = "model2";
	double price_2 = 123.5;
	int quantity_2 = 2;
	Product product_2 = createProduct(id_2, type_2, manufacturer_2, model_2, price_2, quantity_2);
	addElement(&list, product_2);
	int id_3 = 3;
	char type_3[] = "frigider";
	char manufacturer_3[] = "prod3";
	char model_3[] = "model3";
	double price_3 = 12.5;
	int quantity_3 = 3;
	Product product_3 = createProduct(id_3, type_3, manufacturer_3, model_3, price_3, quantity_3);
	addElement(&list, product_3);
	assert(list.len == 3);

	// test resize
	resize(&list);
	assert(list.cp == 8);

	// test length
	int length = lenList(&list);
	assert(length == 3);

	// test get element
	Product product_0 = getElement(&list, 0);
	assert(eq(product, product_0) == 1);

	// test copy
	MyList copy = copyList(&list);
	assert(list.len == 3);
	assert(list.cp == 8);
	assert(eq(list.elements[0], product) == 1);
	assert(eq(list.elements[1], product_2) == 1);
	assert(eq(list.elements[2], product_3) == 1);

	// test set element
	int id_4 = 4;
	char type_4[] = "other";
	char manufacturer_4[] = "prod4";
	char model_4[] = "model4";
	double price_4 = 1.5;
	int quantity_4 = 4;
	Product product_4 = createProduct(id_4, type_4, manufacturer_4, model_4, price_4, quantity_4);
	setElement(&list, 0, product_4);
	assert(eq(list.elements[0], product_4) == 1);

	destroyList(&list);
	destroyList(&copy);
	destroyProduct(&product);
}

void testRepo() {
	MyList products = createList();
	int id = 1;
	char type[] = "laptop";
	char manufacturer[] = "prod1";
	char model[] = "model1";
	double price = 1234.5;
	int quantity = 1;
	Product product = createProduct(id, type, manufacturer, model, price, quantity);

	// test add
	addProductRepo(&products, product, -1);
	assert(products.len == 1);
	assert(eq(products.elements[0], product) == 1);
	Product product_2 = createProduct(id, type, manufacturer, model, price, quantity);
	addProductRepo(&products, product_2, 0);
	assert(products.len == 1);
	assert(products.elements[0].quantity == 2);

	// test search
	int position = searchProductRepo(&products, 1);
	assert(position == 0);
	int position_1 = searchProductRepo(&products, 3);
	assert(position_1 == -1);

	// test update
	int id_3 = 3;
	char type_3[] = "altele";
	char manufacturer_3[] = "prod3";
	char model_3[] = "model3";
	double price_3 = 123.5;
	int quantity_3 = 3;
	Product product_3 = createProduct(id_3, type_3, manufacturer_3, model_3, price_3, quantity_3);
	updateProductRepo(&products, product_3, 0);
	assert(eq(products.elements[0], product_3) == 1);

	// test delete
	int success = deleteProductRepo(&products, id_3);
	assert(products.len == 0);
	assert(success == 0);
	success = deleteProductRepo(&products, 5);
	assert(success == 1);

	destroyList(&products);
}

void testService() {
	MyList products = createList();
	int id = 1;
	char type[] = "laptop";
	char manufacturer[] = "prod1";
	char model[] = "model1";
	double price = 1234.5;
	int quantity = 1;

	// test add
	int success = addProductService(&products, id, type, manufacturer, model, price, quantity);
	assert(products.len == 1);
	assert(products.elements[0].id == 1);
	assert(success == 0);
	int invalid_id = -1;
	success = addProductService(&products, invalid_id, type, manufacturer, model, price, quantity);
	assert(products.len == 1);
	assert(success != 0);

	// test delete
	success = deleteProductService(&products, id);
	assert(products.len == 0);
	assert(success == 0);

	// test update price
	addProductService(&products, id, type, manufacturer, model, price, quantity);
	double new_price = 2999.99;
	success = updateProductPriceService(&products, id, new_price);
	assert(success == 0);
	assert(products.elements[0].price == new_price);
	success = updateProductPriceService(&products, invalid_id, new_price);
	assert(success == 1);
	double invalid_price = -12;
	success = updateProductPriceService(&products, id, invalid_price);
	assert(success == 2);


	// test update quantity
	int new_quantity = 10;
	success = updateProductQuantityService(&products, id, new_quantity);
	assert(success == 0);
	assert(products.elements[0].quantity == new_quantity);
	success = updateProductQuantityService(&products, invalid_id, new_quantity);
	assert(success == 1);
	int invalid_quantity = -12;
	success = updateProductQuantityService(&products, id, invalid_quantity);
	assert(success == 2);
	deleteProductService(&products, id);

	// test filters
	int id_1 = 1, id_2 = 2, id_3 = 3;
	char type_1[] = "laptop", type_2[] = "TV", type_3[] = "other";
	char manufacturer_1[] = "prod1", manufacturer_2[] = "prod2";
	char model_1[] = "model1", model_2[] = "model2", model_3[] = "model3";
	double price_1 = 2000, price_2 = 3000;
	int quantity_1 = 1, quantity_2 = 2;
	addProductService(&products, id_1, type_1, manufacturer, model_1, price_1, quantity_2);
	addProductService(&products, id_2, type_2, manufacturer, model_2, price_2, quantity_1);
	addProductService(&products, id_3, type_3, manufacturer_2, model_3, price_1, quantity_1);
	MyList filtered_list_by_nothing = filterProductService(&products, "a", "");
	assert(filtered_list_by_nothing.len == 3);
	assert(filtered_list_by_nothing.elements[0].id == id_1);
	assert(filtered_list_by_nothing.elements[1].id == id_2);
	assert(filtered_list_by_nothing.elements[2].id == id_3);

	MyList filtered_list_a = filterProductService(&products, "a", "prod1");
	assert(filtered_list_a.len == 2);
	assert(strcmp(filtered_list_a.elements[0].manufacturer, manufacturer) == 0);
	assert(strcmp(filtered_list_a.elements[1].manufacturer, manufacturer) == 0);

	MyList filtered_list_b = filterProductService(&products, "b", "2000");
	assert(filtered_list_b.len == 2);
	assert(eq_doubles(filtered_list_b.elements[0].price, price_1) == 1);
	assert(eq_doubles(filtered_list_b.elements[1].price, price_1) == 1);

	MyList filtered_list_c = filterProductService(&products, "c", "1");
	assert(filtered_list_c.len == 2);
	assert(filtered_list_c.elements[0].quantity == quantity_1);
	assert(filtered_list_c.elements[1].quantity == quantity_1);

	// test cmp price
	Product product_1 = createProduct(id_1, type_1, manufacturer, model_1, price_1, quantity_1);
	Product product_2 = createProduct(id_2, type_2, manufacturer_2, model_2, price_2, quantity_2);
	int res = cmpPrice(product_1, product_2);
	assert(res == 0);
	res = cmpPrice(product_2, product_1);
	assert(res == 1);

	// test cmp quantity
	res = cmpQuantity(product_1, product_2);
	assert(res == 0);
	res = cmpQuantity(product_2, product_1);
	assert(res == 1);

	// test sort
	sort(&products, cmpPrice, "ascending");
	assert(eq_doubles(products.elements[0].price, price_1) == 1);
	assert(eq_doubles(products.elements[1].price, price_1) == 1);
	assert(eq_doubles(products.elements[2].price, price_2) == 1);
	sort(&products, cmpPrice, "descending");
	assert(eq_doubles(products.elements[2].price, price_1) == 1);
	assert(eq_doubles(products.elements[1].price, price_1) == 1);
	assert(eq_doubles(products.elements[0].price, price_2) == 1);

	// test sort by price
	MyList sorted_list_price = sortProductsByPriceService(&products, "ascending");
	assert(eq_doubles(sorted_list_price.elements[0].price, price_1) == 1);
	assert(eq_doubles(sorted_list_price.elements[1].price, price_1) == 1);
	assert(eq_doubles(sorted_list_price.elements[2].price, price_2) == 1);

	// test sort by quantity
	MyList sortedListQuantity = sortProductsByQuantityService(&products, "ascending");
	assert(sortedListQuantity.elements[0].quantity == quantity_1);
	assert(sortedListQuantity.elements[1].quantity == quantity_1);
	assert(sortedListQuantity.elements[2].quantity == quantity_2);

	destroyList(&products);
	destroyList(&filtered_list_by_nothing);
	destroyList(&filtered_list_a);
	destroyList(&filtered_list_b);
	destroyList(&filtered_list_c);
	destroyProduct(&product_1);
	destroyProduct(&product_2);
	destroyList(&sorted_list_price);
	destroyList(&sortedListQuantity);
}
