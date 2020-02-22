# MyDES
以javafx为界面的DES加密程序
>可对中英文字符串进行加密解密，同时可以对txt、jpg、MP3、MP4格式的文件进行加密解密

![image](https://github.com/cwhongtop/MyDES/blob/master/image/Screenshot.png)

### 编译
```
$ javac -cp classes -d classes DEScrypto.java
```

### 运行
```
$ java -cp classes mydes.DEScrypto
```

### 打包成jar
```
$ cd classes
$ jar -cef mydes.DEScrypto DEScrypto.jar mydes
```

### 运行jar
```
$ java -jar DEScrypto.jar
```

### 运行sh
```
$ ./DEScrypto
```
