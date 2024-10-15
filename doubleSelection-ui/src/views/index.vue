<template>
    <div class="app-container home">

        <el-row :gutter="20">
            <el-col :sm="24" :lg="12" style="padding-left: 20px">
                <h2>学生导师双选系统</h2>
                <p class="paragraph">

                    学生导师双选系统旨在为学生与导师之间提供高效的沟通与匹配平台。<br>
                    系统主要功能包括：<br>
                    1.注册与登录：学生和导师均可通过简单的注册流程创建个人账号，快速登录系统。<br>
                    2.个人信息管理：用户可以方便地查看和更新个人信息，确保信息的准确性和时效性。<br>
                    3.信息查看：学生可以浏览导师信息，了解导师的专业领域和研究方向；导师也可查看学生的基本信息，帮助其做出选择。<br>
                    4.选择功能：学生可以选择合适的导师进行指导，导师也可以根据需求选择合适的学生进行培养。<br>
                    5.推荐系统：系统根据用户的兴趣和需求，智能推荐合适的导师或学生，帮助用户找到最佳匹配。<br>
                    6.管理员活动发布：系统管理员可以发布活动信息，促进学生与导师之间的交流与互动。<br>
                    通过本系统，学生和导师能够更高效地匹配，促进学术交流与成长。
                </p>
            </el-col>

            <el-col :sm="24" :lg="12" style="padding-left: 50px">
                <el-row>
                    <el-col :span="12">
                        <h2>技术选型</h2>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <h4>后端技术</h4>
                        <ul>
                            <li>SpringBoot</li>
                            <li>Spring Security</li>
                            <li>JWT</li>
                            <li>MyBatis</li>
                            <li>Druid</li>
                            <li>Fastjson</li>
                            <li>...</li>
                        </ul>
                    </el-col>
                    <el-col :span="6">
                        <h4>前端技术</h4>
                        <ul>
                            <li>Vue</li>
                            <li>Vuex</li>
                            <li>Element-ui</li>
                            <li>Axios</li>
                            <li>Sass</li>
                            <li>Quill</li>
                            <li>...</li>
                        </ul>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>
        <el-divider />
        <el-row :gutter="20">
            <el-col :xs="24" :sm="24">
                <el-card class="update-log">
                    <div slot="header" class="clearfix introduction">
                        <span> 项目背景</span><br><br>
                        <div style="text-align: left;">
                            长期以来，双选机制在高等教育领域扮演着至关重要的角色，但其固有的缺陷——缺乏透明度，却使得这一过程呈现出一种盲目性。
                            具体体现在两个方面：对于学生而言，他们在选择导师时，往往无法准确获取导师的研究方向、
                            学术成果以及当前的热门程度等信息，这导致他们可能无法根据自己的兴趣和发展方向做出最恰当的选择；
                            而对于教师来说，他们同样无法清晰地了解到自己被学生选择的数量以及学生的专业背景、研究意向等，
                            这也在一定程度上影响了他们的教学规划和指导策略。这种信息不公开、不透明的状态极大地影响了双选过程的效率。
                            鉴于此，本课题旨在信创环境下，依托XBase框架，构建一个专业化的双选系统。该系统将借助数据共享和人工智能分析技术，
                            为师生双方提供决策支持，以优化双选流程，提高匹配效率，从而有效解决当前双选过程中存在的问题。
                        </div>
                    </div>
                </el-card>
            </el-col>
            <!-- <el-col :xs="24" :sm="24" :md="12" :lg="8">
                <el-card class="update-log">
                    <div slot="header" class="clearfix">
                        <span>更新日志</span>
                    </div>

                </el-card>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="8">
                <el-card class="update-log">
                    <div slot="header" class="clearfix">
                        <span>捐赠支持</span>
                    </div>

                </el-card>
            </el-col> -->
        </el-row>
        <el-row :gutter="20">
            <el-col :xs="24" :sm="24">
                <el-card class="update-log">
                    <div slot="header" class="clearfix introduction">
                        <span> 团队分工</span><br><br>
                        <div style="text-align: center;">
                            刘志成：数据库设计，后端开发， 项目部署<br><br>
                            邵永康：前端开发，项目联调测试<br><br>
                            钱洁楠，熊丽安，何枫：需求分析，页面原型，ppt编写<br><br>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <!-- <el-col :xs="24" :sm="24" :md="12" :lg="8">
                <el-card class="update-log">邵永康：前端开发，项目联调测试，刘志成：数据库设计，后端开发，项目部署，钱洁楠，熊丽安，何枫：需求分析，页面原型，ppt编写
                    <div slot="header" class="clearfix">
                        <span>更新日志</span>
                    </div>

                </el-card>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="8">
                <el-card class="update-log">
                    <div slot="header" class="clearfix">
                        <span>捐赠支持</span>
                    </div>

                </el-card>
            </el-col> -->
        </el-row>
    </div>
</template>

<script>
export default {
    name: "Index",
    data() {
        return {
            // 版本号
            version: "3.8.8"
        };
    },
    methods: {
        goTarget(href) {
            window.open(href, "_blank");
        }
    }
};
</script>

<style scoped lang="scss">
.introduction {
    font-size: 14px;
    text-align: center;
}

.paragraph {
    line-height: 2;
    font-size: 14px;
}

.home {
    blockquote {
        padding: 10px 20px;
        margin: 0 0 20px;
        font-size: 17.5px;
        border-left: 5px solid #eee;
    }

    hr {
        margin-top: 20px;
        margin-bottom: 20px;
        border: 0;
        border-top: 1px solid #eee;
    }

    .col-item {
        margin-bottom: 20px;
    }

    ul {
        padding: 0;
        margin: 0;
    }

    font-family: "open sans",
    "Helvetica Neue",
    Helvetica,
    Arial,
    sans-serif;
    font-size: 13px;
    color: #676a6c;
    overflow-x: hidden;

    ul {
        list-style-type: none;
    }

    h4 {
        margin-top: 0px;
    }

    h2 {
        margin-top: 10px;
        font-size: 26px;
        font-weight: 100;
    }

    p {
        margin-top: 10px;

        b {
            font-weight: 700;
        }
    }

    .update-log {
        ol {
            display: block;
            list-style-type: decimal;
            margin-block-start: 1em;
            margin-block-end: 1em;
            margin-inline-start: 0;
            margin-inline-end: 0;
            padding-inline-start: 40px;
        }
    }
}
</style>
