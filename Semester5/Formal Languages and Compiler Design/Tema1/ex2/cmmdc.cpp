#include <iostream>
using namespace std;

int main() {
    int a, b, temp;
    cin >> a;  
    cin >> b; 

    for (; b != 0; ) { 
        temp = b; 
        b = a % b; 
        a = temp; 
    }

    cout << a; 

    return 0;
}
