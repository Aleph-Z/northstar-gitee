# 注意：防止文件访问权限问题,请先在Power shell 执行
# set-executionpolicy remotesigned
# 选Y

# 检查JDK环境
Add-Type -AssemblyName System.IO
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
#下载到D盘northstar_env目录，如无该目录则创建
$path = "D:\\northstar_env"
If(!(test-path $path))
{
      New-Item -ItemType Directory -Force -Path $path
}

#JDK17下载地址
$JDK17DownloadUrl = "https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe"
#Node14下载地址
$Node14DownloadUrl = "https://registry.npmmirror.com/-/binary/node/latest-v14.x/node-v14.19.0-x64.msi"
#MavenDownloadUrl下载地址
$MavenDownloadUrl = "https://mirrors.bfsu.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz"
#MongoDB下载地址
$MongoDownloadUrl = "https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe"

# Get-CimInstance -Class Win32_Product |
#     Format-List -Property *

"Start download JDK17..."
#下载的文件名
$FileName = "jdk-17_windows-x64_bin.exe"
$FullPath = "$path\$fullFileName"
Invoke-WebRequest -Uri $JDK17DownloadUrl -OutFile $FullPath
# Download $JDK17DownloadUrl $FileName
"Download JDK17 finished..."

"Start install JDK17..."
Invoke-CimMethod -ClassName Win32_Product -MethodName Install -Arguments @{PackageLocation=$FullPath}
"Install JDK17 finished..."

