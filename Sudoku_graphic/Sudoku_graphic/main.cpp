#include "sudoku_graphic.h"
#include <QtWidgets/QApplication>
#include "sudo.h"
int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	Sudo* sudo = new Sudo();
	sudo->show();
	
	return a.exec();
}
