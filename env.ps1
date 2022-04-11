# 注意：防止文件访问权限问题,请先在Power shell 执行
# set-executionpolicy remotesigned
# 选Y

Add-Type -AssemblyName System.IO
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
#下载到D盘northstar_env目录，如无该目录则创建
$path = "D:\\northstar_env\"
If(!(test-path $path))
{
   New-Item -Path $path -ItemType Directory
}

#JDK17下载地址
$JDK17DownloadUrl = "https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe"
#Node14下载地址
$Node14DownloadUrl = "https://registry.npmmirror.com/-/binary/node/latest-v14.x/node-v14.19.0-x64.msi"
#MongoDB下载地址
$MongoDownloadUrl = "http://downloads.mongodb.org/win32/mongodb-win32-x86_64-2008plus-ssl-4.0.22-signed.msi"

#下载JDK17的文件,,并安装
$FileName = "jdk-17_windows-x64_bin.exe"
If(!(test-path "D:\\northstar_env\$FileName"))
{
    "Start download JDK17..."
    Invoke-WebRequest -Uri $JDK17DownloadUrl -OutFile "D:\\northstar_env\$FileName"
    "Download JDK17 finished..."
    "Start Install JDK17..."
    msiexec.exe /i "D:\\northstar_env\$FileName" /qr
    "Install JDK17 finished..."
}

#下载Node的文件,并安装
$FileName = "node-v14.19.0-x64.msi"
If(!(test-path "D:\\northstar_env\$FileName"))
{
    "Start download Node..."
    Invoke-WebRequest -Uri $Node14DownloadUrl -OutFile "D:\\northstar_env\$FileName"
    "Download Node finished..."
    "Start Install node..."
    msiexec.exe /i "D:\\northstar_env\$FileName" /qr
    "Install node finished..."
}

#下载Mongodb的文件,并安装
$FileName = "mongodb-win32-x86_64-2008plus-ssl-4.0.22-signed.msi"
If(!(test-path "D:\\northstar_env\$FileName"))
{
    "Start download Mongo..."
    Invoke-WebRequest -Uri $MongoDownloadUrl -OutFile "D:\\northstar_env\$FileName"
    "Download MongoDB finished..."
    "Start Install mongodb..."
     msiexec.exe /i "D:\\northstar_env\$FileName" /qr
    "Install mongodb finished..."
}
