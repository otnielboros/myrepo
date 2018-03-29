#ifndef SUDOKU_GRAPHIC_H
#define SUDOKU_GRAPHIC_H

#include <QtWidgets/QMainWindow>
#include "ui_sudoku_graphic.h"

class Sudoku_graphic : public QMainWindow
{
	Q_OBJECT

public:
	Sudoku_graphic(QWidget *parent = 0);
	~Sudoku_graphic();

private:
	Ui::Sudoku_graphicClass ui;
};

#endif // SUDOKU_GRAPHIC_H
