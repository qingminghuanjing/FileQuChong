package com.xiatian.file;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yinhai.xui.tools.MD5Util;

public class DirHandle implements IDirHandle {

	private IDirHandle hd = null;

	public DirHandle() {
		hd = this;
	}

	public DirHandle(IDirHandle h) {
		hd = h;
	};

	public void doFile(File f) {
		if (null != hd) {
			hd.handle(f);
		}
	}

	public DirHandle doDir(String s) {
		File f = new File(s);
		if (f.isFile())
			doFile(f);
		else {
			File[] fs = f.listFiles();
			for (int i = 0, j = fs.length; i < j; i++) {
				if (fs[i].isFile())
					doFile(fs[i]);
				else
					doDir(fs[i].getAbsolutePath());
			}
		}
		return this;
	}

	public String getMd5(File f) {
		long lnSize = f.length();
		long M100 = 1024 * 1024 * 100;
		byte[] b = new byte[3 * 1024 + (int) ((lnSize / M100 + 1) * 10)];//��ȡ�ļ���ǰ2k + ÿ��Ծ100Mȡ10�ֽ� + ���1k����������Md5���жԱȣ����md5��ͬ���ļ���С��ͬ����Ϊ��ͬ
		int i = 0, j, p;
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			j = in.read(b, 0, p = i = 2 * 1024);
			if (lnSize > M100 + 10)
				while (j >= i) {
					in.skip(M100);
					try {
						j = in.read(b, p, p += (i = 10));
					} catch (Exception e) {
						break;
					}
				}
			// in.skip(-(lnSize - 1024));
			// in.read(b, p, 1024);
			return MD5Util.MD5(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

	private Map size = new HashMap();//������ļ���С��ͬ���ļ��ľ���·��
	private Map md5 = new HashMap();//�Ծ���·��Ϊkeyֵ���ļ���MD5Ϊvalue��

	public void doCheck() {
		int i = size.size();
		Iterator it = size.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			List list = (List) en.getValue();
			if (1 < list.size()) {
				String s1 = (String) list.get(0);//�õ�list�е�һ���ļ���·��
				String sMd1 = (String) md5.get(s1);//�õ���һ���ļ���md5ֵ
				for (int x = 1, y = list.size(); x < y; x++) {
					String szFileName = (String) list.get(x);//�õ�list�е�x���ļ��ľ���·��
					if (sMd1.equals(md5.get(szFileName))) {//�ж�md5�Ƿ���ͬ
						System.out.println("@rem ͬ��" + s1 + "\r\ndel \"" + szFileName + "\"");
						/*
						File file = new File(szFileName);
						if(file.exists()){
							file.delete();
						}else{
							System.out.println(szFileName+"������");
						}
						*/
					}
				}
			}
		}
	}

	//�ļ��Ĵ������
	public void handle(File file) {
		long ln = file.length();
		String sz = ln + "";
		List mT = null;
		if (null == (mT = (List) size.get(sz))) {
			size.put(sz, mT = new ArrayList());
		}
		mT.add(file.getAbsolutePath());//��С��ͬ��·���ᱻ�ŵ�ͬһ��list��
		sz = getMd5(file);//������ļ���MD5ֵ
		md5.put(file.getAbsolutePath(), sz);
		// System.out.println("@rem " + sz + " " + file.getAbsolutePath());
	}

	public static void main(String []a)
	{
		new DirHandle().doDir("G:\\�ҵ�ps��Ʒ").doCheck();
	}
}