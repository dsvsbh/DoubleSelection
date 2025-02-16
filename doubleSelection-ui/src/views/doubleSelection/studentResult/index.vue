<template>
    <div class="app-container">
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
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="mentorList" @selection-change="handleSelectionChange">
            <!-- <el-table-column type="selection" width="55" align="center" /> -->
            <el-table-column label="导师姓名" align="center" prop="mentorName" />
            <el-table-column label="导师邮箱" align="center" prop="mentorEmail" />
            <el-table-column label="学生姓名" align="center" prop="studentName" />
            <el-table-column label="学生邮箱" align="center" prop="studentEmail" />
            <el-table-column label="状态" align="center" prop="status">
                <template slot-scope="scope">
                    <span v-if="scope.row.status == 0">审核中</span>
                    <span v-if="scope.row.status == 1">已双选</span>
                    <span v-if="scope.row.status == 2">已拒绝</span>
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
import { cancelSelectionMentor, selectionMentor } from "../../../api/doubleSelection/mentor";
import { getResult } from "../../../api/doubleSelection/currentActivity";

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
            // 总条数
            total: 0,
            // 导师表格数据
            mentorList: [],
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
        /** 查询导师列表 */
        getList() {
            this.loading = true;
            const pageNum = this.queryParams.pageNum
            const pageSize = this.queryParams.pageSize
            getResult({ pageNum: pageNum, pageSize: pageSize }).then(response => {
                this.mentorList = response.records;
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
