#pragma once

#include "product.h"

typedef Product ElementType;

typedef struct {
	ElementType* elements;
	int len;
	int cp;
} MyList;

/*
* Creates the list represented by a dynamic vector
* @returns the created list
*/
MyList createList();

/*
* Deallocate the memory occupied by the list and it's elements
* @param *list: the address of the list
*/
void destroyList(MyList* list);

/*
* Increase the capacity of the list
* @param *list: the address of the list
*/
void resize(MyList* list);

/*
* Add an element
* @param *list: the address of the list
* @param element: the element that will be added
*/
void addElement(MyList* list, ElementType element);

/*
* Determins the length of the list
* @param *list: the address of the list
* @returns the length of the list
*/
int lenList(MyList* list);

/*
* Determins the element at the given position
* @param *list: the address of the list
* @param position: the position if the element
* @returns the searched element
*/
ElementType getElement(MyList* list, int position);

/*
* Creates a copy of a list
* @param *list: the address of the list
* @returns the copy of the list
*/
MyList copyList(MyList* list);

/*
* Update the element at the given position
* @param *list: the address of the list
* @param position: the position of the element
* @param element: the new element
* @returns the old element
*/
ElementType setElement(MyList* list, int position, ElementType element);
