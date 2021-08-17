// Paul Stevens
// October 30, 2019
// Create a binary tree using nodes and using a queue, print out nodes in level-order traversal

#include <iostream>
#include <queue>
using namespace std;

struct node{
	node *left;
	int data;
	node *right;
	node(int x)
	{
		data = x;
		left = 0;
		right = 0;
	}
};

void levelOrderTraverse(node *root)
{
	if (root == NULL)
		return;
	queue<node *> que;
	que.push(root);
	while (que.empty() == false)
	{
		node *node = que.front();
		cout << node->data << " ";
		que.pop();
		if (node->left != NULL)
			que.push(node->left);
		if (node->right != NULL)
			que.push(node->right);
	}	
}

void subrootlevelTraversal(node *root, int key)
{
	node *found = NULL;
	if (root == NULL)
		return;
	queue<node *> que;
	que.push(root);
	while (found == NULL)
	{
		node *node = que.front();
		que.pop();
		if (node->data == key)
		{
			found = node;
		}
		else if (node->left != NULL)
			que.push(node->left);
		else if (node->right != NULL)
			que.push(node->right);
	}
	que = queue<node *>();
	que.push(found);
	while (que.empty() == false)
	{
		node *node = que.front();
		cout << node->data << " ";
		que.pop();
		if (node->left != NULL)
			que.push(node->left);
		if (node->right != NULL)
			que.push(node->right);
	}	
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
	node *root = NULL;

	
	cout << "Please make a choice base on which option you need: " << endl;
	cout << "C: Creates a root node." << endl;
	cout << "L: Search for node with specified data. Creates a left child for that node with specified data." << endl;
	cout << "R: Search for node with specified data. Creates a right child for that node with specified data." << endl;
	cout << "P: Prints the data of each node in a level order traversal manner." << endl;
	cout << "S: Search for node with specified data. Print out data of each node in the subtree rooted at secified data in a level order traversal manner " << endl;
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
				levelOrderTraverse(root);				
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'S':
			case 's':
				cout << "Please enter the node you wish to select as the subtree to print in a level order traversal manner: ";
				cin >> key;
				subrootlevelTraversal(root, key);					
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
