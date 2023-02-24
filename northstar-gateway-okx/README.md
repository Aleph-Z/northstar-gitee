# Northstar-OKX模块

## 开发记录

2023-02-23

1. common模块新增币圈使用的枚举定义
 ```
  proto生成命令
   生成w3CoreEnum:
   protoc --java_out=D:\opso-worspace\northstar\northstar-common\src\main\java  .\w3\exchange\pb\w3_core_enum.proto 
   生成w3CoreField:
   protoc --java_out=D:\opso-worspace\northstar\northstar-common\src\main\java --proto_path=. .\w3\exchange\pb\w3_core_enum.proto .\w3\exchange\pb\w3_core_field.proto
```
2. okx网关模块初始化

