#include "MyList.h"


/*
* Creates the list represented by a dynamic vector
*/
MyList createList() {
	MyList list;
	list.len = 0;
	list.cp = 2;

	// allocate memory for the elements of the list
	list.elements = (ElementType*)malloc(sizeof(ElementType) * list.cp);
	return list;
}


/*
* Deallocate the memory occupied by the list and it's elements
*/
void destroyList(MyList* list) {
	// deallocate the memory occupied by every element
	for (int i = 0; i < list->len; i++) {
		destroyProduct(&list->elements[i]);
	}

	// deallocate the memory occupied by elements
	free(list->elements);

	// give invalid values for marking the destruction of the list
	list->elements = NULL;
	list->len = 0;
}


/*
* Increase the capacity of the list
*/
void resize(MyList* list) {
	// allocate more memory for elements
	int new_cp = list->cp * 2;
	ElementType* new_elements = malloc(sizeof(ElementType) * new_cp);

	// copy the elements in the new location
	if (new_elements) {
		for (int i = -1; i < list->len; i++) {
			if (i > -1) {
				new_elements[i] = list->elements[i];
			}
		}
	}

	// deallocate the old memory location
	free(list->elements);

	// give the old list the new atributes
	list->elements = new_elements;
	list->cp = new_cp;
}


/*
* Add an element
*/
void addElement(MyList* list, ElementType element) {
	// increase the size of the list if it is full
	if (list->len == list->cp) {
		resize(list);
	}

	// add element
	list->elements[list->len] = element;

	// increase the length of the list
	list->len++;
}


/*
* Determins the length of the list
*/
int lenList(MyList* list) {
	return list->len;
}


/*
* Determins the element at the given position
*/
ElementType getElement(MyList* list, int position) {
	return list->elements[position];
}


/*
* Creates a copy of a list
*/
MyList copyList(MyList* list) {
	MyList copy = createList();
	for (int i = 0; i < lenList(list); i++) {
		ElementType element = getElement(list, i);
		addElement(&copy, copyProduct(element));
	}
	return copy;
}


/*
* Update the element at the given position
*/
ElementType setElement(MyList* list, int position, ElementType new_element) {
	ElementType old_element = list->elements[position];
	list->elements[position] = new_element;
	return old_element;
}
