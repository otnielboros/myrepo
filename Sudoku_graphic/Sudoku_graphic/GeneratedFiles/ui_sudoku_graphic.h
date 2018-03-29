/********************************************************************************
** Form generated from reading UI file 'sudoku_graphic.ui'
**
** Created by: Qt User Interface Compiler version 5.6.2
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SUDOKU_GRAPHIC_H
#define UI_SUDOKU_GRAPHIC_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_Sudoku_graphicClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *Sudoku_graphicClass)
    {
        if (Sudoku_graphicClass->objectName().isEmpty())
            Sudoku_graphicClass->setObjectName(QStringLiteral("Sudoku_graphicClass"));
        Sudoku_graphicClass->resize(600, 400);
        menuBar = new QMenuBar(Sudoku_graphicClass);
        menuBar->setObjectName(QStringLiteral("menuBar"));
        Sudoku_graphicClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(Sudoku_graphicClass);
        mainToolBar->setObjectName(QStringLiteral("mainToolBar"));
        Sudoku_graphicClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(Sudoku_graphicClass);
        centralWidget->setObjectName(QStringLiteral("centralWidget"));
        Sudoku_graphicClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(Sudoku_graphicClass);
        statusBar->setObjectName(QStringLiteral("statusBar"));
        Sudoku_graphicClass->setStatusBar(statusBar);

        retranslateUi(Sudoku_graphicClass);

        QMetaObject::connectSlotsByName(Sudoku_graphicClass);
    } // setupUi

    void retranslateUi(QMainWindow *Sudoku_graphicClass)
    {
        Sudoku_graphicClass->setWindowTitle(QApplication::translate("Sudoku_graphicClass", "Sudoku_graphic", 0));
    } // retranslateUi

};

namespace Ui {
    class Sudoku_graphicClass: public Ui_Sudoku_graphicClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SUDOKU_GRAPHIC_H
