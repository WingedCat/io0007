 package com.swing.menu;

 import com.common.dict.Constant2;
 import com.common.util.RequestUtil;
 import com.common.util.SystemHWUtil;
 import com.common.util.WebServletUtil;
 import com.common.util.WindowUtil;
 import com.io.hw.awt.color.CustomColor;
 import com.io.hw.json.HWJacksonUtils;
 import com.io.hw.json.JSONHWUtil;
 import com.string.widget.util.RegexUtil;
 import com.string.widget.util.ValueWidget;
 import com.swing.callback.ActionCallback;
 import com.swing.component.MyNamePanel;
 import com.swing.component.TextCompUtil2;
 import com.swing.dialog.toast.ToastMessage;

 import javax.swing.*;
 import javax.swing.border.Border;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import javax.swing.event.MouseInputAdapter;
 import javax.swing.event.MouseInputListener;
 import javax.swing.text.JTextComponent;
 import java.awt.*;
 import java.awt.event.ActionListener;
 import java.awt.event.MouseEvent;
 import java.io.UnsupportedEncodingException;
 import java.net.URLDecoder;
 import java.net.URLEncoder;
 import java.util.*;
 import java.util.List;

 public class MenuUtil2
{
    public static final String ACTION_STR_OPEN               = "open";
    /***
     * 打开浏览器
     */
    public static final String ACTION_STR_OPEN_BROWSER       = "open browser";
    public static final String ACTION_STR_DELETE_FILE        = "delete file";
    public static final String ACTION_STR_EXIT               = "exit";
    public static final String ACTION_STR_BROWSER            = "browser";
    public static final String ACTION_STR_COPY               = "copy";
    public static final String ACTION_DELETE_TWO_QUOTE = "删除两边的双引号";
    /***
     * 复制表格单元格
     */
    public static final String ACTION_STR_COPY_CELL               = "copy cell";
    /***
     * 复制图片
     */
    public static final String ACTION_IMAGE_COPY               = "copy image";
    /***
     * 复制到剪切板时指定高度
     */
    public static final String ACTION_IMAGE_COPY_SPECIFY_HEIGHT               = "copy image Specify height";
    public static final String ACTION_IMAGE_COPY_SPECIFY_WIDTH               = "copy image Specify width";
    public static final String ACTION_IMAGE_COPY_SPECIFY_WIDTH_HEIGHT               = "copy image Specify widthAndHeight";
    public static final String ACTION_STR_COPY_ALL           = "copy all";
    public static final String ACTION_STR_PASTE              = "paste";
    public static final String ACTION_STR_PASTE_AFTER_DELETE = "删除后黏贴";
    /***
     * 把剪切板中的文本黏贴到表格的单元格中
     */
    public static final String ACTION_STR_PASTE_CELL              = "paste cell";
    /***
     * 黏贴图片
     */
    public static final String ACTION_IMAGE_PASTE              = "paste image";
    public static final String ACTION_STR_REPLACE_ALL              = "replace all";
    public static final String ACTION_STR_DELETE_CONTENT     = "delete";
    public static final String ACTION_STR_DELETE_ALL_CONTENT = "delete all";
    public static final String ACTION_STR_SELECT_ALL_CONTENT = "select all";
    /***
     * 获取文本的长度
     */
    public static final String ACTION_STR_LENGTH = "length";
    public static final String ACTION_STR_REFRESH            = "refresh";
    public static final String ACTION_STR_COPY_FILEPATH      = "copy file path";
    public static final String ACTION_STR_FILE_RENAME        = "rename";
    public static final String ACTION_STR_CLOSE              = "close";
    public static final String ACTION_STR_NEW                = "new";
    public static final String ACTION_STR_ADD                = "add";
    public static final String ACTION_STR_SAVE               = "save";
    public static final String ACTION_STR_EDIT               = "edit";
    public static final String ACTION_STR_VIEW               = "view";
    public static final String ACTION_STR_UPDATE             = "update";
    public static final String ACTION_STR_INSERT             = "insert";
    public static final String ACTION_STR_DELETE_CRLF        = "delete 换行";
    public static final String ACTION_STR_TRIM        = "trim";
    /***
     * 转化为大写
     */
    public static final String ACTION_STR2UPPER_CASE        = "to UpperCase";
    /***
     * 转化为小写
     */
    public static final String ACTION_STR2LOWER_CASE        = "to LowerCase";
    /***
     * 导出
     */
    public static final String ACTION_STR_EXPORT             = "export";
    /***
     * 搜索
     */
    public static final String ACTION_STR_SEARCH             = "search";
    /***
     * 读取二维码
     */
    public static final String ACTION_READ_QR_CODE             = "read qrcode";
    public static final String ACTION_USER_LOGIN             = "login";
    public static final String ACTION_HELP             = "help";
    /***
     * 清空，清除
     */
    public static final String ACTION_STR_CLEANUP             = "clean";
    /***
     * 让文本框可编辑
     */
    public static final String ACTION_TF_EDITABLE             = "editable";
    /**
     * 清空表格(JTable)单元格
     */
    public static final String ACTION_STR_CLEANUP_CELL             = "clean cell";
    public static final String ACTION_STR_MODIFY_CHARSET             = "modify_charset";
    /***
     * 应用修改
     */
    public static final String ACTION_STR_APPLY_MODIFY            = "apply_modify";
    public static final String MENU_COMMAND_CONFIG = "config";
    /***
     * 缩小
     */
    public static final String ACTION_REDUCE             = "reduce";
    /***
     * 放大
     */
    public static final String ACTION_ENLARGE             = "enlarge";
    public static final String ACTION_DOUBLE_QUOTES_ESCAPE        = "双引号转义";
    /***
     * 删除[\n]
     */
    public static final String ACTION_DELETE_N        = "删除[\\n]";
    public static final String ACTION_DELETE_R_N = "删除\\r\\n";
    public static final String ACTION_DELETE_N_R = "删除\\n\\r";
    public static final String ACTION_URL_ENCODE        = "URL 编码";
    public static final String ACTION_URL_DECODE        = "URL 解码";
    public static final String ACTION_XML2JSON        = "xml -> json";
    /***
     * 把请求要素转化为json<br>
     * username=whuang&age=27-->{"username":"whuang","age":27}
     */
    public static final String ACTION_QUERY_STRING2JSON        = "query string -> json";
    /***
     * {"username":"whuang"} -->username=whuang
     */
    public static final String ACTION_JSON2QUERY_STRING        = "json -> query string";
    /***
     * 生成MD5值
     */
    public static final String ACTION_CREATE_MD5        = "md5";
    /***
     * 解密 MD5
     */
    public static final String ACTION_MD5_DECODE        = "MD5原文";
    public static final String ACTION_DELETE_DIGIT_OF_FRONT        = "delete front digit";
    /***
     * 删除每行(单行)注释
     */
    public static final String ACTION_DELETE_NOTES        = "delete Notes";
    
