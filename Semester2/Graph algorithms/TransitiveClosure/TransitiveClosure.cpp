#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void getTransitiveClosureMatrix(vector<vector<int>>& tran_closure_matrix) {
	for (int k = 0; k < tran_closure_matrix.size(); k++) {
		for (int i = 0; i < tran_closure_matrix.size(); i++) {
			for (int j = 0; j < tran_closure_matrix.size(); j++) {
				if (tran_closure_matrix[i][j] == 0) {
					tran_closure_matrix[i][j] = (tran_closure_matrix[i][k] && tran_closure_matrix[k][j]);
				}
			}
		}
	}
}

void printTransitiveClosureMatrix(vector<vector<int>> tran_closure_matrix) {
	cout << "The transitive closure:\n";
	for (int i = 0; i < tran_closure_matrix.size(); i++) {
		for (int j = 0; j < tran_closure_matrix[i].size(); j++) {
			cout << tran_closure_matrix[i][j] << " ";
		}
		cout << "\n";
	}
	cout << "\n";
}

int main()
{
	ifstream fin("graph.txt");
	int n, x, y;

	// read from file
	fin >> n;
	vector<vector<int>> adj_matrix(n, vector<int>(n, 0));
	while (fin >> x >> y) {
		x--;
		y--;
		adj_matrix[x][y] = 1;
	}

	// the transitive closure
	vector<vector<int>> tran_closure_matrix = adj_matrix;
	getTransitiveClosureMatrix(tran_closure_matrix);
	printTransitiveClosureMatrix(tran_closure_matrix);

	fin.close();

	return 0;
}