package main;

import listeners.FrameListener;
import listeners.TabbedPaneChangeListener;
import listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

    private  Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private  JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public View() {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            ExceptionHandler.log(e);
        } catch (InstantiationException e) {
            ExceptionHandler.log(e);
        } catch (IllegalAccessException e) {
            ExceptionHandler.log(e);
        } catch (UnsupportedLookAndFeelException e) {
            ExceptionHandler.log(e);
        }
    }

    public void init(){
        initGui();
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        setVisible(true);
        initMenuBar();


    }

    public void  initMenuBar(){
        //Файл, Редактировать, Стиль, Выравнивание, Цвет, Шрифт и Помощь.
        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this,jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this,jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);
        getContentPane().add(jMenuBar,BorderLayout.NORTH);




    }

    public void initEditor(){
        htmlTextPane.setContentType("text/html");
        JScrollPane jScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML",jScrollPane );
        JScrollPane jScrollPane1 = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст",jScrollPane1);
        tabbedPane.setPreferredSize(new Dimension(480,600));
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        getContentPane().add(tabbedPane,BorderLayout.CENTER);



    }

    public void update(){
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void undo(){
        try {
            undoManager.undo();
        } catch (CannotUndoException e) {
            ExceptionHandler.log(e);
        }

    }

    public void redo(){
        try {
            undoManager.redo();
        } catch (CannotRedoException e) {
            ExceptionHandler.log(e);
        }

    }

    public boolean canUndo(){
        if(undoManager.canUndo()) return true;
        else return false;
    }

    public boolean canRedo(){
        if(undoManager.canRedo()) return true;
        else return false;
    }

    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    public boolean isHtmlTabSelected(){
       if (tabbedPane.getSelectedIndex() == 0 ) return true;
       else return false;
    }

    public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void initGui(){
        initMenuBar();
        initEditor();
        pack();
    }

    public void selectedTabChanged(){
        if (tabbedPane.getSelectedIndex() == 0 ){
            controller.setPlainText(plainTextPane.getText());
        }
        else{
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();

    }

    public Controller getController() {
        return controller;
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String s = e.getActionCommand();
        switch (s){
            case "Новый" :
                controller.createNewDocument();
                break;
            case "Открыть" :
                controller.openDocument();
                break;
            case "Сохранить" :
                controller.saveDocument();
                break;
            case "Сохранить как..." :
                controller.saveDocumentAs();
            case "Выход" :
                controller.exit();
                break;
            case "О программе" :
                showAbout();
                break;
        }

    }

    public void exit(){
        controller.exit();
    }

    public void showAbout(){
        String text = "mrslilex@gmail.com  and ivanovAlexeyAlexandrovich@gmail.com";
        JOptionPane.showMessageDialog(getContentPane(), "Version 1.0" + "\n"+ text, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
