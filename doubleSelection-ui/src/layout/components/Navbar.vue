<template>
    <div class="navbar">
        <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
            @toggleClick="toggleSideBar" />

        <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav" />
        <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav" />

        <div class="right-menu">
            <template v-if="device !== 'mobile'">
                <!-- <search id="header-search" class="right-menu-item" /> -->

                <!-- <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip> -->

                <!-- <el-tooltip content="文档地址" effect="dark" placement="bottom">
                    <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
                </el-tooltip> -->
                <el-button icon="el-icon-chat-line-round" type="text" style="font-size: 28px;"
                    class="right-menu-item hover-effect" @click="dialogTableVisible = true"></el-button>



                <el-dialog title="用户留言" :visible.sync="dialogTableVisible">
                    <el-button size="small" type="text" style="text-decoration: underline;margin-left: 300px;"
                        @click="handleDelete()">删除全部已读消息</el-button>
                    <el-table :data="messagelist">
                        <el-table-column property="sentTime" label="时间" width="150"></el-table-column>
                        <el-table-column property="senderName" label="姓名" width="100"></el-table-column>
                        <el-table-column property="messageContent" label="信息"></el-table-column>
                        <el-table-column property="isRead" label="状态">
                            <template slot-scope="scope">
                                <span v-if="scope.row.isRead" style="color:#008000;">已读</span>
                                <span v-else style="color: #D3D3D3;">未读</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                            <template slot-scope="scope">

                                <el-button size="small" type="text" @click="handleMessageDetail(scope.row.messageId)"
                                    style="text-decoration: underline;">详情</el-button>
                                <el-button size="small" type="text" @click="dialogVisible2 = true"
                                    style="text-decoration: underline;">回复</el-button>

                                <el-dialog title="发送信息" :visible.sync="dialogVisible2" width="40%"
                                    :before-close="handleClose" append-to-body>
                                    <el-input type="textarea" placeholder="请输入内容" v-model="textarea2" :rows="10">
                                    </el-input>
                                    <el-button type="primary" style="width: 100%;"
                                        @click="handleSend(scope.row)">发送</el-button>
                                    <span slot="footer" class="dialog-footer">

                                    </span>
                                </el-dialog>
                            </template>
                        </el-table-column>
                    </el-table>
                    <pagination v-show="total > 0" :total="total" :page.sync="messageBoard.pageNum"
                        :limit.sync="messageBoard.pageSize" @pagination="getList" :page-sizes="[5, 10, 15, 30]" />
                </el-dialog>
                <el-dialog title=" 详细信息" :visible.sync="dialogVisible1" width="30%" :before-close="handleClose">
                    <span style="line-height: 1.5px;">{{ messageDetailText }}</span>
                    <span slot="footer" class="dialog-footer">

                    </span>
                </el-dialog>

                <!-- <screenfull id="screenfull" class="right-menu-item hover-effect" /> -->

                <!-- <el-tooltip content="布局大小" effect="dark" placement="bottom">
                    <size-select id="size-select" class="right-menu-item hover-effect" />
                </el-tooltip>
 -->

            </template>

            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
                <div class="avatar-wrapper">
                    <img :src="avatar" class="user-avatar">
                    <i class="el-icon-caret-bottom" />
                </div>
                <el-dropdown-menu slot="dropdown">
                    <router-link to="/user/profile">
                        <el-dropdown-item>个人中心</el-dropdown-item>
                    </router-link>
                    <!-- <el-dropdown-item @click.native="setting = true">
                        <span>布局设置</span>
                    </el-dropdown-item> -->
                    <el-dropdown-item divided @click.native="logout">
                        <span>退出登录</span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import { deleteMessage, messageDetail, messageList, sendMessage } from '../../api/messageBoard'

export default {
    data() {
        return {
            dialogTableVisible: false,
            dialogVisible1: false,
            dialogVisible2: false,
            textarea2: "",
            messageDetailText: '',
            total: 0,

            messageBoard: {
                pageNum: 1,
                pageSize: 5,
            },
            messagelist: []
        }
    },
    components: {
        Breadcrumb,
        TopNav,
        Hamburger,
        Screenfull,
        SizeSelect,
        Search,
        RuoYiGit,
        RuoYiDoc
    },
    computed: {
        ...mapGetters([
            'sidebar',
            'avatar',
            'device'
        ]),
        setting: {
            get() {
                return this.$store.state.settings.showSettings
            },
            set(val) {
                this.$store.dispatch('settings/changeSetting', {
                    key: 'showSettings',
                    value: val
                })
            }
        },
        topNav: {
            get() {
                return this.$store.state.settings.topNav
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {

        handleSend(row) {
            sendMessage({ receiverId: row.senderId, messageContent: this.textarea2 }).then(response => {
                this.$modal.msgSuccess("发送成功");
            })
            this.dialogVisible2 = false
        },
        handleMessageDetail(messageId) {
            this.dialogVisible1 = true
            messageDetail(messageId).then(response => {
                this.messageDetailText = response.messageContent
            })
        },
        getList() {
            this.loading = true
            messageList(this.messageBoard).then(response => {
                this.messagelist = response.records
                this.total = response.total
                this.loading = false
            });
        },
        handleClose(done) {
            this.getList()
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                })
                .catch(_ => { });
            // this.dialogVisible2 = false
            // this.dialogVisible1 = false
            this.textarea2 = ""
        },
        handleDelete() {
            deleteMessage().then(response => {
                this.$modal.msgSuccess("删除成功")
            })
            this.getList()
        },
        toggleSideBar() {
            this.$store.dispatch('app/toggleSideBar')
        },
        async logout() {
            this.$confirm('确定注销并退出系统吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$store.dispatch('LogOut').then(() => {
                    location.href = '/index';
                })
            }).catch(() => { });
        }
    }
}
</script>

<style lang="scss" scoped>
.navbar {
    height: 50px;
    overflow: hidden;
    position: relative;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

    .hamburger-container {
        line-height: 46px;
        height: 100%;
        float: left;
        cursor: pointer;
        transition: background .3s;
        -webkit-tap-highlight-color: transparent;

        &:hover {
            background: rgba(0, 0, 0, .025)
        }
    }

    .breadcrumb-container {
        float: left;
    }

    .topmenu-container {
        position: absolute;
        left: 50px;
    }

    .errLog-container {
        display: inline-block;
        vertical-align: top;
    }

    .right-menu {
        float: right;
        height: 100%;
        line-height: 50px;

        &:focus {
            outline: none;
        }

        .right-menu-item {
            display: inline-block;
            padding: 0 8px;
            height: 100%;
            font-size: 18px;
            color: #5a5e66;
            vertical-align: text-bottom;

            &.hover-effect {
                cursor: pointer;
                transition: background .3s;

                &:hover {
                    background: rgba(0, 0, 0, .025)
                }
            }
        }

        .avatar-container {
            margin-right: 30px;

            .avatar-wrapper {
                margin-top: 5px;
                position: relative;

                .user-avatar {
                    cursor: pointer;
                    width: 40px;
                    height: 40px;
                    border-radius: 10px;
                }

                .el-icon-caret-bottom {
                    cursor: pointer;
                    position: absolute;
                    right: -20px;
                    top: 25px;
                    font-size: 12px;
                }
            }
        }
    }
}
</style>