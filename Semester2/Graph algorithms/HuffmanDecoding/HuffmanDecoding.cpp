#include <iostream>
#include <fstream>
#include <map>
#include <queue>

using namespace std;

struct HuffmanNode {
    char data;
    int frequency;
    HuffmanNode* left, * right;

    HuffmanNode(char data, int frequency) : data(data), frequency(frequency), left(nullptr), right(nullptr) {}
};

struct CompareNodes {
    bool operator()(HuffmanNode* a, HuffmanNode* b) {
        if (a->frequency == b->frequency) {
            return a->data > b->data;
        }
        return a->frequency > b->frequency;
    }
};

void read(const string& filename, map<char, int>& frequencies, string& huffmanCode) {
    ifstream file(filename);
    int n;
    file >> n;
    for (int i = 0; i < n; ++i) {
        char ch;
        int freq;
        file >> ch >> freq;
        frequencies[ch] = freq;
    }
    file >> huffmanCode;
    file.close();
}

HuffmanNode* buildHuffmanTree(const map<char, int>& frequencies) {
    priority_queue<HuffmanNode*, vector<HuffmanNode*>, CompareNodes> pq;

    for (auto& pair : frequencies) {
        pq.push(new HuffmanNode(pair.first, pair.second));
    }

    while (pq.size() > 1) {
        HuffmanNode* left = pq.top(); pq.pop();
        HuffmanNode* right = pq.top(); pq.pop();

        HuffmanNode* parent = new HuffmanNode('\0', left->frequency + right->frequency);
        parent->left = left;
        parent->right = right;

        pq.push(parent);
    }

    return pq.top();
}

string decodeHuffmanText(const string& huffmanCode, HuffmanNode* root) {
    string decodedText;
    HuffmanNode* current = root;
    for (char c : huffmanCode) {
        if (c == '0') {
            current = current->left;
        }
        else {
            current = current->right;
        }
        if (current->left == nullptr && current->right == nullptr) {
            decodedText += current->data;
            current = root;
        }
    }
    return decodedText;
}

int main(int argc, char* argv[]) {
    map<char, int> frequencies;
    string huffmanCode;

    read(argv[1], frequencies, huffmanCode);

    HuffmanNode* root = buildHuffmanTree(frequencies);

    string decodedText = decodeHuffmanText(huffmanCode, root);

    ofstream outputFile(argv[2]);
    outputFile << decodedText << endl;
    outputFile.close();

    delete root;

    return 0;
}
