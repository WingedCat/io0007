package com.swing.component;

import com.common.bean.FindTxtResultBean;
import com.common.bean.HeightWidthBean;
import com.common.dict.Constant2;
import com.common.util.ImageHWUtil;
import com.common.util.ReflectHWUtils;
import com.common.util.SystemHWUtil;
import com.common.util.WindowUtil;
import com.http.util.HttpSocketUtil;
import com.io.hw.file.util.FileUtils;
import com.io.hw.json.HWJacksonUtils;
import com.string.widget.util.RegexUtil;
import com.string.widget.util.ValueWidget;
import com.swing.callback.ActionCallback;
import com.swing.component.bean.CompDoubleMenuConf;
import com.swing.component.inf.IRightMenu;
import com.swing.component.listener.DoubleMenuListener;
import com.swing.config.ConfigParam;
import com.swing.dialog.*;
import com.swing.dialog.toast.ToastMessage;
import com.swing.event.EventHWUtil;
import com.swing.image.EditSnapShootDialog;
import com.swing.image.bean.BufferedImage2Bean;
import com.swing.listener.DoubleKeyAdapter;
import com.swing.menu.MenuUtil2;
import com.swing.menu.ShiftDropListMenuActionListener;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/***
 * 
 * @author huangweii
 * 2015年11月5日
 */
public class TextCompUtil2 {
	public static final String PLACEHOLDER_DOUBLE_SHIFT="双击Shift,弹出下拉框(生成MD5,生成json)";
	/***
	 * 文本框的字体默认颜色
	 */
	public static final Color DEFAULT_TF_FOREGROUND=Color.black;
	public static final Color PLACEHOLDER_BACKGROUND_COLOR = new Color(204, 204, 204);
	public static final AbstractAction searchAction=new AbstractAction("search111") {
		private static final long serialVersionUID = -3548620001691220571L;

        /***
         * 文本框搜索,文本框关键字搜索
         * @param evt
         */
        public void actionPerformed(ActionEvent evt) {
            searchMnemonicAction(evt);
        }
	};
    public static final AbstractAction maximizeTAAction=new AbstractAction("maximizeTA111") {
        private static final long serialVersionUID = -3548620001691220571L;

        /***
         * 注意:只有把对话框设置到文本框的反射是在com/swing/dialog/DialogUtil.java 的 showMaximizeDialog 中操作的<br />
         * 其他反射操作均在此
         * @param evt
         */
        public void actionPerformed(ActionEvent evt) {
            JTextComponent tf = (JTextComponent) evt.getSource();
            toggleMaximizeDialog(tf);
        }
    };

    public static void toggleMaximizeDialog(JTextComponent tf) {
        int maxStatus = getMaxStatus(tf);
//            System.out.println("maxStatus :" + maxStatus);
        if (maxStatus == 0) {
            setMaxStatus(tf, 1);
            DialogUtil.showMaximizeDialog(tf);
            return;
        }
        JDialog jDialog = getMaxDialog(tf);
        if (null != jDialog) {
            jDialog.dispose();
            setMaxStatus(tf, 0);
            closeMaxDialog(jDialog);
//                    setMaxDialog(tf);
        }
    }

	/***
	 * 用于截图的对话框
	 */
	public static GenericDialog screenshotDialog = getScreenshotGenericDialog();
	public static final AbstractAction copyImage2ClipAction=new AbstractAction("search111") {
		private static final long serialVersionUID = -3548620001691220571L;

		public void actionPerformed(ActionEvent evt) {
			JTextComponent tf = (JTextComponent) evt.getSource();
            //截图,截屏
            TextCompUtil2.copyImgAction(tf);
		}
	};

	private static GenericDialog getScreenshotGenericDialog() {
		return new GenericDialog(){
            @Override
            public void layout3(Container contentPane) {
                super.layout3(contentPane);
                setUndecorated(true);//必需的
                setBackground(Color.RED);
                this.setOpacity(0.3f);//透明度
                ((JPanel) this.getContentPane()).setOpaque(false);
            }
        };
	}

	/***
	 * 文本框中必须有成员变量"findTxtResultBean"
	 * @param evt
     */
	public static void searchMnemonicAction(ActionEvent evt) {
		JTextComponent tf = (JTextComponent) evt.getSource();
		FindTxtResultBean findTxtResultBean = null;

            findTxtResultBean = (FindTxtResultBean) ReflectHWUtils.getObjectValue(tf, Constant2.FINDTXTRESULTBEAN_FIELD);
            int index;
            String keyword = null;
            if (!ValueWidget.isNullOrEmpty(findTxtResultBean)) {
                index = findTxtResultBean.getFoundIndex();
                keyword = findTxtResultBean.getKeyWord();
            } else {
                index = 0;
            }
            if (ValueWidget.isNullOrEmpty(keyword)) {//无关键字,则弹框
                showSearchDialog(tf,null);
                return;
            }
            findTxtResultBean = DialogUtil.searchText(tf, index, keyword);
            if(findTxtResultBean==null){//弹框
                showSearchDialog(tf,keyword);
                return;
            }
            ReflectHWUtils.setObjectValue(tf, Constant2.FINDTXTRESULTBEAN_FIELD, findTxtResultBean);//如果findTxtResultBean为null,则忽略
	}

    public static void addActionMap(final JTextComponent tc, final UndoManager undo, final Map<String, ActionCallback> actionCallbackMap) {
        addActionMap(tc, undo, true, actionCallbackMap);
    }

