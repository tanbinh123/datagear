/*
 * Copyright 2018 datagear.tech. All Rights Reserved.
 */

package org.datagear.persistence.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.datagear.persistence.PstParamMapperException;
import org.datagear.util.IOUtil;
import org.datagear.util.StringUtil;

/**
 * 文件路径值处理器。
 * <p>
 * 此类用于为{@linkplain DelegationConversionPstParamMapper}支持获取特定件路径字符串所表示的文件及其输入流。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class FilePathValueResolver
{
	/** 文件路径值前缀 */
	public static final String FILE_PATH_VALUE_PREFIX = "file:";

	/** 文件值的字符集 */
	private String fileValueCharset;

	public FilePathValueResolver()
	{
		super();
	}

	public boolean hasFileValueCharset()
	{
		return !StringUtil.isEmpty(this.fileValueCharset);
	}

	public String getFileValueCharset()
	{
		return fileValueCharset;
	}

	public void setFileValueCharset(String fileValueCharset)
	{
		this.fileValueCharset = fileValueCharset;
	}

	/**
	 * 获取指定文件路径值的文件。
	 * 
	 * @param filePathValue
	 * @return 返回{@code null}表示不是文件路径值
	 * @throws PstParamMapperException
	 */
	public File getFileValue(String filePathValue) throws PstParamMapperException
	{
		if (!isFilePathValue(filePathValue))
			return null;

		File file = new File(getFilePathContent(filePathValue));

		return (file.exists() ? file : null);
	}

	/**
	 * 获取文件值输入流。
	 * 
	 * @param value
	 * @return
	 * @throws PstParamMapperException
	 */
	public InputStream getInputStream(File value) throws PstParamMapperException
	{
		try
		{
			return IOUtil.getInputStream(value);
		}
		catch (FileNotFoundException e)
		{
			throw new PstParamMapperException(e);
		}
	}

	/**
	 * 获取文件值输入流。
	 * 
	 * @param value
	 * @return
	 * @throws PstParamMapperException
	 */
	public Reader getReader(File value) throws PstParamMapperException
	{
		try
		{
			return IOUtil.getReader(value, this.fileValueCharset);
		}
		catch (IOException e)
		{
			throw new PstParamMapperException(e);
		}
	}

	/**
	 * 给定值是否是文件路径值。
	 * 
	 * @param value
	 * @return
	 * @throws PstParamMapperException
	 */
	public boolean isFilePathValue(String value) throws PstParamMapperException
	{
		return (value != null && value.startsWith(FILE_PATH_VALUE_PREFIX));
	}

	/**
	 * 获取以{@linkplain #FILE_PATH_VALUE_PREFIX}开头文件路径的路径内容。
	 * 
	 * @param filePathValue
	 * @return
	 */
	protected String getFilePathContent(String filePathValue)
	{
		return filePathValue.substring(FILE_PATH_VALUE_PREFIX.length());
	}
}