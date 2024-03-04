#pragma once

#include "service.h"


void printMenu();

void printProducts(MyList* products);

void addProductUi(MyList* products);

void updateProductUi(MyList* products);

void deleteProductUi(MyList* products);

void sortProductsUi(MyList* products);

void filterProductsUi(MyList* products);

void run();
