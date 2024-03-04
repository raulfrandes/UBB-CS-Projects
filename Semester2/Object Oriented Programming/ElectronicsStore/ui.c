#include "service.h"


void printMenu() {
	printf("Menu: \n");
	printf("0. Print products.\n");
	printf("1. Add product.\n");
	printf("2. Update product.\n");
	printf("3. Delete product.\n");
	printf("4. Sort products.\n");
	printf("5. Filter products.\n");
	printf("6. Exit.\n");
}


void printProducts(MyList* products) {
	if (lenList(products) == 0) {
		printf("There are no products in stock!\n");
		return;
	}
	char print_id[] = "Id: ";
	char print_type[] = "Type: ";
	char print_manufacturer[] = "Manufacturer: ";
	char print_model[] = "Model: ";
	char print_price[] = "Price: ";
	char print_quantity[] = "Quantity: ";
	printf("%-15s%-15s%-15s%-15s%-15s%-15s\n", print_id, print_type, print_manufacturer, print_model, print_price, print_quantity);
	for (int i = 0; i < lenList(products); i++) {
		Product product = getElement(products, i);
		printf("%-15d%-15s%-15s%-15s%-15.*g%-15d\n", product.id, product.type, product.manufacturer, product.model, 15, product.price, product.quantity);
	}
}


void addProductUi(MyList* products) {
	int id, quantity;
	double price;
	char type[30], manufacturer[30], model[30];
	printf("Id = ");
	scanf_s("%d", &id);
	printf("Type = ");
	scanf_s("%s", type, 30);
	printf("Manufacturer = ");
	scanf_s("%s", manufacturer, 30);
	printf("Model = ");
	scanf_s("%s", model, 30);
	printf("Price = ");
	scanf_s("%lf", &price);
	printf("Quantity = ");
	scanf_s("%d", &quantity);
	int error_code = addProductService(products, id, type, manufacturer, model, price, quantity);
	if (error_code == 1) {
		printf("Invalid id!\n");
	}
	else if (error_code == 2) {
		printf("Invalid type!\n");
	}
	else if (error_code == 3) {
		printf("Invalid manufacturer!\n");
	}
	else if (error_code == 4) {
		printf("Invalid model!\n");
	}
	else if (error_code == 5) {
		printf("Invalid price!\n");
	}
	else if (error_code == 6) {
		printf("Invalid quantity!\n");
	}
	else {
		printf("Product added successfully!\n");
	}
}


void updateProductUi(MyList* products) {
	int id, success = -1, new_quantity;
	double new_price;
	char option[] = "a";
	printf("Update the:\n\ta) price;\n\tb) quantity.\nEnter option: ");
	scanf_s("%s", option, 2);
	printf("Id = ");
	scanf_s("%d", &id);
	if (strcmp(option, "a") == 0) {
		printf("New price = ");
		scanf_s("%lf", &new_price);
		success = updateProductPriceService(products, id, new_price);
		if (success == 2) {
			printf("Invalid price!\n");
		}
	}
	if (strcmp(option, "b") == 0) {
		printf("New quantity = ");
		scanf_s("%d", &new_quantity);
		success = updateProductQuantityService(products, id, new_quantity);
		if (success == 2) {
			printf("Invalid quantity!\n");
		}
	}
	if (success == 0) {
		printf("Product updated successfully.\n");
	}
	else if (success == 1) {
		printf("The product does not exist!\n");
	}
}


void deleteProductUi(MyList* products) {
	int id, success;
	printf("Id = ");
	scanf_s("%d", &id);
	success = deleteProductService(products, id);
	if (success == 0) {
		printf("Product deleted successfully.\n");
	}
	else {
		printf("The product does not exist!\n");
	}
}


void sortProductsUi(MyList* products) {
	char option[] = "a";
	char mode[20] = "a";
	printf("Sort by:\n\ta) price;\n\tb) quantity.\nEnter option: ");
	scanf_s("%s", option, 2);
	printf("Sorting method(ascending/descending): ");
	scanf_s("%s", mode, 20);
	MyList sorted_list;
	if (strcmp(option, "a") == 0) {
		sorted_list = sortProductsByPriceService(products, mode);
	}
	if (strcmp(option, "b") == 0) {
		sorted_list = sortProductsByQuantityService(products, mode);
	}
	printProducts(&sorted_list);
	destroyList(&sorted_list);
}


void filterProductsUi(MyList* products) {
	char option[] = "a";
	char criteria[30];
	printf("Filter by: \n\ta) manufacturer;\n\tb) price;\n\tc) quantity.\nEnter option: ");
	scanf_s("%s", option, 2);
	if (strcmp(option, "a") == 0) {
		printf("Manufacturer = ");
		scanf_s("%s", criteria, 30);
	}
	if (strcmp(option, "b") == 0) {
		printf("Price = ");
		scanf_s("%s", criteria, 30);
	}
	if (strcmp(option, "c") == 0) {
		printf("Quantity = ");
		scanf_s("%s", criteria, 30);
	}
	MyList filtered_list = filterProductService(products, option, criteria);
	printProducts(&filtered_list);
	destroyList(&filtered_list);
}


void run() {
	MyList products = createList();
	int running = 1;
	while (running) {
		printMenu();
		int option;
		printf("Enter option: ");
		scanf_s("%d", &option);
		switch (option) {
		case 0:
			printProducts(&products);
			break;
		case 1:
			addProductUi(&products);
			break;
		case 2:
			updateProductUi(&products);
			break;
		case 3:
			deleteProductUi(&products);
			break;
		case 4:
			sortProductsUi(&products);
			break;
		case 5:
			filterProductsUi(&products);
			break;
		case 6:
			running = 0;
			break;
		default:
			printf("Invalid option!\n");
		}
	}
	destroyList(&products);
}