    private MenuUtil2()
    {
        throw new Error("Don't let anyone instantiate this class.");
    }

    public static JPopupMenu setPopupMenu(final JTextComponent field2, Map<String, ActionCallback> actionCallbackMap)
    {
        return setPopupMenu(field2, null, actionCallbackMap);
    }
    public static JPopupMenu addPopupMenuItem(final JTextComponent field2,JPopupMenu textMenu){
    	JMenuItem copyM = new JMenuItem(MenuUtil2.ACTION_STR_COPY);
        JMenuItem copyAllM = new JMenuItem(
            MenuUtil2.ACTION_STR_COPY_ALL);
        JMenuItem pasteM = new JMenuItem(MenuUtil2.ACTION_STR_PASTE);
        JMenuItem replaceAllM = new JMenuItem(MenuUtil2.ACTION_STR_REPLACE_ALL);
        JMenuItem deleteM = new JMenuItem(
            MenuUtil2.ACTION_STR_DELETE_CONTENT);
        JMenuItem deleteAllM = new JMenuItem(
            MenuUtil2.ACTION_STR_DELETE_ALL_CONTENT);
        deleteAllM.setActionCommand(MenuUtil2.ACTION_STR_DELETE_ALL_CONTENT);
        MenuUtil2.strongMenuItem(deleteAllM);
        deleteAllM.setForeground(Color.red);
        JMenuItem selAllM = new JMenuItem(
            MenuUtil2.ACTION_STR_SELECT_ALL_CONTENT);
        JMenuItem lengthM = new JMenuItem(
            MenuUtil2.ACTION_STR_LENGTH);
        JMenuItem deleteCRLFM = new JMenuItem(
                MenuUtil2.ACTION_STR_DELETE_CRLF);
        JMenuItem editableM = new JMenuItem(
                MenuUtil2.ACTION_TF_EDITABLE);
        JMenuItem trimM = new JMenuItem(
                MenuUtil2.ACTION_STR_TRIM);

        JMenuItem deleteGlobalBlankM = new JMenuItem("删除全局空格");

        JMenuItem upperCaseM = new JMenuItem(
                MenuUtil2.ACTION_STR2UPPER_CASE);
        
        JMenuItem lowerCaseM = new JMenuItem(
                MenuUtil2.ACTION_STR2LOWER_CASE);
        JMenuItem quotes_escapeM = new JMenuItem(
                MenuUtil2.ACTION_DOUBLE_QUOTES_ESCAPE);
        
        JMenuItem replaceChinaQuotesM = new JMenuItem("替换中文双引号");
        
        JMenuItem deleteNM = new JMenuItem(
                MenuUtil2.ACTION_DELETE_N);
        JMenuItem deleteNRM = new JMenuItem(
                MenuUtil2.ACTION_DELETE_N_R);
        JMenuItem deleteRNM = new JMenuItem(
                MenuUtil2.ACTION_DELETE_R_N);
        
        JMenuItem urlEncodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_ENCODE);
        
