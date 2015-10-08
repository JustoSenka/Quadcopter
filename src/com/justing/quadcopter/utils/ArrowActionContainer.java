package com.justing.quadcopter.utils;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class ArrowActionContainer {
	
	protected short dx = 0, dy = 0, ex = 0, ey = 0, ez = 0, shift = 0;
	protected JFrame frame;
	private final static ArrowActionContainer Instance = new ArrowActionContainer();
	private ArrowActionContainer(){}
	
	public static final ArrowActionContainer getInstance() {
		return Instance;
	}

	public void configureKeyBindings(JFrame frame) {
		this.frame = frame;
		JRootPane rootPane = frame.getRootPane();
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPane.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightArrowDown");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftArrowDown");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "UpArrowDown");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownArrowDown");
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightArrowUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftArrowUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "UpArrowUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownArrowUp");
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "A");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "W");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "S");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "Space");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, false), "f");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false), "v");
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "dUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "aUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "wUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "sUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "SpaceUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, true), "fUp");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, true), "vUp");
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "esc");

        am.put("RightArrowDown", this.new ArrowAction("RightArrowDown"));
        am.put("LeftArrowDown", this.new ArrowAction("LeftArrowDown"));
        am.put("UpArrowDown", this.new ArrowAction("UpArrowDown"));
        am.put("DownArrowDown", this.new ArrowAction("DownArrowDown"));
        
        am.put("RightArrowUp", this.new ArrowAction("RightArrowUp"));
        am.put("LeftArrowUp", this.new ArrowAction("LeftArrowUp"));
        am.put("UpArrowUp", this.new ArrowAction("UpArrowUp"));
        am.put("DownArrowUp", this.new ArrowAction("DownArrowUp"));
        
        am.put("D", this.new ArrowAction("D"));
        am.put("A", this.new ArrowAction("A"));
        am.put("W", this.new ArrowAction("W"));
        am.put("S", this.new ArrowAction("S"));
        am.put("Space", this.new ArrowAction("Space"));
        am.put("f", this.new ArrowAction("f"));
        am.put("v", this.new ArrowAction("v"));
        
        am.put("dUp", this.new ArrowAction("dUp"));
        am.put("aUp", this.new ArrowAction("aUp"));
        am.put("wUp", this.new ArrowAction("wUp"));
        am.put("sUp", this.new ArrowAction("sUp"));
        am.put("SpaceUp", this.new ArrowAction("SpaceUp"));
        am.put("fUp", this.new ArrowAction("fUp"));
        am.put("vUp", this.new ArrowAction("vUp"));
        
        am.put("esc", this.new ArrowAction("esc"));
	}
	
	public short getDx() {return dx;}
	public short getDy() {return dy;}
	public short getEx() {return ex;}
	public short getEy() {return ey;}
	public short getEz() {return ez;}
	public short getShift() {return shift;}
	
	@SuppressWarnings("serial")
	protected class ArrowAction extends AbstractAction {

		private String cmd;
		protected ArrowAction(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (cmd.equalsIgnoreCase("LeftArrowDown")) {
				dx = -1;
			} else if (cmd.equalsIgnoreCase("RightArrowDown")) {
				dx = 1;
			} else if (cmd.equalsIgnoreCase("UpArrowDown")) {
				dy = 1;
			} else if (cmd.equalsIgnoreCase("DownArrowDown")) {
				dy = -1;
			} else if (cmd.equalsIgnoreCase("LeftArrowUp")) {
				if (dx != 1)dx = 0;
			} else if (cmd.equalsIgnoreCase("RightArrowUp")) {
				if (dx != -1)dx = 0;
			} else if (cmd.equalsIgnoreCase("UpArrowUp")) {
				if (dy != -1)dy = 0;
			} else if (cmd.equalsIgnoreCase("DownArrowUp")) {
				if (dy != 1)dy = 0;
			} else if (cmd.equalsIgnoreCase("A")) {
				ex = -1;
			} else if (cmd.equalsIgnoreCase("D")) {
				ex = 1;
			} else if (cmd.equalsIgnoreCase("W")) {
				ez = -1;
			} else if (cmd.equalsIgnoreCase("S")) {
				ez = 1;
			} else if (cmd.equalsIgnoreCase("Space")) {
				shift = 1;
			} else if (cmd.equalsIgnoreCase("f")) {
				ey = 1;
			} else if (cmd.equalsIgnoreCase("v")) {
				ey = -1;
			} else if (cmd.equalsIgnoreCase("aUp")) {
				if (ex != 1)ex = 0;
			} else if (cmd.equalsIgnoreCase("dUp")) {
				if (ex != -1)ex = 0;
			} else if (cmd.equalsIgnoreCase("wUp")) {
				if (ez != 1)ez = 0;
			} else if (cmd.equalsIgnoreCase("sUp")) {
				if (ez != -1)ez = 0;
			} else if (cmd.equalsIgnoreCase("fUp")) {
				if (ey != -1)ey = 0;
			} else if (cmd.equalsIgnoreCase("vUp")) {
				if (ey != 1)ey = 0;
			} else if (cmd.equalsIgnoreCase("SpaceUp")) {
				shift = 0;
			} else if (cmd.equalsIgnoreCase("esc")){
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
}