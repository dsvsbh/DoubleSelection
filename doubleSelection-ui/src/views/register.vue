<template>
    <div class="register">
        <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
            <h3 class="title">学生导师双选管理系统</h3>


            <el-form-item prop="usertype">
                <!-- <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
                    <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input> -->
                <el-select v-model="registerForm.roleId" placeholder="请选择用户类型" clearable style="width: 350px;"
                    @change="changeUser">
                    <el-option v-for="item in useroptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item prop="researchArea" v-if="roleId === 100">
                <el-input v-model="registerForm.researchArea" type="text" auto-complete="off" placeholder="研究方向">
                    <svg-icon slot="prefix" icon-class="education" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="studentLimit" v-if="roleId === 100">
                <label class="stuNeed_label">招生人数</label>
                <el-input-number class="stuNeed_input" v-model="registerForm.studentLimit" :min="0"
                    :max="100"></el-input-number>
            </el-form-item>


            <el-form-item prop="major" v-if="roleId === 101">
                <el-input v-model="registerForm.major" type="text" auto-complete="off" placeholder="专业">
                    <svg-icon slot="prefix" icon-class="education" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="interests" v-if="roleId === 101">
                <el-input v-model="registerForm.interests" type="text" auto-complete="off" placeholder="感兴趣的研究方向">
                    <svg-icon slot="prefix" icon-class="star" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>

            <el-form-item prop="name">
                <el-input v-model="registerForm.name" type="text" auto-complete="off" placeholder="实名">
                    <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="email">
                <el-input v-model="registerForm.email" type="text" auto-complete="off" placeholder="电子邮箱">
                    <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="username">
                <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="用户名">
                    <svg-icon slot="prefix" icon-class="peoples" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input v-model="registerForm.password" type="password" auto-complete="off" placeholder="密码"
                    @keyup.enter.native="handleRegister">
                    <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
                <el-input v-model="registerForm.confirmPassword" type="password" auto-complete="off" placeholder="确认密码"
                    @keyup.enter.native="handleRegister">
                    <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
                </el-input>
            </el-form-item>
            <el-form-item prop="code" v-if="captchaEnabled">
                <el-input v-model="registerForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
                    @keyup.enter.native="handleRegister">
                    <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
                <div class="register-code">
                    <img :src="codeUrl" @click="getCode" class="register-code-img" />
                </div>
            </el-form-item>
            <el-form-item style="width:100%;">
                <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
                    @click.native.prevent="handleRegister">
                    <span v-if="!loading">注 册</span>
                    <span v-else>注 册 中...</span>
                </el-button>
                <div style="float: right;">
                    <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
                </div>
            </el-form-item>
        </el-form>
        <!--  底部  -->
        <div class="el-register-footer">
        </div>
    </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login";

export default {
    name: "Register",
    data() {
        const equalToPassword = (rule, value, callback) => {
            if (this.registerForm.password !== value) {
                callback(new Error("两次输入的密码不一致"));
            } else {
                callback();
            }
        };
        const checkEmail = (rule, value, cb) => {
            //验证邮箱的正则表达式
            const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
            if (regEmail.test(value)) {
                //合法的邮箱
                return cb();
            }
            //邮箱不合法

            cb(new Error('请输入合法的邮箱！'));
        };
        return {
            useroptions: [{
                value: 100,
                label: '导师'
            }, {
                value: 101,
                label: '学生'
            }],
            roleId: -1,
            codeUrl: "",
            registerForm: {
                researchArea: "",
                studentLimit: 0,
                major: "",
                interests: "",
                roleId: "",
                username: "",
                name: "",
                email: "",
                password: "",
                confirmPassword: "",
                code: "",
                uuid: ""
            },
            registerRules: {

                email: [
                    { required: true, trigger: 'blur', message: '请输入邮箱' },
                    { required: true, validator: checkEmail, trigger: 'blur' }
                ],
                researchArea: [
                    { required: true, trigger: 'blur', message: '请输入您的研究方向' }
                ],
                major: [
                    { required: true, trigger: 'blur', message: '请输入您的专业' }
                ],

                interests: [
                    { required: true, trigger: 'blur', message: '请输入您感兴趣的方向' }
                ],
                name: [
                    { required: true, trigger: "blur", message: '请输入您的真实姓名' }
                ],
                username: [
                    { required: true, trigger: "blur", message: "请输入您的账号" },
                    { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
                ],
                password: [
                    { required: true, trigger: "blur", message: "请输入您的密码" },
                    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
                    { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
                ],
                confirmPassword: [
                    { required: true, trigger: "blur", message: "请再次输入您的密码" },
                    { required: true, validator: equalToPassword, trigger: "blur" }
                ],
                code: [{ required: true, trigger: "change", message: "请输入验证码" }]
            },
            loading: false,
            captchaEnabled: true
        };
    },
    created() {
        this.getCode();
    },
    methods: {
        getCode() {
            getCodeImg().then(res => {
                this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
                if (this.captchaEnabled) {
                    this.codeUrl = "data:image/gif;base64," + res.img;
                    this.registerForm.uuid = res.uuid;
                }
            });
        },
        handleRegister() {
            this.$refs.registerForm.validate(valid => {
                if (valid) {
                    this.loading = true;
                    register(this.registerForm).then(res => {
                        const username = this.registerForm.username;
                        this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
                            dangerouslyUseHTMLString: true,
                            type: 'success'
                        }).then(() => {
                            this.$router.push("/login");
                        }).catch(() => { });
                    }).catch(() => {
                        this.loading = false;
                        if (this.captchaEnabled) {
                            this.getCode();
                        }
                    })
                }
            });
        },
        changeUser(selectValue) {
            this.roleId = selectValue
        }
    }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image: url("../assets/images/login-background.jpg");
    background-size: cover;
}

.title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
}

.register-form {
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;
    margin-left: 1000px;

    .el-input {
        height: 38px;

        input {
            height: 38px;
        }
    }

    .input-icon {
        height: 39px;
        width: 14px;
        margin-left: 2px;
    }

}

.register-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
}

.register-code {
    width: 33%;
    height: 38px;
    float: right;

    img {
        cursor: pointer;
        vertical-align: middle;
    }
}

.el-register-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
}

.register-code-img {
    height: 38px;
}

.stuNeed_label {
    margin-right: 50px;
    font-weight: normal;
    letter-spacing: 10px;
}
</style>
