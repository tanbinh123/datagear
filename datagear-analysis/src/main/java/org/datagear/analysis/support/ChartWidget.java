/*
 * Copyright (c) 2018 datagear.tech. All Rights Reserved.
 */

package org.datagear.analysis.support;

import java.util.HashMap;
import java.util.Map;

import org.datagear.analysis.AbstractIdentifiable;
import org.datagear.analysis.Chart;
import org.datagear.analysis.ChartDataSet;
import org.datagear.analysis.ChartPlugin;
import org.datagear.analysis.ChartPluginManager;
import org.datagear.analysis.RenderContext;
import org.datagear.analysis.RenderException;

/**
 * 图表部件。
 * <p>
 * 它可在{@linkplain RenderContext}中渲染自己所描述的{@linkplain Chart}。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class ChartWidget<T extends RenderContext> extends AbstractIdentifiable
{
	/** 图表名称属性名 */
	public static final String CHART_PROPERTY_VALUE_NAME = "name";

	/** 图表更新间隔属性名 */
	public static final String CHART_PROPERTY_VALUE_UPDATE_INTERVAL = "updateInterval";

	/** 图表名称 */
	private String name = "";

	private ChartPlugin<T> chartPlugin;

	private Map<String, ?> chartPropertyValues = new HashMap<String, Object>();

	private ChartDataSet[] chartDataSets = new ChartDataSet[0];

	/** 图表更新间隔毫秒数 */
	private int updateInterval = -1;

	public ChartWidget()
	{
		super();
	}

	public ChartWidget(String id, String name, ChartPlugin<T> chartPlugin, ChartDataSet... chartDataSets)
	{
		super(id);
		this.name = name;
		this.chartPlugin = chartPlugin;
		this.chartDataSets = chartDataSets;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ChartPlugin<T> getChartPlugin()
	{
		return chartPlugin;
	}

	public void setChartPlugin(ChartPlugin<T> chartPlugin)
	{
		this.chartPlugin = chartPlugin;
	}

	public Map<String, ?> getChartPropertyValues()
	{
		return chartPropertyValues;
	}

	public void setChartPropertyValues(Map<String, ?> chartPropertyValues)
	{
		this.chartPropertyValues = chartPropertyValues;
	}

	/**
	 * 添加图表属性值。
	 * 
	 * @param name
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void addChartPropertyValue(String name, Object value)
	{
		((Map<String, Object>) this.chartPropertyValues).put(name, value);
	}

	public ChartDataSet[] getChartDataSets()
	{
		return chartDataSets;
	}

	public void setChartDataSets(ChartDataSet[] chartDataSets)
	{
		this.chartDataSets = chartDataSets;
	}

	/**
	 * 获取图表更新间隔毫秒数。
	 * 
	 * @return {@code <0}：不间隔更新；0 ：实时更新；{@code >0}：间隔更新毫秒数
	 */
	public int getUpdateInterval()
	{
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval)
	{
		this.updateInterval = updateInterval;
	}

	/**
	 * 从{@linkplain ChartPluginManager}查找并设置{@linkplain #setChartPlugin(ChartPlugin)}。
	 * 
	 * @param chartPluginManager
	 * @param chartPluginId
	 * @return
	 */
	public void setChartPlugin(ChartPluginManager chartPluginManager, String chartPluginId)
	{
		ChartPlugin<T> chartPlugin = chartPluginManager.get(chartPluginId);
		setChartPlugin(chartPlugin);
	}

	/**
	 * 渲染{@linkplain Chart}。
	 * 
	 * @param renderContext
	 * @return
	 * @throws RenderException
	 */
	public Chart render(T renderContext) throws RenderException
	{
		inflateInternalChartPropertyValues();

		return this.chartPlugin.renderChart(renderContext, this.chartPropertyValues, this.chartDataSets);
	}

	/**
	 * 设置内置的图表属性。
	 * 
	 * @param propertyValues
	 */
	protected void inflateInternalChartPropertyValues()
	{
		addChartPropertyValue(CHART_PROPERTY_VALUE_NAME, this.name);
		addChartPropertyValue(CHART_PROPERTY_VALUE_UPDATE_INTERVAL, this.updateInterval);
	}
}