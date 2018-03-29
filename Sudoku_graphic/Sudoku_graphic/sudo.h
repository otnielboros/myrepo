#pragma once
#include "qwidget.h"
#include "qpushbutton.h"
#include "qlayout.h"
#include "qboxlayout.h"
#include "qlabel.h"
#include "qpainter.h"
#include "qgridlayout.h"
#include "qlayout.h"
#include <qlineedit.h>
#include <vector>

using namespace std;

class GenericSudo :public QWidget {
private:
	int n;
	QPushButton* solve = new QPushButton{ "Solution" };
	QPushButton* clearSolution = new QPushButton{ "Hide Solution" };
	QPushButton* verify = new QPushButton{ "Verify this" };
	QPushButton* newSudoku = new QPushButton{ "Clear all" };
	QVBoxLayout* desen = new QVBoxLayout;
	int Sudoku[100][100] = {}, Sudoku2[100][100] = {};
	vector<QLineEdit*> allC;
public:
	GenericSudo(int n) :n{n} {
		init();
		connectSignal();
	}
	int Verific();
	int Solutie();
	void connectSignal();
	void init();
	void paint(QWidget* wind);
	void paintEvent(QPaintEvent* ev) override;
};

class Sudo :public QWidget {
private:
	QWidget* wind = new QWidget;
	QPushButton* sudo4x4 = new QPushButton{ "4x4" };
	QPushButton* sudo6x6 = new QPushButton{ "6x6" };
	QPushButton* sudo8x8 = new QPushButton{ "8x8" };
	QPushButton* sudo9x9 = new QPushButton{ "9x9" };
	QLabel* msg = new QLabel{ "<b>Choose what kind of sudoku do you want to solve<\b>" };
	QHBoxLayout* desen = new QHBoxLayout;
	QVBoxLayout* desenp = new QVBoxLayout;
public:
	Sudo() {
		init();
		connectSignals();
	}
	void init();
	void connectSignals();
};