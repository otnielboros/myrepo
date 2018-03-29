#include "sudo.h"
#include "qstatictext.h"
#include "qtextformat.h"
#include "qlineedit.h"
#include "qmessagebox.h"
#include "qpalette.h"
#include "qfont.h"

//exersez clasele , puteam sa fac cu o singura clasa si sa folosesc
//painter in functie de stringul dat la constructor



void GenericSudo::paint(QWidget* wind) {
	desen->addWidget(wind);
}

void GenericSudo::init() {
	this->setLayout(desen);
	desen->setMargin(0);
	desen->setSpacing(1);
	this->setFixedSize(60 * n , 60 * n+130);
	for (int i = 0; i < n; i++) {
		QWidget* wind = new QWidget;
		QHBoxLayout* desenpatrat = new QHBoxLayout;
		desenpatrat->setMargin(0);
		desenpatrat->setSpacing(1);
		wind->setLayout(desenpatrat);
		for (int j = 0; j < n; j++) {
			QLineEdit* casuta = new QLineEdit;
			QFont font;
			font.setPixelSize(23);
			casuta->setFont(font);
			casuta->setFixedWidth(59);
			casuta->setFixedHeight(59);
			desenpatrat->addWidget(casuta);
			allC.push_back(casuta);
		}
		paint(wind);
	}
	solve->setFixedHeight(30);
	clearSolution->setFixedHeight(30);
	verify->setFixedHeight(30);
	newSudoku->setFixedHeight(30);
	desen->addWidget(solve, 0, Qt::AlignBottom);
	desen->addWidget(clearSolution, 0, Qt::AlignBottom);
	desen->addWidget(verify, 0, Qt::AlignBottom);
	desen->addWidget(newSudoku, 0, Qt::AlignBottom);
}


