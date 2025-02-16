> [访问信创环境项目](http://172.16.14.211),需要连vpn
# 技术文档
## 1. 项目技术选型
### 1.1 前端技术选型
- vue2
- element-ui
- vue-router
- axios

### 1.2 后端技术选型
- springboot
- mybatis
- redis
- mysql
- springsecurity
- jwt

## 2.项目细节难点技术方案QA
1. 如何实现导师和学生访问不同界面，权限控制？
    使用RABC权限模型，创建超级管理员，学生和导师三种角色，并对角色赋予不同的菜单和资源访问权限，在用户注册时关联用户的角色从而获取角色对应的权限。
2. 如何实现导师推荐？
    根据学生的感兴趣方向和导师的研究方向做相似度计算排名，由于研究方向通常是短文本，因此舍弃了大模型调用，节省存储和开发成本加快计算速度，使用Jaccard相似度计算，将字符串全部分割放入set，将两个set的交集大小除并集大小计算相似度排名。
3. 导师推荐是基于查询所有导师计算相似度排名，那么如何解决响应速度慢的问题？
    由于此处查数据库和计算都是同步调用，因此只能采用缓存解决每次请求的响应速度问题。此处采用redis将计算出的导师推荐排名缓存，提高查询速度，避免每次都需查数据库计算。由于系统不支持用户修改基础信息，因此影响排行的只有导师数量和导师的剩余名额，故当新注册导师和导师完成双选时进行删除导师推荐缓存保证缓存和数据库的一致性。
4. 为什么不支持修改个人信息？
    当双选活动正在进行时，如果贸然修改学生和导师的信息，将影响学生和导师的选择以及导师推荐的排名，如：当导师剩下最后一个名额，有多人只选了这个导师，这个导师把突然把名额改少了一个，这学生不得气死。而且信息改动需要删除推荐缓存，重新计算排名，如频繁改动将导致导师推荐速度大大下降。
5. 怎么解决个人信息修改问题？
    每年的第一天触发springTask定时任务删除所有用户信息，防止上年度的用户数据和双选数据遗留到当前年度，导师和学生可以重新注册账号进行新年度的双选，由此解决了导师用户信息修改问题，个人信息每年可以重新编辑注册。
6. 如何防止上一次活动的选择记录遗留到下次活动？
    每天凌晨触发springTask定时任务如果发现活动结束则清除所有无效选择（被拒绝和导师没回应的
7. 导师学生是否可以实时聊天
    no，导师学生只是发留言到对方的留言箱中，并且字数不可超过100字，不能够实时聊天

## 3.项目后续优化方向
1. 引入大模型，根据研究方向，专业，个人简介等全方位借助大模型进行相似度计算排名
2. 将查询导师推荐同步请求改成mq异步请求，解耦防止线程资源损耗导致系统瘫痪，防止线程堵塞
3. 整合netty框架将导师留言改成im即时通讯，导师和学生可以在平台直接畅通交流
4. 优化界面ui，为用户设计更加精美的界面，提升用户体验
5. 增加手机号登录和微信扫码登录，加快注册流程，扩展登录方式

> 项目打包部署参考->编译部署文档/Readme.md