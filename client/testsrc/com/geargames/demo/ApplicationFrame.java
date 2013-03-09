package com.geargames.demo;

//import java.awt.*;
import java.awt.event.*;

//import org.eclipse.*;

import javax.swing.*;

//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.widgets.Button;

// AppFrame, MainFrame
public final class ApplicationFrame extends JFrame
{
	private static final long serialVersionUID = -6663224713130273162L;

	public ApplicationFrame() 
	{
		super();
	}

	/** Component initialization */
	@Override
	protected void frameInit()
	{
		super.frameInit();
		
		this.setTitle("Matrix Editor");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		//this.setResizable(false);
		
		this.setSize(600, 400);
//		this.setSize(
//		panel.getWidth()+frame.getInsets().left+frame.getInsets().right, 
//		panel.getHeight()+frame.getInsets().top+frame.getInsets().bottom);
		
		// Center the window
		this.setLocationRelativeTo(null);
		/*
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		*/
		
		initUI();
	}
	
	private void initUI()
	{
//		Button quit = new Button(this, SWT.PUSH);
//        quit.setText("Quit");
//        quit.setBounds(50, 50, 80, 30);
//
//        quit.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//                shell.getDisplay().dispose();
//                System.exit(0);
//            }
//        });
	}
	
	/** Overridden so we can exit when window is closed */
	@Override
	protected void processWindowEvent(WindowEvent e)
	{
		super.processWindowEvent(e);
//		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//			System.exit(0);
//		}
	}
}
