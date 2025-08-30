#ifndef HASHTABLE_H
#define HASHTABLE_H

#define TABLE_SIZE 10

typedef struct {
    char *key;
} HashEntry;

typedef struct {
    HashEntry *table[TABLE_SIZE];
} HashTable;

HashTable* create_table();
int put(HashTable *hash_table, const char *key);
int get(HashTable *hash_table, const char *key);
void free_table(HashTable *hash_table);

#endif