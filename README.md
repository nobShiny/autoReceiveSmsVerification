这是一个自动接受并识别短信验证码的功能，工程内包括两种方式，一种是通过接受系统广播的方式，一种是利用了观察者模式处理短信验证码，推荐使用第二种。

	文件目录如下：
		app\src\main\java\com\lsj\messagetext
			-AutoGetMeassageReceiver.java 接收短信验证码并处理的广播接收者（不推荐）
			-MainActivity.java 主activity，接受短息验证码并显示
			-SmsObserver.java  接收短信验证码并处理的内容观察者（推荐）

使用时按照MainActivity的形式，加入自己的逻辑即可。