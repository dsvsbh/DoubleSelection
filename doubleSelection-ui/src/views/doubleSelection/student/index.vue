<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="学生姓名" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入学生姓名" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="专业" prop="major">
                <el-input v-model="queryParams.major" placeholder="请输入专业" clearable @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="研究兴趣" prop="interests">
                <el-input v-model="queryParams.interests" placeholder="请输入研究兴趣" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['doubleSelection:student:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['doubleSelection:student:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete" v-hasPermi="['doubleSelection:student:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['doubleSelection:student:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
            <el-table-column label="学生姓名" align="center" prop="name" />
            <el-table-column label="学生邮箱" align="center" prop="email" />
            <el-table-column label="专业" align="center" prop="major" />
            <el-table-column label="研究兴趣" align="center" prop="interests" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="small" type="text"
                        @click="() => { dialogVisible2 = true; rowId = scope.row.studentId }"
                        style="text-decoration: underline;">发送信息</el-button>
                    <el-button size="small" type="text" @click="handleIntroduce(scope.row)"
                        style="text-decoration: underline;">个人简介</el-button>
                    <el-dialog title="发送信息" :visible.sync="dialogVisible2" width="40%" :before-close="handleClose">

                        <el-input type="textarea" placeholder="请输入内容" v-model="textarea2" :rows="10">
                        </el-input>
                        <el-button type="primary" style="width: 100%;" @click="handleSend()">发送</el-button>
                        <span slot="footer" class="dialog-footer">

                        </span>
                    </el-dialog>
                    <el-dialog title="个人简介" :visible.sync="dialogVisible3" width="30%" :before-close="handleClose">
                        <span style="line-height: 1.5px;">{{ textIntroduce }}</span>
                        <span slot="footer" class="dialog-footer">
                        </span>
                    </el-dialog>
                </template>
            </el-table-column>


        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改学生对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="学生姓名" prop="name">
                    <el-input v-model="form.name" placeholder="请输入学生姓名" />
                </el-form-item>
                <el-form-item label="学生邮箱" prop="email">
                    <el-input v-model="form.email" placeholder="请输入学生邮箱" />
                </el-form-item>
                <el-form-item label="专业" prop="major">
                    <el-input v-model="form.major" placeholder="请输入专业" />
                </el-form-item>
                <el-form-item label="研究兴趣" prop="interests">
                    <el-input v-model="form.interests" placeholder="请输入研究兴趣" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
import { listStudent, getStudent, delStudent, addStudent, updateStudent } from "@/api/doubleSelection/student";
import { findIntroduce, sendMessage } from "../../../api/messageBoard";

export default {
    name: "Student",
    data() {
        return {
            dialogVisible2: false,
            dialogVisible3: false,
            textIntroduce: '',
            textarea2: "",
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            rowId: '',
            // 学生表格数据
            studentList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                name: null,
                major: null,
                interests: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                name: [
                    { required: true, message: "学生姓名不能为空", trigger: "blur" }
                ],
                email: [
                    { required: true, message: "学生邮箱不能为空", trigger: "blur" }
                ],
                password: [
                    { required: true, message: "学生密码不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        handleIntroduce(row) {

            findIntroduce({ "userId": row.studentId }).then(response => {
                this.dialogVisible3 = true
                this.textIntroduce = response
            })
        },
        /** 查询学生列表 */
        getList() {
            this.loading = true;
            listStudent(this.queryParams).then(response => {
                this.studentList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                studentId: null,
                name: null,
                email: null,
                password: null,
                major: null,
                interests: null,
                createTime: null,
                updateTime: null
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.studentId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加学生";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const studentId = row.studentId || this.ids
            getStudent(studentId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改学生";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.studentId != null) {
                        updateStudent(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addStudent(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },

        handleSend() {

            sendMessage({ "receiverId": this.rowId, "messageContent": this.textarea2 }).then(response => {
                this.$modal.msgSuccess("发送成功");
                this.dialogVisible2 = false
            })
        },
        handleClose(done) {
            this.textarea2 = ""
            this.textIntroduce = ""
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                })
                .catch(_ => { });

        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const studentIds = row.studentId || this.ids;
            this.$modal.confirm('是否确认删除学生编号为"' + studentIds + '"的数据项？').then(function () {
                return delStudent(studentIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('doubleSelection/student/export', {
                ...this.queryParams
            }, `student_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>
<style>
.el-dialog {
    width: 500px;
    height: 500px;
}

.el-textarea {
    height: 300px;

    :deep(.el-textarea__inner) {
        height: 300px;
    }
}
</style>
