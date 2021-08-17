//Paul Stevens
//October 10 2019
//Implement linked-list of FIFO QUEUE, accept input, output element and head of queue (outputs
//"empty" if no elements), and displays entire content of queue depending on user input.

#include <iostream>
#include <string>
#include <iomanip>

using namespace std;

class QUEUE
{
	private:
		struct node
		{	
			int item;
			node* next;
			node(int x)
			{item = x; next = 0;}
		};
	typedef node *link;
	link head, tail;
	
	public:
		QUEUE(int)
		{
			head = 0;
		}
		int empty() const
		{
			return head == 0;
		}
		void put(int x)
		{
			link t = tail;
			tail = new node(x);
			if (head == 0)
				head = tail;
			else
				t->next = tail;
		}
		int get()
		{
			int v = head->item;
			link t = head->next;
			delete head;
			head = t;
			return v;
		}
		void peekAll()
		{
			link t = head;
			while (t != 0)
			{
				cout << t->item << " ";
				t = t->next;
			}
		}
};
const int N = 100;

int main()
{
	QUEUE que(N);
	char input;
	int num;

	
	cout << "Please make a choice base on which option you need: " << endl;
	cout << "P: Pushes an integer onto the queue." << endl;
	cout << "G: Outputs element at the beginning of the queue then removes it from the queue." << endl;
	cout << "E: Outputs 'Empty' if the queue is empty or 'Not Empty' if the queue has some element(s)." << endl;
	cout << "F: Outputs the entire content of the queue without removing any element from it." << endl;
	cout << "Z: Terminates the program." << endl;
	
	cout << "Please enter your selection: ";
	cin >> input;
	cout << endl;
	
	do
	{
		switch (input)
		{
			case 'P':
			case 'p':
				cout << "Please enter a number to push onto the queue: ";
				cin >> num;
				cout << endl;
				que.put(num);
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'G':
			case 'g':
				if (que.empty())
					cout << "Nothing to get. Queue is empty." << endl;
				else
					cout << que.get() << endl;
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'E':
			case 'e':
				if (que.empty())
					cout << "Queue is empty." << endl;
				else
					cout << "Queue is not empty." << endl;
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'F':
			case 'f':
				if (que.empty())
					cout << "Nothing to display. Queue is empty." << endl;
				else
					que.peekAll();
				cout << endl;						
				cout << "Please make another menu selection: ";
				cin >> input;
				break;
			case 'Z':
			case 'z':
				return 0;
			default:
				cout << "Invalid Input - Please make another selection (P, G, E, F, Z): ";
				cin >> input;
		}
	}
	while (input != 'Z');
	return 0;
}
