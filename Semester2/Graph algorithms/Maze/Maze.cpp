#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <algorithm>
#include <string>

using namespace std;

struct Point {
	int x;
	int y;
};

const int dx[] = { -1, 0, 1, 0 };
const int dy[] = { 0, 1, 0, -1 };

bool isValid(int row, int col, int numRows, int numCols) {
	return (row >= 0) && (row < numRows) && (col >= 0) && (col < numCols);
}

bool isUnBlocked(vector<vector<int>>& maze, int row, int col) {
	return (maze[row][col] == 0);
}

vector<Point> shortestPath(vector<vector<int>>& maze, Point src, Point dest) {
	int numRows = (int)maze.size();
	int numCols = (int)maze[0].size();
	vector<vector<int>> dist(numRows, vector<int>(numCols, -1));

	queue<Point> q;
	dist[src.x][src.y] = 0;
	q.push(src);
	while (!q.empty()) {
		Point curr = q.front();
		q.pop();
		if (curr.x == dest.x && curr.y == dest.y) {
			vector<Point> path;
			Point currPoint = dest;
			while (!(currPoint.x == src.x && currPoint.y == src.y)) {
				path.push_back(currPoint);
				for (int i = 0; i < 4; i++) {
					int newRow = currPoint.x + dx[i];
					int newCol = currPoint.y + dy[i];
					if (isValid(newRow, newCol, numRows, numCols) && dist[newRow][newCol] == dist[currPoint.x][currPoint.y] - 1) {
						currPoint = Point{ newRow, newCol };
						break;
					}
				}
			}
			path.push_back(src);
			reverse(path.begin(), path.end());
			return path;
		}
		for (int i = 0; i < 4; i++) {
			int newRow = curr.x + dx[i];
			int newCol = curr.y + dy[i];
			if (isValid(newRow, newCol, numRows, numCols) && isUnBlocked(maze, newRow, newCol) && dist[newRow][newCol] == -1) {
				dist[newRow][newCol] = dist[curr.x][curr.y] + 1;
				q.push(Point{ newRow, newCol });
			}
		}
	}
	return vector<Point>();
}

int main() {
	ifstream fin("maze.txt");
	ofstream fout("output.txt");
	vector<vector<int>> maze(22, vector<int>(42));
	string line;
	char a;
	Point start{ 0, 0 }, finish{ 0, 0 };

	for (int i = 0; i < 22; i++) {
		getline(fin, line);
		for (int j = 0; j < 42; j++) {
			a = line[j];
			if (a == 'S') {
				maze[i][j] = 0;
				start = { i, j };
			}
			else if (a == 'F') {
				maze[i][j] = 0;
				finish = { i, j };
			}
			else if (a == '1') {
				maze[i][j] = 1;
			}
			else {
				maze[i][j] = 0;
			}
		}
	}

	for (int i = 0; i < 21; i++) {
		for (int j = 1; j < 40; j++) {
			if (maze[i][j - 1] == 1 && maze[i][j] == 0) {
				maze[i][j] = 2;
			}
			else if (maze[i][j] == 0 && maze[i][j + 1] == 1)
				maze[i][j] = 2;
		}
	}

	vector<Point> path = shortestPath(maze, start, finish);
	for (int i = 0; i < path.size(); i++)
		maze[path[i].x][path[i].y] = 8;

	for (int i = 0; i < 21; i++) {
		for (int j = 0; j < 41; j++) {
			if (maze[i][j] == 0) {
				fout << ' ';
			}
			else if (maze[i][j] == 8) {
				fout << '*';
			}
			else if (maze[i][j] == 2) {
				fout << ' ';
			}
			else {
				fout << maze[i][j];
			}
		}
		fout << "\n";
	}

	fin.close();
	fout.close();

	return 0;
}