module.exports = {
  // 选项...
  productionSourceMap: false,
  configureWebpack: {
    externals: {
      vue: 'Vue',
      'element-ui': 'ELEMENT'
    }
  },
  devServer: {
	  port: 8088,
    proxy: {
      '/northstar': {
        target: `http://localhost:8088`
      },
      '/redirect*': {
        target: `http://localhost`,
      },
    }
  }
}
