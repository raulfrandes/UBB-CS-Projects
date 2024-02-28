#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <climits>

using namespace std;

void init(vector<vector<pair<int, int>>> list_ad, int s, vector<int>& d, vector<int>& pi) {
	for (int i = 0; i < list_ad.size(); i++) {
		d[i] = INT_MAX - 100;
		pi[i] = -1;
	}
	d[s] = 0;
}

void relax(int u, int v, int w, vector<int>& d, vector<int>& pi) {
	if (d[v] > d[u] + w) {
		d[v] = d[u] + w;
		pi[v] = u;
	}
}

bool BellmanFord(vector<vector<pair<int, int>>> adj_list, int s, vector<int>& d, vector<int>& pi) {
	init(adj_list, s, d, pi);
	for (int i = 0; i < adj_list.size() - 1; i++) {
		for (int j = 0; j < adj_list.size(); j++) {
			for (int k = 0; k < adj_list[j].size(); k++) {
				int u = j;
				int v = adj_list[u][k].first;
				int w = adj_list[u][k].second;
				relax(u, v, w, d, pi);
			}
		}
	}
	for (int i = 0; i < adj_list.size(); i++) {
		for (int j = 0; j < adj_list[i].size(); j++) {
			int u = i;
			int v = adj_list[u][j].first;
			if (d[v] > d[u] + adj_list[u][j].second) {
				return false;
			}
		}
	}
	return true;
}

void Dijkstra(vector<vector<pair<int, int>>> list_ad, int s, vector<int>& d, vector<int>& pi) {
	init(list_ad, s, d, pi);
	vector<int> S;
	priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> Q;
	for (int i = 0; i < list_ad.size(); i++) {
		Q.push(make_pair(i, d[i]));
	}
	while (!Q.empty()) {
		int u = Q.top().first;
		Q.pop();
		S.push_back(u);
		for (int i = 0; i < list_ad[u].size(); i++) {
			int v = list_ad[u][i].first;
			relax(u, v, list_ad[u][i].second, d, pi);
		}
		while (!Q.empty()) {
			Q.pop();
		}
		for (int i = 0; i < list_ad.size(); i++) {
			if (!count(S.begin(), S.end(), i)) {
				Q.push(make_pair(i, d[i]));
			}
		}
	}
}

int main(int argc, char** argv)
{
	ifstream fin(argv[1]);
	ofstream fout(argv[2]);

	int V, E, s, x, y, w;
	fin >> V >> E >> s;
	vector<vector<pair<int, int>>> adj_list(V);
	while (fin >> x >> y >> w) {
		adj_list[x].push_back(make_pair(y, w));
	}

	fout << "Bellman-Ford: ";
	vector<int> d(V);
	vector<int> pi(V);
	bool res = BellmanFord(adj_list, s, d, pi);
	if (res == false) {
		fout << "There is no path.\n";
	}
	else {
		for (int i = 0; i < d.size(); i++) {
			if (d[i] >= INT_MAX - 100) {
				fout << "INF ";
			}
			else {
				fout << d[i] << " ";
			}
		}
		fout << endl;
	}

	fout << "Dijkstra: ";
	vector<int> d2(V);
	vector<int> pi2(V);
	Dijkstra(adj_list, s, d2, pi2);
	for (int i = 0; i < d2.size(); i++) {
		if (d2[i] >= INT_MAX - 100) {
			fout << "INF ";
		}
		else {
			fout << d2[i] << " ";
		}
	}
	fout << endl;

	fin.close();
	fout.close();

	return 0;
}
