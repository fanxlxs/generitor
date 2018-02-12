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
 * �Զ�����ʵ����,mapper
 * �Զ�����table��json
 * @author fxl
 *
 */
public class Test {

	public static void main(String[] args) throws IOException {
		
		//�Զ������ļ� po mapper 
		String xmlPath = "H:\\project\\zz\\generate-test\\src\\main\\resources\\generatorConfig.xml";
		generitor(xmlPath);
		
		//��ȡʵ�����ֶ�
		String str="F:\\generator\\generator\\src\\cn\\po\\DhCustody.java";
		String fileStr=writeInFile(str);
		//���浽map��
		Map<String,String> map=analysis(fileStr);
		//����htmlҳ��,����json�ļ�
		produceHtml(map);
		
	}
	
	/**
	 *���򹤳����ɶ�Ӧ��mapper po���ļ� 
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
	 * ��ȡ�ļ������� 
	 * @param file
	 * @return �ļ��ַ���
	 * @throws IOException
	 */
	private static String writeInFile(String file) throws IOException{
		 File entityFile = new File(file);
		 String str = "";
		 String count = "";
		 try {
             // ʹ���ַ������ļ����ж�ȡ
			 BufferedReader bf = new BufferedReader(new FileReader(entityFile));
			 while (true) {
                //��ȡÿһ�����ݲ����丳ֵ��str
                if ((count = bf.readLine()) != null) {
                     str += count;
                } else {
                    break;
                }
            }
            // �ر���
            bf.close();
       } catch (FileNotFoundException e) {
            e.printStackTrace();
       }
         return str;
    }
	
	/**
	 *����������ʽ ����ʵ�������
	 **/
	private static Map<String,String> analysis(String str) {
		//map<�ֶ�,����ע��>
		Map<String,String> map=new HashMap<>();
		//Pattern pattern = Pattern.compile("private [A-Z][a-z]{0,} ([a-zA-Z]{0,})");
		Pattern pattern = Pattern.compile("\\s{0,}/\\*\\*\\s{0,}\\*\\s{0,}([A-Za-z\\u4e00-\\u9fa5]{0,})\\s{0,}\\*/\\s{0,}private\\s{0,}[A-Za-z]{0,}\\s{0,}([A-Za-z]{0,})");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()) {
        	//������ֶΣ�����һע��
        	map.put(matcher.group(2), matcher.group(1));
        }
		return map;
	}
	
	/**
	 * ����table
	 * @param map
	 */
	private static void produceHtml(Map<String,String> map) {
		//ƴ��table�Ľṹ
		StringBuffer table= new StringBuffer();
		String tr="<tr>";
		String endTr="</tr>";
		table.append(tr);
		table.append("\n");
		//ѭ����ȡ�ֶ�ֵ��ע�ͣ�ƴ�ӳ�th
		for(String key:map.keySet()) {
			String th="<th width='10%' data-options='filed:"+key+"' data-i18n='formLable:"+key+"'><!--"+map.get(key)+"--></th>";
			table.append(th+"\n");
		}
		table.append(endTr);
		System.out.println("start"+table+"end");
		
		//ƴ��table�Ľṹ 
		StringBuffer json= new StringBuffer();
		json.append("{"+"\n"+"'queryLable':{"+"\n"+"},"+"\n"+"'formLabel':{"+"\n");
		//ѭ����Ӧ���ֶκ�ע�Ͳ�����json �Ľṹ����ƴ��
		for(String key:map.keySet()) {
			String str="'"+key+"':"+"'"+map.get(key)+"',"+"\n";
			json.append(str);
		}
		json.deleteCharAt(json.length() - 2);
		json.append("\n"+"}"+"\n"+"}");
		System.out.println(json);
	}


	
}