        JMenuItem urlDecodeM = new JMenuItem(
                MenuUtil2.ACTION_URL_DECODE);
        JMenuItem xml2jsonM = new JMenuItem(
                MenuUtil2.ACTION_XML2JSON);
        JMenuItem md5M = new JMenuItem(
                MenuUtil2.ACTION_CREATE_MD5);
        JMenuItem deleteFrontDigitM = new JMenuItem("删除每行前面的数字");
        deleteFrontDigitM.setActionCommand(MenuUtil2.ACTION_DELETE_DIGIT_OF_FRONT);
        
        JMenuItem deleteNotesM = new JMenuItem("删除每行前面的注释");
        deleteNotesM.setActionCommand(MenuUtil2.ACTION_DELETE_NOTES);
        
        JMenuItem copyImage2ClipM = new JMenuItem("复制图片到剪切板");
        copyImage2ClipM.setActionCommand(MenuUtil2.ACTION_IMAGE_COPY);
        JMenuItem copyImageSpecifyheight=new JMenuItem(MenuUtil2.ACTION_IMAGE_COPY_SPECIFY_HEIGHT);
  		copyImageSpecifyheight.setActionCommand(MenuUtil2.ACTION_IMAGE_COPY_SPECIFY_HEIGHT);

  		JMenuItem copyImageSpecifyWidthHeight=new JMenuItem(MenuUtil2.ACTION_IMAGE_COPY_SPECIFY_WIDTH_HEIGHT);
  		copyImageSpecifyWidthHeight.setActionCommand(MenuUtil2.ACTION_IMAGE_COPY_SPECIFY_WIDTH_HEIGHT);
  		
        JMenuItem queryString2Json = new JMenuItem(MenuUtil2.ACTION_QUERY_STRING2JSON);
        queryString2Json.setActionCommand(MenuUtil2.ACTION_QUERY_STRING2JSON);
        MenuUtil2.strongMenuItem(queryString2Json);
        
        JMenuItem json2QueryString = new JMenuItem(MenuUtil2.ACTION_JSON2QUERY_STRING);
        json2QueryString.setActionCommand(MenuUtil2.ACTION_JSON2QUERY_STRING);
        
        JMenuItem and2CRFL = new JMenuItem("& --> 换行");
        JMenuItem showHtml = new JMenuItem("以HTML显示");
        JMenuItem formatJsonHtml = new JMenuItem("格式化json");
        JMenuItem formatJsonDeepHtml = new JMenuItem("转化为json");
        JMenuItem deleteQuoteBracketsBetter = new JMenuItem("删除中括号两边的引号(优化)");
        JMenuItem deleteJsonExtraComma = new JMenuItem("删除json中多余逗号");
        JMenuItem openBrowserM = new JMenuItem(ACTION_STR_OPEN_BROWSER);
        