    /***
     *
     * @param tc
     * @param undo
     * @param needSearch
     * @param actionCallbackMap :key,"Command_enter","Ctrl_enter","alt_enter"
     */
    public static void addActionMap(final JTextComponent tc, final UndoManager undo, boolean needSearch, final Map<String, ActionCallback> actionCallbackMap) {
        tc.getActionMap().put("Undo", new AbstractAction("Undo11") {
			private static final long serialVersionUID = 2434402629308759912L;
			public void actionPerformed(ActionEvent evt) {
				try {
					boolean b = undo.canUndo();
					// System.out.println("whether undo : "+b);
					if (b) {
						undo.undo();
					}
				} catch (CannotUndoException e) {
				}
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, getDefaultModifier()/*"control Z"*/), "Undo");

		//为什么要注释?因为下面有control R,表示只读
		/*tc.getActionMap().put("Redo", new AbstractAction("Redo1111") {
			private static final long serialVersionUID = 5348330289578410517L;

			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canRedo()) {
						undo.redo();
					}
				} catch (CannotRedoException e) {
				}
			}
		});
		tc.getInputMap().put(KeyStroke.getKeyStroke("control R"), "Redo");*/

		tc.getActionMap().put("Copy", new AbstractAction("Copy111") {
			private static final long serialVersionUID = -5151480809625853288L;
			public void actionPerformed(ActionEvent evt) {
				String selectText=tc.getSelectedText();
				if(ValueWidget.isNullOrEmpty(selectText)){
					//如果没有选择的文本,则复制全部
					WindowUtil.setSysClipboardText(tc.getText());
				}else{
					tc.copy();
				}
			}

		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, getDefaultModifier()), "Copy");

		tc.getActionMap().put("Cut", new AbstractAction("Cut") {

			private static final long serialVersionUID = 7316612864835857713L;

			public void actionPerformed(ActionEvent evt) {
				tc.cut();
			}

		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_X, getDefaultModifier()/*"control X"*/), "Cut");

		tc.getActionMap().put("Paste", new AbstractAction("Paste111") {
			private static final long serialVersionUID = -3548620001691220571L;

			public void actionPerformed(ActionEvent evt) {
				tc.paste();
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, getDefaultModifier()/*"control V"*/), "Paste");

		// redo Ctrl + Y
		tc.getActionMap().put("Redo", new AbstractAction("reDo111") {
			private static final long serialVersionUID = -3548620001691220571L;

			public void actionPerformed(ActionEvent evt) {
				if (undo.canRedo()) {
					undo.redo();
				}
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, getDefaultModifier()/*"control Y"*/), "Redo");

        // right menu-- Ctrl + M
        tc.getActionMap().put("RightMenu", new AbstractAction("RightMenu111") {
            private static final long serialVersionUID = -3548620001691220571L;

            public void actionPerformed(ActionEvent evt) {
                System.out.println("RightMenu");
                if (tc instanceof IRightMenu) {
                    IRightMenu rightMenu = (IRightMenu) tc;
                    rightMenu.showMenu();
                }
            }
        });
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, getDefaultModifier()/*"control M"*/), "RightMenu");



		/*tc.getActionMap().put("Save", new AbstractAction("save111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				dealSave((JTextComponent)evt.getSource());
			}
		});
		tc.getInputMap().put(KeyStroke.getKeyStroke("control S"), "Save");*/

        //按Ctrl+shift+R 使文本框只读,不可编辑
		tc.getActionMap().put("Readonly", new AbstractAction("Readonly111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				JTextComponent tf=(JTextComponent)evt.getSource();
				if(!ValueWidget.isNullOrEmpty(tf)){
					tf.setEditable(false);
                    ToastMessage.toastRight("变为只读", 1000);
				}
			}
		});
        //command+R-->command+shift+R
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, getDefaultModifier() | InputEvent.SHIFT_DOWN_MASK/*"control R"*/), "Readonly");

        //按 shift + Ctrl+E 使文本框可编辑
		tc.getActionMap().put("Editable", new AbstractAction("Editable111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				JTextComponent tf=(JTextComponent)evt.getSource();
                if (ValueWidget.isNullOrEmpty(tf)) {
                    return;
                }
                tf.setEditable(true);
					tf.requestFocus();
					tf.repaint();
					tf.updateUI();
                tf.selectAll();//全选
				}
		});
        //on Mac ,this would be command key
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, getDefaultModifier() | InputEvent.SHIFT_DOWN_MASK/*"control shift E"*/), "Editable");

        tc.getActionMap().put("Command_enter", new AbstractAction("Command_enter111") {
            private static final long serialVersionUID = -3548620001691220571L;

            public void actionPerformed(ActionEvent evt) {
                JTextComponent tf = (JTextComponent) evt.getSource();
                System.out.println("Command_enter");
                Map<String, ActionCallback> actionCallbackMap1 = getActionCallbackMap(tf);
                if (actionCallbackMap1 == null) {
                    actionCallbackMap1 = actionCallbackMap;
                }

                if (!ValueWidget.isNullOrEmpty(actionCallbackMap1)) {
                    ActionCallback actionCallback = actionCallbackMap1.get("Command_enter");
                    if (null != actionCallback) {
                        actionCallback.actionPerformed(evt, tf);
                    }
                }

            }
        });
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, getDefaultModifier()/*"control E"*/), "Command_enter");


        tc.getActionMap().put("Ctrl_enter", new AbstractAction("Ctrl_enter111") {
            private static final long serialVersionUID = -3548620001691220571L;
            public void actionPerformed(ActionEvent evt) {
                JTextComponent tf = (JTextComponent) evt.getSource();
                System.out.println("Ctrl_enter");
                Map<String, ActionCallback> actionCallbackMap1 = getActionCallbackMap(tf);
                if (actionCallbackMap1 == null) {
                    actionCallbackMap1 = actionCallbackMap;
                }
                if (ValueWidget.isNullOrEmpty(actionCallbackMap1)) {
                    return;
                }
                    ActionCallback actionCallback = actionCallbackMap1.get("Ctrl_enter");
                    if (null != actionCallback) {
                        actionCallback.actionPerformed(evt, tf);
                    }
                }
        });
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK), "Ctrl_enter");


        tc.getActionMap().put("alt_enter", new AbstractAction("alt_enter111") {
            private static final long serialVersionUID = -3548620001691220571L;

            public void actionPerformed(ActionEvent evt) {
                JTextComponent tf = (JTextComponent) evt.getSource();
                System.out.println("alt_enter");
                Map<String, ActionCallback> actionCallbackMap1 = getActionCallbackMap(tf);
                if (actionCallbackMap1 == null) {
                    actionCallbackMap1 = actionCallbackMap;
                }
                if (ValueWidget.isNullOrEmpty(actionCallbackMap1)) {
                    return;
                }
                    ActionCallback actionCallback = actionCallbackMap1.get("alt_enter");
                    if (null != actionCallback) {
                        actionCallback.actionPerformed(evt, tf);
                    }
                }
        });
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK), "alt_enter");

        
        
        if (needSearch) {
            //按Ctrl+F 搜索文本
			//需要区分对待,因为有的文本框不需要Ctrl+F 快捷键
			tc.getActionMap().put("search", searchAction);
            tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, getDefaultModifier() /*"control F"*/), "search");

		}

		//按Ctrl+G 截屏(只截文本框)
		tc.getActionMap().put("screenshotDialog", copyImage2ClipAction);
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_G, getDefaultModifier()/*"control G"*/), "screenshotDialog");

		//按Ctrl+L 最大化文本框
		tc.getActionMap().put("rtaMaximizeTAAction", maximizeTAAction);
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_L, getDefaultModifier()/*"control L"*/), "rtaMaximizeTAAction");


		//按Ctrl+D 清空文本框
		tc.getActionMap().put("cleanUp", new AbstractAction("cleanUp111") {
			private static final long serialVersionUID = -3548620001691220571L;
			public void actionPerformed(ActionEvent evt) {
				JTextComponent tf=(JTextComponent)evt.getSource();
				if(!ValueWidget.isNullOrEmpty(tf)){
					tf.setText(SystemHWUtil.EMPTY);
					tf.requestFocus();
				}
			}
		});
        tc.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, getDefaultModifier()/*"control D"*/), "cleanUp");

        //双击Ctrl,触发黏贴
        /*tc.addKeyListener(new KeyListener() {
            private long lastTimeMillSencond;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (EventHWUtil.isJustCtrlDown(e)) {
                    if (lastTimeMillSencond == 0) {
                        lastTimeMillSencond = System.currentTimeMillis();
                    } else {
                        long currentTime = System.currentTimeMillis();
                        if (MenuUtil2.isDoubleClick(currentTime - lastTimeMillSencond)) {
                            System.out.println("双击Ctrl");
                            String content = WindowUtil.getSysClipboardText();
                            if (ValueWidget.isNullOrEmpty(content)) {
                                return;
                            }
                            tc.setText(content);
                            tc.requestFocus();
                            lastTimeMillSencond = 0;
                        } else {
                            lastTimeMillSencond = System.currentTimeMillis();
                        }
                    }
                }
            }
        });*/

    }

    /**
     * Returns the default modifier key for a system.  For example, on Windows
     * this would be the CTRL key (<code>InputEvent.CTRL_MASK</code>).<br>
     *     on Mac ,this would be command key
     *
     * @return The default modifier key.
     */
    protected static final int getDefaultModifier() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    }

	public static void showSearchDialog(JTextComponent tf,String keyword) {
		SearchInputDialog searchInputDialog = new SearchInputDialog(tf,keyword);
		searchInputDialog.setVisible(true);
	}

	/***
	 * 设置默认提示语
	 * @param inputTextArea
	 * @param placeHolder
	 */
	public static void setPlaceHolder(JTextComponent inputTextArea,String placeHolder){
		String oldText=inputTextArea.getText();
		if(ValueWidget.isNullOrEmpty(oldText)){
			inputTextArea.setForeground(PLACEHOLDER_BACKGROUND_COLOR);
			inputTextArea.setText(placeHolder);
		}
		
		try {
			ReflectHWUtils.setObjectValue(inputTextArea, "placeHolderText", placeHolder);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPlaceHolder(JTextComponent inputTextArea,String placeHolder){
		return isPlaceHolder(inputTextArea, null, placeHolder);
	}
	/***
	 * 判断是否是默认提示语
	 * @param inputTextArea
	 * @param placeHolder
	 * @return
	 */
	public static boolean isPlaceHolder(JTextComponent inputTextArea,String text,String placeHolder){
		if(ValueWidget.isNullOrEmpty(text)){
			text=inputTextArea.getText();
		}
		if(ValueWidget.isNullOrEmpty(text)){
			return false;
		}
		return text.equals(placeHolder)
				&& inputTextArea.getForeground().equals(PLACEHOLDER_BACKGROUND_COLOR);
	}
	public static void placeHolderFocus(final JTextComponent inputTextArea,final String placeHolder){
		inputTextArea.addFocusListener(new FocusAdapter() {
			/***
			 * 解决一启动图形界面,就聚焦的情况
			 */
//			private boolean isFirstFocus=true;
			/***
			 * 失去焦点
			 */
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				if(ValueWidget.isNullOrEmpty(inputTextArea.getText())){
					setPlaceHolder(inputTextArea,placeHolder);
				}
			}
			/***
			 * 获取焦点
			 */
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				if(isPlaceHolder(inputTextArea, placeHolder)){
//					if(!isFirstFocus){
						inputTextArea.setText(SystemHWUtil.EMPTY);
						inputTextArea.setForeground(Color.black);
					/*}else{
						isFirstFocus=false;
					}*/
				}
			}
		});
		inputTextArea.addKeyListener(new KeyAdapter() {
			/***
			 * 先按下再松开
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				cleanPlaceHolder(inputTextArea, placeHolder);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyTyped(e);
				cleanPlaceHolder(inputTextArea, placeHolder);
			}
		});
		/***
		 * 鼠标点击
		 */
		inputTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cleanPlaceHolder(inputTextArea, placeHolder);
			}
			
			@Override
		 	public void mouseClicked(MouseEvent e) {
				cleanPlaceHolder(inputTextArea, placeHolder);
			}
		});
		final Document doc = inputTextArea.getDocument();
		DocumentListener docLis = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if(!isPlaceHolder(inputTextArea, placeHolder)){
					inputTextArea.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(!isPlaceHolder(inputTextArea, placeHolder)){
					inputTextArea.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);
				}
			}
		};
		doc.addDocumentListener(docLis);
	}

    /**
     * 在文本框聚焦的情况下,通过按下方向键(仅支持左,右,不支持上下),可以使光标定位到句首或句尾
     *
     * @param e
     * @param inputTextArea
     */
    public static void setTFCaretPosition(KeyEvent e, final JTextComponent inputTextArea, final DoubleKeyAdapter doubleKeyAdapter) {
        if (SystemHWUtil.isWindows) {
            if (e.getKeyCode() == KeyEvent.VK_UP && e.getID() == KeyEvent.KEY_PRESSED) {
                System.out.println("上");
                inputTextArea.setCaretPosition(0);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN && e.getID() == KeyEvent.KEY_PRESSED) {
                System.out.println("下...");
                inputTextArea.setCaretPosition(inputTextArea.getText().length());
            }
        }
        toggleUpperLowerCase(e, inputTextArea, doubleKeyAdapter);

//        System.out.println(e);
        if (EventHWUtil.isJustKeyDown(e, KeyEvent.VK_DOWN)) {//在文本框聚焦的情况下,双击下方向键,可以全选
//                    System.out.println("setTFCaretPosition");
            if (doubleKeyAdapter.getLastTimeMillSencond() == 0) {
                doubleKeyAdapter.setLastTimeMillSencond(System.currentTimeMillis());
                return;
            }
            long currentTime = System.currentTimeMillis();
            if (MenuUtil2.isDoubleClick(currentTime - doubleKeyAdapter.getLastTimeMillSencond())) {
                System.out.println("双击 下 方向键");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        inputTextArea.selectAll();
                    }
                }).start();

                doubleKeyAdapter.setLastTimeMillSencond(0);
                doubleKeyAdapter.setLastTimeMillSencondDown(0);
            } else {
                doubleKeyAdapter.setLastTimeMillSencond(System.currentTimeMillis());
            }
            return;
        }
        if (EventHWUtil.isJustKeyDown(e, KeyEvent.VK_UP)) {//在文本框聚焦的情况下,双击上方向键,可以删除文本框全部内容
//                    System.out.println("setTFCaretPosition");
            if (doubleKeyAdapter.getLastTimeMillSencondDown() == 0) {
                doubleKeyAdapter.setLastTimeMillSencondDown(System.currentTimeMillis());
                return;
            }
            long currentTime = System.currentTimeMillis();
            if (MenuUtil2.isDoubleClick(currentTime - doubleKeyAdapter.getLastTimeMillSencondDown())) {
                System.out.println("双击 (上) 方向键");
                //删除文本框中全部内容,把剪切板内容填入
//                    inputTextArea.setCaretPosition(0);
                inputTextArea.setText(WindowUtil.getSysClipboardText());
                doubleKeyAdapter.setLastTimeMillSencondDown(0);
                doubleKeyAdapter.setLastTimeMillSencond(0);
            } else {
                doubleKeyAdapter.setLastTimeMillSencondDown(System.currentTimeMillis());
            }
        }

    }

    /***
     * 当前选中字符串只有全为大写,才转化为小写<br />
     * command+shift+U : 转化为大写或小写,只对英文字母有效
     * @param e
     * @param inputTextArea
     */
    public static void toggleUpperLowerCase(KeyEvent e, JTextComponent inputTextArea, DoubleKeyAdapter doubleKeyAdapter) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            //command+shift+U : 转化为大写或小写,只对英文字母有效
            if ((e.getKeyCode() == KeyEvent.VK_U)
                    && (((InputEvent) e)
                    .isShiftDown()) && ((InputEvent) e)
                    .isMetaDown()/*MAC 的command键*/) {
                String selectContent = inputTextArea.getSelectedText();
                if (!ValueWidget.isNullOrEmpty(selectContent)) {
                    int selectionStart = inputTextArea.getSelectionStart();
                    int selectionEnd = inputTextArea.getSelectionEnd();
                    String upperCase = selectContent.toUpperCase();
                    if (selectContent.equals(upperCase)) {//当前选中字符串只有全为大写,才转化为小写
                        //如果既有小写也有大写,则转为大写
                        inputTextArea.replaceSelection(selectContent.toLowerCase());
                    } else {
                        inputTextArea.replaceSelection(upperCase);
                    }
                    inputTextArea.setSelectionStart(selectionStart);
                    inputTextArea.setSelectionEnd(selectionEnd);
                }
            } else if (isWillComment(e)/*MAC 的option 键*/) {//注释的快捷键同IDEA 的多行注释快捷键
                String selectContent = inputTextArea.getSelectedText();
                if (ValueWidget.isNullOrEmpty(selectContent)) {
                    //没有选择文本,也删除注释 /*  */,并且同时删除空格
                    selectContent = inputTextArea.getText();
                    selectContent = selectContent.replaceAll("/\\*[\\s]*(.*)[\\s]*\\*/", "$1").replaceAll("[\\s]+", SystemHWUtil.EMPTY);
                    inputTextArea.setText(selectContent);
                } else {
                    if (selectContent.startsWith("/*") && selectContent.endsWith("*/")) {
                        selectContent = selectContent.replaceAll("/\\*[\\s]*(.*)[\\s]*\\*/", "$1");
                        inputTextArea.replaceSelection(selectContent);
                    } else {
                        inputTextArea.replaceSelection("/* " + selectContent + "*/");
                    }
                }
            }

            if (EventHWUtil.isOnlyEscape(e)) {//双击escape 只读
                if (doubleKeyAdapter.getLastTimeMillSencond() == 0) {
                    doubleKeyAdapter.setLastTimeMillSencond(System.currentTimeMillis());
                    searchNoDialog(inputTextArea);
                } else {
                    long currentTime = System.currentTimeMillis();
                    if (MenuUtil2.isDoubleClick(currentTime - doubleKeyAdapter.getLastTimeMillSencond())) {
                        inputTextArea.setEditable(false);
                        ToastMessage.toastRight("变为只读", 1000);
                        doubleKeyAdapter.setLastTimeMillSencond(0);
                    } else {
                        doubleKeyAdapter.setLastTimeMillSencond(System.currentTimeMillis());
                        searchNoDialog(inputTextArea);
                    }
                }
            } /*else { 必须注释掉,否则双击escape 无效,因为按escape 还会触发其他按键
                doubleKeyAdapter.setLastTimeMillSencond(0);
            }*/

        } else if (e.getID() == KeyEvent.KEY_RELEASED) {//仅仅是为了删除多余的"¿"
            if (e.getKeyCode() == KeyEvent.VK_SLASH) {//注释的快捷键同IDEA 的多行注释快捷键
//                System.out.println("aaa :" );
                String source = inputTextArea.getText();
                System.out.println("source :" + source);
                inputTextArea.setText(source.replace("¿", SystemHWUtil.EMPTY));
            }
        }
    }

	/***
     * 双击escape :文本框变为只读<br />
     * 单击escape:循环搜索
     * @param inputTextArea
     */
    public static void searchNoDialog(JTextComponent inputTextArea) {
        FindTxtResultBean findTxtResultBean = (FindTxtResultBean) ReflectHWUtils.getObjectValue(inputTextArea, Constant2.FINDTXTRESULTBEAN_FIELD);
        if (null != findTxtResultBean
                && (null == findTxtResultBean.getHideDialog() || (!findTxtResultBean.getHideDialog()))) {
            findTxtResultBean.setHideDialog(true);
            ToastMessage.toastRight("搜索不弹框", 1000);
        }
    }

    public static void bindKeyEvent(final JTextComponent textField) {
        textField.addKeyListener(new DoubleKeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyTyped(e);
                TextCompUtil2.toggleUpperLowerCase(e, textField, this);
            }

            /***
             * 仅仅是为了删除多余的"¿"
             * @param e
             */
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                TextCompUtil2.toggleUpperLowerCase(e, textField, this);
            }

        });
    }

    public static boolean isWillComment(KeyEvent e) {
        return (e.getKeyCode() == KeyEvent.VK_SLASH)
                && (((InputEvent) e)
                .isShiftDown()) && ((InputEvent) e)
                .isAltDown();
    }

    /***
     * 清除placeholder
     * @param inputTextArea
     * @param placeHolder
     */
    private static void cleanPlaceHolder(final JTextComponent inputTextArea, final String placeHolder){
        if(isPlaceHolder(inputTextArea, placeHolder)){
            inputTextArea.setText(SystemHWUtil.EMPTY);
            inputTextArea.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);
        }
    }
	public static void generateJsonPopup(){
	}

    private static void popupMenu(JTextComponent inputTextArea, KeyEvent e, JPopupMenu textPopupMenu) {
        JTextComponent tf = (JTextComponent) e.getSource();
        Point point = tf.getParent().getLocation();
        textPopupMenu.show(e.getComponent(), point.x + 20,
                point.y + 2);// 下移一点
    }

	/***
	 * 双击Shift 弹出菜单
	 *
	 * @param e
	 */
    private static void popupMenu(JTextComponent inputTextArea, KeyEvent e, boolean isSimple) {
//		System.out.println("双击Shift");
        JPopupMenu textPopupMenu = getTextBoxPopupMenu(inputTextArea, isSimple);
        /*JMenuItem notepadM = new JMenuItem("notepad 编辑文件");
        notepadM.addActionListener(dropListMenuActionListener);
		textPopupMenu.add(notepadM);*/
        popupMenu(inputTextArea, e, textPopupMenu);
    }

    private static JPopupMenu getTextBoxPopupMenu(JTextComponent inputTextArea, boolean isSimple) {
        JPopupMenu textPopupMenu = new JPopupMenu();
        textPopupMenu.setLabel("打开文件");
        textPopupMenu.setLightWeightPopupEnabled(true);
        textPopupMenu.setBackground(Color.GREEN);
        GenerateJsonActionListener dropListMenuActionListener = new GenerateJsonActionListener(
                inputTextArea, new ConfigParam());//TODO ConfigParam是直接初始化的
        if (!isSimple) {
            JMenuItem openFolderM = new JMenuItem("获取json");
            openFolderM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(openFolderM);

            JMenuItem generateMD5M = new JMenuItem(MenuUtil2.ACTION_CREATE_MD5);
            generateMD5M.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(generateMD5M);

            JMenuItem deMD5M = new JMenuItem(MenuUtil2.ACTION_MD5_DECODE);
            deMD5M.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(deMD5M);

            JMenuItem browserFileM = new JMenuItem(MenuUtil2.ACTION_STR_BROWSER);
            browserFileM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(browserFileM);

            //最大化窗口之后编辑
            JMenuItem editM = new JMenuItem(MenuUtil2.ACTION_STR_EDIT);
            editM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(editM);

            //去掉双引号
            JMenuItem deleteTwoQuoteM = new JMenuItem(MenuUtil2.ACTION_DELETE_TWO_QUOTE);
            deleteTwoQuoteM.addActionListener(dropListMenuActionListener);
            textPopupMenu.add(deleteTwoQuoteM);
        }

        //删除后黏贴
        JMenuItem pasteAfterClearM = new JMenuItem(MenuUtil2.ACTION_STR_PASTE_AFTER_DELETE);
        pasteAfterClearM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(pasteAfterClearM);

        JMenuItem urlDecodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_DECODE);
        urlDecodeM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(urlDecodeM);

        JMenuItem urlEncodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_ENCODE);
        urlEncodeM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(urlEncodeM);

        JMenuItem queryString2Json = new JMenuItem(MenuUtil2.ACTION_QUERY_STRING2JSON);
        queryString2Json.setActionCommand(MenuUtil2.ACTION_QUERY_STRING2JSON);
        queryString2Json.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(queryString2Json);

        JMenuItem json2QueryString = new JMenuItem(MenuUtil2.ACTION_JSON2QUERY_STRING);
        json2QueryString.setActionCommand(MenuUtil2.ACTION_JSON2QUERY_STRING);
        json2QueryString.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(json2QueryString);
        return textPopupMenu;
    }


    /***
     * 被com/yunma/dialog/rsa/RSADoublePopupMenuTextArea.java 使用
     * @param inputTextArea
     * @param compDoubleMenuConfs
     * @return
     */
    private static JPopupMenu getDoublePopupMenu(JComponent inputTextArea, java.util.List<CompDoubleMenuConf> compDoubleMenuConfs) {
        if (ValueWidget.isNullOrEmpty(compDoubleMenuConfs)) {
            return null;
        }
        JPopupMenu textPopupMenu = new JPopupMenu();
        textPopupMenu.setLabel("打开文件");
        textPopupMenu.setLightWeightPopupEnabled(true);
        textPopupMenu.setBackground(Color.GREEN);
        int size = compDoubleMenuConfs.size();

        DoubleMenuListener doubleMenuListener = new DoubleMenuListener(compDoubleMenuConfs, inputTextArea);
        for (int i = 0; i < size; i++) {
            CompDoubleMenuConf compDoubleMenuConf = compDoubleMenuConfs.get(i);
            JMenuItem formSubmitDataM = new JMenuItem(compDoubleMenuConf.getDisplayLabel());
            if (!ValueWidget.isNullOrEmpty(compDoubleMenuConf.getCommand())) {
                formSubmitDataM.setActionCommand(compDoubleMenuConf.getCommand());
            }
            formSubmitDataM.addActionListener(doubleMenuListener);
            textPopupMenu.add(formSubmitDataM);
        }
        return textPopupMenu;
    }

    public static void dropListMenuCommon(final JTextComponent inputTextArea, java.util.List<CompDoubleMenuConf> compDoubleMenuConfs) {
        final JPopupMenu textPopupMenu = getDoublePopupMenu(inputTextArea, compDoubleMenuConfs);
        dropListMenu(inputTextArea, textPopupMenu);
    }
    private static JPopupMenu getRequestBodyPopupMenu(JTextComponent inputTextArea) {
        JPopupMenu textPopupMenu = new JPopupMenu();
        textPopupMenu.setLabel("打开文件");
        textPopupMenu.setLightWeightPopupEnabled(true);
        textPopupMenu.setBackground(Color.GREEN);
        GenerateJsonActionListener dropListMenuActionListener = new GenerateJsonActionListener(
                inputTextArea, new ConfigParam());//TODO ConfigParam是直接初始化的

        //删除后黏贴
        JMenuItem formSubmitDataM = new JMenuItem("转化为标准表单数据");
        formSubmitDataM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(formSubmitDataM);

       /* JMenuItem urlDecodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_DECODE);
        urlDecodeM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(urlDecodeM);

        JMenuItem urlEncodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_ENCODE);
        urlEncodeM.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(urlEncodeM);

        JMenuItem queryString2Json = new JMenuItem(MenuUtil2.ACTION_QUERY_STRING2JSON);
        queryString2Json.setActionCommand(MenuUtil2.ACTION_QUERY_STRING2JSON);
        queryString2Json.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(queryString2Json);

        JMenuItem json2QueryString = new JMenuItem(MenuUtil2.ACTION_JSON2QUERY_STRING);
        json2QueryString.setActionCommand(MenuUtil2.ACTION_JSON2QUERY_STRING);
        json2QueryString.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(json2QueryString);*/
        return textPopupMenu;
    }


    public static void dropListMenu(final JTextComponent inputTextArea, final boolean isSimple) {
        final JPopupMenu textPopupMenu = getTextBoxPopupMenu(inputTextArea, isSimple);
        dropListMenu(inputTextArea, textPopupMenu);
    }

    public static void dropListMenuRequestParameterBody(final JTextComponent inputTextArea) {
        final JPopupMenu textPopupMenu = getRequestBodyPopupMenu(inputTextArea);
        dropListMenu(inputTextArea, textPopupMenu);
    }

    public static void dropListMenu(final JTextComponent inputTextArea, final JPopupMenu textPopupMenu) {
        inputTextArea.addKeyListener(new KeyListener() {
            private long lastTimeMillSencond;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!EventHWUtil.isJustShiftDown(e)) {
                    lastTimeMillSencond = 0;
                    return;
                }
                if (lastTimeMillSencond == 0) {
                    lastTimeMillSencond = System.currentTimeMillis();
                } else {
                    long currentTime = System.currentTimeMillis();
                    long delta=currentTime
                            - lastTimeMillSencond;
                    if (MenuUtil2.isDoubleClick(delta)) {//双击Shift
                        popupMenu(inputTextArea, e, textPopupMenu);
                        lastTimeMillSencond = 0;
                    } else {
                        lastTimeMillSencond = System.currentTimeMillis();
                    }
                    delta=0l;
                }
            }
        });
    }

    /***
     * 截图,截屏
     * @param area2
     */
    public static void copyImgAction(JComponent area2) {
        if (null == area2) {
            return;
        }
		HeightWidthBean heightWidthBean=new HeightWidthBean();
		heightWidthBean.setHeight(area2.getHeight());//默认高度
		heightWidthBean.setWidth(area2.getWidth());//默认宽度
		GenericDialog genericDialogTmp= showScreenshotDialog(area2,heightWidthBean.getWidth(),heightWidthBean.getHeight());//只是为了兼容Hijson
		if(null==genericDialogTmp){
			screenshotDialog=getScreenshotDialog(area2);
		}else{
			screenshotDialog=genericDialogTmp;
		}
		SpecifyWidthAndHeightDialog specifyWidthAndHeightDialog=new SpecifyWidthAndHeightDialog(heightWidthBean,screenshotDialog);
		specifyWidthAndHeightDialog.setVisible(true);
        System.out.println(heightWidthBean.isBeSuccess());

        if (!heightWidthBean.isBeSuccess()) {
            ToastMessage.toast("已取消", 1000, Color.RED);
            return;
        }

        if (!heightWidthBean.isValid()) {
            ToastMessage.toast("高度或宽度不合法",2000, Color.RED);
            return;

        }
        if (heightWidthBean.isSaveToFile()) {//是否保存到文件
            java.io.File picSaveFile = DialogUtil.chooseFileDialog(null, " 保存图片", specifyWidthAndHeightDialog, "jpg");

            if (null == picSaveFile) {
                ToastMessage.toast("取消操作", 2000, Color.RED);
                return;
            }
            ComponentUtil.generateImageAndCopy(area2, picSaveFile, heightWidthBean.getHeight(), heightWidthBean.getWidth(), heightWidthBean.getMultiple());
        } else if (heightWidthBean.isUpload2Server()) {//上传截图到远程服务器
            BufferedImage2Bean imgBean = ImageHWUtil.generateImage(area2, null, "jpg"/*picFormat*/
                    , heightWidthBean.getHeight(), heightWidthBean.getWidth(), heightWidthBean.getMultiple());
            imgBean.getG2d().dispose();
            uploadBufferedImage(imgBean.getBufferedImage());
        } else if (heightWidthBean.isEditScreenshots()) {
            BufferedImage2Bean bufferedImageBean = ComponentUtil.generateImageAndCopy(area2, heightWidthBean.getHeight(), heightWidthBean.getWidth(), 1/*heightWidthBean.getMultiple()*/, false);
            bufferedImageBean.getG2d().dispose();
            BufferedImage2Bean bufferedImageBean2 = ComponentUtil.generateImageAndCopy(area2, heightWidthBean.getHeight(), heightWidthBean.getWidth(), heightWidthBean.getMultiple(), false);
            bufferedImageBean2.setOriginImage(bufferedImageBean.getBufferedImage());
            new EditSnapShootDialog(bufferedImageBean2.getBufferedImage(), bufferedImageBean2.getOriginImage(), bufferedImageBean2.getG2d());
        }else{
//                    imgBean.getG2d().dispose();
            ComponentUtil.generateImageAndCopy(area2, heightWidthBean.getHeight(), heightWidthBean.getWidth(), heightWidthBean.getMultiple(), true);
        }
    }

    /***
     * 上传BufferedImage 到远程服务器
     * @param img
     */
    public static void uploadBufferedImage(BufferedImage img) {
        String formatName = "jpg";
        ByteArrayOutputStream baos = imageToByteArrayOutputStream(img, formatName);

        //输出数组
        byte[] bytes = baos.toByteArray();
        uploadFile(formatName, bytes, "Screenshots");
    }

    public static void uploadFile(String formatName, byte[] bytes, String uploadedFileName) {
        String size = FileUtils.formatSize(bytes.length);
        int resultOption = JOptionPane.showConfirmDialog(null, "Are you sure to Upload " + size + " to Server ?", "确认",
                JOptionPane.OK_CANCEL_OPTION);
        if (resultOption != JOptionPane.OK_OPTION) {
            ToastMessage.toast("取消上传", 2000, Color.red);
            return;
        }
        Map<String, String> parameters = null;
        try {
            String result = HttpSocketUtil.uploadFile("http://blog.yhskyc.com/convention2/ajax_image/upload", bytes, parameters,
                    uploadedFileName + SystemHWUtil.ENGLISH_PERIOD + formatName, (Map<String, String>) null);
            Map requestMap = null;
            requestMap = (Map) HWJacksonUtils.deSerialize(result, Map.class);
            StringBuffer stringBuffer = new StringBuffer("<html>");
            for (Object obj : requestMap.keySet()) {
                Object val = requestMap.get(obj);
                stringBuffer.append("<div style=\"padding-bottom:5px;margin-bottom: 5px;border: 1px solid #f38399;border-radius: 5px;\" >");
                stringBuffer.append("<span style=\"color: #ddd;\" >").append(obj).append("</span>").append(":").append("<br />");
                stringBuffer.append(val).append("</div>");
            }
            stringBuffer.append("</html>");
//                System.out.println(stringBuffer);
            ToastMessage.toast("上传成功", 2000);
            CustomDefaultDialog customDefaultDialog = new CustomDefaultDialog(stringBuffer.toString(), "图片路径", true, null, 800);
            customDefaultDialog.setVisible(true);
//                            ComponentUtil.appendResult(area2,result,true,false);

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把BufferedImage 转化为字节数组
     *
     * @param img
     * @return
     */
    public static ByteArrayOutputStream imageToByteArrayOutputStream(BufferedImage img, String formatName) {
        //创建储存图片二进制流的输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            //创建ImageOutputStream流
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(baos);
            //将二进制数据写进ByteArrayOutputStream
            ImageIO.write(img, formatName, imageOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos;
    }

    public static GenericDialog showScreenshotDialog(JComponent area2, int width, int height) {
		Class clazz=area2.getClass();
		  Object obj=null;
		    Method m = null;
			try {
				m = clazz.getMethod("showScreenshotDialog", new Class[]{int.class,int.class});
				m.setAccessible(true);
			    obj=m.invoke(area2, width,height);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if(null==m){//抛异常 :NoSuchMethodException
				GenericDialog genericDialog= screenshotDialog;
				Point point= area2.getLocationOnScreen();
				genericDialog.setBounds(point.x,point.y,width,height);
				genericDialog.launchFrame();
				return genericDialog;
			}
		return null;
	}

    public static GenericDialog getScreenshotDialog(JComponent area2) {
        return (GenericDialog) getReflectGetMethod(area2, "getScreenshotDialog", null, null);
    }

    public static JDialog getMaxDialog(JTextComponent area2) {
        return (JDialog) getReflectGetMethod(area2, "getMaxJDialog", null, null);
    }

    public static void closeMaxDialog(JDialog area2) {
        getReflectGetMethod(area2, "closeDialog", null, null);
    }

    public static void setMaxDialog(JTextComponent area2, Object dialog, Class clazz) {
        getReflectGetMethod(area2, "setMaxJDialog", dialog, clazz);
    }

    public static Integer getMaxStatus(JTextComponent area2) {
        return (Integer) getReflectGetMethod(area2, "getMaxStatus", null, null);
    }

    public static void setMaxStatus(JTextComponent area2, int maxStatus) {
        getReflectGetMethod(area2, "setMaxStatus", maxStatus, null);
    }

    public static Object getReflectGetMethod(Object area2, String methodName, Object param, Class clazz2) {
        Class clazz=area2.getClass();
		  Object obj=null;
		    Method m;
			try {
                if (clazz2 == null && null != param) {
                    clazz2 = param.getClass();
                }
                if (null == clazz2) {
                    m = clazz.getMethod(methodName, new Class[]{});
                } else {
                    m = clazz.getMethod(methodName, new Class[]{clazz2});
                }
                m.setAccessible(true);
                if (null == param) {
                    obj = m.invoke(area2);
                } else {
                    obj = m.invoke(area2, param);
                }

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        return obj;
    }


    public static Map<String, ActionCallback> getActionCallbackMap(JTextComponent area2) {
        return (Map<String, ActionCallback>) getReflectGetMethod(area2, "getActionCallbackMap", null, null);
    }

	static class GenerateJsonActionListener implements ActionListener {
		private JTextComponent ta;
        private ConfigParam configParam;

        public GenerateJsonActionListener(JTextComponent tf, ConfigParam configParam) {
			super();
			this.ta = tf;
            this.configParam = configParam;
		}

		@Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("获取json")) {
//				System.out.println(command);
                GenerateJsonPane generateJsonPane = new GenerateJsonPane(ta, true, this.configParam);
                generateJsonPane.setVisible(true);
            }else if (command.equals(MenuUtil2.ACTION_CREATE_MD5)) {//获取MD5值
                String text = ta.getText();
                createMD5(text, ta);
            } else if (command.equals(MenuUtil2.ACTION_STR_BROWSER)) {//弹出文件选择窗口
                boolean isSuccess = DialogUtil.browserFile(ta, JFileChooser.FILES_ONLY, ta);
            } else if (command.equalsIgnoreCase(MenuUtil2.ACTION_MD5_DECODE)) {
                String text = ta.getText();
                if (!ValueWidget.isNullOrEmpty(text) && text.length() < 16) {
                    //如果文本框中内容不为空,并且字符个数小于16,则计算其MD5,这是为了防止用户的误操作
                    createMD5(text, ta);
                    return;
                }
                String source;
                source = SystemHWUtil.md5Map.get(text);
                if (ValueWidget.isNullOrEmpty(source)) {
                    ToastMessage.toast("暂无md5对应的原文", 3000, Color.red);
                } else {
                    ta.setText(source);
                }
            }else if (command.equals(MenuUtil2.ACTION_STR_EDIT)) {//
				/*SimpleTextEditDialog generateJsonPane = new SimpleTextEditDialog(ta);
				generateJsonPane.setVisible(true);*/
                DialogUtil.showMaximizeDialog(ta);
            }else if (command.equals(MenuUtil2.ACTION_DELETE_TWO_QUOTE)) {
                String content=this.ta.getText();
                if(!ValueWidget.isNullOrEmpty(content)){
                    content= RegexUtil.deleteTwoQuote(content);
                    this.ta.setText(content);
                }
            } else if (command.equals(MenuUtil2.ACTION_STR_PASTE_AFTER_DELETE)) {//删除后黏贴
                String content = WindowUtil.getSysClipboardText();
                if (ValueWidget.isNullOrEmpty(content)) {
                    return;
                }
                this.ta.setText(content);
                this.ta.requestFocus();
            } else if (command.startsWith(MenuUtil2.ACTION_URL_DECODE)) {// 退出应用程序
                MenuUtil2.urlDecode(this.ta);
            } else if (command.startsWith(MenuUtil2.ACTION_URL_ENCODE)) {// 退出应用程序
                MenuUtil2.urlEncode(this.ta);
            } else if (command.startsWith(MenuUtil2.ACTION_QUERY_STRING2JSON)) {
                MenuUtil2.queryString2Json(this.ta, true, false/*isFurther*/);
            } else if (command.startsWith(MenuUtil2.ACTION_JSON2QUERY_STRING)) {
                //{"username":"whuang","age":23} -->username=whuang&age=23
                MenuUtil2.json2queryString(this.ta);
            } else if (command.equals("转化为标准表单数据")) {
                String requestBody = this.ta.getText();
                //有多个等于号,但是没有& 符号
                //{param={"queryParams":{"appId":"7"},"start":0,"rows":1}, aa=bb, cc=dd}
                convert2FormSubmitData(requestBody, ta);
            }
        }

        /***
         * 把 {param={"queryParams":{"appId":"7"},"start":0,"rows":1}, aaa=bbb, ccc=ddd} <br />
         * 转化为:<br />
         * aa=bb&ccc=bbb&param={"queryParams":{"appId":"7"},"start":0,"rows":1}
         * @param requestBody
         * @param ta
         */
        public static void convert2FormSubmitData(String requestBody, JTextComponent ta) {
            if (!ValueWidget.isNullOrEmpty(requestBody) && SystemHWUtil.findStr(requestBody, "=", 0).getCount() > 1) {
                if (!requestBody.contains("&")) {
                    String regux = "[^\\s{=\"},:']";
                    String input;
                    input = requestBody.replaceAll(",[\\s]*(" + regux + "+=)", "&$1");
                    input = SystemHWUtil.deleteCurlyBraces(input);
//                        input=input.replace(",,","&");
                    ta.setText(input);
                }
            }
        }

        public static void createMD5(String text, JTextComponent ta) {
                ta.setText(SystemHWUtil.getMD5(text, SystemHWUtil.CHARSET_UTF));
            }

    }

    public static void addDoubleShiftPopupMenu(AssistPopupTextField textField2) {
        final JPopupMenu textPopupMenu = new JPopupMenu();
        JMenuItem base64M = new JMenuItem("base64 编码");
        JMenuItem deCodebase64M = new JMenuItem("base64 解码");
        ShiftDropListMenuActionListener dropListMenuActionListener = new ShiftDropListMenuActionListener(textField2);
        base64M.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(base64M);
        deCodebase64M.addActionListener(dropListMenuActionListener);
        textPopupMenu.add(deCodebase64M);
        TextCompUtil2.dropListMenu(textField2, textPopupMenu);
    }

}
