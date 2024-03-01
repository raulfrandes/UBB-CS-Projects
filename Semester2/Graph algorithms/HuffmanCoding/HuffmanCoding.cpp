#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <queue>
#include <string>

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

string read(const string& filename) {
    ifstream file(filename);
    string text;

    getline(file, text);
    file.close();

    return text;
}

void writeResultsToFile(const string& filename, const vector<pair<char, int>>& alphabet, const string& encodedText) {
    ofstream file(filename);
    file << alphabet.size() << endl;

    for (const auto& entry : alphabet) {
        file << entry.first << " " << entry.second << endl;
    }

    file << encodedText << endl;

    file.close();
}

map<char, int> calculateFrequencies(const string& text) {
    map<char, int> frequencies;
    for (char c : text) {
        frequencies[c]++;
    }
    return frequencies;
}

HuffmanNode* buildHuffmanTree(const map<char, int>& frequencies) {
    priority_queue<HuffmanNode*, vector<HuffmanNode*>, CompareNodes> pq;

    for (auto& pair : frequencies) {
        pq.push(new HuffmanNode(pair.first, pair.second));
    }

    while (pq.size() > 1) {
        HuffmanNode* left = pq.top();
        pq.pop();
        HuffmanNode* right = pq.top();
        pq.pop();

        HuffmanNode* parent = new HuffmanNode('\0', left->frequency + right->frequency);
        parent->left = left;
        parent->right = right;

        pq.push(parent);
    }

    return pq.top();
}

void generateHuffmanCodes(HuffmanNode* root, const string& code, map<char, string>& huffmanCodes) {
    if (root == nullptr) return;

    if (root->left == nullptr && root->right == nullptr) {
        huffmanCodes[root->data] = code;
    }

    generateHuffmanCodes(root->left, code + "0", huffmanCodes);
    generateHuffmanCodes(root->right, code + "1", huffmanCodes);
}

string encodeText(const string& text, const map<char, string>& huffmanCodes) {
    string encodedText;
    for (char c : text) {
        encodedText += huffmanCodes.at(c);
    }
    return encodedText;
}

int main(int argc, char* argv[]) {
    string text = read(argv[1]);

    map<char, int> frequencies = calculateFrequencies(text);

    HuffmanNode* root = buildHuffmanTree(frequencies);

    map<char, string> huffmanCodes;
    generateHuffmanCodes(root, "", huffmanCodes);

    string encodedText = encodeText(text, huffmanCodes);

    vector<pair<char, int>> alphabet;
    for (auto& entry : frequencies) {
        alphabet.push_back(entry);
    }
    writeResultsToFile(argv[2], alphabet, encodedText);

    delete root;

    return 0;
}