        JMenuItem searchM = new JMenuItem(ACTION_STR_SEARCH);
        
        JMenuItem exitM = new JMenuItem(
                MenuUtil2.ACTION_STR_EXIT);
        Menu2ActionListener myMenuListener=new Menu2ActionListener(field2);
        copyM.addActionListener(myMenuListener);
        copyAllM.addActionListener(myMenuListener);
        pasteM.addActionListener(myMenuListener);
        replaceAllM.addActionListener(myMenuListener);
        deleteM.addActionListener(myMenuListener);
        deleteAllM.addActionListener(myMenuListener);
        selAllM.addActionListener(myMenuListener);
        lengthM.addActionListener(myMenuListener);
        deleteCRLFM.addActionListener(myMenuListener);
        editableM.addActionListener(myMenuListener);
        trimM.addActionListener(myMenuListener);
        deleteGlobalBlankM.addActionListener(myMenuListener);
        
        upperCaseM.addActionListener(myMenuListener);
        lowerCaseM.addActionListener(myMenuListener);
        quotes_escapeM.addActionListener(myMenuListener);
        replaceChinaQuotesM.addActionListener(myMenuListener);
        deleteNM.addActionListener(myMenuListener);
        deleteNRM.addActionListener(myMenuListener);
        deleteRNM.addActionListener(myMenuListener);
        urlEncodeM.addActionListener(myMenuListener);
        urlDecodeM.addActionListener(myMenuListener);
        xml2jsonM.addActionListener(myMenuListener);
        md5M.addActionListener(myMenuListener);
        deleteFrontDigitM.addActionListener(myMenuListener);
        deleteNotesM.addActionListener(myMenuListener);
        copyImage2ClipM.addActionListener(myMenuListener);
        
		 copyImageSpecifyheight.addActionListener(myMenuListener);
		 copyImageSpecifyWidthHeight.addActionListener(myMenuListener);
        
        queryString2Json.addActionListener(myMenuListener);
        json2QueryString.addActionListener(myMenuListener);
        and2CRFL.addActionListener(myMenuListener);
        showHtml.addActionListener(myMenuListener);
        formatJsonHtml.addActionListener(myMenuListener);
        formatJsonDeepHtml.addActionListener(myMenuListener);
        deleteQuoteBracketsBetter.addActionListener(myMenuListener);

        deleteJsonExtraComma.addActionListener(myMenuListener);
        openBrowserM.addActionListener(myMenuListener);
        searchM.addActionListener(myMenuListener);
        exitM.addActionListener(myMenuListener);
        textMenu.add(copyM);
        textMenu.add(copyAllM);
//        if(!(field2 instanceof JTextField)){
        	/*因为JTextField 没有 ta2.insert(content, caret) 方法 .*/
        	 textMenu.add(pasteM);
//        }
       
