#include <iostream>
using namespace std;

int main() {
    int n, i, suma, numar;
    suma = 0; 
    cin >> n;

    for (i = 0; i < n; i++) {
        cin >> numar; 
        suma = suma + numar;
    }

    cout << suma;

    return 0;
}