void GenericSudo::paintEvent(QPaintEvent* ev) {
	QPainter p{ this };
	switch (n) {
	case 4:
	{
		QLine line1 = QLine((60 * n) / 2 - 1, 0, (60 * n) / 2 - 1, 59 * n + 2);
		QLine line2 = QLine((60 * n) / 2, 0, (60 * n) / 2, 59 * n + 2);
		QLine line3 = QLine((60 * n) / 2 - 2, 0, (60 * n) / 2 - 2, 59 * n + 2);
		p.drawLine(line1);
		p.drawLine(line2);
		p.drawLine(line3);

		QLine line4 = QLine(0, 60 * 2, 60 * n, 60 * 2);
		QLine line5 = QLine(0, 59 * 2, 60 * n, 59 * 2);
		QLine line6 = QLine(0, 59 * 2 + 1, 60 * n, 59 * 2 + 1);
		p.drawLine(line4);
		p.drawLine(line5);
		p.drawLine(line6);

		break;
	}
	case 6:
	{
		QLine line1 = QLine((60 * n) / 2-1, 0, (60*n)/2-1, 59 * n+4);
		QLine line2 = QLine((60 * n) / 2, 0, (60 * n) / 2, 59 * n + 4);
		QLine line3 = QLine((60 * n) / 2 -2, 0, (60 * n) / 2 - 2, 59 * n + 4);
		p.drawLine(line1);
		p.drawLine(line2);
		p.drawLine(line3);

		QLine line4 = QLine(0,60*2,60*n,60*2);
		QLine line5 = QLine(0, 59 * 2, 60 * n, 59 * 2);
		QLine line6 = QLine(0, 59 * 2 +1, 60 * n, 59 * 2 + 1);
		p.drawLine(line4);
		p.drawLine(line5);
		p.drawLine(line6);

		QLine line7 = QLine(0, 60 * 4-1, 60 * n, 60 * 4-1);
		QLine line8 = QLine(0, 60 * 4-2, 60 * n, 60 * 4-2);
		QLine line9 = QLine(0, 60 * 4, 60 * n, 60 * 4);
		p.drawLine(line7);
		p.drawLine(line8);
		p.drawLine(line9);

		break;
	}
	case 8:
	{
		QLine line1 = QLine((60 * n) / 2 - 1, 0, (60 * n) / 2 - 1, 59 * n + 6);
		QLine line2 = QLine((60 * n) / 2, 0, (60 * n) / 2, 59 * n + 6);
		QLine line3 = QLine((60 * n) / 2 - 2, 0, (60 * n) / 2 - 2, 59 * n + 6);
		p.drawLine(line1);
		p.drawLine(line2);
		p.drawLine(line3);


		QLine line4 = QLine(0, 60 * 2, 60 * n, 60 * 2);
		QLine line5 = QLine(0, 59 * 2, 60 * n, 59 * 2);
		QLine line6 = QLine(0, 59 * 2 + 1, 60 * n, 59 * 2 + 1);
		p.drawLine(line4);
		p.drawLine(line5);
		p.drawLine(line6);

		QLine line7 = QLine(0, 60 * 4 - 1, 60 * n, 60 * 4 - 1);
		QLine line8 = QLine(0, 60 * 4 - 2, 60 * n, 60 * 4 - 2);
		QLine line9 = QLine(0, 60 * 4, 60 * n, 60 * 4);
		p.drawLine(line7);
		p.drawLine(line8);
		p.drawLine(line9);

		QLine line10 = QLine(0, 60 * 6, 60 * n, 60 * 6);
		QLine line11 = QLine(0, 60 * 6-1, 60 * n, 60 * 6-1);
		QLine line12 = QLine(0, 60 * 6 -2, 60 * n, 60 * 6-2);
		p.drawLine(line10);
		p.drawLine(line11);
		p.drawLine(line12);

		break;
	}
	case 9:
	{
		QLine line1 = QLine((60 * n) / 3 - 1, 0, (60 * n) / 3 - 1, 59 * n + 7);
		QLine line2 = QLine((60 * n) / 3, 0, (60 * n) / 3, 59 * n + 7);
		QLine line3 = QLine((60 * n) / 3 - 2, 0, (60 * n) / 3 - 2, 59 * n + 7);
		p.drawLine(line1);
		p.drawLine(line2);
		p.drawLine(line3);

		QLine line4 = QLine((60 * n)/1.5 - 1, 0, (60 * n) /1.5 - 1, 59 * n + 7);
		QLine line5 = QLine((60 * n) /1.5, 0, (60 * n) /1.5, 59 * n + 7);
		QLine line6 = QLine((60 * n) /1.5 - 2, 0, (60 * n) / 1.5 - 2, 59 * n + 7);
		p.drawLine(line4);
		p.drawLine(line5);
		p.drawLine(line6);

		QLine line7 = QLine(0, 60 * 3 - 1, 60 * n, 60 * 3 - 1);
		QLine line8 = QLine(0, 60 * 3 - 2, 60 * n, 60 * 3 - 2);
		QLine line9 = QLine(0, 60 * 3, 60 * n, 60 * 3);
		p.drawLine(line7);
		p.drawLine(line8);
		p.drawLine(line9);

		QLine line10 = QLine(0, 60 * 6, 60 * n, 60 * 6);
		QLine line11 = QLine(0, 60 * 6 - 1, 60 * n, 60 * 6 - 1);
		QLine line12 = QLine(0, 60 * 6 - 2, 60 * n, 60 * 6 - 2);
		p.drawLine(line10);
		p.drawLine(line11);
		p.drawLine(line12);

		break;
	}
	}
}

