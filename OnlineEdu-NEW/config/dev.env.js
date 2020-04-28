'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_API: '"http://127.0.0.1:8080"',
  OSS_PATH:'"https://harry-file.oss-cn-beijing.aliyuncs.com/"',
  OSS_API:'"http://127.0.0.1:8130/admin"'
})