        textMenu.add(replaceAllM);
        textMenu.add(deleteM);
        textMenu.add(deleteAllM);
        textMenu.add(selAllM);
        textMenu.add(lengthM);
        textMenu.add(deleteCRLFM);
        textMenu.add(editableM);
        textMenu.add(trimM);
        textMenu.add(deleteGlobalBlankM);
        textMenu.add(upperCaseM);
        textMenu.add(lowerCaseM);
        textMenu.add(quotes_escapeM);
        textMenu.add(replaceChinaQuotesM);
        textMenu.add(deleteNM);
        textMenu.add(deleteNRM);
        textMenu.add(deleteRNM);
        textMenu.add(urlEncodeM);
        textMenu.add(urlDecodeM);
        textMenu.add(xml2jsonM);
        textMenu.add(md5M);
        textMenu.add(deleteFrontDigitM);
        textMenu.add(deleteNotesM);
        textMenu.add(copyImage2ClipM);
        textMenu.add(copyImageSpecifyheight);
        textMenu.add(copyImageSpecifyWidthHeight);
        textMenu.add(queryString2Json);
        textMenu.add(json2QueryString);
        textMenu.add(and2CRFL);
        textMenu.add(showHtml);
        textMenu.add(formatJsonHtml);
        textMenu.add(formatJsonDeepHtml);
        textMenu.add(deleteQuoteBracketsBetter);
        textMenu.add(deleteJsonExtraComma);
        textMenu.add(openBrowserM);
        textMenu.add(searchM);
        textMenu.add(exitM);
        return textMenu;
    }
    /***
     * 给文本框增加右键菜单.
     * 
     * @param field2
     */
    public static JPopupMenu setPopupMenu(final JTextComponent field2, JPopupMenu textMenu1, final Map<String, ActionCallback> actionCallbackMap)
    {
    	final JPopupMenu textMenu;
    	if(ValueWidget.isNullOrEmpty(textMenu1)){
        	textMenu = new JPopupMenu();
        }else{
        	textMenu=textMenu1;
        }
        field2.addMouseListener(new MouseInputListener()
        {
            private java.util.Timer timer;
            @Override
            public void mouseMoved(MouseEvent e)
            {

            }

            @Override
            public void mouseDragged(MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(final MouseEvent e)
            {
                //                super.mousePressed(e);
//                System.out.println("click:"+e.getClickCount());
                if (e.getButton() == MouseEvent.BUTTON3)//右键
                {
                    if (e.getClickCount() > 1) {//右键双击
                        if (null != timer) {
                            timer.cancel();
                            timer = null;
                        } else {
                            return;//说明已经执行了textMenu.show(e.getComponent(), e.getX() + 10, e.getY());
                        }
                        if (!ValueWidget.isNullOrEmpty(field2.getText())) {
                            WindowUtil.setSysClipboardText(field2.getText());
                            ToastMessage.toast("已复制到剪切板", 2000);
                        }
                    } else {//启动定时器
                        if (null == timer) {
                            timer = new java.util.Timer();
                        }
                        timer.schedule(new java.util.TimerTask() {
                            @Override
                            public void run() {
                                timer = null;
                                textMenu.show(e.getComponent(), e.getX() + 10, e.getY());
                            }
                        }, /*300*/Constant2.RIGHT_CLICK_DELAY);
                    }
//                	addPopupMenuItem(field2,textMenu);
//
                }else if (e.getButton() == MouseEvent.BUTTON2){//鼠标中键
                	//按下鼠标中键,把剪切板内容黏贴到文本框中
                	String text=WindowUtil.getSysClipboardText();
                	if(!ValueWidget.isNullOrEmpty(text)
                			&&!ValueWidget.isNullOrEmpty(field2)){
                		field2.setText(text);
                		field2.setForeground(TextCompUtil2.DEFAULT_TF_FOREGROUND);//防止placeholder
                		field2.requestFocus();
                        if (null != actionCallbackMap) {
                            ActionCallback callback22 = actionCallbackMap.get(Constant2.EVENT_MIDDLE_MOUSE);
                            if (null != callback22) {
                                callback22.actionPerformed(null, field2);
                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
            }
        });
        return textMenu;
    }
    /***
     * 通过时间间隔来判断是否是双击(不是鼠标,是键盘)
     * @param delta
     * @return
     */
    public static boolean isDoubleClick(long delta){
    	return (delta<300&&delta>100);
    }

    /***
     * 设置弹出菜单
     * @param qrResultLabel
     */
    public static void setImagePopupMenu(JComponent qrResultLabel, final ActionListener myMenuListener) {
        //final QRCodeMenuActionListener myMenuListener=new QRCodeMenuActionListener(this);
        qrResultLabel.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                //                super.mousePressed(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu textMenu = new JPopupMenu();
                    JMenuItem cleanUpM = new JMenuItem(MenuUtil2.ACTION_STR_CLEANUP);
                    JMenuItem copy22M = new JMenuItem(
                            MenuUtil2.ACTION_IMAGE_COPY);
                    JMenuItem paste22M = new JMenuItem(
                            MenuUtil2.ACTION_IMAGE_PASTE);
                    JMenuItem enlargeM = new JMenuItem(
                            MenuUtil2.ACTION_ENLARGE);
                    JMenuItem reduceM = new JMenuItem(
                            MenuUtil2.ACTION_REDUCE);
//                    JMenuItem pasteM = new JMenuItem(MenuUtil2.ACTION_STR_PASTE);

                    JMenuItem exportM = new JMenuItem(
                            MenuUtil2.ACTION_STR_EXPORT);
                    JMenuItem readQRCodeM = new JMenuItem(
                            MenuUtil2.ACTION_READ_QR_CODE);

                    JMenuItem openBrowserM = new JMenuItem(
                            MenuUtil2.ACTION_STR_OPEN_BROWSER);

                    JMenuItem uploadM = new JMenuItem("upload");

                    copy22M.addActionListener(myMenuListener);
                    cleanUpM.addActionListener(myMenuListener);
                    exportM.addActionListener(myMenuListener);
                    enlargeM.addActionListener(myMenuListener);
                    reduceM.addActionListener(myMenuListener);
                    paste22M.addActionListener(myMenuListener);
                    readQRCodeM.addActionListener(myMenuListener);
                    openBrowserM.addActionListener(myMenuListener);
                    uploadM.addActionListener(myMenuListener);
                    textMenu.add(cleanUpM);
                    textMenu.add(copy22M);
                    textMenu.add(paste22M);
//                    exportM.add(pasteM);
                    textMenu.add(exportM);
                    textMenu.add(enlargeM);
                    textMenu.add(reduceM);
                    textMenu.add(readQRCodeM);
                    textMenu.add(openBrowserM);
                    textMenu.add(uploadM);
                    textMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

    }

    public static void urlDecode(JTextComponent area2) {
        String content = area2.getText();
        if (ValueWidget.isNullOrEmpty(content)) {
            return;
        }
        try {
            String result = URLDecoder.decode(content, SystemHWUtil.CHARSET_UTF);//TODO 编码是写死了
            area2.setText(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void urlEncode(JTextComponent area2) {
        String content = area2.getText();
        if (ValueWidget.isNullOrEmpty(content)) {
            return;
        }
        try {
            String encodedStr = URLEncoder.encode(content, SystemHWUtil.CHARSET_UTF);//TODO 编码是写死了
            area2.setText(encodedStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void queryString2Json(JTextComponent area2, boolean isSuppressWarnings, boolean isFurther) {
        String selectContent = area2.getSelectedText();
        boolean isSelectContent = true;
        if (ValueWidget.isNullOrEmpty(selectContent)) {
            isSelectContent = false;
            selectContent = area2.getText();
//				return;
        }
        Map requestMap = new HashMap();
        RequestUtil.setArgumentMap(requestMap, selectContent, true, null, null, false, true);
        String jsonResult = HWJacksonUtils.getJsonP(requestMap);

        if (!ValueWidget.isNullOrEmpty(jsonResult)) {
            if (isSelectContent) {
                WindowUtil.setSysClipboardText(jsonResult);
                ToastMessage.toast("复制json到剪切板", 2000);
            } else {
                area2.setText(jsonResult);
                JSONHWUtil.formatJson(area2, isFurther/*isFurther*/, null, isSuppressWarnings);
            }

        }
    }

    public static void json2queryString(JTextComponent area2) {
        String selectContent = area2.getSelectedText();
        boolean isSelectContent = true;
        if (ValueWidget.isNullOrEmpty(selectContent)) {
            isSelectContent = false;
            selectContent = area2.getText();
        }
        Map map;
            map = (Map) HWJacksonUtils.deSerialize(selectContent, Map.class);
            String jsonResult = WebServletUtil.getRequestBodyFromMap(map);
            if (!ValueWidget.isNullOrEmpty(jsonResult)) {
                if (isSelectContent) {
                    WindowUtil.setSysClipboardText(jsonResult);
                    ToastMessage.toast("复制query String到剪切板", 2000);
                } else {
                    area2.setText(jsonResult);
                }
            }
    }

    public static void searchPopupMenu(JFrame frame, JPopupMenu textPopupMenu, SearchPopupMenuListener searchPopupMenuListener, JMenuItem cancelM, Point point, int delta) {
        if (null != cancelM) {
            cancelM.addActionListener(searchPopupMenuListener);
            textPopupMenu.add(cancelM);
        }

        int y = point.y + 75;// 下移一点
        if (delta > 0) {
            y = y - delta;
        }
        textPopupMenu.show(frame, point.x + 10, y);
    }
    public static void searchResultList(JFrame frame, JTabbedPane tabbedPane_2, List<Integer> tabIndexList, JTextComponent tc) {
        searchResultList(frame, tabbedPane_2, tabIndexList, tc, SystemHWUtil.NEGATIVE_ONE);
    }

    public static void searchResultList(JFrame frame, JTabbedPane tabbedPane_2, List<Integer> tabIndexList, JTextComponent tc, int delta) {
        JPopupMenu textPopupMenu = new JPopupMenu();
        textPopupMenu.setForeground(Color.red);
        textPopupMenu.setBackground(Color.blue);
        textPopupMenu.setOpaque(false);//透明度
        SearchPopupMenuListener searchPopupMenuListener = new SearchPopupMenuListener(tabbedPane_2);
        boolean needSelected = true;
        for (int i = 0; i < tabIndexList.size(); i++) {
            int select = tabIndexList.get(i);
            needSelected = addSearchPopupMenu(tabbedPane_2, textPopupMenu, searchPopupMenuListener, needSelected, select);
            textPopupMenu.addSeparator();
        }
        Point point = tabbedPane_2.getLocation();
        point.y = point.y + 20;
//        JMenuItem cancelM = new JMenuItem("取消");
        searchPopupMenu(frame, textPopupMenu, searchPopupMenuListener, null, point, delta);
    }

    public static void searchResultList(JFrame frame, JTabbedPane tabbedPane_2, Set<Integer> searchResult, JComponent tc) {
        searchResultList(frame, tabbedPane_2, searchResult, tc, SystemHWUtil.NEGATIVE_ONE);
    }


    /***
     * 生成搜索结果下拉菜单<br><br>与上面的方式重载,99%的代码是相同的,只有倒数第二个参数类型不同.
     * @param frame
     * @param tabbedPane_2
     * @param searchResult
     * @param tc
     * @param delta
     */
    public static void searchResultList(JFrame frame, JTabbedPane tabbedPane_2, Set<Integer> searchResult, Container tc, int delta) {
        JPopupMenu textPopupMenu = new JPopupMenu();
        textPopupMenu.setForeground(Color.red);
        textPopupMenu.setBackground(Color.blue);
        textPopupMenu.setOpaque(false);//透明度
        SearchPopupMenuListener searchPopupMenuListener = new SearchPopupMenuListener(tabbedPane_2);
        boolean needSelected = true;
        for (Iterator<Integer> it = searchResult.iterator(); it.hasNext(); ) {
            int select = it.next();
            needSelected = addSearchPopupMenu(tabbedPane_2, textPopupMenu, searchPopupMenuListener, needSelected, select);
        }
        JMenuItem cancelM = new JMenuItem("取消");
        Point point = tc.getLocation();
        searchPopupMenu(frame, textPopupMenu, searchPopupMenuListener, cancelM, point, delta);
    }
    public static boolean addSearchPopupMenu(JTabbedPane tabbedPane_2, JPopupMenu textPopupMenu, SearchPopupMenuListener searchPopupMenuListener, boolean needSelected, int select) {
//        System.out.println("select:"+select);
//        System.out.println(tabbedPane_2.getTabCount());
        if (select >= tabbedPane_2.getTabCount()) {//解决删除请求panel之后,tabbed索引越界的问题
            return false;
        }
        Component comp = tabbedPane_2.getComponentAt(select);
        if (!(comp instanceof MyNamePanel)) {
            return needSelected;
        }
            MyNamePanel requestPanel = (MyNamePanel) comp;
            String requestName = requestPanel.getRequestName();
            if (ValueWidget.isNullOrEmpty(requestName)) {
                requestName = String.valueOf(select);
            } else {
                requestName = requestName + "(" + select + ")";
            }
        requestName = "<html> <span style=\"color: #ff4646;font-weight: bolder;\">" + requestName + "</span>";
        String serverIP = "";
        if (!ValueWidget.isNullOrEmpty(requestPanel.getServerIp())) {
            serverIP = "<span style=\"color: #22ff00;font-weight: bolder;\">" + SystemHWUtil.splitAndFilterString(requestPanel.getServerIp(), 4, false) + ".. " + "</span>";
        }
        if (!ValueWidget.isNullOrEmpty(requestPanel.getActionName())) {
            requestName = requestName + " | " + serverIP + requestPanel.getActionName();
        }
        JMenuItem openFolderM = new JMenuItem(requestName + "</html>");
        ChangeListener cl = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JMenuItem) {
                    JMenuItem item = (JMenuItem) e.getSource();
//                        System.out.println("e:" + e);
                    if (item.isArmed()) {
                        System.out.println("item :" + item.getText());
//                            System.out.println("item :" + item.isSelected() + "," + item.isArmed());
                    }
                }
            }
        };
        openFolderM.addChangeListener(cl);
//            openFolderM.setToolTipText(requestPanel.getActionName());
            openFolderM.setActionCommand(String.valueOf(select));
            openFolderM.addActionListener(searchPopupMenuListener);
            textPopupMenu.add(openFolderM);
            if (needSelected) {
                openFolderM.setSelected(true);
                needSelected = false;
            }
        return needSelected;
    }


    /***
     *
     * @param keyWord
     * @param tabbedPane_2
     * @param includeActionName : 是否比较ActionName,即接口访问路径
     * @return
     */
    public static Set<Integer> searchAction(String keyWord, JTabbedPane tabbedPane_2, boolean includeActionName) {
        int size = tabbedPane_2.getTabCount();
        if (size == 0) {
            return null;
        }
        Set<Integer> searchResult = new HashSet<Integer>();
        for (int i = 0; i < size; i++) {
            if (RegexUtil.contain2(tabbedPane_2.getTitleAt(i), keyWord)) {
                searchResult.add(i);
            } else {
                Component comp = tabbedPane_2.getComponentAt(i);
                if (comp instanceof MyNamePanel) {
                    MyNamePanel requestPanel = (MyNamePanel) comp;
                    if (RegexUtil.contain2(requestPanel.getAlias(), keyWord)) {
                        searchResult.add(i);
                    }
                }
            }
        }
        if (includeActionName && searchResult.size() == 0) {
            //通过接口名称搜索
            for (int i = 0; i < size; i++) {
                Component comp = tabbedPane_2.getComponentAt(i);
                if (comp instanceof MyNamePanel) {
                    MyNamePanel requestPanel = (MyNamePanel) comp;
                    if (RegexUtil.contain2(requestPanel.getActionName(), keyWord)) {
                        searchResult.add(i);
                    }
                }
            }
        }
        if (searchResult.size() == 0) {
            //通过接口名称搜索
            for (int i = 0; i < size; i++) {
                Component comp = tabbedPane_2.getComponentAt(i);
                if (comp instanceof MyNamePanel) {
                    MyNamePanel requestPanel = (MyNamePanel) comp;
                    if (null != requestPanel.getRequetId() && RegexUtil.contain2(requestPanel.getRequetId(), keyWord)) {
                        searchResult.add(i);
                    }
                }
            }
        }
        return searchResult;
    }

    /***
     * 搜索成功的提示
     * @param tc
     */
    public static void setBackgroundWhenSearch(final JTextComponent tc/*, final Color tfDefaultColor*/) {
        final Color tfDefaultColor = tc.getBackground();
        tc.setBackground(CustomColor.green_little);
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                tc.setBackground(tfDefaultColor);
            }
        }, 2000);
    }

    public static void processEvent(MouseEvent e, JComponent component) {
        // Right-click on
        if (!((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0)) {
            // System.out.println(e.getModifiers());
            // System.out.println("Right-click on");
            return;
        }
        int modifiers = e.getModifiers();
        modifiers -= MouseEvent.BUTTON3_MASK;
        modifiers |= MouseEvent.BUTTON1_MASK;
        MouseEvent ne = new MouseEvent(e.getComponent(), e.getID(),
                e.getWhen(), modifiers, e.getX(), e.getY(),
                e.getClickCount(), false);
        component.dispatchEvent(ne);// in order to trigger Left-click
        // the event
    }

    public static void deleteQuoteBracketsBetter(JTextComponent area2) {
        String content = area2.getText();
        if (!ValueWidget.isNullOrEmpty(content)) {
            StringBuffer sb = RegexUtil.deleteQuoteBracketsBetter(content);
            area2.setText(sb.toString());
        }
    }

    public static void strongMenuItem(JMenuItem copyPanelM) {
        Border borderResultTAgur = BorderFactory.createEtchedBorder(Color.white,
                new Color(255, 0, 0));
        copyPanelM.setBorder(borderResultTAgur);
        Font font = copyPanelM.getFont();
        copyPanelM.setFont(new Font("", Font.BOLD, font.getSize()));
    }

}
