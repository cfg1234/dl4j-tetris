环境要求：
1.JDK1.8或者更高版本（已经设置好JAVA_HOME和PATH环境变量）
2.maven 3.3.9或者更高版本（已经设置好PATH环境变量）

编译：
在dl4j-tetris目录下，Linux下运行./compile.sh编译源码（Windows下运行compile.bat）

运行：
1.浏览器打开http://yx.h5uc.com/eluosifangkuai/ ，进入俄罗斯放开游戏界面
2.进入dl4j-tetris目录
3.Linux下运行./run-tetris-area-setter.sh设置俄罗斯方块的游戏区域（Windows下运行run-tetris-area-setter.bat），设置方法参考视频set-tetris-area.mp4（设置完成后不要随意移动浏览器窗体！）
4.打开config.prop配置一些额外的属性（默认不需要配置，可以跳过此步）
5.Linux下运行./run-player.sh（Windows下运行run-player.bat），等待程序初始化完成，然后启动俄罗斯方块的游戏进程（参考视频run-player.mp4）
