package com.swing.component;

import com.swing.callback.ActionCallback;
import com.swing.component.inf.IRightMenu;
import com.swing.menu.MenuUtil2;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/***
 * 增加了右键菜单功能和自动补全功能
 * 
 * @author huangwei
 * @since 2014-02-08
 */
public class AssistPopupTextField extends UndoTextField implements IRightMenu {

	private static final long serialVersionUID = -5051794705721682199L;
	protected JPopupMenu textPopupMenu;
    /**
     * 防止方法custom2执行两遍
     * <br >是否已经执行过方法custom2()
     */
    private boolean hasCustom = false;

	public AssistPopupTextField(boolean needSearch) {
		super(needSearch);
	}

	public AssistPopupTextField() {
		super(true);
	}

	public AssistPopupTextField(int size, boolean needSearch) {
		super(size, needSearch);
	}

	public AssistPopupTextField(int size) {
		super(size, true);
	}

	public AssistPopupTextField(String text, boolean needSearch) {
		super(text, needSearch);
	}

	public AssistPopupTextField(String text) {
		super(text, true);
	}

	@Override
	protected void initlize(boolean needSearch) {
		super.initlize(needSearch);
        custom();
    }

    @Override
    protected void initlize(boolean needSearch, Map<String, ActionCallback> actionCallbackMap) {
        super.initlize(needSearch, actionCallbackMap);
        custom();
    }

    private void custom() {
        if (hasCustom) {
            return;
        }
            hasCustom = true;
            textPopupMenu = new JPopupMenu();
            MenuUtil2.addPopupMenuItem(this, textPopupMenu);
            override4Extend(textPopupMenu);
            MenuUtil2.setPopupMenu(this, textPopupMenu, null);
            ComponentUtil.assistantTF(this);//增加自动补全
        }

    @Override
    public void showMenu() {
        Point point = this.getLocation();
        //see com/swing/menu/MenuUtil2.java 389行
        textPopupMenu.show(this, point.x + 10, point.y);
    }
    /***
     * 用于子类覆写
	 * 
	 * @param textPopupMenu
	 */
	protected void override4Extend(JPopupMenu textPopupMenu) {
	}

}
