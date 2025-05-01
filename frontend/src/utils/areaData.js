import { provinceAndCityData, regionData, CodeToText } from 'china-area-data'

// 省份数据
export const provinces = provinceAndCityData.map(item => ({
  value: item.code,
  label: item.name
}))

// 城市数据
export const cities = {}
provinceAndCityData.forEach(province => {
  cities[province.code] = province.children.map(city => ({
    value: city.code,
    label: city.name
  }))
})

// 区县数据
export const districts = {}
regionData.forEach(region => {
  districts[region.code] = region.children.map(district => ({
    value: district.code,
    label: district.name
  }))
})

// 导出编码转文本的工具函数
export { CodeToText } 