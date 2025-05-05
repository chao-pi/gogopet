<template>
    <div class="metric-card" :style="{ '--color': color }">
      <div class="icon-container">
        <slot name="icon">
          <div class="default-icon">
            {{ iconMap[icon] || '📊' }}
          </div>
        </slot>
      </div>
      <div class="content">
        <div class="title">{{ title }}</div>
        <div class="value">{{ value }}</div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'MetricCard',
    props: {
      title: {
        type: String,
        required: true
      },
      value: {
        type: [String, Number],
        required: true
      },
      icon: {
        type: String,
        default: 'chart'
      },
      color: {
        type: String,
        default: '#36a2eb'
      }
    },
    data() {
      return {
        iconMap: {
          order: '🐾',
          like: '👍',
          company: '🏢',
        }
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .metric-card {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
    transition: all 0.3s ease;
    height: 100%;
    border-top: 3px solid var(--color);
  
    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
    }
  
    .icon-container {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      background-color: rgba(var(--color-rgb), 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      flex-shrink: 0;
  
      .default-icon {
        font-size: 24px;
        color: var(--color);
      }
    }
  
    .content {
      flex: 1;
      
      .title {
        font-size: 14px;
        color: #666;
        margin-bottom: 4px;
      }
      
      .value {
        font-size: 22px;
        font-weight: 600;
        color: #333;
      }
    }
  }
  
  // 动态计算颜色透明度
  .metric-card {
    --color-rgb: calc(red(var(--color))), calc(green(var(--color))), calc(blue(var(--color)));
  }
  </style>