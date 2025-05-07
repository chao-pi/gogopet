<template>
    <div class="analysis-page">
      <!-- 顶部标题区 -->
      <div class="header">
        <h1 class="title">宠物托运数据分析中心</h1>
        <p class="subtitle">实时监控业务关键指标</p>
      </div>
  
      <!-- 图表展示区 -->
      <div class="dashboard">
        <!-- 第一行：关键指标 -->
        <div class="row metrics-row">
          <metric-card 
            v-for="(metric, index) in metrics" 
            :key="index"
            :title="metric.title"
            :value="metric.value"
            :trend="metric.trend"
            :icon="metric.icon"
            :color="metric.color"
          />
        </div>
  
        <!-- 主要图表区域 -->
        <div class="main-charts">
          <!-- 左侧大图表 -->
          <div class="main-chart-left">
            <chart-card title="公司订单量排行" height="623px">
              <div ref="orderChart" class="chart"></div>
            </chart-card>
          </div>
          
          <!-- 右侧两个小图表 -->
          <div class="main-chart-right">
            <chart-card title="运输方式分布" height="300px">
              <div ref="transportMethodChart" class="chart"></div>
            </chart-card>
            <chart-card title="订单状态总览" height="300px">
              <div ref="statusChart" class="chart"></div>
            </chart-card>
          </div>
        </div>
  
        <!-- 底部图表区域 -->
        <div class="bottom-charts">
          <div class="bottom-chart-item">
            <chart-card title="宠物状态统计" height="380px">
              <div ref="petStatusChart" class="chart"></div>
            </chart-card>
          </div>
          <div class="bottom-chart-item">
            <chart-card title="用户评价关键词" height="380px">
              <div ref="commentWordCloud" class="chart"></div>
            </chart-card>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue'
  import * as echarts from 'echarts'
  import 'echarts-wordcloud'
  import { getAnalysisData } from '@/api/analysis'
  import ChartCard from '@/components/analysis/ChartCard.vue'
  import MetricCard from '@/components/analysis/MetricCard.vue'
  
  const analysis = ref([])
  
  export default {
    name: 'Analysis',
    components: { ChartCard, MetricCard },
    setup() {
      const orderChart = ref(null)
      const statusChart = ref(null)
      const transportMethodChart = ref(null)
      const petStatusChart = ref(null)
      const commentWordCloud = ref(null)
  
      // 设置关键指标数据
      const metrics = ref([
        {
          title: '总订单量',
          value: '0',
          icon: 'order',
          color: '#36a2eb'
        },
        {
          title: '平台好评率',
          value: '0%',
          icon: 'like',
          color: '#4bc0c0'
        },
        {
          title: '活跃公司',
          value: '0',
          icon: 'company',
          color: '#ff9f40'
        }
      ])
  
      // 初始化图表
      const initCharts = async () => {
        try {
          const response = await getAnalysisData()
          analysis.value = response
          const analysisData = response.data
  
          // 更新关键指标数据
          metrics.value = [
            {
              title: '总订单量',
              value: analysisData.totalOrders.toString(),
              icon: 'order',
              color: '#36a2eb'
            },
            {
              title: '平台好评率',
              value: `${analysisData.goodRate}%`,
              icon: 'like',
              color: '#4bc0c0'
            },
            {
              title: '活跃公司',
              value: analysisData.activeCompanies.toString(),
              icon: 'company',
              color: '#ff9f40'
            }
          ]
          
          // 柱状图配置
          const initBarChart = (ref, data, nameKey, valueKey) => {
            const instance = echarts.init(ref.value)
            instance.setOption({
              grid: {
                containLabel: true,
                left: '3%',
                right: '4%',
                bottom: '3%',
                top: '10%'
              },
              tooltip: {
                trigger: 'axis',
                axisPointer: { type: 'shadow' }
              },
              xAxis: {
                type: 'category',
                data: data.map(item => item[nameKey]),
                axisLabel: {
                  rotate: 30,
                  interval: 0
                }
              },
              yAxis: { type: 'value' },
              series: [{
                name: '数量',
                type: 'bar',
                data: data.slice(0, 10).map(item => item[valueKey]),
                itemStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#83bff6' },
                    { offset: 1, color: '#188df0' }
                  ])
                },
                emphasis: {
                  itemStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                      { offset: 0, color: '#2378f7' },
                      { offset: 1, color: '#093d9e' }
                    ])
                  }
                }
              }]
            })
            return instance
          }
  
          // 饼图配置
          const initPieChart = (ref, data, nameKey, valueKey, options = {}) => {
            const instance = echarts.init(ref.value)
            const mergedOptions = {
              tooltip: { trigger: 'item' },
              legend: {
                orient: 'vertical',
                right: 0,
                top: 'top'
              },
              series: [{
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                  borderRadius: 10,
                  borderColor: '#fff',
                  borderWidth: 2
                },
                label: {
                  show: false
                },
                data: data.map(item => ({
                  name: item[nameKey],
                  value: item[valueKey]
                }))
              }]
            }
  
            instance.setOption(mergedOptions)
            return instance
          }
  
          // 旭日图配置
          const initSunburstChart = (ref, data, nameKey, valueKey) => {
            const instance = echarts.init(ref.value)
            
            // 使用 ECharts 经典配色
            const echartClassicColors = [
              '#3f72af', '#3fc1c9', '#eaeaea','#364f6b', '#f9ed69', '#ff9a00', '#f07b3f'
            ];
  
            // 构建旭日图数据
            const sunburstData = {
              name: '总计',
              color: echartClassicColors[0],
              value: data.reduce((sum, item) => sum + item[valueKey], 0),
              children: data.map((item, index) => ({
                name: item[nameKey],
                value: item[valueKey],
                itemStyle: {
                  color: echartClassicColors[(index % (echartClassicColors.length - 1)) + 1] // 子节点循环配色
                }
              }))
            }
  
            instance.setOption({
              tooltip: {
                trigger: 'item',
                formatter: '{b}: {c}'
              },
              series: [{
                type: 'sunburst',
                data: [sunburstData],
                radius: ['0%', '90%'],
                label: {
                  rotate: 'radial',
                  color: '#333'
                },
                levels: [
                  {},
                  {
                    r0: '15%',
                    r: '45%',
                    itemStyle: {
                      borderWidth: 2
                    },
                    label: {
                      rotate: 'tangential'
                    }
                  },
                  {
                    r0: '45%',
                    r: '80%',
                    label: {
                      align: 'right'
                    }
                  }
                ],
                emphasis: {
                  focus: 'ancestor'
                }
              }]
            })
            return instance
          }

          // 仪表盘配置
          const initGaugeChart = (ref, data, nameKey, valueKey) => {
            const instance = echarts.init(ref.value)
            
            // 计算正常率
            const total = data.reduce((sum, item) => sum + item[valueKey], 0)
            const normalCount = data.find(item => item[nameKey] === '正常')?.count || 0
            const normalRate = total > 0 ? (normalCount / total * 100).toFixed(1) : 0
  
            instance.setOption({
              tooltip: {
                formatter: '{a} <br/>{b} : {c}%'
              },
              series: [{
                name: '正常率',
                type: 'gauge',
                min: 0,
                max: 100,
                progress: {
                  show: true,
                  width: 18
                },
                axisLine: {
                  lineStyle: {
                    width: 18
                  }
                },
                axisTick: {
                  show: false
                },
                splitLine: {
                  length: 15,
                  lineStyle: {
                    width: 2,
                    color: '#999'
                  }
                },
                axisLabel: {
                  color: '#999',
                  distance: 25,
                  fontSize: 14
                },
                pointer: {
                  icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
                  length: '12%',
                  width: 20,
                  offsetCenter: [0, '-60%'],
                  itemStyle: {
                    color: 'inherit'
                  }
                },
                detail: {
                  valueAnimation: true,
                  formatter: '{value}%',
                  color: 'inherit',
                  fontSize: 20,
                  offsetCenter: [0, '0%']
                },
                data: [{
                  value: normalRate,
                  name: '正常率'
                }]
              }]
            })
            return instance
          }

          // 词云图配置
          const initWordCloudChart = (ref, data, nameKey, valueKey) => {
            const instance = echarts.init(ref.value)
            instance.setOption({
              backgroundColor: '#f8f9fa',
              series: [{
                type: 'wordCloud',
                shape: 'circle',
                left: 'center',
                top: 'center',
                width: '90%',
                height: '90%',
                sizeRange: [14, 60],
                rotationRange: [0, 0],
                gridSize: 12,
                drawOutOfBound: false,
                textStyle: {
                  fontFamily: 'Microsoft YaHei',
                  fontWeight: 'bold',
                  color: function () {
                    return `rgb(${[
                      Math.round(Math.random() * 155 + 100),
                      Math.round(Math.random() * 155 + 100),
                      Math.round(Math.random() * 155 + 100)
                    ].join(',')})`
                  }
                },
                data: data.slice(0, 50).map(item => ({
                  name: item.word,
                  value: item.value
                }))
              }]
            })
          }
  
          // 初始化各图表
          const instances = {
            order: initBarChart(orderChart, analysisData.companyOrderCounts, 'userName', 'count'),
            status: initSunburstChart(statusChart, analysisData.orderStatusCounts, 'orderStatus', 'count'),
            transportMethod: initPieChart(transportMethodChart, analysisData.transportMethodCounts, 'transportMethod', 'count'),
            petStatus: initGaugeChart(petStatusChart, analysisData.petStatusCounts, 'petStatus', 'count'),
            commentWordCloud: initWordCloudChart(commentWordCloud, analysisData.commentWords, 'word', 'value')
          }
  
          // 窗口大小变化时重绘图表
          window.addEventListener('resize', () => {
            Object.values(instances).forEach(instance => instance?.resize())
          })
          return instances
        } catch (error) {
          console.error('图表初始化失败:', error)
        }
      }
  
      onMounted(initCharts)
  
      return {
        orderChart,
        statusChart,
        transportMethodChart,
        petStatusChart,
        commentWordCloud,
        metrics
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .analysis-page {
    padding: 2rem;
    background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%);
    min-height: calc(100vh - 60px);
    
    .header {
      margin-bottom: 24px;
      .title {
        font-size: 24px;
        font-weight: 600;
        color: #1a1a1a;
        margin-bottom: 8px;
      }
      .subtitle {
        font-size: 14px;
        color: #666;
      }
    }
  
    .dashboard {
      max-width: 1100px;
      margin: 0 auto;
      padding: 0 24px;
      
      .metrics-row {
        display: flex;
        flex-wrap: wrap;
        gap: 24px;
        margin-bottom: 24px;
      }
      
      .main-charts {
        display: flex;
        gap: 24px;
        margin-bottom: 24px;
        width: 100%;
        
        .main-chart-left {
          flex: 0 0 66.666%;
          max-width: 66.666%;
        }
        
        .main-chart-right {
          flex: 0 0 33.333%;
          max-width: 33.333%;
          display: flex;
          flex-direction: column;
          gap: 24px;
        }
      }
      
      .bottom-charts {
        display: flex;
        gap: 24px;
        width: 100%;
        
        .bottom-chart-item {
          flex: 0 0 50%;
          max-width: 50%;
        }
      }
    }
  
    .chart {
      width: 100%;
      height: 100%;
      min-height: 200px;
      position: relative;
    }
  }
  
  @media (max-width: 1200px) {
    .analysis-page {
      .dashboard {
        padding: 0 16px;
        
        .metrics-row {
          gap: 16px;
        }
        
        .main-charts {
          flex-direction: column;
          
          .main-chart-left,
          .main-chart-right {
            flex: 0 0 100%;
            max-width: 100%;
          }
        }
        
        .bottom-charts {
          flex-direction: column;
          
          .bottom-chart-item {
            flex: 0 0 100%;
            max-width: 100%;
          }
        }
      }
    }
  }
  
  @media (max-width: 768px) {
    .analysis-page {
      padding: 16px;
      
      .dashboard {
        padding: 0 12px;
        
        .metrics-row {
          gap: 12px;
        }
      }
    }
  }
  </style>