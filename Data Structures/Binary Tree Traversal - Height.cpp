// Paul Stevens
// October 30, 2019
// Create a binary tree using nodes and using a queue, print out height of the maximum level number
// of any node in the tree rooted at height 1.

#include <iostream>
#include <queue>
using namespace std;

struct node{
	node *left;
	int data;
	int level;
	node *right;
	node(int x)
	{
		data = x;
		left = 0;
		right = 0;
		level = 0;
	}
};

int levelOrderTraverse(node *root)
{
	int height = 1;
	if (root == NULL)
		return 0;
	queue<node *> que;
	que.push(root);
	while (que.empty() == false)
	{
		node *node = que.front();
		height = node->level;
		que.pop();
		if (node->left != NULL)
			que.push(node->left);
		if (node->right != NULL)
			que.push(node->right);
	}
	return height;
}

void treeinsertLeft(node *root, int key, int value)
{
	if (root == NULL)
		return;
	if (root->data == key)
	{
		if (root->left == NULL)
		{
			root->left = new node(value);
			root->left->level = root->level + 1;
			return;
		}
		else
			cout << "Node already has a left child." << endl;
			return;
	}
	else
		treeinsertLeft(root->left, key, value);
		treeinsertLeft(root->right, key, value);	
}

void treeinsertRight(node *root, int key, int value)
{
	if (root == NULL)
		return;
	if (root->data == key)
	{
		if (root->right == NULL)
		{
			root->right = new node(value);
			root->right->level = root->level + 1;
			return;
		}
		else
			cout << "Node already has a right child." << endl;
			return;
	}
	else
		treeinsertRight(root->left, key, value);
		treeinsertRight(root->right, key, value);	
}


int main()
{
	char input;
	int num, key;
	int height;
	node *root = NULL;

	
	cout << "Please make a choice base on which option you need: " << endl;
	cout << "C: Creates a root node." << endl;
	cout << "L: Search for node with specified data. Creates a left child for that node with specified data." << endl;
	cout << "R: Search for node with specified data. Creates a right child for that node with specified data." << endl;
	cout << "P: Prints the maximum height of any node in the tree." << endl;
	cout << "Z: Terminates the program." << endl;
	
	cout << "Please enter your selection: ";
	cin >> input;
	cout << endl;
	
	do
	{
		switch (input)
		{
			case 'C':
			case 'c':
				if (root != NULL)
				{
					cout << "There is already a root node, you can't create another one. Please make another selection: ";
					cin >> input;
					break;
				}
				else
					cout << "Please enter the number you wish to create a root node with: ";
					cin >> num;
					root = new node(num);
				cout << "Please make another selection: ";
				cin >> input;	
				break;
			case 'L':
			case 'l':
				cout << "Please enter the node you wish to add a left child to: ";
				cin >> key;
				cout << "Please enter the value you want to create a node with: ";
				cin >> num;
				treeinsertLeft(root, key, num);
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'R':
			case 'r':
				cout << "Please enter the node you wish to add a right child to: ";
				cin >> key;
				cout << "Please enter the value you want to create a node with: ";
				cin >> num;
				treeinsertRight(root, key, num);
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'P':
			case 'p':
				cout << "Height of the tree is " << levelOrderTraverse(root) << endl;			
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'Z':
			case 'z':
				return 0;
			default:
				cout << "Invalid Input - Please make another selection (C, L, R, P, S, Z): ";
				cin >> input;
		}
	}
	while (input != 'Z');
	return 0;
}
