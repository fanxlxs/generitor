package com.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
/**
 * 自动生成实体类,mapper
 * 自动生成table和json
 * @author fxl
 *
 */
public class Test {

	public static void main(String[] args) throws IOException {
		
		//自动生成文件 po mapper 
		String xmlPath = "H:\\project\\zz\\generate-test\\src\\main\\resources\\generatorConfig.xml";
		generitor(xmlPath);
		
		//读取实体类字段
		String str="F:\\generator\\generator\\src\\cn\\po\\DhCustody.java";
		String fileStr=writeInFile(str);
		//保存到map中
		Map<String,String> map=analysis(fileStr);
		//生成html页面,生成json文件
		produceHtml(map);
		
	}
	
	/**
	 *反向工程生成对应的mapper po等文件 
	 * @param xmlPath
	 */
	private static void generitor(String xmlPath) {
		   try {
		        List<String> warnings = new ArrayList<String>();
		        boolean overwrite = true;
		        File configFile = new File(xmlPath);
		        ConfigurationParser cp = new ConfigurationParser(warnings);
		        Configuration config = cp.parseConfiguration(configFile);
		        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		        myBatisGenerator.generate(null);
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    } catch (InvalidConfigurationException e) {
		        e.printStackTrace();
		    } catch (XMLParserException e) {
		        e.printStackTrace();
		    }
	}
	
	/**
	 * 读取文件工具类 
	 * @param file
	 * @return 文件字符串
	 * @throws IOException
	 */
	private static String writeInFile(String file) throws IOException{
		 File entityFile = new File(file);
		 String str = "";
		 String count = "";
		 try {
             // 使用字符流对文件进行读取
			 BufferedReader bf = new BufferedReader(new FileReader(entityFile));
			 while (true) {
                //读取每一行数据并将其赋值给str
                if ((count = bf.readLine()) != null) {
                     str += count;
                } else {
                    break;
                }
            }
            // 关闭流
            bf.close();
       } catch (FileNotFoundException e) {
            e.printStackTrace();
       }
         return str;
    }
	
	/**
	 *利用正则表达式 解析实体类代码
	 **/
	private static Map<String,String> analysis(String str) {
		//map<字段,中文注释>
		Map<String,String> map=new HashMap<>();
		//Pattern pattern = Pattern.compile("private [A-Z][a-z]{0,} ([a-zA-Z]{0,})");
		Pattern pattern = Pattern.compile("\\s{0,}/\\*\\*\\s{0,}\\*\\s{0,}([A-Za-z\\u4e00-\\u9fa5]{0,})\\s{0,}\\*/\\s{0,}private\\s{0,}[A-Za-z]{0,}\\s{0,}([A-Za-z]{0,})");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()) {
        	//分组二字段，分组一注释
        	map.put(matcher.group(2), matcher.group(1));
        }
		return map;
	}
	
	/**
	 * 生成table
	 * @param map
	 */
	private static void produceHtml(Map<String,String> map) {
		//拼接table的结构
		StringBuffer table= new StringBuffer();
		String tr="<tr>";
		String endTr="</tr>";
		table.append(tr);
		table.append("\n");
		//循环读取字段值和注释，拼接成th
		for(String key:map.keySet()) {
			String th="<th width='10%' data-options='filed:"+key+"' data-i18n='formLable:"+key+"'><!--"+map.get(key)+"--></th>";
			table.append(th+"\n");
		}
		table.append(endTr);
		System.out.println("start"+table+"end");
		
		//拼接table的结构 
		StringBuffer json= new StringBuffer();
		json.append("{"+"\n"+"'queryLable':{"+"\n"+"},"+"\n"+"'formLabel':{"+"\n");
		//循环相应的字段和注释并按照json 的结构进行拼接
		for(String key:map.keySet()) {
			String str="'"+key+"':"+"'"+map.get(key)+"',"+"\n";
			json.append(str);
		}
		json.deleteCharAt(json.length() - 2);
		json.append("\n"+"}"+"\n"+"}");
		System.out.println(json);
	}


	
}
