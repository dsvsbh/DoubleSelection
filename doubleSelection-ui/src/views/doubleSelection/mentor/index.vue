<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="导师姓名" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入导师姓名" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="研究领域" prop="researchArea">
                <el-input v-model="queryParams.researchArea" placeholder="请输入研究领域" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="招生人数" prop="studentLimit">
                <el-input v-model="queryParams.studentLimit" placeholder="请输入招生人数" clearable
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
                    v-hasPermi="['doubleSelection:mentor:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['doubleSelection:mentor:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete" v-hasPermi="['doubleSelection:mentor:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['doubleSelection:mentor:export']">导出</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="mentorList" @selection-change="handleSelectionChange">
            <!-- <el-table-column type="selection" width="55" align="center" /> -->
            <el-table-column label="导师姓名" align="center" prop="name" />
            <el-table-column label="导师邮箱" align="center" prop="email" />
            <el-table-column label="研究领域" align="center" prop="researchArea" />
            <el-table-column label="招生人数" align="center" prop="studentLimit" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['doubleSelection:mentor:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['doubleSelection:mentor:remove']">删除</el-button>
                    <el-button size="small" type="text" @click="handleSelect(scope.row)"
                        style="text-decoration: underline;">选择</el-button>
                    <el-button size="small" type="text" @click="handleCancel(scope.row)"
                        style="text-decoration: underline;">取消选择</el-button>
                    <el-button size="small" type="text" @click="dialogVisible2 = true"
                        style="text-decoration: underline;">发送信息</el-button>
                    <el-button size="small" type="text" @click="handleIntroduce(scope.row)"
                        style="text-decoration: underline;">个人简介</el-button>
                    <el-dialog title="发送信息" :visible.sync="dialogVisible2" width="40%" :before-close="handleClose">


                        <el-input type="textarea" placeholder="请输入内容" v-model="textarea2" :rows="10">
                        </el-input>
                        <el-button type="primary" style="width: 100%;" @click="handleSend(scope.row)">发送</el-button>
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

        <!-- 添加或修改导师对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="导师姓名" prop="name">
                    <el-input v-model="form.name" placeholder="请输入导师姓名" />
                </el-form-item>
                <el-form-item label="导师邮箱" prop="email">
                    <el-input v-model="form.email" placeholder="请输入导师邮箱" />
                </el-form-item>
                <el-form-item label="研究领域" prop="researchArea">
                    <el-input v-model="form.researchArea" placeholder="请输入研究领域" />
                </el-form-item>
                <el-form-item label="招生人数" prop="studentLimit">
                    <el-input v-model="form.studentLimit" placeholder="请输入招生人数" />
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
import { listMentor, getMentor, delMentor, addMentor, updateMentor } from "@/api/doubleSelection/mentor";
import { cancelSelectionMentor, selectionMentor } from "@/api/doubleSelection/mentor";
import { sendMessage } from "@/api/messageBoard";
import { findIntroduce } from "../../../api/messageBoard";

export default {
    name: "Mentor",
    data() {
        return {
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
            textarea2: '',
            textIntroduce: '',
            // 总条数
            total: 0,
            // 导师表格数据
            mentorList: [],
            dialogVisible2: false,
            dialogVisible3: false,

            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                name: null,
                researchArea: null,
                studentLimit: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                name: [
                    { required: true, message: "导师姓名不能为空", trigger: "blur" }
                ],
                email: [
                    { required: true, message: "导师邮箱不能为空", trigger: "blur" }
                ],
                researchArea: [
                    { required: true, message: "研究领域不能为空", trigger: "blur" }
                ],
                studentLimit: [
                    { required: true, message: "招生人数不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        handleIntroduce(row) {

            findIntroduce(row.mentorId).then(response => {
                this.textIntroduce = response
            })
            this.dialogVisible3 = true
        },
        handleSend(row) {
            const mentorId = row.mentorId
            sendMessage({ "receiverId": mentorId, "messageContent": this.textarea2 }).then(response => {
                this.$modal.msgSuccess("发送成功");
            })
        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    done();
                })
                .catch(_ => { });
            this.textarea2 = ""
            this.dialogVisible2 = false

        },
        /** 查询导师列表 */
        getList() {
            this.loading = true;
            listMentor(this.queryParams).then(response => {
                this.mentorList = response.rows;
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
                mentorId: null,
                name: null,
                email: null,
                password: null,
                researchArea: null,
                studentLimit: null,
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
            this.ids = selection.map(item => item.mentorId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加导师";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const mentorId = row.mentorId || this.ids
            getMentor(mentorId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改导师";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.mentorId != null) {
                        updateMentor(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addMentor(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const mentorIds = row.mentorId || this.ids;
            this.$modal.confirm('是否确认删除导师编号为"' + mentorIds + '"的数据项？').then(function () {
                return delMentor(mentorIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('doubleSelection/mentor/export', {
                ...this.queryParams
            }, `mentor_${new Date().getTime()}.xlsx`)
        },
        handleCancel(row) {
            // console.log(row);
            const mentorId = row.mentorId
            cancelSelectionMentor(mentorId).then(response => {
                this.$modal.msgSuccess("取消选择成功");
            });

        },
        handleSelect(row) {
            // console.log(row);
            const mentorId = row.mentorId
            selectionMentor(mentorId).then(response => {
                this.$modal.msgSuccess("选择成功");
            });

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