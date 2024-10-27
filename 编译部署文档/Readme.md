> [访问信创环境项目](http://172.16.14.211),需要连vpn
# 项目打包构建
## 后端打包
>该项目基于maven构建，使用maven-assembly-plugin插件打包。
1. 确保jdk版本为1.8,maven版本3.5以上。
2. 将项目在idea中打开，将项目添加为maven项目，加载完maven依赖。
3. 使用maven插件构建打包项目。
4. 在doubleSelection-admin/target/文件夹中的doubleSelection-admin.jar即为可执行jar包。

## 前端打包
>项目基于vue-cli构建，使用vue-cli打包。
1. node版本为20.15.1
2. 将项目根目录vscode打开，在vscode的终端输入`npm install`
3. `npm build`进行打包，生成dist文件即为打包好的文件夹

# 项目部署
## 本机部署
1. 准备好mysql和redis，创建数据库并运行sql文件夹下的sql脚本。
2. 修改项目配置文件application.yml，指定mysql和redis连接信息。
3. 直接运行启动类，或者使用maven插件打包成jar包，运行jar包。
4. 前端doubleSelection-ui运行npm install，npm run build，直接运行在本地，访问http://localhost:80

## linux服务器部署
1. 在虚拟机准备好mysql和redis，创建数据库并运行sql文件夹下的sql脚本。
2. 修改项目配置文件application.yml，指定mysql和redis连接信息。
3. 将后端打包部署到服务器。
4. 前端修改后端请求地址，打包部署到服务器并运行。
5. 访问http://你的服务器ip地址:80