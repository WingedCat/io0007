package com.string.widget.util;

import com.common.dict.Constant2;
import com.common.util.SystemHWUtil;
import com.io.hw.file.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static final String REGEX_DELETE_QUOT_brackets = "(:[\\s]*)\"([\\[\\{].*[\\]\\}])[\\s]*\"";

    /***
	 * 
	 * @param aa
	 * @param index
	 *            : 初始值为0
	 */
	protected static List<StringBuffer> cc(String[][] aa, int index,
			List<StringBuffer> list, boolean isDealRegex) {

		if (index >= aa.length) {// 说明已经遍历完成
			return list;// 并不是每次循环都会执行,最后才会执行此语句.
		}
		String cc[] = aa[index];
		int length = cc.length;
		List<StringBuffer> listNew = new ArrayList<StringBuffer>();
		if (list == null || list.size() == 0) {// 首次循环
			for (int i = 0; i < length; i++) {// 必须保证顺序,所以不能使用 foreach
				if (isDealRegex && cc[i].equals("*")) {
					cc[i] = "\\*";
				}
				// if(isDealRegex){
				// listNew.add(new StringBuffer(cc[i]+"?"));
				// }else{
				listNew.add(new StringBuffer(cc[i]));
				// }

			}
		} else {
			for (int i = 0; i < length; i++) {// 必须保证顺序,所以不能使用 foreach
				for (int j = 0; j < list.size(); j++) {// 必须保证顺序,所以不能使用 foreach
					StringBuffer sb = list.get(j);
					StringBuffer sb2 = new StringBuffer(sb);
					if (isDealRegex && cc[i].equals("*")) {
						cc[i] = "\\*";
					}
					// if(isDealRegex ){
					// sb2.append(cc[i]+"?");
					// }else{
					sb2.append(cc[i]);
					// }
					listNew.add(sb2);
				}
			}
		}
		++index;
		List<StringBuffer> list33 = cc(aa, index, listNew, isDealRegex);
		if (!ValueWidget.isNullOrEmpty(list33)) {
			return list33;
		}
		return null;
	}

	/***
	 * 修改C:\Windows\System32\drivers\etc\hosts,删除注释#
	 * 
	 * @param content
	 * @param domain
	 * @return
	 */
	public static String deleteComment(String content, String domain) {
		String regex = "[ ]*#[\\s]*(127.0.0.1[\\s]*" + domain + ")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		if (!result) {
			return null;
		}
		while (result) {
			String str = m.group(1);
			m.appendReplacement(sb, str);
			result = m.find();
		}

		m.appendTail(sb);
		return sb.toString();
	}

	/***
	 * 修改C:\Windows\System32\drivers\etc\hosts,增加注释#
	 * 
	 * @param content
	 * @param domain
	 * @return
	 */
	public static String addComment(String content, String domain) {
		String regex = "[ ]*127.0.0.1[\\s]*" + domain;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		while (result) {
			String str = m.group();
			m.appendReplacement(sb, "# " + str);
			result = m.find();
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/***
	 * replaceAll,忽略大小写<br>
	 * 方式二
	 * 
	 * @param input
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceAll2(String input, String regex,
			String replacement) {
		if (ValueWidget.isNullOrEmpty(input)) {
			return input;
		}
		StringBuffer sb = customFind(input, regex, replacement, Pattern.CASE_INSENSITIVE);
		return sb.toString();
	}

	public static StringBuffer customFind(String input, String regex, String replacement, int caseInsensitive) {
		Pattern p = Pattern.compile(regex, caseInsensitive);
		Matcher m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		while (result) {
			m.appendReplacement(sb, replacement);
			result = m.find();
		}
		m.appendTail(sb);
		return sb;
	}

	/***
	 * 是否包含指定字符串,不区分大小写
	 * 
	 * @param input
	 *            : 原字符串
	 * @param regex
	 * @return
	 */
	public static boolean contain2(String input, String regex) {
        /***
         * input:(1)bss登录(2) <br>
         regex:bss登录(2)
         */
        regex = regex.replace("(", "\\(");
        regex = regex.replace(")", "\\)");
        if (ValueWidget.isNullOrEmpty(input)) {
			return false;
		}
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		boolean result = m.find();
		return result;
	}

	/***
	 * replaceAll,忽略大小写<br>
	 * 方式二
	 * 
	 * @param input
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceAll2(String input, String regex,
			String replacement, int patternFlags) {
		if (ValueWidget.isNullOrEmpty(input)) {
			return input;
		}
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE
				| patternFlags);
		Matcher m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
		while (result) {
			m.appendReplacement(sb, replacement);
			result = m.find();
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/***
	 * replaceAll,忽略大小写<br>
	 * 方式三
	 * 
	 * @param input
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceAll3(String input, String regex,
			String replacement) {
		if (ValueWidget.isNullOrEmpty(input)) {
			return input;
		}
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		String result = m.replaceAll(replacement);
		return result;
	}

	/***
     * startsWith,忽略大小写,兼容正则表达式<br>
     *
     * @param input
     * @param regex
     * @return
     */
    public static boolean startsWith(String input, String regex) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return false;
        }
        Pattern p = Pattern.compile("^" + regex + ".*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean startsWith(String input, String regex1, String regex2) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return false;
        }
        return startsWith(input, regex1) || startsWith(input, regex2);
    }

    public static boolean startsWith(String input, String regex1, String regex2, String regex3) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return false;
        }
        return startsWith(input, regex1) || startsWith(input, regex2) || startsWith(input, regex3);
    }

    public static boolean startsWith(String input, String... regex1) {
        if (regex1.length == 1) {
            return startsWith(input, regex1[0]);
        }
        boolean inite = startsWith(input, regex1[0]);
        for (int i = 1; i < regex1.length; i++) {
            if (inite) {
                return inite;
            }
            inite = inite || startsWith(input, regex1[i]);
        }
        return inite;
    }

    /***
     * 高级的split
     *
     * @param source
     * @param regex
     * @return
     */
    public static String[] splitRegex(String source, String regex) {
        Pattern pattern = Pattern.compile(regex);
        String[] dataArr = pattern.split(source);
        return dataArr;
    }

    public static boolean equalsWildcard(String source, String regex) {
        return equalsWildcard(source, regex, false);
    }

    public static boolean endsWithWildcard(String source, String regex) {
        return equalsWildcard(source, regex, true);
    }

	/***
	 * 
	 * @param source
	 * @param regex
	 *            : 含有通配符,通配符只有一个:*.<br>
     *            *表示任何字符,不限个数<br />
     *            不区分大小写
     * @return
	 */
    public static boolean equalsWildcard(String source, String regex, boolean isEndsWith) {
        regex = regex.replace(SystemHWUtil.ENGLISH_PERIOD, "\\.");
        regex = regex.replace("*", "(.*)");// 加上小括号,方便查看
		// System.out.println(regex);
        String regex2 = null;
        if (isEndsWith) {
            regex2 = regex + "$";
        } else {
            regex2 = "^" + regex + "$";
        }
        Pattern p = Pattern
                .compile(regex2, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
		return m.find();
	}

	/***
	 * 把中文替换为指定字符<br>
	 * 注意:一次只匹配一个中文字符
	 * 
	 * @param source
	 * @param replacement
	 * @return
	 */
	public static String replaceChinese(String source, String replacement) {
		if (ValueWidget.isNullOrEmpty(source)) {
			return null;
		}
		if (replacement == null) {
			replacement = SystemHWUtil.EMPTY;
		}
		String reg = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(reg);
		Matcher mat = pat.matcher(source);
		String repickStr = mat.replaceAll(replacement);
		return repickStr;
	}

	/***
	 * ("normalPrice": "{"storagePrice":66,"ud1Price":1,"userPeriodPrice":99}",)
	 * to<br>
	 * ("normalPrice": {"storagePrice":66,"ud1Price":1,"userPeriodPrice":99},)
	 * 
	 * @param input
	 * @param isStrict
	 *            : 是否严格,true:[^{}]<br>
	 *            false:.*
	 * @return
	 */
	public static String getJsonFromQuotes(String input, boolean isStrict) {
		String regexLoose = "\"(\\{.*\\})\"";
		String regexStrict = "\"(\\{[^{}]*\\})\"";
		String regex = null;
		if (isStrict) {
			regex = regexStrict;
		} else {
			regex = regexLoose;
		}
		String result = input.replaceAll(regex, "$1");
		return result;
	}

	/***
	 * strict :严格模式
	 * @param input
	 * @param isDeleteN
	 * @return
	 */
	public static String dealCiaResponse(String input, boolean isDeleteN) {
		return dealCiaResponse(input, Constant2.REGEX_DELETE_FRONT_OF_CIA_RESPONSE_STRICT, isDeleteN);
	}
	public static String dealCiaResponse(String input, String regex, boolean isDeleteN) {
		Pattern p = Pattern.compile(regex,Pattern.MULTILINE);
		Matcher m = p.matcher(input);
		String result = m.replaceAll("$1");
        if (!isDeleteN) {
            return result;
        }
        result = result.replace("[\\n]", SystemHWUtil.EMPTY);
			result = result.replace("[\\r]", SystemHWUtil.EMPTY);
		// System.out.println(result);
		return result;
	}
	public static String dealCiaRequest(String input, String regex, boolean isDeleteN) {
		Pattern p = Pattern.compile(regex,Pattern.MULTILINE);
		Matcher m = p.matcher(input);
		String result = m.replaceAll("请求:$1");
		if (isDeleteN) {
			result = result.replace("[\\n]", SystemHWUtil.EMPTY);
			result = result.replace("[\\r]", SystemHWUtil.EMPTY);
		}
		// System.out.println(result);
		return result;
	}
	/***
	 * 删除每行前面的数字
	 * @param input
	 * @return
	 */
	public static String deleteDigit(String input) {
		if(ValueWidget.isNullOrEmpty(input)){
			return null;
		}
		Pattern p = Pattern.compile("^[\\d]+[,:]?[\\s]*",Pattern.MULTILINE);
		Matcher m = p.matcher(input);
		String result = m.replaceAll(SystemHWUtil.EMPTY);
		return result;
	}

    /***
     * 仅仅删除每行前面的//
     * @param input
     * @return
     */
    public static String deleteNotes(String input) {
        if(ValueWidget.isNullOrEmpty(input)){
            return null;
        }
        Pattern p = Pattern.compile("^[/]{2}",Pattern.MULTILINE);
        Matcher m = p.matcher(input);
        String result = m.replaceAll(SystemHWUtil.EMPTY);
        return result;
    }
	public static String dealCiaResponseRequest(String input,  boolean isDeleteN) {
		String result=dealCiaResponse(input, true);
		result=dealCiaRequest(result,Constant2.REGEX_DELETE_FRONT_OF_CIA_REQUEST_STRICT , true);
		result=dealCiaRequest(result,Constant2.REGEX_DELETE_FRONT_OF_CIA_REQUEST_LOOSE , true);
		result=dealCiaResponse(result,Constant2.REGEX_DELETE_FRONT_OF_CIA_RESPONSE_LOOSE , true);
		return result;
	}
	/***
	 * 删除两边的双引号
	 * @param input
	 * @return
	 */
	public static String deleteTwoQuote(String input){
		if(ValueWidget.isNullOrEmpty(input)){
			return input;
		}
		return input.replaceAll("\"(.+)[\\s-]*\"[\\s-]*.*", "$1");
	}
	/***
	 * 删除{ 前面的一切内容
	 * @param input
	 * @return
	 */
	public static String deleteEveryThingBeforeBrace(String input){
		if(ValueWidget.isNullOrEmpty(input)){
			return input;
		}
		return input.replaceAll("^[\\s-]*[^{]*(\\{.*)", "$1");
	}

	public static String splitPlus(String input, String cr, String quote) {
		return splitPlus(input, cr, quote, false);
	}
	/***
	 * 把换行的段落当做字符串拼接
	 * @param input
	 * @param cr
	 * @param quote
	 * @return
	 */
	public static String splitPlus(String input, String cr, String quote, boolean keepBlank) {
		input=input.replaceAll(cr+"+$","");
		String chinese;
//		String quote="\"";
        String replacement = quote + (quote.equals("'") ? "" : " + SystemHWUtil.CRLF") + "/\\* \\\\r\\\\n\\ */ +$1" + quote + "$2";
        String regex = null;
        String regexMutip = null;//是否匹配多个换行
        if (keepBlank) {//保持空格 <br />[^\n\r]表示不是换行
            regex = "([^\n\r]*)";
            regexMutip = "";
        } else {
			regex = "[\\s]*([^\\s]+.*)";
            regexMutip = "+";
        }
        input = input.replace("\\", "\\\\");//对斜杠转义,必须在 replace(quote, "\\"+quote) 上面
        input=input.replace(quote, "\\"+quote);//原字符串转义
        //简单点说,就是把[换行]替换为['(单引号)+(加号)'(单引号)]
        //所以最前面和最后面需要各补一个单引号
        if(cr.equals("\\r\\n")){
            chinese = input.replaceAll("(\r\n)" + regexMutip + regex, replacement);
        }else if(cr.equals("\\n")){
            chinese = input.replaceAll("(\n)" + regexMutip + regex, replacement);
        }else if(cr.equals("\\r")){
            chinese = input.replaceAll("(\r)" + regexMutip + regex, replacement);
        }else{
            chinese = input.replaceAll("(\n\r)" + regexMutip + regex, replacement);
        }
		return quote+chinese+quote+";";
	}

    public static String sed(String source, String regex, String replacement) {
        return sed(source, regex, replacement, Pattern.MULTILINE);
    }

    /***
	 * __showLog=True -->__showLog=false
	 * @param source
	 * @param regex : 正则表达式
	 * @param replacement
	 * @return
	 */
    public static String sed(String source, String regex, String replacement, int flags) {
        Pattern p = Pattern.compile(regex, flags);
        Matcher m=p.matcher(source);
        String result = m.replaceAll(replacement);
		return result;
	}

    /***
     *  regex 中必须包含小括号 <br />
     *   RegexUtil.sed(requestHeaderAndServletPath, "(Cookie:[^:]+)")
     * @param source
     * @param regex
     * @return
     */
    public static String sed(String source, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(source);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

	/***
	 * 
	 * @param source
	 * @param arrFrom
	 * @param arrTo
	 * @return
	 */
	public static String sedY(String source,String[] arrFrom,String[] arrTo){
		if(ValueWidget.isNullOrEmpty(source)){
			return SystemHWUtil.EMPTY;
		}
		int length=arrFrom.length;
		for(int i=0;i<length;i++){
			String tmp=arrFrom[i];
			if(source.equals(tmp)){
				return arrTo[i];
			}
		}
//		return SystemHWUtil.EMPTY;
		throw new RuntimeException("can not find");
	}
	/***
	 * 模拟linux 的sed 功能
	 * <br>将关闭输出流
	 * @param source
	 * @param regex : 正则表达式
	 * @param replacement
	 * @return
	 */
	public static String sed(File source,String regex,String replacement,String charset){
		String input=null;
		try {
			if(ValueWidget.isNullOrEmpty(charset)){
				charset=SystemHWUtil.CHARSET_UTF;
			}
			input = FileUtils.getFullContent2(source, charset,true);
			input=sed(input, regex, replacement);
			FileUtils.writeStrToFile(source, input, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	/***
	 * 判断是否是HTML 网页
	 * @param input
	 * @return
	 */
	public static boolean isHTMLWebPage(String input){
		if(ValueWidget.isNullOrEmpty(input)){
			return false;
		}
		return isMatches(input, ".*<html[\\s\\w'\"=]*>.*", Pattern.DOTALL);
	}
	/***
	 * 
	 * @param source
	 * @param regex
	 * @param flags :Pattern.DOTALL
	 * @return
	 */
	public static boolean isMatches(String source,String regex,int flags){
		Pattern p=Pattern.compile(regex,flags);
        Matcher m=p.matcher(source);
		return m.matches();
	}

	/***
	 * 通过正则表达式判断是否匹配
	 *
	 * @param source
	 * @param regx
	 * @param flags
	 * @return
	 */
	public static boolean isFind(String source, String regx, int flags) {
		Pattern p = Pattern.compile(regx, flags);
		Matcher m = p.matcher(source);
		return m.find();
	}
	/***
	 * 驼峰标识<br>
	 * 
	 * @param input : aaa_bbb_ccc_ddd
	 * @return : aaaBbbCccDdd
	 */
	public static String humpMarking(String input){
		Pattern p=Pattern.compile("_[a-z]");
        Matcher m=p.matcher(input);
        StringBuffer sb = new StringBuffer();
        boolean result=m.find();
        while(result){
            String findResult=m.group().toUpperCase().replaceAll("^_", SystemHWUtil.EMPTY);
            m.appendReplacement(sb, findResult);
            result=m.find();
        }
        m.appendTail(sb);
        return sb.toString().replaceAll("_$", SystemHWUtil.EMPTY);
	}

	public static String convertBr(String input) {
		return input.replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
	}

    /***
     * 删除字符串中的空格,包括中间的空格
     *
     * @param input
     * @return
     */
    public static String filterBlank(String input) {
        String result = input.replaceAll("[\\s]+", SystemHWUtil.EMPTY);//IE中识别不了有空格的json
        return result;
    }

    /***
     * 过滤表情符号<br>
     * see:http://blog.csdn.net/beatrice_g20/article/details/48489437
     *
     * @param str
     * @return
     */
    public static String filterExpression(String str) {

        if (ValueWidget.isNullOrEmpty(str)) {
            return str;
        }
        String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
        Pattern emoji = Pattern.compile(pattern);
        Matcher emojiMatcher = emoji.matcher(str);
        str = emojiMatcher.replaceAll(SystemHWUtil.EMPTY);
        return str;
    }

    /***
     * 删除重复的行(必须是相连的)
     * @param input
     * @return
     */
    public static String deleteDuplicateRow(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return input;
        }
        String[] rows = input.split(SystemHWUtil.CRLF);
        List<String> stringlist = new ArrayList<String>();
        for (int i = 0; i < rows.length; i++) {
            stringlist.add(rows[i]);
        }
        // 搜索结果是经过排序的，根据此规律删除不合要求File
        for (int i = 0; i < stringlist.size() - 1; i++) {
            if (/*stringlist.get(i).length()>1&& */stringlist.get(i).equals(stringlist.get(i + 1))) {
                stringlist.remove(i);
                if (i != 0) i--;
            }
        }
        return SystemHWUtil.formatArr(stringlist, SystemHWUtil.CRLF);
    }

    /***
     * 删除json格式中多余的逗号<br />
     * 例如:"amount": {
     "period": 12,
     "ud1": 300,
     "storage": 0,
     "user": 300,
     "freeStorage": 0,
     }
     * @param input
     * @return
     */
    public static String deleteJsonExtraComma(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return input;
        }
        System.out.println(input);
        input = input.replaceAll(",([\\s]*[\\]}])", "$1");
        return input;
    }

    /***
     * 删除中括号两边的引号
     * @param content
     * @return
     */
    public static StringBuffer deleteQuoteBracketsBetter(String content) {
        Pattern p = Pattern.compile(REGEX_DELETE_QUOT_brackets);
        Matcher m = p.matcher(content);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while (result) {
            String group1 = m.group(1);
            String group2 = m.group(2);
            String findResult = group2/*.replace("\\\\", "\\")*/.replace("\\\"", "\"");
            m.appendReplacement(sb, group1 + findResult);
            result = m.find();
        }
        m.appendTail(sb);
        return sb;
    }

    /***
     * 获取复杂字符串中的数字
     * @param input
     * @return
     */
    public static String getDigit(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return null;
        }
        Pattern p = Pattern.compile(".*Content-Length: ([\\d]+)[^\\d]+.*", Pattern.DOTALL);
        Matcher m = p.matcher(input);
        String result = m.replaceAll("$1");
        return result;
    }

    /***
     * 获取复杂字符串中的数字
     * @param input
     * @return
     */
    public static String getDigitAnyWhere(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return null;
        }
        Pattern p = Pattern.compile("[^\\d]*([\\d]+)[^\\d]*", Pattern.DOTALL);
        Matcher m = p.matcher(input);
        String result = m.replaceAll("$1");
        return result;
    }

    /***
     * 从url链接地址中获取id(数字)
     * @param input
     * @return
     */
    public static String getDigitFromServletPath(String input) {
        if (ValueWidget.isNullOrEmpty(input)) {
            return null;
        }
        Pattern p = Pattern.compile("^.*/([\\d]+)(/.*)?$", Pattern.DOTALL);
        Matcher m = p.matcher(input);
        String result = m.replaceAll("$1");
        return result;
    }
}
