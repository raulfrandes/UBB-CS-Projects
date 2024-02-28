#include <fstream>
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <climits>

using namespace std;

vector<vector<pair<int, int>>> addVertex(vector<vector<pair<int, int>>> adj_list) {
	vector<vector<pair<int, int>>> new_adj_list = adj_list;
	vector<pair<int, int>> new_vertex;
	for (int i = 0; i < adj_list.size(); i++) {
		new_vertex.push_back(make_pair(i, 0));
	}
	new_adj_list.push_back(new_vertex);
	return new_adj_list;
}

void init(vector<vector<pair<int, int>>> adj_list, int s, vector<int>& d, vector<int>& pi) {
	for (int i = 0; i < adj_list.size(); i++) {
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

void Dijkstra(vector<vector<pair<int, int>>> adj_list, int s, vector<int>& d, vector<int>& pi) {
	init(adj_list, s, d, pi);
	vector<int> S;
	priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> Q;
	for (int i = 0; i < adj_list.size(); i++) {
		Q.push(make_pair(d[i], i));
	}
	while (!Q.empty()) {
		int u = Q.top().second;
		Q.pop();
		S.push_back(u);
		for (int i = 0; i < adj_list[u].size(); i++) {
			int v = adj_list[u][i].first;
			relax(u, v, adj_list[u][i].second, d, pi);
		}
		while (!Q.empty()) {
			Q.pop();
		}
		for (int i = 0; i < adj_list.size(); i++) {
			if (!count(S.begin(), S.end(), i)) {
				Q.push(make_pair(d[i], i));
			}
		}
	}
}

vector<vector<int>> Johnson(vector<vector<pair<int, int>>>& adj_list) {
	vector<vector<pair<int, int>>> new_adj_list = addVertex(adj_list);
	int V = new_adj_list.size();
	int E = 0;
	for (int i = 0; i < new_adj_list.size(); i++) {
		E += new_adj_list[i].size();
	}

	vector<int> d(V);
	vector<int> pi(V);
	if (!BellmanFord(new_adj_list, new_adj_list.size() - 1, d, pi)) {
		return vector<vector<int>>(adj_list.size());
	}
	else {
		vector<int> h(adj_list.size());
		for (int i = 0; i < adj_list.size(); i++) {
			h[i] = d[i];
		}
		for (int i = 0; i < adj_list.size(); i++) {
			for (int j = 0; j < adj_list[i].size(); j++) {
				int u = i;
				int v = adj_list[u][j].first;
				adj_list[i][j].second = adj_list[i][j].second + h[u] - h[v];
			}
		}
		vector<vector<int>> D(adj_list.size());
		for (int i = 0; i < adj_list.size(); i++) {
			int u = i;
			vector<int> d(adj_list.size());
			vector<int> pi(adj_list.size());
			Dijkstra(adj_list, u, d, pi);
			for (int j = 0; j < adj_list.size(); j++) {
				int v = j;
				d[v] = d[v] - h[u] + h[v];
			}
			D[u] = d;
		}
		return D;
	}
}

int main(int argc, char** argv)
{
	ifstream fin(argv[1]);
	ofstream fout(argv[2]);
	int V, E, u, v, w;

	fin >> V >> E;
	vector<vector<pair<int, int>>> adj_list(V);
	while (fin >> u >> v >> w) {
		adj_list[u].push_back(make_pair(v, w));
	}

	vector<vector<int>> dist = Johnson(adj_list);
	if (dist[0].empty()) {
		fout << -1;
	}
	else {
		for (int i = 0; i < adj_list.size(); i++) {
			for (int j = 0; j < adj_list[i].size(); j++) {
				fout << i << " " << adj_list[i][j].first << " " << adj_list[i][j].second << endl;
			}
		}
		fout << endl;
		for (int i = 0; i < dist.size(); i++) {
			for (int j = 0; j < dist[i].size(); j++) {
				if (dist[i][j] >= INT_MAX - 100) {
					fout << "INF ";
				}
				else {
					fout << dist[i][j] << " ";
				}
			}
			fout << endl;
		}
	}

	fin.close();
	fout.close();

	return 0;
}