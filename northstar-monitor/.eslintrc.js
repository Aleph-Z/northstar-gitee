module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: ['plugin:vue/essential', 'eslint:recommended', 'prettier'],
  parserOptions: {
    //更新依赖包后发现无法build，按照教程提升，可以build
    //parser: 'babel-eslint'
    //https://stackoverflow.com/questions/70386909/problem-with-babel-eslint-parsing-error-require-of-es-module
    parser: '@babel/eslint-parser',
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off'
  }
}
