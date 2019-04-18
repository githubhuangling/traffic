package com.ctf.traffic.util;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import org.xmlpull.v1.*;

/**
 * 基于xmlpull对微信的xml消息做解析
 * @author admin
 *
 */
public class XmlPullParserUtil {

	/**
	 * 参数：
	 *   xml的内容，是放到请求对象的流里面的。
	 *   InputStream?Reader?其实两种都可以，只是为了方便做测试，使用Reader
	 * 返回值：
	 *   方案2：使用Map，不同的消息都是Map，只是Map里面的Key-value对不一样。（采纳）
	 * @return
	 */
	
	public static Map<String,String> parse(Reader reader) {
		Map<String,String> result = new HashMap<>();
		try {
			//1）xmlPull解析器
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			//2)设置解析内容
			parser.setInput(reader);
			//3)解析
			//XmlPullParser.START_DOCUMENT //文档开始
			//XmlPullParser.END_DOCUMENT//文档结束
			//XmlPullParser.START_TAG//标签开始
			//XmlPullParser.END_TAG//标签结束
			//当前事件类型
			int eventType = parser.getEventType();
			
			//文档解析结束时机，文档结束。
			while (eventType != XmlPullParser.END_DOCUMENT) {
				//解析内容的时机 ？标签开始并且标签名不为xml
				//解析内容怎么来？下一个文本
				String tagName = parser.getName();
				if (eventType == XmlPullParser.START_TAG && !"xml".equals(tagName)) {
					String tagContent = parser.nextText();
					result.put(tagName, tagContent);
				}
				
				//像迭代器一样，让它指向下一个事件。并且要把事件类型赋值给控制解析变量
				eventType = parser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		String content ="<xml>"
				+ "<ToUserName><![CDATA[gh_ae9b8f50b1b3]]></ToUserName> "
				+ "<FromUserName><![CDATA[oxLXms6cL0ECuVST7vQiDZdg4RbU]]></FromUserName> "
				+ "<CreateTime>1507706381</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType> "
				+ "<Content><![CDATA[呵呵]]></Content>"
				+ "<MsgId>6475549598927510918</MsgId>"
				+ "</xml>";
		StringReader sb = new StringReader(content);
		Map<String, String> result = parse(sb);
		//怎么遍历Map 
		//方案1：keyset foreach 获取value 不能边遍历边做删除和修改 会报异常。
		/*Set<String> keySet = result.keySet();
		for (String key : keySet) {
			String value = result.get(key);
		}*/
		//方案2：迭代器
		Set<Entry<String, String>> entrySet = result.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			System.out.println(entry.getKey()+"--->"+entry.getValue());
		}
	}
}
