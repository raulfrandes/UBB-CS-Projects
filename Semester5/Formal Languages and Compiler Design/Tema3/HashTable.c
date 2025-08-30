#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "HashTable.h"

unsigned int hash(const char *key) {
    unsigned int hash_value = 0;
    for (int i = 0; key[i] != '\0'; i++) {
        hash_value += key[i];
    }
    hash_value += strlen(key);
    return hash_value % TABLE_SIZE;
}

HashTable* create_table() {
    HashTable *hash_table = malloc(sizeof(HashTable));
    for (int i = 0; i < TABLE_SIZE; i++) {
        hash_table->table[i] = NULL;
    }
    return hash_table;
}

int put(HashTable *hash_table, const char *key) {
    unsigned int index = hash(key);
    unsigned int original_index = index;

    while (hash_table->table[index] != NULL) {
        if (strcmp(hash_table->table[index]->key, key) == 0) {
            return index;
        }
        index = (index + 1) % TABLE_SIZE;
        if (index == original_index) {
            return -1;
        }
    }

    hash_table->table[index] = malloc(sizeof(HashEntry));
    hash_table->table[index]->key = strdup(key);
    return index;
}

int get(HashTable *hash_table, const char *key) {
    unsigned int index = hash(key);
    unsigned int original_index = index;

    while (hash_table->table[index] != NULL) {
        if (strcmp(hash_table->table[index]->key, key) == 0) {
            return index;
        }
        index = (index + 1) % TABLE_SIZE;
        if (index == original_index) {
            break;
        }
    }

    return -1;
}

void free_table(HashTable *hash_table) {
    for (int i = 0; i < TABLE_SIZE; i++) {
        if (hash_table->table[i] != NULL) {
            free(hash_table->table[i]->key);
            free(hash_table->table[i]);
        }
    }
    free(hash_table);
}