int GenericSudo::Verific()
{
	int i, k, j, l, x;
	for (i = 0; i<9; i++)
	{
		for (j = 0; j<9; j++)
		{
			for (k = j + 1; k<9; k++)
			{
				if (Sudoku[i][j] != 0 && Sudoku[i][j] == Sudoku[i][k])
					return 0;
				if (Sudoku[j][i] != 0 && Sudoku[j][i] == Sudoku[k][i])
					return 0;
			}
		}
	}
	//De aici incep verificarea chenarelor pentru fiecare n dat
	if (n == 9)
	{
		for (i = 0; i <= 2; i++)
		{
			for (j = 0; j <= 2; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 2; k++)
				{
					for (l = 0; l <= 2; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 3; j <= 5; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 2; k++)
				{
					for (l = 3; l <= 5; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 6; j <= 8; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 2; k++)
				{
					for (l = 6; l <= 8; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 3; i <= 5; i++)
		{
			for (j = 0; j <= 2; j++)
			{
				x = Sudoku[i][j];
				for (k = 3; k <= 5; k++)
				{
					for (l = 0; l <= 2; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 3; j <= 5; j++)
			{
				x = Sudoku[i][j];
				for (k = 3; k <= 5; k++)
				{
					for (l = 3; l <= 5; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 6; j <= 8; j++)
			{
				x = Sudoku[i][j];
				for (k = 3; k <= 5; k++)
				{
					for (l = 6; l <= 8; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 6; i <= 8; i++)
		{
			for (j = 0; j <= 2; j++)
			{
				x = Sudoku[i][j];
				for (k = 6; k <= 8; k++)
				{
					for (l = 0; l <= 2; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 3; j <= 5; j++)
			{
				x = Sudoku[i][j];
				for (k = 6; k <= 8; k++)
				{
					for (l = 3; l <= 5; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 6; j <= 8; j++)
			{
				x = Sudoku[i][j];
				for (k = 6; k <= 8; k++)
				{
					for (l = 6; l <= 8; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
	}
	if (n == 4)
	{
		for (i = 0; i <= 1; i++)
		{
			for (j = 0; j <= 1; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= i; k++)
				{
					for (l = 0; l <= 1; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 2; j <= 3; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 1; k++)
				{
					for (l = 2; l <= 3; l++)
					{
						if (k != i && l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 2; i <= 3; i++)
		{
			for (j = 0; j <= 1; j++)
			{
				x = Sudoku[i][j];
				for (k = 2; k <= 3; k++)
				{
					for (l = 0; l <= 1; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 2; j <= 3; j++)
			{
				x = Sudoku[i][j];
				for (k = 2; k <= 3; k++)
				{
					for (l = 2; l <= 3; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
	}
	if (n == 6)
	{
		for (i = 0; i <= 1; i++)
		{
			for (j = 0; j <= 2; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 1; k++)
				{
					for (l = 0; l <= 2; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 3; j <= 5; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 1; k++)
				{
					for (l = 3; l <= 5; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 2; i <= 3; i++)
		{
			for (j = 0; j <= 2; j++)
			{
				x = Sudoku[i][j];
				for (k = 2; k <= 3; k++)
				{
					for (l = 0; l <= 2; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;

						}
					}
				}
			}
			for (j = 3; j <= 5; j++)
			{
				x = Sudoku[i][j];
				for (k = 2; k <= 3; k++)
				{
					for (l = 3; l <= 5; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 4; i <= 5; i++)
		{
			for (j = 0; j <= 2; j++)
			{
				x = Sudoku[i][j];
				for (k = 4; k <= 5; k++)
				{
					for (l = 0; l <= 2; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return x;
						}
					}
				}
			}
			for (j = 3; j <= 5; j++)
			{
				x = Sudoku[i][j];
				for (k = 4; k <= 5; k++)
				{
					for (l = 3; l <= 5; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
	}
	if (n == 8)
	{
		for (i = 0; i <= 1; i++)
		{
			for (j = 0; j <= 3; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 1; k++)
				{
					for (l = 0; l <= 3; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 4; j <= 7; j++)
			{
				x = Sudoku[i][j];
				for (k = 0; k <= 1; k++)
				{
					for (l = 4; l <= 7; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 2; i <= 3; i++)
		{
			for (j = 0; j <= 3; j++)
			{
				x = Sudoku[i][j];
				for (k = 2; k <= 3; k++)
				{
					for (l = 0; l <= 3; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;

						}
					}
				}
			}
			for (j = 4; j <= 7; j++)
			{
				x = Sudoku[i][j];
				for (k = 2; k <= 3; k++)
				{
					for (l = 4; l <= 7; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 4; i <= 5; i++)
		{
			for (j = 0; j <= 3; j++)
			{
				x = Sudoku[i][j];
				for (k = 4; k <= 5; k++)
				{
					for (l = 0; l <= 3; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return x;
						}
					}
				}
			}
			for (j = 4; j <= 7; j++)
			{
				x = Sudoku[i][j];
				for (k = 4; k <= 5; k++)
				{
					for (l = 4; l <= 7; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}
		for (i = 6; i <= 7; i++)
		{
			for (j = 0; j <= 3; j++)
			{
				x = Sudoku[i][j];
				for (k = 6; k <= 7; k++)
				{
					for (l = 0; l <= 3; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
			for (j = 4; j <= 7; j++)
			{
				x = Sudoku[i][j];
				for (k = 6; k <= 7; k++)
				{
					for (l = 4; l <= 7; l++)
					{
						if (k != i || l != j)
						{
							if (Sudoku[k][l] != 0 && Sudoku[k][l] == x)
								return 0;
						}
					}
				}
			}
		}

	}
	return 1;

}
int GenericSudo::Solutie()
{
	// in x si y retin coordonatele liniei si coloane cea mai usoara
	int x = -1, y = -1, minim = 10, i, j, c, k,ok=0;
	for (i = 0; i<n; i++)
	{
		for (j = 0; j<n; j++)
		{
			if (Sudoku[i][j] == 0)
			{
				//incep cu cea mai simpla linie sau coloana , crescand dificultatea
				//odata cu umplerea matricei
				c = 0;
				for (k = 1; k <= n; k++)
				{
					Sudoku[i][j] = k;
					if (Verific())
						c++;
					Sudoku[i][j] = 0;
				}
				if (minim>c)
				{
					minim = c;
					x = i;
					y = j;
				}
			}
		}
	}
	//se umple recursiv
	// x nu devine -1 pana cand ajung pozitiile pe ultimu loc gol;
	if (x == -1)
		return 1;
	for (k = 1; k <= n; k++)
	{
		Sudoku[x][y] = k;
		if (Verific())
		{
			if (Solutie())
				return 1;
		}
	}
	Sudoku[x][y] = 0;
	return 0;
}

void GenericSudo::connectSignal() {
	QObject::connect(clearSolution, &QPushButton::clicked, [&]() {
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				string numar = to_string(Sudoku2[i][j]);
				if (numar == "0")
					numar = "";
				Sudoku[i][j] = Sudoku2[i][j];
				allC[k]->setText(QString::fromStdString(numar));
				QPalette *pallete = new QPalette();
				pallete->setColor(QPalette::Text, Qt::black);
				allC[k]->setPalette(*pallete);
				k++;
			}
		}
	});
	QObject::connect(solve, &QPushButton::clicked, [&]() {
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				QString numar = allC[k]->text();
				Sudoku[i][j] = numar.toInt();
				Sudoku2[i][j] = Sudoku[i][j];
				k++;
			}
		}
		if (!Solutie()) {
			QMessageBox::warning(this, "Error", QString::fromStdString("Nu exista solutie!"));
			k = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					allC[k]->setText(QString::fromStdString(""));
					k++;
				}
			}
		}
		else {
			k = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
						string numar = to_string(Sudoku[i][j]);
						if (Sudoku[i][j]!= Sudoku2[i][j]) {
							QPalette *pallete = new QPalette();
							pallete->setColor(QPalette::Text, Qt::green);
							allC[k]->setPalette(*pallete);
						}
						allC[k]->setText(QString::fromStdString(numar));
						k++;
				}
			}
			QMessageBox::information(this, "Info", QString::fromStdString("Solved."));
		}
	});
	QObject::connect(verify, &QPushButton::clicked, [&]() {
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				QString numar = allC[k]->text();
				Sudoku[i][j] = numar.toInt();
				Sudoku2[i][j] = Sudoku[i][j];
				k++;
			}
		}
		k = 0;
		if (Solutie()) {
			if (Verific()) {
				int tr = 1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						if (Sudoku[i][j] != allC[k]->text().toInt()) {
							tr = 0;
							QMessageBox::warning(this, "Info", QString::fromStdString("Incorrect!"));
							break;
						}
						k++;
					}
					if (tr == 0)
						break;
				}
				if (tr == 1)
					QMessageBox::information(this, "Info", QString::fromStdString("Correct!"));
			}
			else {
				QMessageBox::warning(this, "Info", QString::fromStdString("Incorrect!"));
				int k = 0;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						allC[k]->setText(QString::fromStdString(""));
						k++;
					}
				}
			}
		}
		else
		{
			QMessageBox::warning(this, "Info", QString::fromStdString("Incorrect!"));
			int k = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					allC[k]->setText(QString::fromStdString(""));
					k++;
				}
			}
		}
	});
	
	QObject::connect(newSudoku, &QPushButton::clicked, [&]() {
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Sudoku[i][j] =Sudoku2[i][j]= 0;
				allC[k]->setText(QString::fromStdString(""));
				QPalette *pallete = new QPalette();
				pallete->setColor(QPalette::Text, Qt::black);
				allC[k]->setPalette(*pallete);
				k++;
			}
		}
	});
}

void Sudo::init() {
	this->setLayout(desenp);
	wind->setLayout(desen);
	desenp->addWidget(msg);
	desen->addWidget(sudo4x4);
	desen->addWidget(sudo6x6);
	desen->addWidget(sudo8x8);
	desen->addWidget(sudo9x9);
	desenp->addWidget(wind);
	
}

void Sudo::connectSignals() {
	QObject::connect(sudo4x4, &QPushButton::clicked, [&]() {
		GenericSudo* s4 = new GenericSudo{ 4 };
		s4->show();
	});

	QObject::connect(sudo6x6, &QPushButton::clicked, [&]() {
		GenericSudo* s6 = new GenericSudo{ 6 };
		s6->show();
	});

	QObject::connect(sudo8x8, &QPushButton::clicked, [&]() {
		GenericSudo* s8 = new GenericSudo{ 8 };
		s8->show();
	});

	QObject::connect(sudo9x9, &QPushButton::clicked, [&]() {
		GenericSudo* s9 = new GenericSudo{ 9 };
		s9->show();
	});
}